import {Component, OnInit} from '@angular/core';
import {AppService} from "../app.service";
import {HttpClient} from "@angular/common/http";
import {AvioCompanyService} from "../avio-company/avio-company.service";
import {Chart} from "chart.js";

declare var $: any;

@Component({
  selector: 'app-user-profile',
  templateUrl: '../userProfile.html',
  styleUrls: ['./user-profile.component.css',
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
    '../home/css/style.css'],
})

export class UserProfileComponent implements OnInit {

  constructor(private service: AppService, private http: HttpClient,
              private avioService:AvioCompanyService) {}

  LineChart=[];
  public prihodHotela=0;
  public incomeHotelDay = 0;
  public incomeHotelMonth = 0;
  public incomeHotelWeek = 0;
  public reservedRoom;
  public aaaaaa;
  public del;
  public user;
  public authority1;
  public authority2;
  public auth;
  public idAvio;
  public idRenta;
  public idHotel;
  public users;
  public authority;
  public flight;
  public createAvio = true;
  public createHotel = true;
  public createRent = true;
  public allAuthority;
  public userProfile;
  public HotelFavourOne;
  public HotelFavour;
  public HotelFavour1;
  public HotelFavour2;
  public RoomHotel;
  public RoomHotell;
  public RoomHotel2;
  public editRoomId;
  public addFavour = true;
  public deleteRoom;
  public deleteRoom1;
  public showAddRoom = true;
  public isHidden = true;
  public isHiddenSearch = true;
  public isHiddenRequest = true;
  public newLocationHidden = true;
  public deleteLocationHidden = true;
  public addFlightHidden = true;
  public allFlightsHidden = true;
  public allTicketsHidden = true;
  public allRoomHidden = true;
  public allPricesHidden = true;
  public friends;
  public possibleFriends;
  public possibleFriendsFilter;
  public friendRequests;
  public possibleLocations;
  public connectingCities = [];
  public ourFlights;
  public ourTickets;
  public unreservedTickets;
  public fastReserveTickets;
  public pickedTicket;
  public pickedFlight;
  public pricing;
  public avioIncome = 0;

  ngOnInit() {

    this.allFlight();

    this.changeStepper();
    return this.http.get('api/auth/profile')
      .subscribe(data => {
        this.userProfile = data;
        console.log(this.userProfile);
        this.authority = this.userProfile.authority[0].name;
        console.log(this.authority);
        this.ChartAvio();
        if(this.userProfile.avioCompany != null)

        this.http.get('api/aviocompanies/allOurTicketsAll'
        ).subscribe(data => {
          this.aaaaaa = data;

          for (let i = 0; i < this.aaaaaa.length; i++) {
            if (this.aaaaaa[i].reserved == true) {
              this.avioIncome += this.aaaaaa[i].flight.ticketPrice;
            }
          }
        });

        this.allUsers();
        this.Authority();
        this.allAvio();

        if (this.userProfile.hotel != null) {
          this.ChartHotel();
        }
      });
  }

  searchByName(event) {
    event.preventDefault();

    let name = event.target.querySelector("#nameSearch").value;
    let surname = event.target.querySelector("#lastNameSearch").value;

    if (name)
      this.possibleFriends = this.possibleFriendsFilter.filter((friend) => friend.name == name);

    if (surname)
      this.possibleFriends = this.possibleFriendsFilter.filter((friend) => friend.surname == surname);

    console.log(name + '  ' + surname);
  }

  changeInfo(event) {
    event.preventDefault();

    let pass1 = event.target.querySelector("#pass1").value;
    let pass2 = event.target.querySelector("#pass2").value;

    this.userProfile.username = event.target.querySelector("#usernameChange").value;
    this.userProfile.email = event.target.querySelector("#emailChange").value;
    this.userProfile.city = event.target.querySelector("#cityChange").value;
    this.userProfile.name = event.target.querySelector("#nameChange").value;
    this.userProfile.surname = event.target.querySelector("#surnameChange").value;
    this.userProfile.number = event.target.querySelector("#numberChange").value;

    this.service.changeUser(this.userProfile);

    if ((!pass1 && pass2) || (pass1 && !pass2)) {
      alert('Please fill both password fields');
      return;
    }

    if (pass1 && pass2)
      this.service.changePassword(pass1, pass2);
  }

