import { Component } from '@angular/core';
import axios from 'axios';
import { ShareCatalogService } from 'src/app/services/share-catalog.service';
import { ShareDataService } from 'src/app/services/share-data.service';

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.css']
})
export class CatalogComponent {
  raspunsCatalog: any = null;

  constructor(private shareDataService:ShareCatalogService) {}

  formData: any = {
    nume: '',
    prenume: '',
    mail: '',
    parola: '',
    materie: ''
  };

  ngOnInit(): void {
    setTimeout(() => {
      this.raspunsCatalog = this.shareDataService.getRaspunsCatalog();
      console.log("Uite ce am primit catalog"+this.raspunsCatalog);
    }, 1000);  
  }

  async adaugaNota(){
    const endpoint = 'http://localhost:8085/';
    const jsonObj = JSON.stringify(this.formData);
    const accessToken = localStorage.getItem('access_token');
    if(accessToken){
    axios.put(endpoint, jsonObj, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${accessToken}`
      }
    })
      .then(response => {
        console.log('JSON trimis cu succes:', response.data);
        window.location.reload();
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


  decodeJwtClaims(token: string) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
  }

  esteProfesor() :boolean{
    const accessToken = localStorage.getItem('access_token');
    let ok =false;
    if(accessToken){
      const claimsDecodat = this.decodeJwtClaims(accessToken)
      let rol = claimsDecodat.roles;
      let rol1 = JSON.stringify(rol);
      console.log("Tip "+typeof rol1)
        const numeRol = rol1.trim()
        console.log("Rol "+numeRol);
        ok = (numeRol === '["ROLE_PROFESOR"]');
        console.log("OK "+ok)
    
  }
    return ok;
  }
}
