import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http : HttpClient) { }

  private TOKEN_KEY = 'jwtToken';

  getJwtToken() {
    return localStorage.getItem(this.TOKEN_KEY);
  };

  setJwtToken(token) {
    localStorage.setItem(this.TOKEN_KEY, token);
  };

  removeJwtToken() {
    localStorage.removeItem(this.TOKEN_KEY);
  };

  createAuthorizationTokenHeader() {
    var token = this.getJwtToken();
    if (token) {
      return {
        'Authorization': "Bearer " + token,
        'Content-Type': 'application/json'
      };
    } else {
      return {
        'Content-Type': 'application/json'
      };
    }
  }
}

