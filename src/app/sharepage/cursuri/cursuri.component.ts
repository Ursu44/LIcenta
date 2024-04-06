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
  pozitieActuala :number[] =[];
  progres :number[] =[];
  lungimi :number[]=[];

  apasat = false;
  progresAdaugat:number[] =[];
  subvectori:any;
  Object: any;
  stare:boolean = false;
  indexDeschis: number = 0;
  isCollapsed: boolean[] = [];
  nume="Fizica";

  constructor(private shareDataService: ShareDataService, private dialog: MatDialog, private refresh: RefreshService, private shareNume:SharenumeService) {}

  formData: any = {
    numeLectie:"",
    activat:false,
    numeMaterie:""
  };

  ngOnInit(): void {
    this.preiaDate();
  }


  async preiaDate(): Promise<void> {
    this.getData();
    console.log("Aceasta este materia selectata "+this.nume);
    const accessToken = localStorage.getItem('access_token');
    const refreshToken = localStorage.getItem('refresh_token');
    if (accessToken && refreshToken) {

      const accessTokenPayload = this.decodeJwtClaims(accessToken);
      const expirationTime = accessTokenPayload.exp * 1000; 

      if (expirationTime > Date.now()) {
        let securedEndpoint:string;
        if(this.esteProfesor()){
        securedEndpoint = 'http://localhost:8082/'+this.nume+'/profesor';
        }
        else{
          securedEndpoint = 'http://localhost:8082/'+this.nume+'/elev';
        }
        try {
          const response = await axios.get(securedEndpoint , {
            method: 'GET',
            headers: {
              'Content-Type': 'text/plain',
              'Authorization': `Bearer ${accessToken}`,
            },
          });
          let raspunsServer = response.data;
          let raspunsServerModficat = raspunsServer.slice(0, -1);
          const raspunsJSON = JSON.stringify(raspunsServerModficat);
          this.shareDataService.sendRaspuns(raspunsServerModficat);
           console.log("Raspuns server "+ raspunsServerModficat);
           console.log("Raspuns server json"+ raspunsJSON);
        } catch (error) {
          console.error('Eroare la cererea GET cu token valid:', error);
        }
      } else {
        console.log('Token-ul de acces a expirat. Solicităm o nouă autentificare.');
        this.refresh.refresh();
        //this.formLogin.show();
        //await this.refreshAccessToken();
      }
    } else {
      console.error('Token-uri lipsă.');
    }
  }


  getData() {
    setTimeout(() => {
      this.raspuns = this.shareDataService.getRaspuns();
      console.log("Uite ce am primit" + this.raspuns);
      if( this.raspuns != undefined){
      this.raspunsObj = JSON.parse(this.shareDataService.getRaspuns());
      console.log("Answear "+this.raspunsObj["lectie2Detalii"])
      this.raspunsObj['Lectia1']['Inforamtie1'] = "Cinematica explorează mișcarea obiectelor fără a lua în considerare forțele implicate, concentrându-se pe aspecte precum poziția, timpul, viteza și accelerarea. Acesta se bazează pe sisteme de coordonate, cum ar fi cele cartezian și polar, pentru a descrie mișcarea în spațiu. Conceptele cheie includ mișcarea rectilinie uniformă (MRU), mișcarea rectilinie uniform variată (MRUV), mișcarea circulară uniformă (MCU) și mișcarea circulară uniform variată (MCUV), fiecare având ecuații specifice care descriu mișcarea în funcție de timp și poziție. Înțelegerea acestor concepte este fundamentală pentru analiza și predicția mișcării într-o varietate de situații fizice și tehnologice."
      for(let j =0;j<= Object.keys(this.raspunsObj).length-1; j++){
        let k =j+1;
        this.lungimi[j] = Object.keys(this.raspunsObj["Lectia"+k]).length-2;
        console.log("b "+this.lungimi[j]);
        this.progresAdaugat[j] = 100 / this.lungimi[j] ;
        this.progres[j] = 0;
        this.pozitieActuala[j] = 0;
      }
      this.subvectori = Object.values(this.raspunsObj);
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

  actualizeazapozitiaInainte(i:number){
    const accessToken = localStorage.getItem('access_token');
    const refreshToken = localStorage.getItem('refresh_token');
    if (accessToken && refreshToken) {
      const accessTokenPayload = this.decodeJwtPayload(accessToken);
      const expirationTime = accessTokenPayload.exp * 1000; 
  
  
      if (expirationTime > Date.now()) {
    this.apasat =true;
    console.log("Pozitia1 "+this.pozitieActuala[i]);
    console.log("Progres1 "+this.progres[i]);
    console.log("Apasat1 "+this.apasat);

    if(this.pozitieActuala[i]>=0 && this.pozitieActuala[i]<this.lungimi[i] && this.apasat ==true){
      this.pozitieActuala[i]++;
      this.progres[i] += this.progresAdaugat[i];
    }
  }
    else{
      this.refresh.refresh();
    }    
  }
}

  actualizeazapozitiaInapoi(i:number){
    let accessToken = localStorage.getItem('access_token');
    let refreshToken = localStorage.getItem('refresh_token');
    
    if (accessToken && refreshToken) {
    const accessTokenPayload = this.decodeJwtPayload(accessToken);
    const expirationTime = accessTokenPayload.exp * 1000; 


    if (expirationTime > Date.now()) {
    this.apasat =true;
    console.log("Pozitia "+this.pozitieActuala[i]);
    console.log("Progres "+this.progres[i]);
    console.log("Apasat "+this.apasat);
    if(this.pozitieActuala[i]>=1 && this.pozitieActuala[i]<=this.lungimi[i] && this.apasat ==true){
      this.pozitieActuala[i]--;
      this.progres[i] -= this.progresAdaugat[i];
      this.apasat =true;
    }
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

  toggleCollapse(index: number) {
    if (this.indexDeschis === index) {
      this.isCollapsed[index] = !this.isCollapsed[index];
    } else {
      this.isCollapsed.fill(false);
      this.isCollapsed[index] = true;
      this.indexDeschis = index;
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

}
