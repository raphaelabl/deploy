import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CreateTemplateComponent} from './components/create-template/create-template.component';
import {WorkflowCreationComponent} from './components/workflow-creation/workflow-creation.component';
import {LandingComponent} from './components/landing/landing.component';
import {GithubauthComponent} from './components/githubauth/githubauth.component';
import {DeploymentOverviewComponent} from './components/deployment-overview/deployment-overview.component';

const routes: Routes = [
  {path: "job-templates", component: CreateTemplateComponent},
  {path: "workflow-creation", component: WorkflowCreationComponent},
  {path: "deployment-overview", component: DeploymentOverviewComponent},
  {path: "github/callback", component: GithubauthComponent},
  {path: "", component: LandingComponent},
  {path: "**", component: LandingComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
