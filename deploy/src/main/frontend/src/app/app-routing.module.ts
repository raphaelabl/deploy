import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CreateTemplateComponent} from './components/create-template/create-template.component';
import {WorkflowCreationComponent} from './components/workflow-creation/workflow-creation.component';
import {LandingComponent} from './components/landing/landing.component';
import {GithubauthComponent} from './components/githubauth/githubauth.component';
import {DeploymentOverviewComponent} from './components/deployment-overview/deployment-overview.component';
import {AutociComponent} from './components/more/autoci/autoci.component';
import {authGuard} from './services/auth.service';
import {GithubLoginComponent} from './components/github-login/github-login.component';
import {LizenzBuyComponent} from './components/lizenz-buy/lizenz-buy.component';
import {ShopComponent} from './components/shop/shop.component';
import {AccountComponent} from './components/account/account.component';

const routes: Routes = [
  {path: "job-templates", component: CreateTemplateComponent, canActivate: [authGuard] },
  {path: "workflow-creation", component: WorkflowCreationComponent, canActivate: [authGuard]},
  {path: "deployment-overview", component: DeploymentOverviewComponent, canActivate: [authGuard]},
  {path: "github/callback", component: GithubauthComponent},
  {path: "login", component: GithubLoginComponent},
  {path: "auto-ci", component: AutociComponent},
  {path: "shop", component: ShopComponent},
  {path: "cart", component: LizenzBuyComponent},
  {path: "account", component: AccountComponent},
  {path: "", component: LandingComponent},
  {path: "**", component: LandingComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
