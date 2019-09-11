import { Component, OnInit } from '@angular/core';
import {AvioCompanyService} from "../avio-company/avio-company.service";
import {FlightService} from "../flight/flight.service";


@Component({
  selector: 'app-home1',
  templateUrl: '../home1.html',
  styleUrls: ['/home1.component.css',
    '../home/css/animate.css',
    '../home/css/aos.css',
    '../home/css/bootstrap/bootstrap-grid.css',
    '../home/css/bootstrap/bootstrap-reboot.css',
    '../home/css/flaticon.css',
    '../home/css/icomoon.css',
    '../home/css/ionicons.min.css',
    '../home/css/jquery.timepicker.css',
    '../home/css/magnific-popup.css',
    '../home/css/open-iconic-bootstrap.min.css',
    '../home/css/owl.theme.default.min.css',
    '../home/css/style.css']
})
export class Home1Component implements OnInit {

  HotName;

  public avio = {};

  public avioView;
  public flightSearc;
  public flightSer;
  public flight;
  public location;
  public locationSer;
  public locationSearch;

  public destinacija={};

  constructor(private serviceAvio : AvioCompanyService,
              private flightServ : FlightService) { }

  ngOnInit() {
    this.serviceAvio.getAllAvioCompany();
    this.avio = JSON.parse(localStorage.getItem("avioCompany"));
    localStorage.removeItem("avioCompany");

    this.flightServ.getAllFlight();
    this.flight = JSON.parse(localStorage.getItem('flights'));
    localStorage.removeItem('flights');
    this.flightServ.getLocation();
    this.location = JSON.parse(localStorage.getItem('location'));
    console.log(this.location);
    localStorage.removeItem('location');
  }

