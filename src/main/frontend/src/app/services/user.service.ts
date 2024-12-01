import { Injectable } from '@angular/core';
import {UserConnector} from '../models/user-connector';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor() { }


  public isUserLoggedIn(): boolean {
    let validateUserConnector: string | null = localStorage.getItem('userConnector');
    return (validateUserConnector !== null && validateUserConnector !== undefined);
  }

  public setUserConnector(userConnector: UserConnector) {
    localStorage.setItem('userConnector', JSON.stringify(userConnector));
  }

  public logOut(){
    localStorage.removeItem('userConnector');
  }

  public getUserConnector(): UserConnector {

    let stringUserConnector = localStorage.getItem('userConnector')

    if(stringUserConnector){
      return JSON.parse(stringUserConnector);
    }

    return undefined!;
  }

}
