import { Component, HostListener, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import axios from 'axios';
import { ShareDataService } from 'src/app/services/share-data.service';
import { ShareCatalogService } from 'src/app/services/share-catalog.service';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ForgotPopupComponent } from '../forgot-popup/forgot-popup.component';
import { RefreshService } from 'src/app/services/refresh.service';
import { PopupComponent } from '../popup/popup.component';
import { CourseDetailsService } from 'src/app/services/course-details.service';
import { AppComponent } from 'src/app/app.component';
import { ServiciuService } from 'src/app/services/serviciu.service';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { FormGroup } from '@angular/forms';

declare var window: any;

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  ok = false;
  ok1 = false;
  ok2 = false;
  isScrolled = false;
  formRegister: any;
  formLogin: any;
  snackBar: any;
  confirmModal: any;
  formSuccesLogin: any;
  isProfesorSelected: boolean = false;
  animaterii:any = new Map<string, object>(); 
  grupe: string = "";
  grupeAici: string[] = [];
  materiisiani:any;
  formData: any = {
    nume: '',
    prenume: '',
    mail: '',
    parola: '',
    materie: '',
    grupe:'1301,1302'
  };

  loginFormData:any = {
    username: '',
    password: ''
  }

  refreshData:any = {
    username: '',
    password: ''
  }
  ani : any = localStorage.getItem("an");
  raspunsServer: string = ''; 
  router: any;
  categories:any;
  selected:any;

  constructor(private servicu:ServiciuService ,private appComponent:AppComponent,private courseDetalis:CourseDetailsService, private httpClient: HttpClient, private shareDataService:ShareDataService, private shareDataCatalog:ShareCatalogService, private dialog:MatDialog,  private refresh:RefreshService) { 
    this.isProfesorSelected = false;
  }

  selectedItems: string[] = [];
  selectedItems1: string[] = [];
  dropdownItems: string[] = ['Fizică - AnI', 'Matematică - AnI', 'Istorie - AnI', 'Geografie - AnI', 'Limba engleza - AnI', 'Limba si literatura romana - AnI', 'Fizică - AnII', 'Matematică - AnII', 'Istorie - AnII', 'Geografie - AnII', 'Limba engleza - AnII', 'Limba si literatura romana - AnII', 'Fizică - AnIII', 'Matematică - AnIII', 'Istorie - AnIII', 'Geografie - AnIII', 'Limba engleza - AnIII', 'Limba si literatura romana - AnIII'];
  dropdownItems1: string[] = ['1101', '1102', '1103', '1104', '1105', '1201', '1202', '1203', '1204', '1301', '1302', '1303', '1304'];

  ngOnInit(): void {
    //localStorage.clear();
    //this.appComponent.raspuns1 = true;
    const accessToken = localStorage.getItem('access_token');
    const refreshToken = localStorage.getItem('refresh_token');
    if (accessToken && refreshToken) {
      this.ok = true;
    }

    if(!accessToken && refreshToken){
      this.ok1 = true;
    }
    
    this.formLogin = new window.bootstrap.Modal(
      document.getElementById("LoginForm")
    );

    this.formRegister = new window.bootstrap.Modal(
      document.getElementById("RegisterForm")
    );

    this.formSuccesLogin = new window.bootstrap.Modal(
      document.getElementById("SuccesLogin")
    );

    this.formData = {
      nume: '',
      prenume: '',
      mail: '',
      parola: '',
      materie: '',
      grupe:'',
      materii:new Map<string, object>()
    };

    this.loginFormData = {
      username: '',
      password: ''
    }
    
    this.refreshData = {
      username: '',
      password: ''
    }

  }
  preiaAni(event: Event) {
    const selectElement = event.target as HTMLSelectElement;
   this.materiisiani =Array.from(selectElement.selectedOptions).map(option => option.value);
  }

  preiaGrupe(event: Event) {
    const selectElement = event.target as HTMLSelectElement;
    this.grupeAici =Array.from(selectElement.selectedOptions).map(option => option.value);
  }

  openLogin() {
    this.formLogin.show();
  }

  openRegister() {
    this.formRegister.show();
  }

  openRefrsh() {
    this.formSuccesLogin.show();
  }

  doHiddingLogin() {
    this.formLogin.hide();
  }

  doHiddingRgister() {
    this.formRegister.hide();
  }
  doHiddingRefresh(){
    this.formSuccesLogin.hide();
  }

  @HostListener('window:scroll', ['$event'])
  onScroll(event: Event): void {
    if (window.pageYOffset > 100) {
      this.isScrolled = true;
    } else {
      this.isScrolled = false;
    }
  }

  handleRadioButtonSelection(event: Event) {
    const radioButton = event.target as HTMLInputElement;
    this.isProfesorSelected = radioButton.value === 'profesor';
    console.log("Valoarea adaugata adevar "+this.isProfesorSelected);
    console.log("Rol selectat "+radioButton.value);
    if(radioButton.value !== 'profesor'){
      console.log("Rol selectat 1 "+radioButton.value)
      this.formData.rol = "student";
      delete this.formData.materie;
      delete this.formData.grupe;
      delete this.formData.materii;
    }
    else{
      console.log("Rol selectat 2 "+radioButton.value)
      this.formData.rol = "profesor";
      this.formData.materie='';
      this.formData.grupe='';
     this.formData.materii=new Map<string, object>();
    }
  }

  async sendFormData() {
    console.log("role"+this.formData.rol);
    let backendEndpoint 
    if(this.formData.rol === 'profesor'){
        backendEndpoint = 'http://localhost:8086/add/profesor';
        for(let i = 0;i<this.grupeAici.length; i++){
          this.grupe += this.grupeAici[i]+",";
        }
        this.grupe = this.grupe.substring(0, this.grupe.length - 1);
        this.formData.grupe = this.grupe;
        for(let i=0 ;i<this.materiisiani.length; i++){
          let j = i+1;
          this.formData.materii["materii"+j] = this.materiisiani[i];
        }
        console.log('Selected item123:', this.formData.materii);
        //console.log("daaaaaaaaaaaaaaaaaaaa "+this.animaterii);
        //this.formData.materii = this.animaterii;
    }
    else{
      backendEndpoint = 'http://localhost:8080/firebase/add/student';
    }    
    const jsonObj = JSON.stringify(this.formData);
    console.log('Selected item: ', jsonObj);
    axios.post(backendEndpoint, jsonObj, {
      headers: {
        'Content-Type': 'application/json'
      }
    })
      .then(response => {
        console.log('JSON trimis cu succes:', response.data);
        this.doHiddingRgister();
        //window.location.reload();
        //this.confirmModal.show();
        this.formData = {
          nume: '',
          prenume: '',
          mail: '',
          parola: '',
          materie: '',
          rol:'',
          grupe:'',
           materii:new Map<string, object>()
        };
        
      })
      .catch(error => {
        console.log("Aicisa");
        console.error('Eroare la trimiterea JSON-ului:', error);
      });

  }

  async sendFormDataLogin() {
    const jsonObj = JSON.stringify(this.loginFormData);
    console.log('Form Data:', jsonObj);
    const backendEndpoint = 'http://localhost:8081/login';
    
    axios.post(backendEndpoint, jsonObj, {
      headers: {
        'Content-Type': 'application/json'
      }
    })
      .then(async response => {
        console.log('JSON trimis cu succes:', response.data);
        console.log('Acess-token', response.data.access_token);
        console.log('Refrsh ', response.data.refresh_token);

        const accessToken = response.data.access_token;
        
        localStorage.clear();
        localStorage.setItem('access_token', accessToken);
        localStorage.setItem('refresh_token', response.data.refresh_token);
        this.ok = true;
        this.doHiddingLogin();
      this.loginFormData = {
        username: '',
        password: ''
      }

      })
      
      .catch(error => {
        console.error('Eroare la trimiterea JSON-ului:', error);
        this.ok2= true;
      }); 
  }

  async efectueazaCerereaGet() {
    const accessToken = localStorage.getItem('access_token');
    const refreshToken = localStorage.getItem('refresh_token');
    if (accessToken && refreshToken) {

      const accessTokenPayload = this.decodeJwtPayload(accessToken);
      const expirationTime = accessTokenPayload.exp * 1000; 
      let securedEndpoint;
      if(!this.esteProfesor()){
         securedEndpoint = 'http://localhost:8082/elev';
      }
      else{
         securedEndpoint = 'http://localhost:8082/profesor';
      }
      if (expirationTime > Date.now()) {;
        try {
          const response = await axios.get(securedEndpoint, {
            method: 'GET',
            headers: {
              'Content-Type': 'text/plain',
              'Authorization': `Bearer ${accessToken}`,
            },
          });
          /*let raspunsServer = response.data;
          let raspunsServerModficat = raspunsServer.slice(0, -1);
          const raspunsJSON = JSON.stringify(raspunsServerModficat);
          this.shareDataService.sendRaspuns(raspunsServerModficat);
           console.log("Raspuns server "+ raspunsServerModficat);
           console.log("Raspuns server json"+ raspunsJSON);*/

          localStorage.setItem("an" , response.data);
           

         
           if(!this.esteProfesor()){
           console.log("AN primit "+response.data);
           localStorage.setItem("ani", response.data);
           this.courseDetalis.curataVector();
           this.courseDetalis.modifica(response.data);
           }
           else {
            console.log("AN primit "+response.data);
            localStorage.setItem("ani", response.data);
            this.courseDetalis.curataVectorProf();
            this.courseDetalis.modificaProf(response.data);
           }
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

  async refreshAccessToken() {
    const refreshToken = localStorage.getItem('refresh_token');
    const accessToken = localStorage.getItem('access_token');
    console.log("TOken "+accessToken);
    console.log("Refresh folosit "+refreshToken);
    const backendEndpoint = 'http://localhost:8081/oauth/access_token';

  const requestData = {
    grant_type: 'refresh_token',
    refresh_token: refreshToken,
  };

  try {
    const response = await axios.post(backendEndpoint, requestData, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Authorization': `Bearer ${accessToken}`,
      },
    });

    const newAccessToken = response.data.access_token;
    console.log('Noul access token:', newAccessToken);
    localStorage.clear();
    this.ok = true;
    localStorage.setItem('access_token', newAccessToken);
    localStorage.setItem('refresh_token', response.data.refresh_token);

    return newAccessToken;
    } catch (error) {
      console.error('Eroare la reîmprospătarea token-ului de acces:', error);
      throw error; 
    }
  }
  
  decodeJwtPayload(token: string) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    console.log("1 " + base64Url);
    console.log("2 " + base64);
    console.log("3 " + jsonPayload);

    return JSON.parse(jsonPayload);
  }

  async logout(){

    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    //this.formLogin.show();
    this.ok = false;
   
  }

  async accessStatistici(){
    console.log("Am intrat la statisci")
    const accessToken = localStorage.getItem('access_token');
    const refreshToken = localStorage.getItem('refresh_token');

    if (accessToken && refreshToken) {
      const accessTokenPayload = this.decodeJwtPayload(accessToken);
      const expirationTime = accessTokenPayload.exp * 1000; 
      
    if (expirationTime > Date.now()) {
        let securedEndpoint ;
    if(this.esteProfesor()){

       securedEndpoint = 'http://localhost:8096/statistici/statisticiProfesor';
    }
    else{
      securedEndpoint = 'http://localhost:8096/statistici/statisticiElev';
    }
 
        try {
          const response = await axios.get(securedEndpoint, {
            method: 'GET',
            headers: {
              'Content-Type': 'text/plain',
              'Authorization': `Bearer ${accessToken}`,
            },
          });
          let raspunsServer = response.data;
          let raspunsTRimis = JSON.stringify(raspunsServer);
          console.log("Raspuns statistici "+raspunsTRimis);
          this.servicu.sendStatistici(raspunsTRimis);
         
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
  
  async accesCatalog(){
    console.log("Am intrat la catalog")
    const accessToken = localStorage.getItem('access_token');
    const refreshToken = localStorage.getItem('refresh_token');

    if (accessToken && refreshToken) {
      const accessTokenPayload = this.decodeJwtPayload(accessToken);
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
          const response = await axios.get(securedEndpoint, {
            method: 'GET',
            headers: {
              'Content-Type': 'text/plain',
              'Authorization': `Bearer ${accessToken}`,
            },
          });
          let raspunsServer = response.data;
          const raspunsJSON = JSON.stringify(raspunsServer);
          console.log("Raspuns "+raspunsServer);
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

  open(){
    this.doHiddingLogin();
    this.dialog.open(PopupComponent);
  }

  openForgot(){
    this.doHiddingLogin();
    this.dialog.open(ForgotPopupComponent);

  }

  async operatie(){
    if(this.ok1 === false){
      this.openLogin();
    }
    else if(this.ok1 === true){
      this.refreshAccessToken();
    }
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
}