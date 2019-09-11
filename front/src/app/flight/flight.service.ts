import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class FlightService {

  constructor(private http : HttpClient) { }

  getAllFlight()
  {
    return this.http.get('api/flights/all').subscribe(data=>localStorage.setItem('flights',JSON.stringify(data)));
  }

  ReserveFlight(data)
  {
     return this.http.post('api/reservations/reserveFlight',data).subscribe(data=>{ alert('USPESNO REZERVISAN LET') });
  }
  getLocation()
  {
    return this.http.get('api/locations/all').subscribe(data=>localStorage.setItem('location',JSON.stringify(data)));
  }
  getAllTicket()
  {
    return this.http.get('api/tickets/all').subscribe(data=>localStorage.setItem('tickets',JSON.stringify(data)));
  }

  getReservation()
  {
    return this.http.get('api/reservations/all').subscribe(data=>localStorage.setItem('reservedFlight',JSON.stringify(data)));
  }

}
