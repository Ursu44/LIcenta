import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SharenumeService {

  constructor() { }
  private raspunsCatalog: any;

  sendNume(raspuns: any){
    this.raspunsCatalog = raspuns;
  }

  getNume(){
    return this.raspunsCatalog;
  }
}