  changeAvioInfo(event) {
    event.preventDefault();
    this.userProfile.avioCompany.name = event.target.querySelector("#avioName").value;
    this.userProfile.avioCompany.address = event.target.querySelector("#avioAddress").value;
    this.userProfile.avioCompany.promoDescription = event.target.querySelector("#avioPromoDescription").value;

    this.service.changeAvio(this.userProfile.avioCompany);
  }



  friendList() {
    return this.http.get(
      'api/friendships/friends'
    ).subscribe(data => {
      this.friends = data;

      this.isHidden = null;
      this.isHiddenRequest = true;
      this.isHiddenSearch = true;

    });
  }

  searchFriends() {
    return this.http.get(
      '/api/auth/allNonFriends'
    ).subscribe(
      data => {
        this.possibleFriends = data;
        this.possibleFriendsFilter = data;

        this.isHidden = true;
        this.isHiddenRequest = true;
        this.isHiddenSearch = null;
      })

  }

  addFriend(friendUsername) {
    console.log(friendUsername);
    return this.http.post(
      'api/friendships/add',
      friendUsername
    ).subscribe(
      data => {
        alert('Friend request sent');
        location.reload();
      }
    )
  }

  getFriendRequests() {
    return this.http.get(
      'api/friendships/friendRequests'
    ).subscribe(
      data => {
        this.friendRequests = data;

        this.isHidden = true;
        this.isHiddenSearch = true;
        this.isHiddenRequest = null;
      }
    );
  }

  acceptFriendRequest(sender) {
    return this.http.put(
      'api/friendships/accept',
      sender
    ).subscribe(
      data => {
        alert('Friend request accepted.');
        location.reload();
      })
  }

  removeFriend(friend) {
    return this.http.put(
      'api/friendships/remove',
      friend
    ).subscribe(
      data => {
        alert('Friend removed from friend list.');
        location.reload();
      })
  }

  showLocationDialog() {
    if (this.newLocationHidden == null)
      this.newLocationHidden = true;
    else
      this.newLocationHidden = null;

    this.http.get(
      'api/aviocompanies/allNotMine',
    ).subscribe(data => {
      this.possibleLocations = data;
      console.log(data)
    });
  }

  showLocationDeleteDialog() {
    if (this.deleteLocationHidden == null)
      this.deleteLocationHidden = true;
    else
      this.deleteLocationHidden = null;
  }

  ShowAddRoomDialog() {
    if (this.showAddRoom == null)
      this.showAddRoom = true;
    else
      this.showAddRoom = null;

  }

  deleteLocation(location) {
    this.service.deleteLocation(location);
  }

  addLocation(loc) {
    this.service.addLocation(loc);
  }

  showFlightDialog() {
    if (this.addFlightHidden == null)
      this.addFlightHidden = true;
    else {
      this.addFlightHidden = null;
      this.connectingCities = [];
    }
  }

  addFlight(event) {
    event.preventDefault();
    let flightTime = event.target.querySelector("#flightTimeFlight").value;
    let ticketPrice = event.target.querySelector("#ticketPriceFlight").value;
    let departure = event.target.querySelector("#departure").value;
    let arrival = event.target.querySelector("#arrival").value;
    let destinationId = event.target.querySelector("#destinationFlight").value;

    let destination = this.userProfile.avioCompany.locations.filter((location) => location.id == destinationId);


    console.log(flightTime);
    console.log(ticketPrice);
    console.log(departure);
    console.log(arrival);
    console.log(destination);
    this.service.addFlight(flightTime, ticketPrice, departure, arrival, destination[0], this.connectingCities);
  }


  addLocationToFlight(location) {
    console.log(location);
    if (this.connectingCities.filter((loc) => loc == location).length == 0) {
      this.connectingCities.push(location);
      alert('Added');
      console.log(this.connectingCities);
    } else {
      alert('Already added');
    }
  }

  showAllFlightsDialog() {
    if (this.allFlightsHidden == null){
      this.allFlightsHidden = true;
      // this.ourFlights = JSON.parse(localStorage.getItem("tabelaLetova"));
      // console.log(this.ourFlights);
    }
    else {
      this.allFlightsHidden = null;
    }
  }

