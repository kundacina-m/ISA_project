import { Component, OnInit } from '@angular/core';
import {AppService} from "../app.service";


@Component({
  selector: 'app-registration',
  templateUrl: './registration.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  public alert = "";
  public registration = false;
  constructor(private Service : AppService) { }

  ngOnInit() {
  }

  userRegistration(event)
  {
    event.preventDefault();
    const target = event.target;
    const username = target.querySelector('#username').value;
    const password = target.querySelector('#password').value;
    const name = target.querySelector('#name').value;
    const surname = target.querySelector('#surname').value;
    const email = target.querySelector('#email').value;
    const address = target.querySelector('#address').value;
    const city = target.querySelector('#city').value;
    const phone = target.querySelector('#phone').value;

    this.Service.getUserRegistration(username,password,name,surname,email,address,city,phone)

    this.registration=true;
    this.alert = localStorage.getItem('alert');
    localStorage.removeItem('alert');
  }
}
