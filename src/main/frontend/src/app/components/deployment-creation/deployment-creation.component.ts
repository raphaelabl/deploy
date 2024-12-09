import {Component, Inject, input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {UserConnector} from '../../models/user-connector';
import {DeploymentInfo} from '../../models/deployment-info';
import {RepositoryInfo} from '../../models/repository-info';
import {HttpClient} from '@angular/common/http';
import {HttpService} from '../../services/http.service';

@Component({
  selector: 'app-deployment-creation',
  standalone: false,

  templateUrl: './deployment-creation.component.html',
  styleUrl: './deployment-creation.component.scss'
})
export class DeploymentCreationComponent{

  inputDeployment: DeploymentInfo = {};
  repoSelection: RepositoryInfo[] = [];

  deploymentSteps: string[] = [
    "file-path-localization",
    "dockerfile-creation",
    "workflow-creation",
    "docker-compose-creation",
    "github-push",
    "server-installation"
  ]

  ghUser: UserConnector = undefined!;

  constructor(
    private dialogRef: MatDialogRef<DeploymentCreationComponent>,
    @Inject(MAT_DIALOG_DATA) public inputData: {ghUser: UserConnector, deployment: DeploymentInfo, edit: boolean},
    private http: HttpService
    ){
    this.ghUser = inputData.ghUser;

    if (inputData.edit){

      this.inputDeployment = inputData.deployment;

      if(this.inputData.deployment.repository){
        let foundRepository = this.ghUser.repositories.find(element => element.fullName === inputData.deployment.repository!.fullName)!;

        if(foundRepository){
            this.repoSelection.push(foundRepository);
        }
      }
    }
  }


  saveDeployment(){
    if(this.inputDeployment.repository === null || this.inputDeployment.repository === undefined){
      this.inputDeployment.repository = this.repoSelection[0];
    }else if(this.repoSelection.length > 0 && this.repoSelection[0].fullName !== this.inputDeployment.repository!.fullName){
        this.inputDeployment.repository = this.repoSelection[0];
    }
    this.inputDeployment.ghUser = this.ghUser;
    this.http.postDeploymentInfo(this.inputDeployment).subscribe({
      next: data => {
        this.inputDeployment = data;
      },
      error: err => {
        console.error(err);
      }
    });

    this.dialogRef.close();
  }

}
