import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CreateTemplateComponent} from './components/create-template/create-template.component';

const routes: Routes = [
  {path: "", component: CreateTemplateComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
