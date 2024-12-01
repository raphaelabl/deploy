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

@NgModule({
  declarations: [
    AppComponent,
    CreateTemplateComponent,
    WorkflowCreationComponent,
    LandingComponent,
    GithubauthComponent
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
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
