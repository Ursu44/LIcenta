import { Component } from '@angular/core';
import axios from 'axios';
import { RefreshService } from 'src/app/services/refresh.service';
import { ShareCatalogService } from 'src/app/services/share-catalog.service';
import { ShareDataService } from 'src/app/services/share-data.service';
import { PopupComponent } from '../popup/popup.component';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.css']
})
export class CatalogComponent {
  raspunsCatalog: any = this.shareDataService.getRaspunsCatalog();;

  constructor(private shareDataService:ShareCatalogService, private refresh:RefreshService, private dialog: MatDialog) {}

  formData: any = {
    nume: 0,
    prenume: '',
    mail: '',
    parola: '',
    materie: ''
  };

  ngOnInit(): void {
      this.preiaDate();
  }

  preiaDate(): void{
    setTimeout(() => {
      this.raspunsCatalog = this.shareDataService.getRaspunsCatalog();
      console.log("Uite ce am primit catalog"+this.raspunsCatalog);
    }, 2100);  
  }

  async adaugaNota(){
    const endpoint = 'http://localhost:8085/';
    const accessToken = localStorage.getItem('access_token');
    var notaNoua = document.getElementById('element')  as HTMLInputElement | null;
    let value = notaNoua?.value;
    console.log("Nota noua "+notaNoua);
    console.log("Nota noua1 "+notaNoua);

    const jsonObj = JSON.parse(this.raspunsCatalog);  
    jsonObj.nota2 = value;
    jsonObj.nota3 = value;

    if(accessToken){
      this.refresh.refresh();
     await axios.put(endpoint, jsonObj, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${accessToken}`
      }
    })
      .then(response => {
        console.log('JSON trimis cu succes:', response.data);
        this.shareDataService.sendRaspunsCatalog(jsonObj);
        this.formData = {
          nume: 0,
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
    var notaNoua = document.getElementById('element') as HTMLInputElement | null;
    console.log("Nota noua "+notaNoua)
    const accessToken = localStorage.getItem('access_token');
    let ok =false;
    if(accessToken){
      const claimsDecodat = this.decodeJwtClaims(accessToken)
      let rol = claimsDecodat.roles;
      let rol1 = JSON.stringify(rol);
        const numeRol = rol1.trim();
        ok = (numeRol === '["ROLE_PROFESOR"]');
  }
    return ok;
  }

  open(){
    const dialogRefresh = new MatDialogConfig();
    dialogRefresh.width = "60%";
    
    this.dialog.open(PopupComponent);
  }
  
}
