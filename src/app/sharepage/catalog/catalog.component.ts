import { Component } from '@angular/core';
import axios from 'axios';
import { RefreshService } from 'src/app/services/refresh.service';
import { ShareCatalogService } from 'src/app/services/share-catalog.service';
import { ShareDataService } from 'src/app/services/share-data.service';
import { PopupComponent } from '../popup/popup.component';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.css']
})
export class CatalogComponent {
  raspunsCatalog: any = this.shareDataService.getRaspunsCatalog();
;

  constructor(private shareDataService:ShareCatalogService, private refresh:RefreshService, private dialog: MatDialog) {}

  AccessToken: any;
  rol:string = "";
  raspunsCatalogJson:any;
  vectorCatalog:any;

  formData: any = {
    nume: '',
    prenume: '',
    mail: '',
    materie: '',
    tipNota: '',
    nota:0
  };

  ngOnInit(): void {
      this.preiaDate();
  }

  preiaDate(): void{
    setTimeout(() => {
      this.raspunsCatalog = this.shareDataService.getRaspunsCatalog();
      if(this.raspunsCatalog != undefined){
        this.raspunsCatalogJson = JSON.parse(this.raspunsCatalog );
        this.vectorCatalog = Object.values(this.raspunsCatalogJson);
        this.calculeazaMedia();
      }
      console.log("Uite ce am primit catalog"+this.raspunsCatalog);
    }, 2100);  
  }

  async adaugaNota(){
  
    let accessToken = localStorage.getItem('access_token');
    let refreshToken = localStorage.getItem('refresh_token');

    if(accessToken && refreshToken){
    const endpoint = 'http://localhost:8085/';
    const accessTokenPayload = this.decodeJwtClaims(accessToken);
    const expirationTime = accessTokenPayload.exp * 1000; 
    this.rol = accessTokenPayload.roles;
    console.log("afagas "+this.rol);

    if (expirationTime > Date.now()) {
    var nume = document.getElementById('nume')  as HTMLInputElement | null;
    let numeValoare = nume?.value;
    this.formData.nume = numeValoare;

    console.log('Opțiunea selectată:', numeValoare);
    var prenume = document.getElementById('prenume')  as HTMLInputElement | null;
    let prenumeValoare = prenume?.value;
    this.formData.prenume = prenumeValoare;

    console.log('Opțiunea selectată:', prenumeValoare);
    var mail = document.getElementById('mail')  as HTMLInputElement | null;
    let mailValoare = mail?.value;
    console.log('Opțiunea selectată:', mailValoare);
    this.formData.mail = mailValoare;
    
    const selectMaterie = document.getElementById('selectMaterie') as HTMLSelectElement;
    const materieValoare = selectMaterie.value;
    console.log('Materie:', materieValoare);
    this.formData.materie = materieValoare;


    var nota = document.getElementById('selectNota')  as HTMLInputElement | null;
    let notaValoare = nota?.value;
    console.log('Opțiunea selectată select 2:', notaValoare);
    this.formData.tipNota = notaValoare;


    var nota1 = document.getElementById('nota')  as HTMLInputElement | null;
    let notaVal = nota1?.value;
    console.log('Opțiunea selectată select 2:', notaVal);
    this.formData.nota = notaVal;

    const jsonObj = JSON.stringify(this.formData);
    console.log("JSON: ", jsonObj);


     await axios.put(endpoint, jsonObj, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${accessToken}`
      }
    })
      .then(response => {
        console.log('JSON trimis cu succes:', response.data);
        this.shareDataService.sendRaspunsCatalog(jsonObj);
        this.formData = {
          nume: '',
          prenume: '',
          mail: '',
          materie: '',
          tipNota: '',
          nota:0
        };
        
      })
      .catch(error => {
        console.error('Eroare la trimiterea JSON-ului:', error);
      });
    }
    else{
      this.refresh.refresh();
    }
  }
}


  decodeJwtClaims(token: string) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
  }

  esteProfesor() :boolean{
    var notaNoua = document.getElementById('element') as HTMLInputElement | null;
    const accessToken = localStorage.getItem('access_token');
    let ok =false;
    if(accessToken){
      const claimsDecodat = this.decodeJwtClaims(accessToken)
      let rol = claimsDecodat.roles;
      let rol1 = JSON.stringify(rol);
        const numeRol = rol1.trim();
        ok = (numeRol === '["ROLE_PROFESOR"]');
  }
    return ok;
  }

  open(){
    const dialogRefresh = new MatDialogConfig();
    dialogRefresh.width = "60%";
    
    this.dialog.open(PopupComponent);
  }

  calculeazaMedia(){
    for(let i=0;i<this.vectorCatalog.length;i++){
      if(this.vectorCatalog[i].Fizica.Nota1!=0 && this.vectorCatalog[i].Fizica.Nota2!=0 && this.vectorCatalog[i].Fizica.Nota3!=0 && this.vectorCatalog[i].Fizica.Nota4!=0){
        this.vectorCatalog[i].Fizica.Medie = 
        Math.round(0.2 *this.vectorCatalog[i].Fizica.Nota1 
        + 0.2*this.vectorCatalog[i].Fizica.Nota2 
        + 0.3* this.vectorCatalog[i].Fizica.Nota3
        + 0.3* this.vectorCatalog[i].Fizica.Nota4);
      }
    }
  }


}
