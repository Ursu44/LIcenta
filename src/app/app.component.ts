import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';


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
  apasat = true;

  constructor(private formBuilder: FormBuilder, private dialog : MatDialog) { }
  show(){
    this.showModal = true; 
    
  }
  hide(){
    this.showModal = false;
  }
  
}
