import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  title = 'LearnHub';
  showModal!: boolean;
  registerForm!: FormGroup;
  submitted = false;

  constructor(private formBuilder: FormBuilder) { }
  show(){
    this.showModal = true; 
    
  }
  hide(){
    this.showModal = false;
  }

  
}
