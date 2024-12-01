import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {JobTemplate} from '../models/job-template';
import {UserConnector} from '../models/user-connector';

@Injectable({
  providedIn: 'root'
})
export class HttpService {


  BACKEND_URL = environment.API_URL;

  constructor(private http: HttpClient) { }

  addJobTemplate(jobTemplate: JobTemplate):Observable<JobTemplate> {
    return this.http.post(this.BACKEND_URL + "job/addTemplate", jobTemplate);
  }

  getAllJobTemplates(): Observable<JobTemplate[]> {
    return this.http.get<JobTemplate[]>(this.BACKEND_URL + "job/getAllTemplates");
  }

  postCode(code: string): Observable<UserConnector>{
    return this.http.get<UserConnector>(this.BACKEND_URL + "github/callback", {params: {code: code}});
  }

}
