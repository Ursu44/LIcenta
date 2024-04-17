import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ShareCatalogService {

  private raspunsCatalog: any;
  constructor() { }

  sendRaspunsCatalog(raspuns: any){
    this.raspunsCatalog = raspuns;
  }

  getRaspunsCatalog(){
    return this.raspunsCatalog;
  }
}
