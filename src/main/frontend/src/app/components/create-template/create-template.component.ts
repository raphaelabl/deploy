import {Component, inject, input, OnInit} from '@angular/core';
import {JobTemplate} from '../../models/job-template';
import {HttpService} from '../../services/http.service';
import {writeErrorToLogFile} from '@angular/cli/src/utilities/log-file';
import {MatSnackBar} from '@angular/material/snack-bar';
import {PersistenceUtils} from '../../utils';

@Component({
  selector: 'app-create-template',
  standalone: false,
  templateUrl: './create-template.component.html',
  styleUrl: './create-template.component.scss'
})
export class CreateTemplateComponent implements OnInit {

  // Search regex: #{[^+\-}#]*}#

  site: number = 1;

  regex:RegExp = /#{([^}#]+)}#/g;
  allMessage: {type: number, message: string}[] = [];

  jobTemplateList: JobTemplate[] = [];

  inputJobTemplate: JobTemplate = {workflowVariables: []};
  inputWorkflowVariable: {value: string, idx: number} = {value: "", idx: -1};
  inputDockerVariable: {value: string, idx: number} = {value: "", idx: -1};
  inputDockerComposeVariable: {value: string, idx: number} = {value: "", idx: -1};

  private snackBar: MatSnackBar = inject(MatSnackBar);

  constructor(private http: HttpService) { }

  ngOnInit(): void {
    this.loadAllJobTemplates();
  }

  toggle(to: number){
    console.log(this.site)
    this.site = to;
  }

  //region Workflow Variables
  addWorkflowVariable(): void {
    if(this.inputWorkflowVariable.value.length == 0){
      return;
    }

    this.inputJobTemplate.workflowVariables =
      (this.inputJobTemplate.workflowVariables === null || this.inputJobTemplate.workflowVariables === undefined)?
        []:
        this.inputJobTemplate.workflowVariables;

    if(this.inputWorkflowVariable.idx === -1){
      this.inputJobTemplate.workflowVariables = [...this.inputJobTemplate.workflowVariables, this.inputWorkflowVariable.value];
      this.inputWorkflowVariable = {value: "", idx: -1};
    }else{
      this.inputJobTemplate.workflowVariables[this.inputWorkflowVariable.idx] = this.inputWorkflowVariable.value;
      this.inputWorkflowVariable = {value: "", idx: -1};
    }

    if(this.inputJobTemplate.id !== null && this.inputJobTemplate.id !== undefined && this.inputJobTemplate.id !== 0) {
      this.postJobTemplate(true);
    }
  }

  popWorkflowVarAt(i: number): void {
    this.inputJobTemplate.workflowVariables = this.inputJobTemplate.workflowVariables!.filter((value: string, index: number)=> index !== i);
  }

  editWorkflowVar(editIndex: number): void {
    this.inputWorkflowVariable = {value: this.inputJobTemplate.workflowVariables![editIndex], idx: editIndex};
  }
  //endregion

  //region Dockerfile Variables
  addDockerfileVariable(): void {
    if(this.inputDockerVariable.value.length == 0){
      return;
    }

    this.inputJobTemplate.dockerFileVariables =
      (this.inputJobTemplate.dockerFileVariables === null || this.inputJobTemplate.dockerFileVariables === undefined)?
        []:
        this.inputJobTemplate.dockerFileVariables;

    if(this.inputDockerVariable.idx === -1){
      this.inputJobTemplate.dockerFileVariables = [...this.inputJobTemplate.dockerFileVariables, this.inputDockerVariable.value];
      this.inputDockerVariable = {value: "", idx: -1};
    }else{
      this.inputJobTemplate.dockerFileVariables[this.inputDockerVariable.idx] = this.inputDockerVariable.value;
      this.inputDockerVariable = {value: "", idx: -1};
    }

    if(this.inputJobTemplate.id !== null && this.inputJobTemplate.id !== undefined && this.inputJobTemplate.id !== 0) {
      this.postJobTemplate(true);
    }
  }

  popDockerfileVarAt(i: number): void {
    this.inputJobTemplate.dockerFileVariables = this.inputJobTemplate.dockerFileVariables!.filter((value: string, index: number)=> index !== i);
  }

  editDockerVar(editIndex: number): void {
    this.inputDockerVariable = {value: this.inputJobTemplate.dockerFileVariables![editIndex], idx: editIndex};
  }
  //endregion

  //region Dockercompose Variables
  addDockerComposeVariable(): void {
    if(this.inputDockerComposeVariable.value.length == 0){
      return;
    }

    this.inputJobTemplate.dockerComposeVariables =
      (this.inputJobTemplate.dockerComposeVariables === null || this.inputJobTemplate.dockerComposeVariables === undefined)?
        []:
        this.inputJobTemplate.dockerComposeVariables;

    if(this.inputDockerComposeVariable.idx === -1){
      this.inputJobTemplate.dockerComposeVariables = [...this.inputJobTemplate.dockerComposeVariables, this.inputDockerComposeVariable.value];
      this.inputDockerComposeVariable = {value: "", idx: -1};
    }else{
      this.inputJobTemplate.dockerComposeVariables[this.inputDockerComposeVariable.idx] = this.inputDockerComposeVariable.value;
      this.inputDockerComposeVariable = {value: "", idx: -1};
    }

    if(this.inputJobTemplate.id !== null && this.inputJobTemplate.id !== undefined && this.inputJobTemplate.id !== 0) {
      this.postJobTemplate(true);
    }
  }

  popDockerComposeVarAt(i: number): void {
    this.inputJobTemplate.dockerComposeVariables = this.inputJobTemplate.dockerComposeVariables!.filter((value: string, index: number)=> index !== i);
  }

  editDockerComposeVar(editIndex: number): void {
    this.inputDockerComposeVariable = {value: this.inputJobTemplate.dockerComposeVariables![editIndex], idx: editIndex};
  }
  //endregion

  //region Job
  editJobClick(jobTemplate: JobTemplate) {
    this.site = 1;
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
    console.log(this.inputJobTemplate);
    let isValide = this.checkValidation();
    console.log(isValide);
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


  trackByFn(index: number, item: any): number {
    return index; // oder ein eindeutiger Identifier für `item`
  }

  protected readonly PersistenceUtils = PersistenceUtils;
}
