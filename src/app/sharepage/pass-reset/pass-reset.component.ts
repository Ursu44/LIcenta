import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import axios from 'axios';

@Component({
  selector: 'app-pass-reset',
  templateUrl: './pass-reset.component.html',
  styleUrls: ['./pass-reset.component.css']
})
export class PassResetComponent {
  constructor(private snackBar: MatSnackBar,private dialog:MatDialog) {}

  formData: any = {
    parola: 'ceva'  
  };

  resetParola(){
    const input = document.getElementById('parola')  as HTMLInputElement | null; 
    const mailValue = input?.value;
    console.log(mailValue);
    const backendEndpoint = 'http://localhost:8087/change/pass/reset';
    this.formData.parola = mailValue;
    const jsonObj = JSON.stringify(this.formData);
  axios.post(backendEndpoint, jsonObj, {
    headers: {
      'Content-Type': 'application/json'
    }
  }).then(async response => {

    this.formData = {
      parola: ''
    }
    this.dialog.closeAll();
    this.snackBar.open('Parolă schimbată', 'Închide', {
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
}
