import { Component, OnInit } from '@angular/core';
import {AvioCompanyService} from "./avio-company.service";
import {FlightService} from "../flight/flight.service";

@Component({
  selector: 'app-avio-company',
  templateUrl:'../avioCompany.html',
  styleUrls: ['./avio-company.component.css']
})
export class AvioCompanyComponent implements OnInit {

  public avioView={};
  public  avio;
  public letovi1;
  public letovi2;
  public letovi;
  public letovi3;
  constructor(private serviceAvio : AvioCompanyService,private flight : FlightService) { }

  ngOnInit() {
    this.serviceAvio.getAllAvioCompany();
    this.avio = JSON.parse(localStorage.getItem("avioCompany"));
    console.log(this.avio);
    this.flight.getAllFlight();
    this.letovi = JSON.parse(localStorage.getItem('flights'));
    console.log(this.letovi);

    localStorage.removeItem("avioCompany");
  }
  searchFlight(event)
  {
    event.preventDefault();
    var destinationAerodrom = event.target.querySelector("#dest").value;
    var arrivalTime = event.target.querySelector("#checkout_date2").value;
    var destinationTime = event.target.querySelector("#checkin_date1").value;
    this.letovi1=this.letovi;
    this.letovi2=this.letovi;

    if(destinationAerodrom && arrivalTime && destinationTime)
    this.letovi = this.letovi2.filter((letovi) => letovi.locationDTO.airport == destinationAerodrom && letovi.arrivalTime.substring(0,16) == arrivalTime && letovi.departureTime.substring(0,16) == destinationTime);
    else if(destinationAerodrom && arrivalTime)
    {
      this.letovi = this.letovi2.filter((letovi) => letovi.locationDTO.airport == destinationAerodrom && letovi.arrivalTime.substring(0,16) == arrivalTime);
    }else if(destinationAerodrom && destinationTime)
    {
      this.letovi = this.letovi2.filter((letovi) => letovi.locationDTO.airport == destinationAerodrom && letovi.departureTime.substring(0,16) == destinationTime);
    }else if(arrivalTime && destinationTime)
    {
      this.letovi = this.letovi2.filter((letovi) => letovi.arrivalTime.substring(0,16) == arrivalTime && letovi.departureTime.substring(0,16) == destinationTime);
    }else if(destinationAerodrom)
    {
      this.letovi = this.letovi2.filter((letovi) => letovi.locationDTO.airport == destinationAerodrom);
    }else if(arrivalTime)
    {
      this.letovi = this.letovi2.filter((letovi) => letovi.arrivalTime.substring(0,16) == arrivalTime);
    }else if(destinationTime)
    {
      this.letovi = this.letovi2.filter((letovi) => letovi.departureTime.substring(0,16) == destinationTime);
    }
    this.letovi2=this.letovi;
    console.log(this.letovi);


  }
  avioComp(av)
  {
    this.avioView=av;
    this.serviceAvio.getAvioCompany(av);
  }
  Sort(event)
  {
    event.preventDefault();
    var filterAvio = event.target.querySelector("#FilterByAvio").value;
    var filterTime = event.target.querySelector("#FilterByTime").value;
    var filterPrice = event.target.querySelector("#FilterByPrice").value;

    console.log(filterAvio,filterTime,filterPrice);

    this.letovi3 = this.letovi2;

    if(filterAvio && filterTime && filterPrice)
      this.letovi=this.letovi3.filter((flight) => flight.companyDTO.name == filterAvio && flight.flightTime == filterTime && flight.ticketPrice == filterPrice);
    else if(filterAvio && filterTime)
    {
      this.letovi=this.letovi3.filter((flight) => flight.companyDTO.name == filterAvio && flight.flightTime == filterTime);
    }else if(filterAvio && filterPrice)
    {
      this.letovi=this.letovi3.filter((flight) => flight.companyDTO.name == filterAvio && flight.ticketPrice == filterPrice);
    }else if(filterTime && filterPrice)
    {
      this.letovi=this.letovi3.filter((flight) => flight.flightTime == filterTime && flight.ticketPrice == filterPrice);
    }else if(filterAvio)
    {
      this.letovi=this.letovi3.filter((flight) => flight.companyDTO.name == filterAvio);
    }else if(filterTime)
    {
      this.letovi=this.letovi3.filter((flight) => flight.flightTime == filterTime);
    }else if(filterPrice)
    {
      this.letovi=this.letovi3.filter((flight) => flight.ticketPrice == filterPrice);
    }
      console.log(this.letovi);


  }
  chooseFlight(event)
  {
    event.preventDefault();
    var check = event.target.querySelector("input[name='gender']:checked").value;

    location.href = "/flight";
  }

}