  showAllRoomDialog() {
    if (this.allRoomHidden == null)
      this.allRoomHidden = true;
    else {
      this.allRoomHidden = null;
    }
  }

  showAllTicketsDialog() {
    if (this.allTicketsHidden == null)
      this.allTicketsHidden = true;
    else {
      this.allTicketsHidden = null;

      return this.http.get('api/aviocompanies/allOurTickets'
      ).subscribe(data => {
        this.ourTickets = data;
        console.log(this.ourTickets)
        this.unreservedTickets = this.ourTickets.filter((ticket) => ticket.fastReservationDiscount == 0);
        this.fastReserveTickets = this.ourTickets.filter((ticket) => ticket.fastReservationDiscount != 0);
      })
    }
  }

  showAllPricesDialog() {
    if (this.allPricesHidden == null)
      this.allPricesHidden = true;
    else {
      this.allPricesHidden = null;
    }
  }

  changeTicket(event) {
    event.preventDefault();
    let id = event.target.querySelector("#id" + this.pickedTicket).value;
    let flight = event.target.querySelector("#flight" + this.pickedTicket).value;
    let seatNumber = event.target.querySelector('input[name="proba' + id + '"]').value;
    let discount = event.target.querySelector('input[name="proba1' + id + '"]').value;
    console.log(id);
    console.log(seatNumber);
    console.log(flight)

    let tickets = this.ourTickets.filter((ticket) => ticket.flight.id == flight);
    tickets = tickets.filter((ticket) => ticket.id != id);
    tickets = tickets.filter((ticket) => ticket.seatNumber == seatNumber);

    if (tickets.length > 0)
      alert('Seat number already exists');
    else {
      let ticket = this.ourTickets.filter((ticket) => ticket.id == id)[0];
      ticket.seatNumber = seatNumber;
      ticket.fastReservationDiscount = discount;
      console.log(ticket);


      this.service.changeTicket(ticket);
    }
  }

  addTicket(event) {
    event.preventDefault();
    let id = this.pickedFlight.id;
    console.log(this.pickedFlight);
    let seatNumber = event.target.querySelector('input[name="proba' + id + '"]').value;
    let fastReservDisc = event.target.querySelector('input[name="proba1' + id + '"]').value;
    console.log(this.pickedFlight);
    console.log(seatNumber);
    console.log(fastReservDisc);

    return this.http.get('api/aviocompanies/allOurTickets'
    ).subscribe(data => {
      this.ourTickets = data;

      let tickets = this.ourTickets.filter((ticket) => ticket.flight.id == id);
      tickets = tickets.filter((ticket) => ticket.seatNumber == seatNumber);

      if (tickets.length > 0)
        alert('Seat number already exists');
      else {
        this.service.addTicket(this.pickedFlight, seatNumber, fastReservDisc);
      }
    });
  }

  pickTicket(id) {
    this.pickedTicket = id;
  }

  addFavourPrice(event) {
    event.preventDefault();
    var name = event.target.querySelector("#favourName").value;
    var price = event.target.querySelector("#pri").value;
    var discount = event.target.querySelector("#dis").value;
    var postoji = false;
    this.HotelFavour1 = this.HotelFavour;
    console.log(this.HotelFavour1[0].priceList[0].name);
    for (var i = 0; i < this.HotelFavour1[0].priceList.length; i++) {
      if (this.HotelFavour1[0].priceList[i].name == name) {
        alert("Postoji vec ta dodatna usluga");
        postoji = true;
        break;
      }
    }
    if (!postoji) {
      //this.roomService.addFavour(this.userProfile.hotel.id, name, price, discount);
    }
  }

  allUsers() {
    return this.http.get('api/users/all').subscribe(data => {
      this.users = data;
      console.log(this.users);
    });
  }

  Authority() {
    return this.http.get('api/authority/all').subscribe(data => this.allAuthority = data);
  }

  allFlight() {
    return this.http.get('api/flights/all').subscribe(data => {
      this.ourFlights = data;
      // console.log("pozvao sve letove");
      // localStorage.setItem("tabelaLetova",JSON.stringify(data));
  
    })
  }

