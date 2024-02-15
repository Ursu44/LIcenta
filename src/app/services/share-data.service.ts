import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ShareDataService {
  
  private raspunsJson: any;
  constructor() { }

  sendRaspuns(raspuns: any){
    this.raspunsJson = raspuns;
  }

  getRaspuns(){
    return this.raspunsJson;
  }
}
