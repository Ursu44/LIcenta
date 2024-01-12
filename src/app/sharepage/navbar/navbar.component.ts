import { Component, HostListener, OnInit } from '@angular/core';

declare var window:any;

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})


export class NavbarComponent implements OnInit{
  
  isScrolled = false;
  formRegister:any;
  formLogin:any;
  isProfesorSelected: boolean = false;

  ngOnInit(): void {
    this.formLogin = new window.bootstrap.Modal(
      document.getElementById("LoginForm")
    );

    this.formRegister =  new window.bootstrap.Modal(
      document.getElementById("RegisterForm")
    );
  }

  openLogin(){
    this.formLogin.show();
  }


  openRegister(){
    this.formRegister.show();
  }


  doHiddingLogin(){
    this.formLogin.hide();
  }

  doHiddingRgister(){
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
  }

}
