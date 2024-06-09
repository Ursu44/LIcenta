import { Component } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import axios from 'axios';
import { VerifyCodComponent } from '../verify-cod/verify-cod.component';

@Component({
  selector: 'app-forgot-popup',
  templateUrl: './forgot-popup.component.html',
  styleUrls: ['./forgot-popup.component.css']
})
export class ForgotPopupComponent {
  
  constructor(private dialog:MatDialog) {}

  formData: any = {
    mail: 'ceva'  
  };

    trimiteParola(){
      const input = document.getElementById('mail')  as HTMLInputElement | null; 
      const mailValue = input?.value;
      console.log(mailValue);
      const backendEndpoint = 'http://localhost:8087/change/';
      this.formData.mail = mailValue;
      const jsonObj = JSON.stringify(this.formData);
    axios.post(backendEndpoint, jsonObj, {
      headers: {
        'Content-Type': 'application/json'
      }
    }).then(async response => {

      this.formData = {
        mail: ''
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
    dialogRefresh.width = "50%";
    dialogRefresh.width = "50%";
    
    this.dialog.open(VerifyCodComponent);
  }
}
