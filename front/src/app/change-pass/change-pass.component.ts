import { Component, OnInit } from '@angular/core';
import {AppService} from "../app.service";
import {AuthService} from "../login/auth.service";

declare var $:any;

@Component({
  selector: 'app-change-pass',
  templateUrl: '../changePass.html',
  styleUrls: ['./change-pass.component.css']
})
export class ChangePassComponent implements OnInit {

  constructor(private userService : AppService,private auth: AuthService) { }

  ngOnInit() {

  }
  changePass(event)
  {
    event.preventDefault();
    var oldPass = event.target.querySelector("#old").value;
    var newPass = event.target.querySelector("#new").value;

    console.log(oldPass);
    console.log(newPass);

    this.userService.changePassword(oldPass,newPass);
  }
  Denied()
  {
    this.auth.removeJwtToken();
    localStorage.removeItem('user');
    location.href='/home';
  }
}
