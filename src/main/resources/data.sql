insert into jdbc.avio_company (name,image,promo_description) values ('Air France','airfrance.jpg','Promo Opis AirFrance');
insert into jdbc.avio_company (name,image,promo_description)
values ('Lufthansa','lufthansa.jpg','The Lufthansa Group is an aviation company with operations worldwide. In the financial year 2017, the Lufthansa Group generated revenue of EUR 35.6bn and employed an average of 128,856 staff.');
insert into jdbc.avio_company (name,image,promo_description)
values ('Air Serbia','airserbia.png','Er Srbija već leti do ukupno 40 destinacije u Evropi, na Mediteranu, Bliskom istoku i u Severnoj Americi, kako u putničkom, tako i u teretnom saobraćaju.');
insert into jdbc.avio_company (name,image,promo_description)
values ('Swiss','swiss.png','SWISS is The Airline of Switzerland. It serves over 100 destinations all over the world from Zurich, Geneva and Lugano. SWISS is part of the Lufthansa Group and a member of the Star Alliance.');

insert into jdbc.user (username, email, pass, first_name, last_name, city, phone_number, enabled, pass_change_req)
values ('user1', 'user1@gmail.com', '$2a$10$uT0lZ2AQG2qNEVZn5zQfP.tng7Di09OEH4OwHK4bx4gdpYSqLQds2', 'Korisnik1', 'Korisnikovic', 'Bileca', '0691-1111111', true , true);

insert into jdbc.user (username, email, pass, first_name, last_name, city, phone_number, enabled, pass_change_req, avio_company)
values ('user2', 'user2@gmail.com', '$2a$10$uT0lZ2AQG2qNEVZn5zQfP.tng7Di09OEH4OwHK4bx4gdpYSqLQds2', 'Korisnik2', 'Useric', 'Novi Sad', '062-22222222', true, true, 3);

insert into jdbc.user (username, email, pass, first_name, last_name, city, phone_number, enabled,pass_change_req)
values ('user3', 'user3@gmail.com', '$2a$10$uT0lZ2AQG2qNEVZn5zQfP.tng7Di09OEH4OwHK4bx4gdpYSqLQds2', 'Korisnik3', 'Admin', 'Beograd', '063-3333333', true,true);

insert into jdbc.avio_prices (name, price, avio_company) values ('Drink', 5, 1);
insert into jdbc.avio_prices (name, price, avio_company) values ('Food', 25, 1);
insert into jdbc.avio_prices (name, price, avio_company) values ('Wifi', 3, 1);

insert into jdbc.authority (name) values ('USER');
insert into jdbc.authority (name) values ('AVIO');
insert into jdbc.authority (name) values ('ADMIN');


insert into jdbc.user_authority(user_id, authority_id) values (1, 1);
insert into jdbc.user_authority(user_id, authority_id) values (2, 1);

insert into jdbc.user_authority(user_id, authority_id) values (3, 1);
insert into jdbc.user_authority(user_id, authority_id) values (3, 2);

insert into jdbc.user_authority(user_id, authority_id) values (2, 3);

insert into jdbc.friendship(sender, receiver, valid) values (1, 2, true);


insert into jdbc.location(airport, city, country) values ('Nikola Tesla', 'Belgrade', 'Serbia');
insert into jdbc.location(airport, city, country) values ('JFK', 'New York', 'USA');

insert into jdbc.location_avio_company(avio_company_id, location_id) values (3, 1);
insert into jdbc.location_avio_company(avio_company_id, location_id) values (3, 2);

insert into jdbc.flight(arrival_time, departure_time, flight_time, ticket_price, company, destination, version)
values ('2019-08-12 11:00','2019-09-27 13:00',1.5,100,2,2,0);

insert into jdbc.flight(arrival_time, departure_time, flight_time, ticket_price, company, destination, version)
values ('2019-09-23 15:00','2019-09-13 18:00',1.5,100,1,1,0);

insert into jdbc.location (airport, city, country, connecting_flight)
VALUES ('Nikola Tesla','Kraljevo','Serbia',1);

insert into jdbc.location (airport, city, country, connecting_flight)
VALUES ('Batajnica','Kragujevac','Serbia',2);


--insert into jdbc.ticket(version, fast_reservation_discount, passenger_surname, passenger_name, passport_number, reserved, seat_number, flight)
--VALUES(0, 10,'','','',true,1,1);

--insert into jdbc.ticket(version,fast_reservation_discount, passenger_surname, passenger_name, passport_number, reserved, seat_number, flight)
--VALUES(0, 0,'','','',true,2,1);
--insert into jdbc.ticket(version,fast_reservation_discount, passenger_surname, passenger_name, passport_number, reserved, seat_number, flight)
--VALUES(0,0,'','','',false,3,1);
--
--insert into jdbc.ticket(version,fast_reservation_discount, passenger_surname, passenger_name, passport_number, reserved, seat_number, flight)
--VALUES(0,0,'','','',true,4,1);
--insert into jdbc.ticket(version,fast_reservation_discount, passenger_surname, passenger_name, passport_number, reserved, seat_number, flight)
--VALUES(0,0,'','','',false,5,1);
--
--insert into jdbc.ticket(version,fast_reservation_discount, passenger_surname, passenger_name, passport_number, reserved, seat_number, flight)
--VALUES(0,50,'','','',false,6,1);
--insert into jdbc.ticket(version,fast_reservation_discount, passenger_surname, passenger_name, passport_number, reserved, seat_number, flight)
--VALUES(0,0,'','','',false,7,1);
--
--insert into jdbc.ticket(version,fast_reservation_discount, passenger_surname, passenger_name, passport_number, reserved, seat_number, flight)
--VALUES(0,0,'','','',false,8,2);
