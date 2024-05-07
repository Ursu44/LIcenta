import { Injectable } from '@angular/core';
import { SendTestService } from './send-test.service';

@Injectable({
  providedIn: 'root'
})
export class ServiciuService {

  constructor(private servicu:SendTestService) { }
  private statistica: any = {};

  sendStatistici(raspuns: any){
    this.statistica = raspuns;
    localStorage.setItem("statistica", raspuns);
  }

  getStatistici(){
    return this.statistica;
  }
}

