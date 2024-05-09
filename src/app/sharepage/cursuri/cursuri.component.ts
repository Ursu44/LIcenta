import { Component, HostListener, OnInit } from '@angular/core';
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
  nota1: any = "0";
  nota2: any = "0";
  nume="Fizica";
  nota1False: any= false;
  nota2False: any= false;
  incercari:number = 0;
  incercari1:number = 0;
  ora = 0;
  minute =0;
  ora1 = 0;
  minute1 =0;
  deschis :any =true;
  

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
    capitole:"",
    mailElev:""
  };

  date: any = {
    ora:"",
    ora1:"",
    incercari:"",
    incercari1:"",
    materie:""
  };

  dateProgres: any ={
    progresAdd:0,
    materie:"",
    mailElev:"",
    capitol:""
  };

  ngOnInit(): void {
    this.preiaDate();
    //this.appComponent.raspuns1 = true;
  }

  @HostListener('click') onClick() {
    if(!this.esteProfesor()){
      if(localStorage.getItem("opened")){
        this.deschis= (localStorage.getItem("opened") === "true");
        this.appComponent.app = (localStorage.getItem("opened") === "true");
      }
    }
  }

  @HostListener('window:scroll', ['$event'])
  onScroll(event: Event): void {
    if(localStorage.getItem("nota1")){
          this.nota1False = true;
      this.nota1= localStorage.getItem("nota1");
  }
 
  if(localStorage.getItem("nota2")){
    this.nota2False = true;
    this.nota2= localStorage.getItem("nota2");
}

  }

  @HostListener('window:storage', ['$event'])
  onStorageChange(event: StorageEvent) {
    if (event.key === 'opened') { 
      this.nota2False = true;
      if(localStorage.getItem("opened")){
        this.deschis= (localStorage.getItem("opened") === "false");
        localStorage.setItem("opened", "true");
    }
    }
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
      console.log("Progres actual "+this.raspunsObj['Capitol1'].progres)
      this.ora = (this.raspunsObj["Detalii_test"].durataTest).split(":")[0];
      localStorage.setItem("ora1", String(this.ora));
      this.minute = (this.raspunsObj["Detalii_test"].durataTest).split(":")[1];
      localStorage.setItem("minute1", String(this.minute));

      this.ora1 = (this.raspunsObj["Detalii_test"].durataTest1).split(":")[0];
      localStorage.setItem("ora2", String(this.ora1));
      this.minute1 = (this.raspunsObj["Detalii_test"].durataTest1).split(":")[1];
      localStorage.setItem("minute2", String(this.minute1));
      this.incercari = this.raspunsObj["Detalii_test"].incercari;
      console.log("Incercari 123"+this.incercari);

      this.incercari1 = this.raspunsObj["Detalii_test"].incercari1;
      delete this.raspunsObj["Detalii_test"];
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
    
    if(capitole === '1_2'){
        const ora123:string | null = localStorage.getItem("ora1");
        if (ora123 !== null) {
          localStorage.setItem("ora", ora123);
      } else {
      }

      const minute123:string | null = localStorage.getItem("minute1");
      if (minute123 !== null) {
        localStorage.setItem("minute", minute123);
    } else {
    }
    } else if(capitole === '3_4'){
      const ora123:string | null = localStorage.getItem("ora2");
      if (ora123 !== null) {
        localStorage.setItem("ora", ora123);
    } else {
    }

    const minute123:string | null = localStorage.getItem("minute2");
    if (minute123 !== null) {
      localStorage.setItem("minute", minute123);
  } else {
  }
    }

    if (accessToken && refreshToken) {
    const accessTokenPayload = this.decodeJwtPayload(accessToken);
    const expirationTime = accessTokenPayload.exp * 1000; 
    const mail = accessTokenPayload.sub;
   // const nume = mail.split[1];
    console.log("Mail elev 1234 "+mail);
    if (expirationTime > Date.now()) {
      const numeMaterie = this.shareNume.getNume();
      const nume = numeMaterie.split('_')[0];
      const an = numeMaterie.split('_')[1];
      console.log("Materie "+nume);
      console.log("An "+an);
      
      this.dateTest.an = an;
      this.dateTest.mailElev = mail;
      this.dateTest.capitole = capitole;

      this.dateTest.materie = nume;
      if(nume === "Fizică"){
        this.dateTest.materie = "Fizica";
      }
      if(nume === "Matematică"){
        this.dateTest.materie = "Matematica";
      }
      if(nume === "Limba si literatura romana"){
        this.dateTest.materie = "Limbasiliteraturaromana";
      }
      if(nume === "Limba engleza"){
        this.dateTest.materie = "Limbaengleza";
      }

      console.log("Rspusns pregatit "+this.dateTest);
      const jsonObj = JSON.stringify(this.dateTest);
      console.log("Rspusns pregatit "+jsonObj);
      if(!this.esteProfesor()){
      const securedEndpoint = 'http://localhost:8089/api/';
    
      axios.post(securedEndpoint, jsonObj, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${accessToken}`
        }
      })
      .then(async response => {
        const jsonObj1 = JSON.stringify(response.data);
        const jsonObj2 =  JSON.stringify( this.dateTest);
        localStorage.setItem("dateElev",jsonObj2);
        console.log(typeof(jsonObj1));
        console.log(" "+jsonObj1);
        console.log('JSON trimis cu succes:', jsonObj1);
        localStorage.setItem("intrebari",jsonObj1);
        this.sendtest.sendRaspunsTest(jsonObj1);
        this.sendtest.getRaspunsTest();
        window.open("/test", '_blank', "width=1548,height=860");
        this.deschis = false;
        localStorage.setItem("opened","false");
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
    }
    else{

      const securedEndpoint = 'http://localhost:8091/corectare/grafic';
    
      axios.post(securedEndpoint, jsonObj, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${accessToken}`
        }
      })
      .then(async response => {
        const jsonObj1 = JSON.stringify(response.data);
        const jsonObj2 =  JSON.stringify( this.dateTest);
        console.log("Raspuns primit grafic "+jsonObj1);
        localStorage.setItem("noteGrafic",jsonObj1);
        /*console.log(typeof(jsonObj1));
        console.log(" "+jsonObj1);
        console.log('JSON trimis cu succes:', jsonObj1);
        localStorage.setItem("intrebari",jsonObj1);
        this.sendtest.sendRaspunsTest(jsonObj1);
        this.sendtest.getRaspunsTest();*/
        window.open("/grafic", '_blank', "width=1648,height=960");
        
      })

    }
    }else{
      this.refresh.refresh();
    }
  }
}

