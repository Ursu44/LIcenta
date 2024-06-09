import { Component } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import axios from 'axios';
import { PassResetComponent } from '../pass-reset/pass-reset.component';

@Component({
  selector: 'app-verify-cod',
  templateUrl: './verify-cod.component.html',
  styleUrls: ['./verify-cod.component.css']
})
export class VerifyCodComponent {

  constructor(private dialog:MatDialog) {}

  formData: any = {
    cod: '123'  
  };

  verifica(){
    console.log("Da da da")
    const input = document.getElementById('cod')  as HTMLInputElement | null; 
    const mailValue = input?.value;
    console.log(mailValue);
    const backendEndpoint = 'http://localhost:8087/change/pass';
    this.formData.cod = mailValue;
    const jsonObj = JSON.stringify(this.formData);
  axios.post(backendEndpoint, jsonObj, {
    headers: {
      'Content-Type': 'application/json'
    }
  }).then(async response => {

    this.formData = {
      cod: ''
    }
      this.dialog.closeAll();
      this.open();
    })
    .catch(error => {
      console.error('Eroare la trimiterea JSON-ului:', error);
    }); 
  }

  open(){
    const dialogRefresh = new MatDialogConfig();
    dialogRefresh.width = "60%";
    dialogRefresh.width = "60%";
    
    this.dialog.open(PassResetComponent);
  }

}
