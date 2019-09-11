import { Component, OnInit } from '@angular/core';
import {AppService} from "../app.service";
import {HttpResponse, HttpSentEvent} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {MatSnackBar} from "@angular/material";

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private Service : AppService) { }

  ngOnInit() {

  }

  userLogin(event)
  {
    event.preventDefault();
    const target = event.target;
    const username = target.querySelector('#username1').value;
    const password = target.querySelector('#password1').value;
    const credentials = {username, password};
    this.Service.getUserLogin(credentials);
  }
}
