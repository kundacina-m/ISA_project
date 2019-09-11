import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthService} from "./login/auth.service";
import {a} from "@angular/core/src/render3";


@Injectable({
  providedIn: 'root'
})
export class AppService {

  constructor(private http: HttpClient, private auth: AuthService) {
  }

  public user;

  getUserLogin(credentials) {
    return this.http.post('api/auth/login',
      credentials
    ).subscribe(data => {
      localStorage.setItem('user', credentials['username']);
      this.auth.setJwtToken(data['accessToken']);

      return this.http.get('api/auth/profile')
        .subscribe(data => {
          this.user = data;

          console.log(this.user.passChanged);
          if (this.user.avioCompany || this.user.hotel || this.user.rentCar) {
            if (this.user.passChanged != true) {
              alert('Please change your password');
              location.href='/viewChangePass';
            }
            else {
              location.reload();
            }
          }else{
            location.reload();
          }
        });
    }, error => {
      alert("wrong username and/or password");
    })
  }

  changeUser(user) {
    console.log(user);
    return this.http.post('api/auth/changeInfo',
      {
        "username": user.username,
        "name": user.name,
        "surname": user.surname,
        "email": user.email,
        "address": user.address,
        "city": user.city,
        "number": user.number
      }).subscribe(data => location.reload());
  }

  getFriends() {
    return this.http.get(
      'api/friendships/friends'
    ).subscribe(data => {
      localStorage.setItem('friends', JSON.stringify(data));
    });
  }

  getAllUsers() {
    return this.http.get(
      'api/users/all'
    ).subscribe(
      data => {
        localStorage.setItem('possibleFriends', JSON.stringify(data));
      })
  }

  changeAvio(avio) {
    return this.http.put('api/aviocompanies/changeAvio',
      {
        "id": avio.id,
        "name": avio.name,
        "address": avio.address,
        "promoDescription": avio.promoDescription
      }).subscribe(data => location.reload());
  }

  changeHotel(hotel)
  {

    return this.http.put('api/hotel/changeHotel',{
      "id":hotel.id,
      "name":hotel.name,
      "address":hotel.address,
      "promoDescription": hotel.promoDescription
    }).subscribe(data=>location.reload());
  }

  changePassword(pass1, pass2) {
    this.http.post('api/auth/change-password',
      {
        "oldPassword": pass1,
        "newPassword": pass2
      }).subscribe(data => location.reload());
  }

  userProfile() {
    return this.http.get('api/auth/profile')
      .subscribe(data => {
        localStorage.setItem('userProfile', JSON.stringify(data));
      });
  }

  userLogout() {
    localStorage.removeItem('user');
    this.auth.removeJwtToken();
    location.reload();
  }

  getUserRegistration(username, password, name, surname, email, address, city, phone) {
    return this.http.post(
      'api/users/registerUser',
      {
        "username": username, "password": password, "name": name, "surname": surname, "email": email,
        "address": address, "city": city, "number": phone
      }).subscribe(
      data => {
        localStorage.setItem('alert', 'Please finish your authentication by clicking on a link that will soon be in your e-mail inbox.');
      }
    );
  }

  deleteLocation(loc) {
    return this.http.put(
      'api/aviocompanies/remove',
      {
        "id": loc.id,
        "name": loc.name
      }).subscribe(
      data => {
        alert('Location deleted');
        location.reload();
      });
  }

  addLocation(loc) {
    return this.http.put(
      'api/aviocompanies/addToAvio',
      {
        "id": loc.id,
        "name": loc.name
      }).subscribe(data => alert('Location added'));
  }


  addFlight(flightTime, ticketPrice, departure, arrival, destination, connectingCities) {
    console.log(flightTime);
    console.log(ticketPrice);
    console.log(departure);
    console.log(arrival);
    console.log(destination);


    return this.http.post('api/flights/add',
      {
        "flightTime": flightTime,
        "ticketPrice": ticketPrice,
        "arrivalTime": arrival,
        "departureTime": departure,
        "destination": destination,
        "connectingCities": connectingCities
      }).subscribe(data => alert('Flight successfully added'));
  }

  changeTicket(ticket) {
    return this.http.put('api/tickets/change', {
      "id": ticket.id,
      "seatNumber": ticket.seatNumber,
      "fastReservationDiscount": ticket.fastReservationDiscount
    }).subscribe(data => {
      alert('Ticket changed');
      location.reload();
    })
  }

  addTicket(flight, seatNumber, fastReservDisc) {
    console.log(flight);
    console.log(seatNumber);
    console.log(fastReservDisc);

    return this.http.post('api/tickets/add', {
      "flight": flight,
      "seatNumber": seatNumber,
      "fastReservationDiscount": fastReservDisc
    }).subscribe(data => {
      alert('Ticket added');
      location.reload();
    })
  }

  removeTicket(id) {
    return this.http.put('api/tickets/remove',
      id
    ).subscribe(data => {
      alert('Ticket deleted');
      location.reload();
    })
  }

  //omogucava postavljanje usera kada se uloguje ili registruje
  getUser(username) {
    return this.http.get('api/users/profile').subscribe(data => {
      console.log(data), sessionStorage.setItem("user", data['username']);
    });
  }

  changeAuthority(username,auth)
  {
    return this.http.put('api/users/changeAuth',
      {"username":username,
              "authority":auth
            }).subscribe(data => alert("USPESNO IZVRSENA IZMENA"));
  }
  delAuthority(username,auth)
  {
    return this.http.post('api/users/delAuth',
      {"username":username,
        "authority":auth
      }).subscribe(data => alert("USPESNO IZVRSENA IZMENA"));
  }
  addHUser(username,a)
  {
    return this.http.put('api/users/changePriv',{"username":username,"name":a}).subscribe(data => data);
  }
  addAUser(username,a)
  {
    return this.http.put('api/users/changePriv',{"username":username,"name":a}).subscribe(data => data);
  }
  addRUser(username,a)
  {
    return this.http.put('api/users/changePriv',{"username":username,"name":a}).subscribe(data => data);
  }
}
