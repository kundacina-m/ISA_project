import {Component, OnInit} from '@angular/core';
import {AvioCompanyService} from "../avio-company/avio-company.service";
import {FlightService} from "../flight/flight.service";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-view-avio',
  templateUrl: './viewAvio.html',
  styleUrls: ['./view-avio.component.css']
})
export class ViewAvioComponent implements OnInit {

  public user;
  public viewAvio = {};
  public company;
  public flight = {};
  public flights = [];
  public ourTickets;
  public displayTickets = [];

  constructor(private avio: AvioCompanyService, private flightService: FlightService, private http: HttpClient) {
  }

  ngOnInit() {
    this.flightService.getAllFlight();
    this.flight = JSON.parse(localStorage.getItem('flights'));
    this.flights = JSON.parse(localStorage.getItem('flights'));
    console.log(this.flight);
    this.avio.newData.subscribe(data => {
        this.viewAvio = data;
        this.company = data;
        console.log(data);

        let u = localStorage.getItem("user");

        console.log(localStorage.getItem("user"));
        if (u) {
          console.log('usao sam ovde')
          this.http.get('api/auth/profile'
          ).subscribe(data => {
            this.user = data;

            this.http.get('api/tickets/all'
            ).subscribe(data => {
              this.ourTickets = data;

              this.flights = this.flights.filter((flight) => flight.companyDTO.id == this.company.id);

              for (let i = 0; i < this.flights.length; i++) {

                let ticks = this.ourTickets.filter((ticket) => ticket.flight.id == this.flights[i].id);
                ticks = ticks.filter((ticket) => ticket.fastReservationDiscount > 0);

                for (let j = 0; j < ticks.length; j++)
                  this.displayTickets.push(ticks[j]);
              }

            });
          }, error => {
            console.log(error);
            this.user = undefined;
            return;
          });
        }
      });
  }

  reserveTicket(ticket) {
    ticket.passengerUsername = this.user.username;
    ticket.passengerName = this.user.name;
    ticket.passengerLastName = this.user.surname;
    this.flightService.ReserveFlight([ticket]);
  }

}
