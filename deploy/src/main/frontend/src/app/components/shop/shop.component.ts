import { Component } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';

@Component({
  selector: 'app-shop',
  standalone: false,

  templateUrl: './shop.component.html',
  styleUrl: './shop.component.scss'
})
export class ShopComponent {

  selectedModules: number[] = [];

  constructor(private router: Router) { }


  toggleModule(module: number){
    if(this.selectedModules.includes(module)){
      this.selectedModules = this.selectedModules.filter(m => module !== m);
      return;
    }
    this.selectedModules.push(module);
    return;
  }

  card(){
    localStorage.setItem('selectedModules',JSON.stringify(this.selectedModules));
    this.router.navigate(['/cart']);
  }

  buyModule(module: number){
    localStorage.setItem('selectedModules',JSON.stringify([module]));
    this.router.navigate(['/cart']);
  }

}
