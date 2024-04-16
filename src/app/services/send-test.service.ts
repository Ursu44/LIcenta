import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SendTestService {

  private raspunsTest: any ;
  constructor() { }


   sendRaspunsTest(raspuns: any){
    this.raspunsTest = raspuns;
    console.log("Raspunsul de trimis servicu te trimis"+ JSON.stringify(this.raspunsTest));
    return this.raspunsTest;
  }

   getRaspunsTest(){
    console.log("Raspunsul de primit servicu te trimis"+ JSON.stringify(this.raspunsTest));
    return this.raspunsTest;
  }

}
