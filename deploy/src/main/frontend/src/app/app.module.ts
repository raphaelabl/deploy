import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CreateTemplateComponent } from './components/create-template/create-template.component';
import {HttpClientModule} from '@angular/common/http';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {FormsModule} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {MatList, MatListItem, MatListOption, MatSelectionList} from '@angular/material/list';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { WorkflowCreationComponent } from './components/workflow-creation/workflow-creation.component';
import {CdkCopyToClipboard} from '@angular/cdk/clipboard';
import { LandingComponent } from './components/landing/landing.component';
import { GithubauthComponent } from './components/githubauth/githubauth.component';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {MatTooltip} from '@angular/material/tooltip';
import { DeploymentOverviewComponent } from './components/deployment-overview/deployment-overview.component';
import {DeploymentCreationComponent} from './components/deployment-creation/deployment-creation.component';
import {MatCard, MatCardContent, MatCardHeader} from "@angular/material/card";
import {NgOptimizedImage} from '@angular/common';
import { AutociComponent } from './components/more/autoci/autoci.component';
import { GithubLoginComponent } from './components/github-login/github-login.component';
import { LizenzBuyComponent } from './components/lizenz-buy/lizenz-buy.component';
import { ShopComponent } from './components/shop/shop.component';
import {MatCheckbox} from '@angular/material/checkbox';
import { AccountComponent } from './components/account/account.component';

@NgModule({
  declarations: [
    AppComponent,
    CreateTemplateComponent,
    WorkflowCreationComponent,
    LandingComponent,
    GithubauthComponent,
    DeploymentOverviewComponent,
    DeploymentCreationComponent,
    AutociComponent,
    GithubLoginComponent,
    LizenzBuyComponent,
    ShopComponent,
    AccountComponent,
  ],
  imports: [
    BrowserAnimationsModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatButton,
    MatList,
    MatListItem,
    CdkCopyToClipboard,
    MatListOption,
    MatSelectionList,
    MatProgressSpinner,
    MatTooltip,
    MatCard,
    MatCardHeader,
    MatCardContent,
    NgOptimizedImage,
    MatCheckbox,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
