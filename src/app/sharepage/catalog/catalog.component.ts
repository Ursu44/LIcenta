import { Component, HostListener } from '@angular/core';
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

  constructor(private shareDataService:ShareCatalogService, private refresh:RefreshService, private dialog: MatDialog, private shareDataCatalog:ShareCatalogService) {}

  AccessToken: any;
  rol:string = "";
  raspunsCatalogJson:any;
  vectorCatalog:any;
  materie:string[] = ['Fizica', 'Matematica', 'Istoria', 'Geografie', 'Limbaengleza', 'Limbasiliteraturaromana'] ;
  deataliiAn: any = {};
  aniDestudiu:any = {};
  materiiAfisare :any = [];
  ani :any = [];
  grupe :any = [];
  grupeSelectate :any = [];
  anSelectat: string = "";
  detalii: any = [];
  nume: any = [];
  prenume: any = [];
  mail: any = [];

  formData: any = {
    nume: '',
    mail: '',
    materie: '',
    an:'',
    tipNota: '',
    grupa:'',
    nota:0
  };

  ngOnInit(): void {
      this.preiaDate();
      //this.iaDate();
  }

  gupeAlese(anLuat:any){
      this.grupe = this.deataliiAn[anLuat];
  }

  detaliiAlese(anLuat:any, grupa:any){
    this.detalii = this.deataliiAn[anLuat][grupa];
  }

  adaugNUmPrenumeMail(nume:String, prenume:String ,mail:String){
    let adaugat: String = "";
    adaugat = nume+" "+prenume;
    if(!this.nume.includes(adaugat)){
      this.nume.push(adaugat);
    }

    if(!this.mail.includes(mail)){
      this.mail.push(mail);
    }
  }

  async preiaDate(){
    try {
        await new Promise<void>((resolve) => {
            setTimeout(() => {
                this.raspunsCatalog = this.shareDataService.getRaspunsCatalog();
                if(this.raspunsCatalog != undefined){
                    this.raspunsCatalogJson = JSON.parse(this.raspunsCatalog );
                    this.vectorCatalog = Object.values(this.raspunsCatalogJson);
                    for(let nota1 of this.vectorCatalog){
                        for(let j =0;j<this.materie.length;j++){
                            if(this.materie[j] in nota1){
                              if(!this.materiiAfisare.includes(this.materie[j])){
                                this.materiiAfisare.push(this.materie[j]);   
                              }
                                let materiedepus;
                                if(this.materie[j] === "Fizica"){
                                    materiedepus= "Fizică";
                                }
                                if(this.materie[j] === "Matematica"){
                                    materiedepus= "Matematică";
                                }
                                if(this.materie[j] === "Limbaengleza"){
                                    materiedepus= "Limba engleză";
                                }
                                if(this.materie[j] === "Limbasiliteraturaromana"){
                                    materiedepus= "Limba și literatura română";
                                }
                                let detaliiElev = {
                                  nume: nota1.nume,
                                  prenume: nota1.prenume,
                                  mail: nota1.mail
                              };
                              if(!this.ani.includes(nota1.an)){
                                this.ani.push(nota1.an);
                              }
                              if (!this.deataliiAn.hasOwnProperty(nota1.an)) {

                                  this.deataliiAn[nota1.an] = {}; 
                              }

                              if (!this.deataliiAn[nota1.an].hasOwnProperty(nota1.grupa)) {
                                this.deataliiAn[nota1.an][nota1.grupa] = []; 
                            }
                            if(!this.deataliiAn[nota1.an][nota1.grupa].includes(detaliiElev)){
                              this.deataliiAn[nota1.an][nota1.grupa].push(detaliiElev);   
                            }
                            }
                        }
                    }
                    for (const an in this.ani) {
                          console.log("An:", an);
                    }
                  
                    // this.calculeazaMedia();
                }
                for(let i in this.materiiAfisare){
                    console.log("i= "+this.materiiAfisare[i]);
                }
                console.log("Uite ce am primit catalog" + this.raspunsCatalog);
                resolve(); 
            }, 2100);
        });
    } catch (error) {
        console.error('Eroare în preluarea datelor:', error);
    }
}
@HostListener('change', ['$event.target.value'])
onAnSelectatChange() {
  let anLuat :any;
  let grupaLuat :any;
  anLuat = document.getElementById('selectAn')as HTMLInputElement | null;
  var text = anLuat.options[anLuat.selectedIndex].text;
  this.gupeAlese(text); 
  //for(let i of this.grupe){
   this.grupeSelectate = Object.keys(this.grupe);  
  //}
  grupaLuat = document.getElementById('selectGrupa')as HTMLInputElement | null;
  var text1 = grupaLuat.options[grupaLuat.selectedIndex].text;
  console.log("An luat "+text1);
  this.detaliiAlese(text, text1);
  this.nume = [];
  this.prenume = [];
  this.mail = [];
  for (const detaliu in this.detalii) {
      const detaliiElev = this.detalii[detaliu];
      console.log("Nume:", detaliiElev.nume);
      console.log("Prenume:", detaliiElev.prenume);
      console.log("Mail:", detaliiElev.mail);  
      this.adaugNUmPrenumeMail(detaliiElev.nume, detaliiElev.prenume, detaliiElev.mail);
}
}

  async iaDate(){
    console.log("DSA ADA AFA")
    const accessToken = localStorage.getItem('access_token');
    const refreshToken = localStorage.getItem('refresh_token');

    if (accessToken && refreshToken) {
      const accessTokenPayload = this.decodeJwtClaims(accessToken);
      const expirationTime = accessTokenPayload.exp * 1000; 
      
    if (expirationTime > Date.now()) {

    let securedEndpoint ;
    if(this.esteProfesor()){
       securedEndpoint = 'http://localhost:8084/catalog/profesor';
    }
    else{
      securedEndpoint = 'http://localhost:8084/catalog/elev';
    }
        try {
          const response1 = await axios.get(securedEndpoint, {
            method: 'GET',
            headers: {
              'Content-Type': 'text/plain',
              'Authorization': `Bearer ${accessToken}`,
            },
          });
          let raspunsServer = response1.data;
          console.log("afafga "+raspunsServer);
          const raspunsJSON = JSON.stringify(raspunsServer);
          console.log("afafga "+raspunsJSON);
          this.raspunsCatalogJson = JSON.parse(this.raspunsCatalog );
          console.log("afafgaafafa fafag "+this.raspunsCatalogJson);
        //this.vectorCatalog = Object.values(this.raspunsCatalogJson);

        this.shareDataCatalog.sendRaspunsCatalog(raspunsJSON);
        this.calculeazaMedia();
           console.log("Raspuns server catalog "+ raspunsJSON);
        } catch (error) {
          console.error('Eroare la cererea GET cu token valid:', error);
        }
      }
        else {
          console.log('Token-ul de acces a expirat. Solicităm o nouă autentificare.');
          this.refresh.refresh();
          //this.formLogin.show();
          //await this.refreshAccessToken();
        }
      } else {
        console.error('Token-uri lipsă.');
      }
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
    var materie = document.getElementById('selectMaterie')  as HTMLInputElement | null;
    let materieValoare = materie?.value;
    this.formData.materie = materieValoare;

    var grupa = document.getElementById('selectGrupa')  as HTMLInputElement | null;
    let grupaValoare = grupa?.value;
    this.formData.grupa = grupaValoare;
    

    var an = document.getElementById('selectAn')  as HTMLInputElement | null;
    let anvaloare = an?.value;
    this.formData.an = anvaloare;
    
    var nume = document.getElementById('selectNume')  as HTMLInputElement | null;
    let numeValoare = nume?.value;
    this.formData.nume = numeValoare;
    console.log('Opțiunea selectată:', numeValoare);

    var mail = document.getElementById('selectMail')  as HTMLInputElement | null;
    let mailValoare = mail?.value;
    console.log('Opțiunea selectată:', mailValoare);
    this.formData.mail = mailValoare;
    
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
      if(this.vectorCatalog[i].Matematica.Nota1!=0 && this.vectorCatalog[i].Matematica.Nota2!=0 && this.vectorCatalog[i].Matematica.Nota3!=0 && this.vectorCatalog[i].Matematica.Nota4!=0){
        this.vectorCatalog[i].Matematica.Medie = 
        Math.round(0.2 *this.vectorCatalog[i].Matematica.Nota1 
        + 0.2*this.vectorCatalog[i].Matematica.Nota2 
        + 0.3* this.vectorCatalog[i].Matematica.Nota3
        + 0.3* this.vectorCatalog[i].Matematica.Nota4);
      }
    }
  }

  prezentinJson(materie:string):boolean{
    let ok3 =false;
    if(this.raspunsCatalog.includes(materie)){
      ok3 = true;
    }
    return ok3;
  }


}