  ShowAvioDialog() {
    if (this.createAvio == null)
      this.createAvio = true;
    else {
      this.createAvio = null;
    }
  }

  ShowHotelDialog() {
    if (this.createHotel == null)
      this.createHotel = true;
    else {
      this.createHotel = null;
    }
  }

  ShowRentDialog() {
    if (this.createRent == null)
      this.createRent = true;
    else {
      this.createRent = null;
    }
  }

  changeAuthority(event) {
    event.preventDefault();
    var postoji = false;
    if (this.auth == undefined) {
      var type = event.target.querySelector("#changeAuth" + this.del.username).value;
      for (var k = 0; k < this.del.authority.length; k++) {
        if (this.del.authority[k].name == type) {
          postoji = true;
          break;
        }
      }
      if (!postoji) {
        alert('User nema zadati autoritet!');
      } else {
        this.authority1 = this.allAuthority;
        this.authority2 = this.allAuthority;
        this.authority1 = this.authority2.filter((authority1) => authority1.name == type);
        this.service.delAuthority(this.del.username, this.authority1);
      }
    }
    else {
      var type = event.target.querySelector("#changeAuth" + this.auth.username).value;
      for (var k = 0; k < this.auth.authority.length; k++) {
        if (this.auth.authority[k].name == type) {
          postoji = true;
          break;
        }
      }
      if (postoji) {
        alert('Autoritet je vec dodat!');
      } else {
        this.authority1 = this.allAuthority;
        this.authority2 = this.allAuthority;
        this.authority1 = this.authority2.filter((authority1) => authority1.name == type);
        this.service.changeAuthority(this.auth.username, this.authority1);
      }
    }
  }

  addAndChange(event) {
    event.preventDefault();
    console.log(this.user.username);
    for (var i = 1; i < this.user.authority.length; i++) {
      if (this.user.authority[i].name == "AVIO_ADMIN") {
        var a = event.target.querySelector("#Idavio" + this.user.username).value;
        this.service.addHUser(this.user.username, a);
      } else if (this.user.authority[i].name == "HOTEL_ADMIN") {
        var hotel = event.target.querySelector("#Idhot" + this.user.username).value;
        this.service.addAUser(this.user.username, hotel);
        console.log(hotel);
      } else if (this.user.authority[i].name == "RENTA_ADMIN") {
        var renta = event.target.querySelector("#Idrenta" + this.user.username).value;
        console.log(renta);
        this.service.addRUser(this.user.username, renta);

      }
    }
  }

  change(user) {
    this.user = user;
  }

  chaAuth(user) {
    this.auth = user;
  }

  delAuth(user) {
    this.del = user;
  }

  createAvioo(event) {
    event.preventDefault();
    var name = event.target.querySelector("#avNa").value;
    var address = event.target.querySelector("#avAddr").value;
    var promo = event.target.querySelector("#avPromo").value;

    this.avioService.addAvio(name, address, promo);
  }

  allAvio() {
    return this.http.get('api/rent/all').subscribe(data => this.idRenta = data);
  }

  changeStepper() {
    $("#P").click(function () {
      $("#A").removeClass("active");
      $("#H").removeClass("active");
      $("#C").removeClass("active");
      $("#U").removeClass("active");
      $("#P").addClass("active");
    });
    $("#A").click(function () {
      $("#P").removeClass("active");
      $("#H").removeClass("active");
      $("#C").removeClass("active");
      $("#U").removeClass("active");
      $("#A").addClass("active");
    });
    $("#C").click(function () {
      $("#A").removeClass("active");
      $("#H").removeClass("active");
      $("#P").removeClass("active");
      $("#U").removeClass("active");
      $("#C").addClass("active");
    });
    $("#prev2").click(function () {
      $("#3").removeClass("btn-primary");
      $("#2").removeClass("btn-default");
      $("#2").addClass("btn-primary");
    });
    $("#next3").click(function () {
      $("#3").removeClass("btn-primary");
      $("#4").removeClass("btn-default");
      $("#4").addClass("btn-primary");
    });
    $("#prev3").click(function () {
      $("#4").removeClass("btn-primary");
      $("#3").removeClass("btn-default");
      $("#3").addClass("btn-primary");
    });
  }

