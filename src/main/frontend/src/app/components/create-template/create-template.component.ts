import { Component } from '@angular/core';
import {JobTemplate} from '../../models/job-template';
import {HttpService} from '../../services/http.service';

@Component({
  selector: 'app-create-template',
  standalone: false,
  templateUrl: './create-template.component.html',
  styleUrl: './create-template.component.scss'
})
export class CreateTemplateComponent {

  inputJobTemplate: JobTemplate = {};

  constructor(private http: HttpService) { }

  postJobTemplate() {
    this.http.addJobTemplate(this.inputJobTemplate).subscribe({
      next: data => {
        this.inputJobTemplate = data;
      },
      error: err => {
        console.log(err);
      }
    });
  }

}
