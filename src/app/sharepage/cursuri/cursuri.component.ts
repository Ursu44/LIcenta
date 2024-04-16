import { Component, OnInit } from '@angular/core';
import { ShareDataService } from '../../services/share-data.service';
import axios from 'axios';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { PopupComponent } from '../popup/popup.component';
import { RefreshService } from 'src/app/services/refresh.service';
import { coerceStringArray } from '@angular/cdk/coercion';
import { ShareCatalogService } from 'src/app/services/share-catalog.service';
import { SharenumeService } from 'src/app/services/sharenume.service';
import { SendTestService } from 'src/app/services/send-test.service';
import { ServiciuService } from 'src/app/services/serviciu.service';
import { AppComponent } from 'src/app/app.component';

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

  constructor(private appComponent:AppComponent,private servicu:ServiciuService ,private shareDataService: ShareDataService, private dialog: MatDialog, private refresh: RefreshService, private shareNume:SharenumeService, private sendtest:SendTestService) {}

  formData: any = {
    numeLectie:"",
    activat:false,
    numeMaterie:"",
    mail:""
  };

  dateTest: any = {
    materie:"",
    an:"",
    capitole:""
  };

  ngOnInit(): void {
    this.preiaDate();
    //this.appComponent.raspuns1 = true;
  }


  async preiaDate(): Promise<void> {
    this.getData();
    const numeMaterie = this.shareNume.getNume();
    console.log("Aceasta este materia selectata "+numeMaterie);
    const accessToken = localStorage.getItem('access_token');
    const refreshToken = localStorage.getItem('refresh_token');
    if (accessToken && refreshToken) {

      const accessTokenPayload = this.decodeJwtClaims(accessToken);
      const expirationTime = accessTokenPayload.exp * 1000; 

      if (expirationTime > Date.now()) {
        let securedEndpoint:string;
        if(this.esteProfesor()){
        securedEndpoint = 'http://localhost:8082/'+numeMaterie+'/profesor';
        }
        else{
          securedEndpoint = 'http://localhost:8082/'+numeMaterie+'/elev';
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
      for(let j =0;j<= Object.keys(this.raspunsObj).length-1; j++){
        let k =j+1;
        this.lungimi[j] = Object.keys(this.raspunsObj["Capitol"+k]).length-3;
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
      console.log("Pozitia1 "+this.pozitieActuala[i]);
      console.log("Progres1 "+this.progres[i]);
      console.log("Apasat1 "+this.apasat);
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
      console.log("Pozitia1 "+this.pozitieActuala[i]);
      console.log("Progres1 "+this.progres[i]);
      console.log("Apasat1 "+this.apasat);
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
      this.formData.numeLectie = "Capitlolul "+index;
      this.formData.activat = this.stare;
      this.formData.numeMaterie = numeMaterie;
      this.formData.mail = accessTokenPayload.username;
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
          activat:false,
          numeMaterie:"",
          mail:""
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

  async preiaTestul(capitole:string){
    let accessToken = localStorage.getItem('access_token');
    let refreshToken = localStorage.getItem('refresh_token');
    
    if (accessToken && refreshToken) {
    const accessTokenPayload = this.decodeJwtPayload(accessToken);
    const expirationTime = accessTokenPayload.exp * 1000; 


    if (expirationTime > Date.now()) {
      const numeMaterie = this.shareNume.getNume();
      const nume = numeMaterie.split('_')[0];
      const an = numeMaterie.split('_')[1];
      console.log("Materie "+nume);
      console.log("An "+an);

      const securedEndpoint = 'http://localhost:8089/api/';
      this.dateTest.materie = nume;
      if(nume === "Fizică"){
        this.dateTest.materie = "Fizica";
      }
      this.dateTest.an = an;
      this.dateTest.capitole = capitole;
      console.log("Rspusns pregatit "+this.dateTest);
      const jsonObj = JSON.stringify(this.dateTest);
      console.log("Rspusns pregatit "+jsonObj);
      axios.post(securedEndpoint, jsonObj, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${accessToken}`
        }
      })
      .then(async response => {
        const jsonObj1 = JSON.stringify(response.data);
        const jsonObj2 =  JSON.parse(jsonObj1);
        console.log(typeof(jsonObj1));
        console.log(" "+jsonObj1);
        console.log('JSON trimis cu succes:', jsonObj1);
        localStorage.setItem("intrebari",jsonObj1);
        this.sendtest.sendRaspunsTest(jsonObj1);
        this.sendtest.getRaspunsTest();
        window.open("/test", '_blank', "width=1648,height=960");
        this.formData = {
          numeLectie:"",
          activat:false,
          numeMaterie:"",
          mail:""
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

}
