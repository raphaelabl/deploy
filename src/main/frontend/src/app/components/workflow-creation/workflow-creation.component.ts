import {Component, input, OnInit} from '@angular/core';
import {HttpService} from '../../services/http.service';
import {JobTemplate} from '../../models/job-template';
import {Workflow} from '../../models/workflow.model';
import {UserService} from '../../services/user.service';
import {UserConnector} from '../../models/user-connector';
import {Job} from '../../models/job';
import {JobAttribute} from '../../models/job-attribute';

@Component({
  selector: 'app-workflow-creation',
  standalone: false,

  templateUrl: './workflow-creation.component.html',
  styleUrl: './workflow-creation.component.scss'
})
export class WorkflowCreationComponent implements OnInit {

  clientId = 'Ov23li8HrLXJ9gME6pH5'; // Von GitHub erhalten
  redirectUri = 'http://localhost:4200/github/callback'; // Frontend-URL

  ghUser!: UserConnector;

  allJobTemplates: JobTemplate[] = [];
  selectedJobTemplate: JobTemplate[] = [];

  inputWorkflow: Workflow = {}

  step: number = 0;

  constructor(private http: HttpService, private userService: UserService) {
    if(userService.isUserLoggedIn()){
      this.ghUser = userService.getUserConnector();
      this.step++;
    }
  }

  //region Init
  ngOnInit(): void {
    this.loadAllJobTemplates();
  }
  //endregion

  //region Module Selection
  moduleSelectionChange() {
    this.addJobReferences();
  }

  addJobReferences(){
    if(this.inputWorkflow.modules === null || this.inputWorkflow.modules === undefined){
      this.inputWorkflow.modules = this.createJobFromJobTemplate(this.selectedJobTemplate)
    }else{
      let newSelected = this.selectedJobTemplate
        .filter(
          element => !this.inputWorkflow.modules!
            .map(module => module.refTemplate!.name)
            .includes(element.name)
        );
      this.inputWorkflow.modules = [...this.inputWorkflow.modules, ...this.createJobFromJobTemplate(newSelected)]
    }
    this.inputWorkflow.modules = this.inputWorkflow.modules
      .filter(
        element => this.selectedJobTemplate
          .map(job => job.name)
          .includes(element.refTemplate!.name)
      );
  }

  createJobFromJobTemplate(jobTemplates: JobTemplate[]): Job[]{
    return jobTemplates.map(element => {
      return {
        name: element.name,
        refTemplate: element,
        attributeList: element.variables!.map(jobVariable => {
            return {
              name: jobVariable,
              value: ""
            }
          })
      }
    });
  }

  //endregion

  //region Data Loading
  loadAllJobTemplates(): void {
    this.http.getAllJobTemplates().subscribe({
      next: data => {
        this.allJobTemplates = data;
      },
      error: err => {
        console.error(err);
      }
    })
  }
  //endregion

  //region Site Paging
  addSiteStep(amount: number) {
    this.step += amount;
  }
  //endregion

  //region GitHub Authentication
  logOut(){
    this.userService.logOut();
    this.ghUser = undefined!;
    this.step = 0;
  }
  //endregion

  //region Testing
    testLog(obj: any){
      console.log(obj);
    }
  //endregion

  protected readonly input = input;
}