import { Component, OnInit } from '@angular/core';
import { ShareDataService } from '../../services/share-data.service';
import axios from 'axios';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { PopupComponent } from '../popup/popup.component';
import { RefreshService } from 'src/app/services/refresh.service';
import { coerceStringArray } from '@angular/cdk/coercion';
import { ShareCatalogService } from 'src/app/services/share-catalog.service';
import { SharenumeService } from 'src/app/services/sharenume.service';

@Component({
  selector: 'app-cursuri',
  templateUrl: './cursuri.component.html',
  styleUrls: ['./cursuri.component.css']
})
export class CursuriComponent implements OnInit {
  i = 0;
  raspunsObj:any;
  raspuns: any;
  pozitieActuala = 0;
  progres = 0;
  apasat = false;
  progresAdaugat = 0;
  subvectori:any;
  Object: any;
  stare:boolean = false;

  constructor(private shareDataService: ShareDataService, private dialog: MatDialog, private refresh: RefreshService, private shareNume:SharenumeService) {}

  formData: any = {
    numeLectie:"",
    activat:false,
    numeMaterie:""
  };

  ngOnInit(): void {
    this.preiaDate();
  }

  preiaDate(): void {
    this.getData();
  }

  getData() {
    setTimeout(() => {
      this.raspuns = this.shareDataService.getRaspuns();
      console.log("Uite ce am primit" + this.raspuns);
      if( this.raspuns != undefined){
      this.raspunsObj = JSON.parse(this.shareDataService.getRaspuns());
      console.log("Answear "+this.raspunsObj["lectie2Detalii"])
      this.pozitieActuala = Object.keys(this.raspunsObj).length ;
      this.progresAdaugat = 100 / this.pozitieActuala;
      this.subvectori = Object.values(this.raspunsObj);
      this.progres = 0 ;
      this.pozitieActuala++;
      console.log("Dimensiune "+this.i)
      }
    }, 3000);
  }
  

  async updateProgres() {
    const endpoint = 'http://localhost:8083/';
    const jsonObj = JSON.stringify(this.formData);
    const accessToken = localStorage.getItem('access_token');
    
    if (accessToken) {
      this.refresh.refresh();
      axios.put(endpoint, jsonObj, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${accessToken}`
        }
      })
      .then(response => {
        console.log('JSON trimis cu succes:', response.data);
        this.formData = {
          nume: '',
          prenume: '',
          mail: '',
          parola: '',
          materie: ''
        };
      })
      .catch(error => {
        console.error('Eroare la trimiterea JSON-ului:', error);
      });
    }
  }

  open() {
    const dialogRefresh = new MatDialogConfig();
    dialogRefresh.width = "60%";
    this.dialog.open(PopupComponent);
  }

  actualizeazapozitiaInainte(){
    const accessToken = localStorage.getItem('access_token');
    const refreshToken = localStorage.getItem('refresh_token');
    if (accessToken && refreshToken) {
      const accessTokenPayload = this.decodeJwtPayload(accessToken);
      const expirationTime = accessTokenPayload.exp * 1000; 
  
  
      if (expirationTime > Date.now()) {
    this.apasat =true;
    console.log("Pozitia "+this.pozitieActuala);
    console.log("Progres "+this.progres);
    if(this.pozitieActuala>1 && this.pozitieActuala<=5 && this.apasat ==true){
      this.pozitieActuala--;
      this.progres += this.progresAdaugat;
    }
  }
    else{
      this.refresh.refresh();
    }    
  }
}

  actualizeazapozitiaInapoi(){
    let accessToken = localStorage.getItem('access_token');
    let refreshToken = localStorage.getItem('refresh_token');
    
    if (accessToken && refreshToken) {
    const accessTokenPayload = this.decodeJwtPayload(accessToken);
    const expirationTime = accessTokenPayload.exp * 1000; 


    if (expirationTime > Date.now()) {
    this.apasat =true;
    console.log("Pozitia "+this.pozitieActuala);
    console.log("Progres "+this.progres);
    if(this.pozitieActuala>=1 && this.pozitieActuala<5 && this.apasat ==true){
      this.pozitieActuala++;
      this.progres -= this.progresAdaugat;
      this.apasat =true;
    }
  }
  else{
    this.refresh.refresh();
  }

  }
  }
   
  decodeJwtPayload(token: string) {
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
      const claimsDecodat = this.decodeJwtPayload(accessToken)
      let rol = claimsDecodat.roles;
      let rol1 = JSON.stringify(rol);
        const numeRol = rol1.trim();
        ok = (numeRol === '["ROLE_PROFESOR"]');
  }
    return ok;
  }

  async activeaza(index:number){
    let accessToken = localStorage.getItem('access_token');
    let refreshToken = localStorage.getItem('refresh_token');
    
    if (accessToken && refreshToken) {
    const accessTokenPayload = this.decodeJwtPayload(accessToken);
    const expirationTime = accessTokenPayload.exp * 1000; 


    if (expirationTime > Date.now()) {
      const numeMaterie = this.shareNume.getNume();
      console.log("Materie "+numeMaterie);
      console.log("i = "+index);
      console.log("check = "+this.stare);
      const securedEndpoint = 'http://localhost:8082/activare';
      index++;
      this.formData.numeLectie = "Lectia"+index;
      this.formData.activat = this.stare;
      this.formData.numeMaterie = numeMaterie;
      console.log("Rspusns pregatit "+this.formData);
      const jsonObj = JSON.stringify(this.formData);
      console.log("Rspusns pregatit "+jsonObj);
      axios.put(securedEndpoint, jsonObj, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${accessToken}`
        }
      })
      .then(response => {
        console.log('JSON trimis cu succes:', response.data);
        this.formData = {
          numeLectie:"",
          activat:false
        };
      })
      .catch(error => {
        console.error('Eroare la trimiterea JSON-ului:', error);
      });

    }else{
      this.refresh.refresh();
    }
  }

  }

  getIndex(subvector: any): number {
    let count = 0;
    for (let j = 0; j < this.subvectori.length; j++) {
      if (this.subvectori[j].activat) {
        count++;
      }
      if (this.subvectori[j] === subvector) {
        return count;
      }
    }
    return -1; 
  }

}