async trimitDateTest(){
  let accessToken = localStorage.getItem('access_token');
  let refreshToken = localStorage.getItem('refresh_token');
  
  if (accessToken && refreshToken) {
  const accessTokenPayload = this.decodeJwtPayload(accessToken);
  const expirationTime = accessTokenPayload.exp * 1000; 
  const mail = accessTokenPayload.sub;
 // const nume = mail.split[1];
  console.log("Mail elev 1234 "+mail);
  if (expirationTime > Date.now()) {

    if(this.esteProfesor()){
    const securedEndpoint = 'http://localhost:8083/update/timp';
    let ora1 = document.getElementById('ora1')as HTMLInputElement | null;
    let minute = document.getElementById('minute1')as HTMLInputElement | null;
    this.date.ora = ora1?.value+":"+minute?.value;
    let ora2 = document.getElementById('ora12')as HTMLInputElement | null;
    let minute1 = document.getElementById('minute12')as HTMLInputElement | null;
    this.date.ora1 = ora2?.value+":"+minute1?.value;
    let incercari1 = document.getElementById('incercari1')as HTMLInputElement | null;
    this.date.incercari = incercari1?.value;
    let incercari12 = document.getElementById('incercari12')as HTMLInputElement | null;
    this.date.incercari1 = incercari12?.value;
    this.date.materie = this.shareNume.getNume();
    const jsonObj = JSON.stringify(this.date);
    axios.put(securedEndpoint, jsonObj, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${accessToken}`
      }
    })
    .then(async response => {
      const jsonObj1 = JSON.stringify(response.data);
      console.log(" "+jsonObj1);
    })
    .catch(error => {
      console.error('Eroare la trimiterea JSON-ului:', error);
    });
  }

  }else{
    this.refresh.refresh();
  }
}
}

async trimitereProgres(i:number){
  let accessToken = localStorage.getItem('access_token');
  let refreshToken = localStorage.getItem('refresh_token');
  
  if (accessToken && refreshToken) {
  const accessTokenPayload = this.decodeJwtPayload(accessToken);
  const expirationTime = accessTokenPayload.exp * 1000; 
  const mail = accessTokenPayload.sub;
 // const nume = mail.split[1];
  //console.log("Mail elev 1234 "+mail);
  if (expirationTime > Date.now()) {
    //console.log("Progresul ce va fi trimis"+this.progres[i]);
    let j = i+1;
    //console.log("Progres ce va fi trimis Capitol"+j);
    if(!this.esteProfesor()){
    const securedEndpoint = 'http://localhost:8083/update/progres';
    this.dateProgres.mailElev = mail;
    this.dateProgres.progresAdd = this.progres[i];
    this.dateProgres.capitol = "Capitlolul "+j;
    this.dateProgres.materie = this.shareNume.getNume();
    const jsonObj = JSON.stringify(this.dateProgres);
    axios.put(securedEndpoint, jsonObj, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${accessToken}`
      }
    })
    .then(async response => {
      const jsonObj1 = JSON.stringify(response.data);
      console.log(" "+jsonObj1);
    })
    .catch(error => {
      console.error('Eroare la trimiterea JSON-ului:', error);
    });
  }

  }else{
    this.refresh.refresh();
  }
}
}

}
