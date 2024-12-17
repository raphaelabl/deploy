import { Component } from '@angular/core';
import {UserConnector} from '../../models/user-connector';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-github-login',
  standalone: false,

  templateUrl: './github-login.component.html',
  styleUrl: './github-login.component.scss'
})
export class GithubLoginComponent {

  ghUser: UserConnector;
  clientId = 'Ov23li8HrLXJ9gME6pH5'; // Von GitHub erhalten
  redirectUri = 'http://localhost:4200/github/callback'; // Frontend-URL


  constructor(private userService: UserService) {
    this.ghUser = userService.getUserConnector();
  }

}
