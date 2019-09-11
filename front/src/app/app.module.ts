import { BrowserModule } from '@angular/platform-browser';
import {NgModule, NO_ERRORS_SCHEMA} from '@angular/core';
import { RouterModule } from "@angular/router";
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { AppRoutingModule } from './app-routing.module';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import { RegistrationComponent } from './registration/registration.component';

import { Home1Component } from './home1/home1.component';


import { AvioCompanyComponent } from './avio-company/avio-company.component';
import { UserProfileComponent } from './user-profile/user-profile.component';


import { ViewAvioComponent } from './view-avio/view-avio.component';

import { FlightComponent } from './flight/flight.component'
import {IntercepterService} from "./intercepter.service";

import {MatSliderModule} from '@angular/material/slider';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {FormsModule} from "@angular/forms";
import {MatSelectModule} from '@angular/material/select';
import {MatFormFieldModule} from "@angular/material";
import {MatInputModule,MatAutocompleteModule,MatNativeDateModule} from "@angular/material";
import {ReactiveFormsModule} from "@angular/forms";
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { ChangePassComponent } from './change-pass/change-pass.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,

    Home1Component,

    AvioCompanyComponent,
    UserProfileComponent,

    ViewAvioComponent,

    FlightComponent,

    ChangePassComponent,

  ],schemas:[NO_ERRORS_SCHEMA],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    RouterModule,
    AppRoutingModule,
    MatSliderModule,
    FormsModule,
    MatSelectModule,
    MatFormFieldModule,
    MatAutocompleteModule,
    MatInputModule,
    ReactiveFormsModule,
    MatProgressBarModule,
    MatSnackBarModule,
    MatDatepickerModule,
    MatNativeDateModule,
  ],
  providers: [{
    provide : HTTP_INTERCEPTORS,
    useClass: IntercepterService,
    multi   : true,
    }],
  bootstrap: [AppComponent]
})
export class AppModule { }
