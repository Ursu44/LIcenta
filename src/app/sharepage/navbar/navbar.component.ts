import { Component, HostListener, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import axios from 'axios';

declare var window: any;

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  isScrolled = false;
  formRegister: any;
  formLogin: any;
  confirmModal: any;
  formRefresh: any;
  isProfesorSelected: boolean = false;

  formData: any = {
    nume: '',
    prenume: '',
    mail: '',
    parola: '',
    materie: ''
  };

  loginFormData:any = {
    username: '',
    password: ''
  }

  refreshData:any = {
    username: '',
    password: ''
  }

  raspunsServer: string = ''; 
  jwtHelper: any;
  constructor(private httpClient: HttpClient) { 
    this.isProfesorSelected = false;
  }


  ngOnInit(): void {
    this.efectueazaCerereaGet();
    this.formLogin = new window.bootstrap.Modal(
      document.getElementById("LoginForm")
    );

    this.formRegister = new window.bootstrap.Modal(
      document.getElementById("RegisterForm")
    );

    this.formRefresh = new window.bootstrap.Modal(
      document.getElementById("RefreshForm")
    );

    this.formData = {
      nume: '',
      prenume: '',
      mail: '',
      parola: '',
      materie: ''
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

  openLogin() {
    this.formLogin.show();
  }

  openRegister() {
    this.formRegister.show();
  }

  openRefrsh() {
    this.formRefresh.show();
  }

  doHiddingLogin() {
    this.formLogin.hide();
  }

  doHiddingRgister() {
    this.formRegister.hide();
  }
  doHiddingRefresh(){
    this.formRefresh.hide();
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

    if(radioButton.value !== 'profersor'){
      delete this.formData.materie;
    }
  }

  async sendFormData() {

    const jsonObj = JSON.stringify(this.formData);
    console.log('Form Data:', jsonObj);

    const backendEndpoint = 'http://localhost:8080/firebase/add/student';
    
    axios.post(backendEndpoint, jsonObj, {
      headers: {
        'Content-Type': 'application/json'
      }
    })
      .then(response => {
        console.log('JSON trimis cu succes:', response.data);
        this.doHiddingRgister();
        window.location.reload();
        //this.confirmModal.show();
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

        const securedEndpoint = 'http://localhost:8082/';
       
       const securedResponse = await fetch(securedEndpoint, {
          headers: {
            method: 'GET',
           'Content-Type': 'text/plain',
            'Authorization': `Bearer ${accessToken}`,
          },
        });
    
        if (!securedResponse.ok) {
          throw new Error(`HTTP error! Status: ${securedResponse.status}`);
        }

        const securedData = await securedResponse.text();
        console.log('Răspuns de la endpoint-ul securizat:', securedData);

        this.doHiddingRgister();

        //window.location.reload();
        //this.confirmModal.show();
      this.loginFormData = {
        username: '',
        password: ''
      }

      })
      .catch(error => {
        console.error('Eroare la trimiterea JSON-ului:', error);
      }); 
  }

  async efectueazaCerereaGet() {
    const accessToken = localStorage.getItem('access_token');
    const refreshToken = localStorage.getItem('refresh_token');
    if (accessToken && refreshToken) {
      const accessTokenPayload = this.decodeJwtPayload(accessToken);
      const expirationTime = accessTokenPayload.exp * 1000; 
      

      if (expirationTime > Date.now()) {
        const securedEndpoint = 'http://localhost:8082/';
        try {
          const response = await axios.get(securedEndpoint, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${accessToken}`,
            },
          });
          this.raspunsServer = response.data;
           console.log("Raspuns server "+ response.data);
        } catch (error) {
          console.error('Eroare la cererea GET cu token valid:', error);
        }
      } else {
        console.log('Token-ul de acces a expirat. Solicităm o nouă autentificare.');
        this.formLogin.show();
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
    this.formLogin.show();
    /*const securedEndpoint = 'http://localhost:8080/logout';
        try {
          const response = await axios.post(securedEndpoint, {
            headers: {
              'Authorization': `Bearer ${accessToken}`,
            },
          });
        }
          catch (error) {
            console.error('Eroare la logout:', error);
    }*/
  }
  


}