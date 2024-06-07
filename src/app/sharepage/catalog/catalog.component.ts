import { Component, HostListener } from '@angular/core';
import axios from 'axios';
import { RefreshService } from 'src/app/services/refresh.service';
import { ShareCatalogService } from 'src/app/services/share-catalog.service';
import { ShareDataService } from 'src/app/services/share-data.service';
import { PopupComponent } from '../popup/popup.component';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AppComponent } from 'src/app/app.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.css']
})
export class CatalogComponent {
  raspunsCatalog: any = this.shareDataService.getRaspunsCatalog();

  constructor(private snackBar: MatSnackBar, private appComponent: AppComponent, private shareDataService:ShareCatalogService, private refresh:RefreshService, private dialog: MatDialog, private shareDataCatalog:ShareCatalogService) {}

  AccessToken: any;
  rol:string = "";
  raspunsCatalogJson:any;
  vectorCatalog:any;
  materie:string[] = ['Fizica', 'Matematica', 'Istorie', 'Geografie', 'Limbaengleza', 'Limbasiliteraturaromana'] ;
  deataliiAn: any = {};
  deataliiAn1: any = {};
  aniDestudiu:any = {};
  materiiAfisare :any = [];
  ani :any = [];
  ani1 :any = [];
  grupe :any = [];
  grupeSelectate :any = [];
  anSelectat: string = "";
  detalii: any = [];
  nume: any = [];
  prenume: any = [];
  mail: any = [];
  materia :any;
  an: any;
  materieMedie: any;

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
      //this.appComponent.raspuns1 = true;
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
                this.raspunsCatalog = localStorage.getItem("catalog");
                if(this.raspunsCatalog!=null){
                  this.raspunsCatalogJson = JSON.parse(this.raspunsCatalog );
                    this.vectorCatalog = Object.values(this.raspunsCatalogJson);
                    for(let nota1 of this.vectorCatalog){
                     console.log("ELEv "+nota1);
                        for(let j =0;j<this.materie.length;j++){
                          console.log("Materie 2 "+this.materie[j]);
                            if(this.materie[j] in nota1){
                              console.log("Materie 1 "+this.materie[j]);
                              if(!this.materiiAfisare.includes(this.materie[j])){
                                console.log("Materie "+this.materiiAfisare);
                                this.materiiAfisare.push(this.materie[j]);   
                              }
                                let materiedepus;
                                if(this.materie[j] === "Fizica"){
                                    materiedepus= "Fizică";
                                }
                                else if(this.materie[j] === "Matematica"){
                                    materiedepus= "Matematică";
                                }
                                else if(this.materie[j] === "Limbaengleza"){
                                    materiedepus= "Limba engleză";
                                }
                                else if(this.materie[j] === "Limbasiliteraturaromana"){
                                    materiedepus= "Limba și literatura română";
                                }
                                else{
                                  materiedepus = this.materie[j];
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
                    this.calculeazaMedia(this.materiiAfisare[i]);
                }
                console.log("Uite ce am primit catalog" + this.raspunsCatalog);
                resolve(); 
            }, 6200);
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
  console.log("Ani "+this.ani)
  var text = anLuat.options[anLuat.selectedIndex].text;
  this.materia = text.split('_')[0];
  this.an =  text.split('_')[1];
  console.log("Nume:", this.materia);
  if(this.raspunsCatalog != undefined){
    this.raspunsCatalogJson = JSON.parse(this.raspunsCatalog );
    this.vectorCatalog = Object.values(this.raspunsCatalogJson);
    console.log("Raspuns host listenere "+this.vectorCatalog);
    for(let nota1 of this.vectorCatalog){
    let materiedepus;
    this.materieMedie = this.materia;
    this.calculeazaMedia(this.materieMedie);
    if(this.materia === "Fizica"){
        materiedepus= "Fizică";
    }
    else if(this.materia === "Matematica"){
        materiedepus= "Matematică";
    }
    else if(this.materia === "Limbaengleza"){
        materiedepus= "Limba engleză";
    }
    else if(this.materia === "Limbasiliteraturaromana"){
        materiedepus= "Limba și literatura română";
    }
    else{
      materiedepus= this.materia;
    }
  
    let detaliiElev = {
      nume: nota1.nume,
      prenume: nota1.prenume,
      mail: nota1.mail
  };
  if(!this.ani1.includes(nota1.an)){
    this.ani1.push(nota1.an);
  }
  if (!this.deataliiAn1.hasOwnProperty(nota1.an)) {

      this.deataliiAn1[nota1.an] = {}; 
  }

  if (!this.deataliiAn1[nota1.an].hasOwnProperty(nota1.grupa)) {
    this.deataliiAn1[nota1.an][nota1.grupa] = []; 
}
if(!this.deataliiAn1[nota1.an][nota1.grupa].includes(detaliiElev)){
  this.deataliiAn1[nota1.an][nota1.grupa].push(detaliiElev);   
}
    }
  }
  this.gupeAlese(text); 
  //for(let i of this.grupe){
   this.grupeSelectate = Object.keys(this.grupe);  
  //}
  grupaLuat = document.getElementById('selectGrupa')as HTMLInputElement | null;
  var text1 = grupaLuat.options[grupaLuat.selectedIndex].text;
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
       securedEndpoint = 'http://localhost:8094/catalog/profesor';
    }
    else{
      securedEndpoint = 'http://localhost:8094/catalog/elev';
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

    var grupa = document.getElementById('selectGrupa')  as HTMLInputElement | null;
    let grupaValoare = grupa?.value;
    this.formData.grupa = grupaValoare;
    

    var an = document.getElementById('selectAn')  as HTMLInputElement | null;
    let materieValoare = an?.value.split('_')[0];
    this.formData.materie = materieValoare;
    let anvaloare = an?.value.split('_')[1];
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
        this.snackBar.open('Notă inrodusă cu succes', 'Închide', {
          duration: 2500,
          panelClass: 'custom-snackbar',
          horizontalPosition: 'center',
          verticalPosition: 'top',
        });
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

  calculeazaMedia(materia: any) {
    for (let nota1 of this.vectorCatalog) {
      if (materia === "Fizică") {
        materia = "Fizica"
      } else if (materia === "Matematică") {
        materia = "Matematica"
      }
      else if(this.materia === "Limba engleză"){
        materia= "Limbaengleza";
    }
    else if(this.materia === "Limba și literatura română"){
      materia= "Limbasiliteraturaromana";
    }
      if (nota1[materia] && nota1[materia].Nota1 != 0 && nota1[materia].Nota2 != 0 && nota1[materia].Nota3 != 0 && nota1[materia].Nota4 != 0) {
        nota1[materia].Medie =
          Math.round(0.2 * nota1[materia].Nota1
            + 0.2 * nota1[materia].Nota2
            + 0.3 * nota1[materia].Nota3
            + 0.3 * nota1[materia].Nota4);
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

  verificareMaterie(materie:string):boolean{
    let ok3 =false;
    if(materie === "Fizica"){
      materie= "Fizică";
  }
  if(materie === "Matematica"){
    materie= "Matematică";
  }
  if(materie === "Limbaengleza"){
    materie= "Limba engleza";
  }
  if(materie === "Limbasiliteraturaromana"){
    materie= "Limba si literatura romana";
  }

    if(materie === this.materia){
      ok3 = true;
    }
    return ok3;
  }

  contineCheia(materie:string, nota:any):boolean{
    let ok3 =false;

    if(nota.hasOwnProperty(materie)){
      ok3 = true;
    }
   
    return ok3;
  }

  updateazaNota(nota: any, materie: string, notaKey: string, event: any) {
    const value = (event.target as HTMLElement)?.textContent || ''; 
    const intValue = value === '' ? 0 : parseInt(value);
    
    if (!nota[materie]) {
      nota[materie] = {};
    }
    
    nota[materie][notaKey] = intValue;
    
    this.calculeazaMediaLive(nota, materie);
    console.log("Recalculare 12"+notaKey+" "+materie);
    console.log("Recalculare "+value);
    
}

calculeazaMediaLive(nota: any, materie: string) {
    const note = ['Nota1', 'Nota2', 'Nota3', 'Nota4'].map(key => nota[materie][key] || 0);
    const total = note.reduce((sum, value) => sum + value, 0);
    const media = total / note.length;

    if (nota[materie] && nota[materie].Nota1 != 0 && nota[materie].Nota2 != 0 && nota[materie].Nota3 != 0 && nota[materie].Nota4 != 0) {
      nota[materie].Medie =
        Math.round(0.2 * nota[materie].Nota1
          + 0.2 * nota[materie].Nota2
          + 0.3 * nota[materie].Nota3
          + 0.3 * nota[materie].Nota4);
    }

    //nota[materie].Medie = media;

    

    console.log("Recalculare 2"+media);
}


}
