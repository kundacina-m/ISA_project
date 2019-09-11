import { Component, OnInit } from '@angular/core';
import {FlightService} from "./flight.service";
import {HttpClient} from "@angular/common/http";
import {AppService} from "../app.service";

declare var $:any;

@Component({
  selector: 'app-flight',
  templateUrl:'../flight.html',
  styleUrls: ['./flight.component.css']
})
export class FlightComponent implements OnInit {

  public loginUser;
  public Korisnik;
  public podaciKorisnika=[];
  public PunoFrenda=[];
  public PunoFreKor=[];
  public fr;
  public Ispis_polja=[];
  public fUkupno=[];
  public selectFriend=[];
  public fly = null;
  public set = true;
  public fri = true;
  public user = true;
  public option = true;
  public flight = {};
  public ListHotel = {};
  public proba= {};
  public avio;
  public hote;
  public carRes;
  public userDetail;
  public ticket;
  public ticketi2;
  public tickSearc;
  public tickSer;
  public ticket1;
  public ticket2;
  public ticket3;
  public ticket4;
  public ticket5;
  public ticket6;
  public ticket7;

  public numeracija = ['A','B','C','D','E','F'];
  public sedista =[];
  public friend;
  public praviTiket1=[];
  public tiketi=[];
  constructor(private flightService: FlightService, private http : HttpClient,
              private userProf : AppService) {
  }

  ngOnInit() {
    this.changeStepper();
    this.fly = null;
    this.set = true;
    this.fri = true;
    this.user = true;
    this.option = true;
    this.flightService.getAllTicket();
    this.userProf.getAllUsers();
    this.ticket = JSON.parse(localStorage.getItem('tickets'));

    this.userProf.userProfile();
    this.loginUser = JSON.parse(localStorage.getItem('userProfile'));

    console.log(this.loginUser);
    this.tickSearc =this.ticket;
    this.tickSer = this.ticket;

    if(localStorage.getItem('flights')==null)
    {
      return this.http.get('api/flights/all').subscribe(data => this.flight=data);
    }else
    {
      this.flight = JSON.parse(localStorage.getItem('flights'));
      localStorage.removeItem('flights');
    }
    console.log(this.flight);

  }

  flights() {
    this.fly = null;
    this.set = true;
    this.fri = true;
    this.user = true;
    this.option = true;
    this.ticket = JSON.parse(localStorage.getItem('tickets'));
    this.tickSearc=this.ticket;
    this.tickSer=this.ticket;
  }

  seats() {
    this.fly = true;
    this.set = null;
    this.fri = true;
    this.user = true;
    this.option = true;
  }

  friends() {
    if(this.sedista.length > 1)
    {
      this.fly = true;
      this.set = true;
      this.fri = null;
      this.user = true;
      this.option = true;
      this.userProf.getFriends();
      this.friend = JSON.parse(localStorage.getItem('friends'));
      var friendsMany = this.friend;
      console.log(friendsMany);

      this.Korisnik = JSON.parse(localStorage.getItem('possibleFriends'));
      localStorage.removeItem('possibleFriends');
      this.PunoFrenda = this.Korisnik;
      console.log(this.PunoFrenda);
      this.PunoFreKor=[];
      for(var i=0;i<this.PunoFrenda.length;i++)
      {
        for(var j=0;j<friendsMany.length;j++)
        {
          if(this.PunoFrenda[i].username == friendsMany[j])
          {
            this.PunoFreKor.push(this.PunoFrenda[i]);
          }
        }
      }
      this.selectFriend=[];
    }else{
      this.userData();
    }
  }
  friendsPrev()
  {
    if(this.sedista.length > 1) {
      this.fly = true;
      this.set = true;
      this.fri = null;
      this.user = true;
      this.option = true;
      this.userProf.getFriends();
      this.friend = JSON.parse(localStorage.getItem('friends'));
      var friendsMany = this.friend;
      console.log(friendsMany);

      this.Korisnik = JSON.parse(localStorage.getItem('possibleFriends'));
      localStorage.removeItem('possibleFriends');
      this.PunoFrenda = this.Korisnik;
      console.log(this.PunoFrenda);
      this.PunoFreKor = [];
      for (var i = 0; i < this.PunoFrenda.length; i++) {
        for (var j = 0; j < friendsMany.length; j++) {
          if (this.PunoFrenda[i].username == friendsMany[j]) {
            this.PunoFreKor.push(this.PunoFrenda[i]);
          }
        }
      }
      this.selectFriend = [];
    }else{
      this.seats();

    }
  }
  userData() {

    this.fly = true;
    this.set = true;
    this.fri = true;
    this.user = null;
    this.option = true;

    if(this.Ispis_polja.length > 0) {
      this.podaciKorisnika = [];
      for (var i = 0; i < this.tiketi.length - this.Ispis_polja.length - 1; i++) {
        this.podaciKorisnika[i] = {};
      }
    }else {
      this.podaciKorisnika = [];
      for (var i = 0; i < this.tiketi.length - 1; i++) {
        this.podaciKorisnika[i] = {};
      }
    }
  }
  hotel()
  {
    this.fly = true;
    this.set = true;
    this.fri = true;
    this.user = true;
    this.option = null;
  }
  chooseAvio(event)
  {
    event.preventDefault();
    const target = event.target;
    if(target.querySelector('input[name="gender"]:checked').value!=null)
    {
      this.avio = target.querySelector('input[name="gender"]:checked').value;
      console.log(this.avio);
      this.tickSearc = this.tickSer.filter((ticket) => ticket.flight.id == this.avio && ticket.reserved == false && ticket.fastReservationDiscount == 0);
      this.ticket=this.tickSearc;
      console.log(this.ticket);
      console.log("TIKETI ZA BAS TU AVIO KOMPANUJU");
          this.tickSer=this.tickSearc.filter((ticket) => ticket.seatNumber <= 6);
          this.ticket=this.tickSer;
          console.log(this.ticket);
          this.tickSer=this.tickSearc.filter((ticket) => ticket.seatNumber > 6 && ticket.seatNumber <=12);
          this.ticket1=this.tickSer;
          console.log(this.ticket1);
          this.tickSer=this.tickSearc.filter((ticket) => ticket.seatNumber >12 && ticket.seatNumber <= 18);
          this.ticket2=this.tickSer;
          console.log(this.ticket2);
          this.tickSearc=this.tickSer.filter((ticket) => ticket.seatNumber >18 && ticket.seatNumber <= 24);
          this.ticket3=this.tickSearc;
          this.tickSearc=this.tickSer.filter((ticket) => ticket.seatNumber >24 && ticket.seatNumber <= 30);
          this.ticket4=this.tickSearc;
          this.tickSearc=this.tickSer.filter((ticket) => ticket.seatNumber >30 && ticket.seatNumber <= 36);
          this.ticket5=this.tickSearc;
          this.tickSearc=this.tickSer.filter((ticket) => ticket.seatNumber >36 && ticket.seatNumber <= 42);
          this.ticket6=this.tickSearc;
          this.tickSearc=this.tickSer.filter((ticket) => ticket.seatNumber >42 && ticket.seatNumber <= 48);
          this.ticket7=this.tickSearc;
    }
  }