  searchFlight(event)
  {
    event.preventDefault();
    const target = event.target;
    const from = target.querySelector('#dest').value;
    const date_in = target.querySelector('#checkin_date1').value;
    const date_out = target.querySelector('#checkout_date2').value;
    const price = target.querySelector('#price').value;
    this.flightSer = this.flight;
    this.flightSearc = this.flight;
    if(from && date_in && date_out && price)
    {
      this.destinacija = from.split(",");
      this.flightSearc=this.flightSer.filter((flight) => (flight.locationDTO.city === this.destinacija[0] && flight.locationDTO.country === this.destinacija[1])&& date_out == JSON.stringify(flight.arrivalTime).substr(1, 16) && date_in == JSON.stringify(flight.departureTime).substr(1, 16) && price === flight.ticketPrice);
      localStorage.setItem('flights',JSON.stringify(this.flightSearc));
      location.href = "http://localhost:4200/flight";
    }else if(from && date_out && price)
  {
    this.destinacija = from.split(",");
    this.flightSearc=this.flightSer.filter((flight) => (flight.locationDTO.city === this.destinacija[0] && flight.locationDTO.country === this.destinacija[1])&& date_out == JSON.stringify(flight.arrivalTime).substr(1, 16) && price === flight.ticketPrice);
    localStorage.setItem('flights',JSON.stringify(this.flightSearc));
    location.href = "http://localhost:4200/flight";
    }else if(date_in && date_out && price)
    {
    this.flightSearc=this.flightSer.filter((flight) => date_out == JSON.stringify(flight.arrivalTime).substr(1, 16) && date_in == JSON.stringify(flight.departureTime).substr(1, 16) && price === flight.ticketPrice);
    localStorage.setItem('flights',JSON.stringify(this.flightSearc));
    location.href = "http://localhost:4200/flight";
    }else if(from && date_in && date_out)    {
    this.destinacija = from.split(",");
    this.flightSearc=this.flightSer.filter((flight) => (flight.locationDTO.city === this.destinacija[0] && flight.locationDTO.country === this.destinacija[1]) && date_out == JSON.stringify(flight.arrivalTime).substr(1, 16) && date_in == JSON.stringify(flight.departureTime).substr(1, 16));
    localStorage.setItem('flights',JSON.stringify(this.flightSearc));
    location.href = "http://localhost:4200/flight";
  }else if(from && date_in && price)
  {
    this.destinacija = from.split(",");
    this.flightSearc=this.flightSer.filter((flight) => (flight.locationDTO.city === this.destinacija[0] && flight.locationDTO.country === this.destinacija[1]) && date_in == JSON.stringify(flight.departureTime).substr(1, 16) && price === flight.ticketPrice);
    localStorage.setItem('flights',JSON.stringify(this.flightSearc));
    location.href = "http://localhost:4200/flight";
  }else if(from && date_in)
  {
    this.destinacija = from.split(",");
    this.flightSearc=this.flightSer.filter((flight) => (flight.locationDTO.city === this.destinacija[0] && flight.locationDTO.country === this.destinacija[1]) && date_in == JSON.stringify(flight.departureTime).substr(1, 16));
    localStorage.setItem('flights',JSON.stringify(this.flightSearc));
    location.href = "http://localhost:4200/flight";

  }else if(from && date_out)
  {
    this.destinacija = from.split(",");
    this.flightSearc=this.flightSer.filter((flight) => (flight.locationDTO.city === this.destinacija[0] && flight.locationDTO.country === this.destinacija[1]) && date_out == JSON.stringify(flight.arrivalTime).substr(1, 16));
    localStorage.setItem('flights',JSON.stringify(this.flightSearc));
    location.href = "http://localhost:4200/flight";
  }else if(from && price)
  {
    this.destinacija = from.split(",");
    this.flightSearc=this.flightSer.filter((flight) => (flight.locationDTO.city === this.destinacija[0] && flight.locationDTO.country === this.destinacija[1])&& price == flight.ticketPrice);
    localStorage.setItem('flights',JSON.stringify(this.flightSearc));
    location.href = "http://localhost:4200/flight";
  }else if(date_in && date_out)
  {
    this.flightSearc=this.flightSer.filter((flight) => date_out == JSON.stringify(flight.arrivalTime).substr(1, 16) && date_in == JSON.stringify(flight.departureTime).substr(1, 16));
    localStorage.setItem('flights',JSON.stringify(this.flightSearc));
    location.href = "http://localhost:4200/flight";
  }else if(date_in && price)
  {
    this.flightSearc=this.flightSer.filter((flight) => date_in == JSON.stringify(flight.arrivalTime).substr(1, 16) && flight.ticketPrice === price);
    localStorage.setItem('flights',JSON.stringify(this.flightSearc));
    location.href = "http://localhost:4200/flight";
  }else if(date_out && price)
  {
    this.flightSearc=this.flightSer.filter((flight) => date_out == JSON.stringify(flight.arrivalTime).substr(1, 16) && flight.ticketPrice === price);
    localStorage.setItem('flights',JSON.stringify(this.flightSearc));
    location.href = "http://localhost:4200/flight";
  }else if(from)
  {
    this.destinacija = from.split(",");
    this.flightSearc=this.flightSer.filter( (flight) => flight.locationDTO.city === this.destinacija[0] && flight.locationDTO.country === this.destinacija[1]);
    console.log(this.flightSearc);
    localStorage.setItem('flights',JSON.stringify(this.flightSearc));
    location.href = "http://localhost:4200/flight";
  }else if(date_in)
  {
    console.log(date_in);
    this.flightSearc=this.flightSer.filter((flight) => date_in == JSON.stringify(flight.departureTime).substr(1, 16));
    localStorage.setItem('flights',JSON.stringify(this.flightSearc));
    location.href = "http://localhost:4200/flight";

  }else if(date_out)
  {
    this.flightSearc=this.flightSer.filter((flight) => date_out == JSON.stringify(flight.arrivalTime).substr(1, 16));
    localStorage.setItem('flights',JSON.stringify(this.flightSearc));
    location.href = "http://localhost:4200/flight";
  }else if(price)
  {
    this.flightSearc=this.flightSer.filter((flight) => flight.ticketPrice === price)
    localStorage.setItem('flights',JSON.stringify(this.flightSearc));
    location.href = "http://localhost:4200/flight";
  }
  }

  avioComp(av)
  {
    this.avioView = av;
    this.serviceAvio.getAvioCompany(av);
  }
}
