import {AfterViewInit, Component, Input, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {UserConnector} from '../../models/user-connector';
import {HttpClient} from '@angular/common/http';
import {firstValueFrom} from 'rxjs';
import {ActivatedRoute, Route, Router} from '@angular/router';


declare const paypal: any;

@Component({
  selector: 'app-lizenz-buy',
  standalone: false,

  templateUrl: './lizenz-buy.component.html',
  styleUrl: './lizenz-buy.component.scss'
})
export class LizenzBuyComponent implements AfterViewInit, OnInit {
  githubUserName!: string;
  selectedModules: number[] = [];

  modules: {id: number, name: string, price: number}[] = [
      {id: -1, name: "All Tools Package", price: 6.99},
      {id: 1, name: "Automatic CI/CD", price: 6.99},
    ];

  constructor(private userService: UserService, private http: HttpClient,private router: Router) {
    this.githubUserName = userService.getUserConnector().username;
  }

  ngOnInit(): void {
    let lcStorage = localStorage.getItem("selectedModules")
    if(lcStorage){
      this.selectedModules = JSON.parse(lcStorage);
    }else{
      this.router.navigate(['shop']);
    }
  }

  ngAfterViewInit(): void {
    paypal.Buttons({
      createOrder: () => {
        if(this.githubUserName){
          return firstValueFrom(
            this.http.post<{ id: string }>('http://localhost:3000/paypal/create-order',
              { modules: this.selectedModules, username: this.githubUserName }),
          ).then(res => res.id);
        }
        return null;
      },
      onApprove: (data: any) => {
        return firstValueFrom(
          this.http.post('http://localhost:3000/paypal/capture-order', { orderId: data.orderID, modules: this.selectedModules, githubUserName: this.githubUserName }),
        ).then(res => {
          alert('Bezahlung erfolgreich! Lizenz wird generiert.');
          console.log('Antwort von Capture-Order:', res);
        });
      },
      onError: (err: any) => {
        console.error('Fehler bei der Bezahlung:', err);
      }
    }).render('#paypal-button-container');
  }

  getTotalPrice(){
    let result = this.getShoppingCart().map(element => element.price).reduce((accumulator, currentValue) => accumulator + currentValue, 0);
    if(result){
      return result;
    }
    return 'error';
  }

  getShoppingCart(){
    let selModules = this.modules.filter(module => this.selectedModules.includes(module.id));

    if(selModules.length == 0){
      this.router.navigate(['/shop']);
    }
    return selModules;
  }

}
