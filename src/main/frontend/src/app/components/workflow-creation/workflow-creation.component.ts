import {Component, Inject, input, OnInit} from '@angular/core';
import {HttpService} from '../../services/http.service';
import {JobTemplate} from '../../models/job-template';
import {Workflow} from '../../models/workflow.model';
import {UserService} from '../../services/user.service';
import {UserConnector} from '../../models/user-connector';
import {Job} from '../../models/job';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {DeploymentInfo} from '../../models/deployment-info';
import {PersistenceUtils} from '../../utils';

@Component({
  selector: 'app-workflow-creation',
  standalone: false,

  templateUrl: './workflow-creation.component.html',
  styleUrl: './workflow-creation.component.scss'
})
export class WorkflowCreationComponent implements OnInit {

  spinner: boolean = true;

  ghUser!: UserConnector;

  allJobTemplates: JobTemplate[] = [];
  selectedJobTemplate: JobTemplate[] = [];

  inputWorkflow: Workflow = {}

  step: number = 0;

  constructor(
    private http: HttpService,
    private userService: UserService,
    private dialogRef: MatDialogRef<WorkflowCreationComponent>,
    @Inject(MAT_DIALOG_DATA) public deploymentInfo: DeploymentInfo,
  ) {

    if(userService.isUserLoggedIn()){
      this.ghUser = userService.getUserConnector();
      this.step++;
    }
  }

  //region Init
  ngOnInit(): void {
    this.spinner = true;
    this.loadAllJobTemplates();
  }
  //endregion

  //region Module Selection
  moduleSelectionChange() {
    this.addJobReferences();
  }

  addJobReferences(){
    if(this.inputWorkflow.modules === null || this.inputWorkflow.modules === undefined || this.inputWorkflow.modules.length === 0){
      //When the modules are undefined
      this.inputWorkflow.modules = this.createJobFromJobTemplate(this.selectedJobTemplate)
    }else{
      //Add new selected Jobs
      let newSelected = this.selectedJobTemplate
        .filter(
          element => !this.inputWorkflow.modules!
            .map(module => module.refTemplate!.name)
            .includes(element.name)
        );
      console.log(newSelected);
      this.inputWorkflow.modules = [...this.inputWorkflow.modules, ...this.createJobFromJobTemplate(newSelected)]
    }
    //Remove all deselected Jobs
    this.inputWorkflow.modules = this.inputWorkflow.modules
      .filter(
        element => this.selectedJobTemplate
          .map(job => job.name)
          .includes(element.refTemplate!.name)
      );

    console.log(this.inputWorkflow.modules);
  }

  createJobFromJobTemplate(jobTemplates: JobTemplate[]): Job[]{

    let result: Job[] = jobTemplates.map(element => {
      return {
        name: element.name,
        refTemplate: element,
        attributeList: element.workflowVariables!.map(jobVariable => {
          return {
            name: jobVariable,
            value: ""
          }
        })
      }
    });

    console.log("result")
    console.log(result);

    result.forEach(element => {
      if(!PersistenceUtils.isNullOrUndefined(this.deploymentInfo.workflow) && !PersistenceUtils.isNullOrUndefined(this.deploymentInfo.workflow!.modules)) {
        let existing = this.deploymentInfo.workflow!.modules!
          .find(persistedJob => persistedJob.refTemplate!.name === element.refTemplate!.name && PersistenceUtils.isEntityPersisted(persistedJob))

        if(PersistenceUtils.isEntityPersisted(existing)){
          element = existing!;
        }
      }
    })

    return result;
  }

  //endregion

  //region Data Loading
  loadAllJobTemplates(): void {
    this.http.getAllJobTemplates().subscribe({
      next: data => {
        this.allJobTemplates = data;

        if(
          PersistenceUtils.isEntityPersisted(this.deploymentInfo) &&
          PersistenceUtils.isEntityPersisted(this.deploymentInfo.workflow)
        ){
          //If the workflow is already persisted it is getting loaded
          this.inputWorkflow = this.deploymentInfo.workflow!;
          this.selectedJobTemplate = this.allJobTemplates.filter(
            job => this.deploymentInfo.workflow!.modules!.map(element => element.refTemplate!.name!).flat().includes(job.name!)
          );
        }

        this.spinner = false;
      },
      error: err => {
        console.error(err);
      }
    })
  }

  saveWorkflow(){
    this.spinner = true;
    if(
      PersistenceUtils.isEntityPersisted(this.deploymentInfo) &&
      PersistenceUtils.isEntityPersisted(this.deploymentInfo.ghUser)
    ){
      this.http.postWorkflow(this.inputWorkflow, this.deploymentInfo.id!, this.deploymentInfo.ghUser!.id!).subscribe({
        next: data => {
          this.inputWorkflow = data;
          this.spinner = false;
          this.dialogRef.close();
        },
        error: err => {
          console.log(err)
        }
      })
    }
  }
  //endregion

  //region Site Paging
  addSiteStep(amount: number) {
    this.step += amount;
  }

  canForwardStep(){
    if(this.step === 1 && this.selectedJobTemplate !== undefined && this.selectedJobTemplate.length >= 0){
      return true;
    }else if(this.step === 2){
      let attributes = this.inputWorkflow.modules!
                                  .map(element =>
                                    element.attributeList!.map(attribute => attribute.value)
                                  ).flat()
      return attributes.filter(element => (element === null || element === undefined || element === "")).length === 0;
    }

    return false;
  }
  //endregion

  //region Testing
    testLog(obj: any){
      console.log(obj);
    }
  //endregion

  protected readonly PersistenceUtils = PersistenceUtils;
}
