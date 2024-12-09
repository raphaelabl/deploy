import {Component, inject, input, OnInit} from '@angular/core';
import {JobTemplate} from '../../models/job-template';
import {HttpService} from '../../services/http.service';
import {writeErrorToLogFile} from '@angular/cli/src/utilities/log-file';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-create-template',
  standalone: false,
  templateUrl: './create-template.component.html',
  styleUrl: './create-template.component.scss'
})
export class CreateTemplateComponent implements OnInit {

  // Search regex: #{[^+\-}#]*}#


  regex:RegExp = /#{([^}#]+)}#/g;
  allMessage: {type: number, message: string}[] = [];

  jobTemplateList: JobTemplate[] = [];

  inputJobTemplate: JobTemplate = {workflowVariables: []};
  inputVariable: {value: string, idx: number} = {value: "", idx: -1};

  private snackBar: MatSnackBar = inject(MatSnackBar);

  constructor(private http: HttpService) { }

  ngOnInit(): void {
    this.loadAllJobTemplates();
  }


  //region Variables
  addVariable(): void {
    if(this.inputVariable.value.length == 0){
      return;
    }

    this.inputJobTemplate.workflowVariables =
      (this.inputJobTemplate.workflowVariables === null || this.inputJobTemplate.workflowVariables === undefined)?
        []:
        this.inputJobTemplate.workflowVariables;

    if(this.inputVariable.idx === -1){
      this.inputJobTemplate.workflowVariables = [...this.inputJobTemplate.workflowVariables, this.inputVariable.value];
      this.inputVariable = {value: "", idx: -1};
    }else{
      this.inputJobTemplate.workflowVariables[this.inputVariable.idx] = this.inputVariable.value;
      this.inputVariable = {value: "", idx: -1};
    }

    if(this.inputJobTemplate.id !== null && this.inputJobTemplate.id !== undefined && this.inputJobTemplate.id !== 0) {
      this.postJobTemplate(true);
    }
  }

  popVarAt(i: number): void {
    this.inputJobTemplate.workflowVariables = this.inputJobTemplate.workflowVariables!.filter((value: string, index: number)=> index !== i);
  }

  editVar(editIndex: number): void {
    this.inputVariable = {value: this.inputJobTemplate.workflowVariables![editIndex], idx: editIndex};
  }
  //endregion

  //region Job
  editJobClick(jobTemplate: JobTemplate) {
    this.inputJobTemplate = jobTemplate;
  }
  //endregion

  //region Validation

  getMissingVariables(jobTemplate: JobTemplate): string[] {
    if(jobTemplate === undefined || jobTemplate === null || jobTemplate.workflowVariables === undefined || jobTemplate.workflowVariables === null){
        return ["Unvalid Jobtemplate"];
    }

    let variables: string[] = [];
    let match;

    while ((match = this.regex.exec(jobTemplate.workflowFileContent!)) !== null) {
      variables.push(match[1]); // Extrahiere nur den Inhalt innerhalb der #{ }
    }

    return jobTemplate.workflowVariables!.filter(item => !variables.includes(item));
  }

  checkValidation(): boolean{
    this.allMessage = [];

    this.jobTemplateList.forEach((item) => {
      let result = this.getMissingVariables(item);
      if(result.length > 0){
        this.allMessage.push({type: 1, message: item.name + " - [Missing Variables in Job-Script]: " + result.join(", ")});
      }
    })
    return this.allMessage.length === 0;
  }
  //endregion

  //region Http-Requests
  loadAllJobTemplates(): void {
    this.http.getAllJobTemplates().subscribe({
      next: data => {
        this.jobTemplateList = data;
      },
      error: err => {
        console.error(err);
      }
    })
  }

  postJobTemplate(wasEdit: boolean = false) {
    let isValide = this.checkValidation();

    if(!isValide || this.allMessage.filter(element => [1].includes(element.type))){
      this.http.addJobTemplate(this.inputJobTemplate).subscribe({
        next: data => {
          this.loadAllJobTemplates();
          if(!wasEdit){
            this.inputJobTemplate = {}
          }
        },
        error: err => {
          console.log(err);
        }
      });
    }else{
      this.allMessage = [
        {type: 2, message: "One Big error, Job could not be saved!"}
      ];
    }
  }
  //endregion
}
