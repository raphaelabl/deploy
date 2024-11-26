import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {JobTemplate} from '../models/job-template';

@Injectable({
  providedIn: 'root'
})
export class HttpService {


  BACKEND_URL = environment.API_URL;

  constructor(private http: HttpClient) { }

  addJobTemplate(jobTemplate: JobTemplate):Observable<JobTemplate> {
    return this.http.post(this.BACKEND_URL + "job/addTemplate", jobTemplate);
  }


}
