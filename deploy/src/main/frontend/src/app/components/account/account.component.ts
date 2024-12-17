import {Component, OnInit} from '@angular/core';
import {HttpService} from '../../services/http.service';
import {UserData} from '../../models/user-data';
import {UserService} from '../../services/user.service';
import {UserConnector} from '../../models/user-connector';
import {environment} from '../../../environments/environment';

@Component({
  selector: 'app-account',
  standalone: false,

  templateUrl: './account.component.html',
  styleUrl: './account.component.scss'
})
export class AccountComponent implements OnInit {

  ghUser?: UserConnector;

  clientId = environment.client_id; // Von GitHub erhalten
  redirectUri = 'http://localhost:4200/github/callback'; // Frontend-URL


  account: UserData = {
    id: 0,
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    place: '',
    houseNumber: '',
    addressAddition: '',
    zipCode: '',
    cityName: '',
    countryName: ''
  };

  constructor(private httpService: HttpService, private userService: UserService) {
    this.ghUser = userService.getUserConnector();
  }

  ngOnInit(): void {
    if(this.ghUser) {
      this.httpService.getUserFromUsername(this.ghUser!.username).subscribe({
        next: data => {
          this.ghUser = data;
          this.account = data.userData!;
        },
        error: err => {
          console.log(err);
        }
      })
    }
  }

  onSubmit() {
    if(this.ghUser !== null && this.ghUser !== undefined && this.checkAccountValidation()){
      this.ghUser!.userData = this.account;
      this.httpService.postUserToUserConnector(this.ghUser!).subscribe({
        next: data => {
          this.account = data.userData!;
        },
        error: err => {
          console.log(err);
        }
      });
    }

  }

  checkAccountValidation(): boolean{
    return (this.account !== null && this.account !== undefined &&
      this.account.firstName !== "" &&
      this.account.lastName !== "" &&
      this.account.phoneNumber !== "" &&
      this.account.email !== "" &&
      this.account.place !== "" &&
      this.account.houseNumber !== "" &&
      this.account.zipCode !== "" &&
      this.account.cityName !== "" &&
      this.account.countryName !== "")
  }

  logOut(){
    this.userService.logOut();
  }

}
