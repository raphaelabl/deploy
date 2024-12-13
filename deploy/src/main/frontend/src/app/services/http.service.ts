import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {JobTemplate} from '../models/job-template';
import {UserConnector} from '../models/user-connector';
import {Workflow} from '../models/workflow.model';
import {DeploymentInfo} from '../models/deployment-info';
import {Job} from '../models/job';
import {UserData} from '../models/user-data';

@Injectable({
  providedIn: 'root'
})
export class HttpService {


  BACKEND_URL = environment.API_URL;

  constructor(private http: HttpClient) { }

  //region Deployment

  getDeploysFromGhUserName(username: string): Observable<DeploymentInfo[]> {
    return this.http.get<DeploymentInfo[]>(`${environment.API_URL}deploy/allFromUser/${username}`);
  }

  postDeploymentInfo(deploymentInfo: DeploymentInfo): Observable<DeploymentInfo> {
    return this.http.post<DeploymentInfo>(`${environment.API_URL}deploy/post/deploymentInfo`, deploymentInfo);
  }

  uploadFilesToGithub(deployment: DeploymentInfo): Observable<string> {
    return this.http.post<string>(`${environment.API_URL}deploy/uploadToGithub`,deployment);
  }

  //endregion


  //region Job Template
  addJobTemplate(jobTemplate: JobTemplate):Observable<JobTemplate> {
    return this.http.post<JobTemplate>(this.BACKEND_URL + "job/addTemplate", jobTemplate);
  }
  getAllJobTemplates(): Observable<JobTemplate[]> {
    return this.http.get<JobTemplate[]>(this.BACKEND_URL + "job/getAllTemplates");
  }
  //endregion

  //region Workflow
  postWorkflow(workflow: Workflow, deployId: number, userId: number):Observable<Workflow> {
    return this.http.post<Workflow>(this.BACKEND_URL + "workflow/addWorkflow", workflow, {params: {
                                                                                              deploymentId: deployId,
                                                                                              userId: userId
                                                                                            }});
  }
  //endregion

  //region Github-Auth

  getUserFromUsername(username: string){
    return this.http.get<UserConnector>(this.BACKEND_URL + "user/withGithubUsername", {params: {username}});
  }

  postUserToUserConnector(user: UserConnector):Observable<UserConnector> {
    return this.http.post<UserConnector>(this.BACKEND_URL + "user/dataToConnector", user);
  }

  postCode(code: string): Observable<UserConnector>{
    return this.http.get<UserConnector>(this.BACKEND_URL + "github/callback", {params: {code: code}});
  }
  //endregion
}
