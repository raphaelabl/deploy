import {inject, Injectable} from '@angular/core';
import {UserService} from './user.service';
import {CanActivateFn, Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private userService: UserService, private router: Router) { }

  canActivate(): boolean{
    if(this.userService.isUserLoggedIn()){
      console.log("Yea")
      return true
    } else {
      this.router.navigate(['/login'])
      return true;
    }
  }

}

export const authGuard: CanActivateFn = (route, state) => {
  return inject(AuthService).canActivate();
};
