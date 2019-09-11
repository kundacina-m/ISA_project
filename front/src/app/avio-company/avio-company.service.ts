import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AvioCompanyService {

  public newData = new Subject<any>();

  constructor(private http : HttpClient) { }

  getAllAvioCompany()
  {
    return this.http.get("api/aviocompanies/all").subscribe(data => { localStorage.setItem("avioCompany", JSON.stringify(data));});
  }

  getAvioCompany(data)
  {
    this.newData.next(data);
  }
  addAvio(name,address,promo)
  {
    return this.http.post('api/aviocompanies/add',{"name":name,"address":address,"promoDescription":promo}).subscribe(data=>alert('Uspesno dodata avio kompanija'))
  }
}
