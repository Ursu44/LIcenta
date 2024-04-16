import { Injectable } from '@angular/core';
import { SendTestService } from './send-test.service';

@Injectable({
  providedIn: 'root'
})
export class ServiciuService {

  constructor(private servicu:SendTestService) { }
  private raspunsTest: any = {};

  deschide(){
    window.open("/test", '_blank', "width=1648,height=960");
  }
}