  choseSeats(id)
  {
    if(this.sedista.includes(id))
    {
      var del = this.sedista.indexOf(id);
      this.sedista[del]=0;
    }else
    {
      this.sedista.push(id);
    }
    console.log(this.sedista);
    this.flightService.getAllTicket();
    this.ticketi2 = JSON.parse(localStorage.getItem('tickets'));
    this.tickSearc =this.ticketi2;
    this.tickSer = this.ticketi2;
    this.tickSearc = this.tickSer.filter((ticket) => ticket.flight.id == this.avio);
    console.log(this.tickSearc);
    //this.praviTiket=this.tickSearc;
    this.praviTiket1=this.tickSearc;
    this.tiketi=[];
    for(var i=0;i<this.sedista.length;i++)
    {
      for(var j=0; j<this.praviTiket1.length;j++)
      {
        if(this.sedista[i] == this.praviTiket1[j].seatNumber)
        {
          this.tiketi.push(this.praviTiket1[j]);
        }
      }
    }
    this.podaciKorisnika=this.tiketi;
    this.changeStepper();
    console.log(this.podaciKorisnika);
  }

  chooseFriends(k)
  {
    var svi;

    if(this.selectFriend.indexOf(k) > -1)
    {
      this.selectFriend[this.selectFriend.indexOf(k)]=[];
    }else
    {
      this.selectFriend.push(k);
      console.log(this.selectFriend);
    }
    svi=this.PunoFreKor;
    this.fUkupno=[];
    for(var i=0;i<svi.length;i++)
    {
      for(var j=0;j<this.selectFriend.length;j++)
      {
        if(svi[i].username == this.selectFriend[j].username)
        {
          this.fUkupno.push(this.selectFriend[j]);
        }
      }
    }
    this.Ispis_polja = this.fUkupno;
    this.podaciKorisnika=[];
    for(var i=0;i<this.tiketi.length-this.Ispis_polja.length-1;i++)
    {
      this.podaciKorisnika[i]={};
    }

    console.log(this.podaciKorisnika);
  }

  userDet(event)
  {
    event.preventDefault();
    const target = event.target;
    const name = target.querySelector("#nam").value;
    const surname = target.querySelector("#sur").value;
    const passport = target.querySelector("#pass").value;

    this.tiketi[0].passengerName = name;
    this.tiketi[0].passengerLastName = surname;
    this.tiketi[0].passportNumber = passport;
    this.tiketi[0].passengerUsername = this.loginUser.username;

    for(var k=0;k<this.Ispis_polja.length;k++)
    {

      this.tiketi[k+1].passportNumber = target.querySelector("#pass"+this.Ispis_polja[k].id).value;
      this.tiketi[k+1].passengerLastName = this.Ispis_polja[k].surname;
      this.tiketi[k+1].passengerName = this.Ispis_polja[k].name;
      this.tiketi[k+1].passengerUsername = this.Ispis_polja[k].username;
    }

    console.log(JSON.stringify(this.tiketi));
    this.flightService.ReserveFlight(JSON.stringify(this.tiketi));
  }

  changeStepper()
  {
    $("#next1").click(function(){
      $("#1").removeClass("btn-primary");
      $("#2").removeClass("btn-default");
      $("#2").addClass("btn-primary");
    });
    $("#prev1").click(function(){
      $("#2").removeClass("btn-primary");
      $("#1").removeClass("btn-default");
      $("#1").addClass("btn-primary");
    });
      $("#next2").click(function(){
        $("#2").removeClass("btn-primary");
        $("#3").removeClass("btn-default");
        $("#3").addClass("btn-primary");
      });
      $("#prev2").click(function(){
        $("#3").removeClass("btn-primary");
        $("#2").removeClass("btn-default");
        $("#2").addClass("btn-primary");
      });
      $("#next3").click(function(){
        $("#3").removeClass("btn-primary");
        $("#4").removeClass("btn-default");
        $("#4").addClass("btn-primary");
      });
      $("#prev3").click(function(){
        $("#4").removeClass("btn-primary");
        $("#3").removeClass("btn-default");
        $("#3").addClass("btn-primary");
      });
    $("#next4").click(function(){
      $("#3").removeClass("btn-primary");
      $("#5").removeClass("btn-default");
      $("#5").addClass("btn-primary");
    });

  }
}