  deleteTicket(id) {
    this.service.removeTicket(id);
  }

  pickFlight(id) {
    this.pickedFlight = id;
  }

  changePricing(event) {
    event.preventDefault();
    let id = this.pricing.id;
    let name = event.target.querySelector('input[name="priceName' + id + '"]').value;
    let price = event.target.querySelector('input[name="pricePrice' + id + '"]').value;
    console.log(name);
    console.log(price);


    return this.http.put('api/avioprices/change', {
      "id": this.pricing.id,
      "name": name,
      "price": price
    }).subscribe(data => {
      alert('Pricing successfully changed');
      location.reload();
    })
  }

  deletePricing(pricing) {
    return this.http.put('api/avioprices/remove', {
      "id": pricing.id,
      "name": pricing.name,
      "price": pricing.price
    }).subscribe(data => {
      alert('Pricing successfully deleted');
      location.reload();
    })
  }

  pickPricing(pricing) {
    this.pricing = pricing;
  }

  addPricing(event) {
    event.preventDefault();

    let name = event.target.querySelector("#name").value;
    let price = event.target.querySelector("#price").value;

    if (!name || !price) {
      alert('Please fill both fields');
      return;
    }

    this.http.post('api/avioprices/add', {
      "name": name,
      "price": price
    }).subscribe(data => {
      alert('Pricing successfully added');
      location.reload();
    })
  }
  ChartHotel()
  {
    return this.http.get('api/hotelReservation/all').subscribe(data => {
      this.reservedRoom = data;
      console.log("Rezervisane sobe");
      console.log(this.reservedRoom);
      if(this.reservedRoom.length > 0)
      {
        for(var i=0;i<this.reservedRoom.length;i++)
        {
          for(var j=0;j<this.reservedRoom[i].rooms.length;j++)
          {
            console.log(this.reservedRoom[i].rooms[j].date_in);
            if(this.reservedRoom[i].rooms[j].date_in == "2019-01-26")
                this.incomeHotelDay = this.incomeHotelDay + (+this.reservedRoom[i].rooms[j].number_bed);
            if(this.reservedRoom[i].rooms[j].date_in >= "2019-01-26" && this.reservedRoom[i].rooms[j].date_out <= "2019-02-26")
                this.incomeHotelMonth = this.incomeHotelMonth + (+this.reservedRoom[i].rooms[j].number_bed)
            if(this.reservedRoom[i].rooms[j].date_in >= "2019-01-26" && this.reservedRoom[i].rooms[j].date_out <= "2018-02-01")
                this.incomeHotelWeek = this.incomeHotelWeek + (+this.reservedRoom[i].rooms[j].number_bed);
            //sracunati prihodi rezervisanih hotela u svim sobama
            this.prihodHotela = this.prihodHotela + (+this.reservedRoom[i].rooms[j].price);
          }
        }
      }
      console.log("DAY");
      console.log(this.incomeHotelDay);
      console.log("MONTH");
      console.log(this.incomeHotelMonth);
      console.log("WEEK");
      console.log(this.incomeHotelWeek);

        var a = document.getElementById("chartContainer");
        console.log(a);

        this.LineChart = new Chart(a, {
          type: 'bar',
          data: {
            labels: ["Day", "Week", "Month"],
            datasets: [{
              data: [3, 31, 42],
              fill: false,
              lineTension: 0.2,
              borderColor: "red",
              borderWidth: 1
            }]
          },
          options: {
            title: {
              text: "Hotel income",
              display: true
            },
            scales: {
              yAxes: [{
                ticks: {
                  beginAtZero: true
                }
              }]
            }
          }
        });
      })
  }
  ChartAvio()
  {
    var a = document.getElementById("chartContainer1");
    console.log(a);

    this.LineChart = new Chart(a, {
      type: 'bar',
      data: {
        labels: ["Day", "Week", "Month"],
        datasets: [{
          data: [5, 30, 60],
          fill: false,
          lineTension: 0.2,
          borderColor: "red",
          borderWidth: 1
        }]
      },
      options: {
        title: {
          text: "Avio tickets",
          display: true
        },
        scales: {
          yAxes: [{
            ticks: {
              beginAtZero: true
            }
          }]
        }
      }
    });
  }
}

