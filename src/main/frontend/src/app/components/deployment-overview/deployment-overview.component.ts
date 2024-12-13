import {Component, OnInit} from '@angular/core';
import {DeploymentInfo} from '../../models/deployment-info';
import {UserConnector} from '../../models/user-connector';
import {UserService} from '../../services/user.service';
import {HttpService} from '../../services/http.service';
import {MatDialog} from '@angular/material/dialog';
import {DeploymentCreationComponent} from '../deployment-creation/deployment-creation.component';
import {WorkflowCreationComponent} from '../workflow-creation/workflow-creation.component';

@Component({
  selector: 'app-deployment-overview',
  standalone: false,

  templateUrl: './deployment-overview.component.html',
  styleUrl: './deployment-overview.component.scss'
})
export class DeploymentOverviewComponent implements OnInit {

  deployments: DeploymentInfo[] = [];
  ghUser: UserConnector = undefined!;

  clientId = 'Ov23li8HrLXJ9gME6pH5'; // Von GitHub erhalten
  redirectUri = 'http://localhost:4200/github/callback'; // Frontend-URL

  constructor(
    private userService: UserService,
    private http: HttpService,
    private dialog: MatDialog
  ) {
    if(userService.isUserLoggedIn()){
      this.ghUser = userService.getUserConnector();
    }
  }


  ngOnInit() {
    this.loadUserDeploys();
  }

  newDeployDialog(){
    let dialogRef = this.dialog.open(DeploymentCreationComponent, {
      width: '90%',
      height: '90%',
      maxWidth: '90vw',
      maxHeight: '90vh',
      data: {ghUser: this.ghUser, deployment: undefined, edit: false}
    })
  }

  editDeployment(deployment: DeploymentInfo){
    let dialogRef = this.dialog.open(DeploymentCreationComponent, {
      width: '90%',
      height: '90%',
      maxWidth: '90vw',
      maxHeight: '90vh',
      data: {ghUser: this.ghUser, deployment: deployment, edit: true}
    })
  }

  uploadFiles(deploymentInfo: DeploymentInfo){
    this.http.uploadFilesToGithub(deploymentInfo).subscribe({
      next: data => {
        deploymentInfo.state = data;
      },
      error: err => {
        console.error(err);
      }
    });
  }


  newWorkflow(deployment: DeploymentInfo){
    let dialogRef = this.dialog.open(WorkflowCreationComponent, {
      width: '90%',
      height: '90%',
      maxWidth: '90vw',
      maxHeight: '90vh',
      data: deployment
    })
  }

  loadUserDeploys() {
    if(this.ghUser){
      this.http.getDeploysFromGhUserName(this.ghUser.username).subscribe({
        next: data => {
          this.deployments = data;
        },
        error: err => {
          console.log(err);
        }
      });
    }
  }

  logOut(){
    this.userService.logOut();
  }

}
