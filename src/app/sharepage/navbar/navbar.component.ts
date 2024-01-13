import { Component, HostListener, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
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
  isProfesorSelected: boolean = false;

  formData: any = {
    nume: '',
    prenume: '',
    mail: '',
    parola: '',
    materie: ''
  };

  constructor(private httpClient: HttpClient) { 
    this.isProfesorSelected = false;
  }


  ngOnInit(): void {
    this.formLogin = new window.bootstrap.Modal(
      document.getElementById("LoginForm")
    );

    this.formRegister = new window.bootstrap.Modal(
      document.getElementById("RegisterForm")
    );

    this.formData = {
      nume: '',
      prenume: '',
      mail: '',
      parola: '',
      materie: ''
    };
  
  }

  openLogin() {
    this.formLogin.show();
  }

  openRegister() {
    this.formRegister.show();
  }

  doHiddingLogin() {
    this.formLogin.hide();
  }

  doHiddingRgister() {
    this.formRegister.hide();
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
        //window.location.reload();
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
}
