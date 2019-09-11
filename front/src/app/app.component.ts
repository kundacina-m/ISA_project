import {Component, OnInit} from '@angular/core';
import {AppService} from "./app.service";

declare var $:any;

@Component({
  selector: 'app-root',
  templateUrl: './home.html',
  styleUrls: ['./app.component.css',
    './home/css/animate.css',
    './home/css/aos.css',
    './home/css/bootstrap/bootstrap-grid.css',
    './home/css/bootstrap/bootstrap-reboot.css',
    './home/css/flaticon.css',
    './home/css/icomoon.css',
    './home/css/ionicons.min.css',
    './home/css/jquery.timepicker.css',
    './home/css/magnific-popup.css',
    './home/css/open-iconic-bootstrap.min.css',
    './home/css/owl.theme.default.min.css',
    './home/css/style.css']
})
export class AppComponent implements OnInit {

  public isLogin = false;

  public user = null;

  constructor(private Service : AppService) { }

  public ngOnInit()
  {

    this.user = localStorage.getItem('user');
    if(this.user!=null)
    {
      this.isLogin=true;
    }
  }

  public logout()
  {
    this.user = null;
    localStorage.removeItem('user');
    this.isLogin=false;
    this.Service.userLogout();
  }
}
