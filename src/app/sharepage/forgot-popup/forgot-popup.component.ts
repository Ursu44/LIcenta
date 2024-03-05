import { Component } from '@angular/core';
import axios from 'axios';

@Component({
  selector: 'app-forgot-popup',
  templateUrl: './forgot-popup.component.html',
  styleUrls: ['./forgot-popup.component.css']
})
export class ForgotPopupComponent {
  
  formData: any = {
    mail: 'ceva'  
  };

    trimiteParola(){
      const input = document.getElementById('mail')  as HTMLInputElement | null; 
      const mailValue = input?.value;
      console.log(mailValue);
      const backendEndpoint = 'http://localhost:8087/change/password/profesor';
      this.formData.mail = mailValue;
      const jsonObj = JSON.stringify(this.formData);
    axios.put(backendEndpoint, jsonObj, {
      headers: {
        'Content-Type': 'application/json'
      }
    }).then(async response => {

      this.formData = {
        mail: ''
      }

      })
      .catch(error => {
        console.error('Eroare la trimiterea JSON-ului:', error);
      }); 
    }

}
