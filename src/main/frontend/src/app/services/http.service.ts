import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {JobTemplate} from '../models/job-template';
import {UserConnector} from '../models/user-connector';
import {Workflow} from '../models/workflow.model';

@Injectable({
  providedIn: 'root'
})
export class HttpService {


  BACKEND_URL = environment.API_URL;

  constructor(private http: HttpClient) { }

  //region Job Template
  addJobTemplate(jobTemplate: JobTemplate):Observable<JobTemplate> {
    return this.http.post(this.BACKEND_URL + "job/addTemplate", jobTemplate);
  }
  getAllJobTemplates(): Observable<JobTemplate[]> {
    return this.http.get<JobTemplate[]>(this.BACKEND_URL + "job/getAllTemplates");
  }
  //endregion

  //region Workflow
  postWorkflow(workflow: Workflow):Observable<Workflow> {
    return this.http.post<Workflow>(this.BACKEND_URL + "workflow/newWorkflow", workflow);
  }
  //endregion

  //region Github-Auth
  postCode(code: string): Observable<UserConnector>{
    return this.http.get<UserConnector>(this.BACKEND_URL + "github/callback", {params: {code: code}});
  }
  //endregion
}
