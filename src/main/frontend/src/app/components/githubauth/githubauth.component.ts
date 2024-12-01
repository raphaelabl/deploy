import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {HttpService} from '../../services/http.service';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-githubauth',
  standalone: false,

  templateUrl: './githubauth.component.html',
  styleUrl: './githubauth.component.scss'
})
export class GithubauthComponent implements OnInit {

  isConnected = false;

  constructor(private route: ActivatedRoute, private http: HttpService, private router: Router, private userService: UserService) {
    this.isConnected = userService.isUserLoggedIn();
    if(this.isConnected){
      this.router.navigate(['workflow-creation']);
    }
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const code = params['code'];
      if (code) {
        // Sende den Code ans Backend, um den Access Token zu erhalten
        this.http.postCode(code).subscribe({
          next: data => {

            if(data !== null && data !== undefined) {
              localStorage.setItem('github_token', data.token);
              this.userService.setUserConnector(data);
              this.router.navigate(['/workflow-creation']);
            }
            //Hier kommt später die Benutzerverwaltung...

          },
          error: err => {
            console.log(err)
          }
        });
      } else {
        alert('Kein Code erhalten.');
      }
    });
  }
}
