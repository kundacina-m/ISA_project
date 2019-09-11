import { NgModule } from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {LoginComponent} from "./login/login.component";
import {RegistrationComponent} from "./registration/registration.component";
import {Home1Component} from "./home1/home1.component";
import {UserProfileComponent} from "./user-profile/user-profile.component";

import {ViewAvioComponent} from "./view-avio/view-avio.component";
import {AvioCompanyComponent} from "./avio-company/avio-company.component";
import {FlightComponent} from "./flight/flight.component";

import {ChangePassComponent} from "./change-pass/change-pass.component";


const appRoutes: Routes = [
  {path: 'home',component: Home1Component},
  {path:'login', component: LoginComponent},
  {path:'registration', component: RegistrationComponent},
  {path:'',redirectTo:'/home',pathMatch:'full'},
  {path:'userProfile', component : UserProfileComponent},
  {path:'viewAvio',component:ViewAvioComponent},
  {path:'avioCompany',component:AvioCompanyComponent},
  {path:'flight',component:FlightComponent},
  {path:'viewChangePass',component:ChangePassComponent}
];

@NgModule({
  imports: [
    RouterModule.forRoot(appRoutes)
  ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
