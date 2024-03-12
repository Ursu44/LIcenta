import { Component, OnInit } from '@angular/core';
import { ShareDataService } from '../../services/share-data.service';
import axios from 'axios';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { PopupComponent } from '../popup/popup.component';
import { RefreshService } from 'src/app/services/refresh.service';

@Component({
  selector: 'app-cursuri',
  templateUrl: './cursuri.component.html',
  styleUrls: ['./cursuri.component.css']
})
export class CursuriComponent implements OnInit {
  i = 0;
  raspunsObj:any;
  raspuns: any;
  pozitieActuala = 0;
  progres = 0;
  progresAdaugat = 0;

  constructor(private shareDataService: ShareDataService, private dialog: MatDialog, private refresh: RefreshService) {}

  formData: any = {
    nume: '',
    prenume: '',
    mail: '',
    parola: '',
    materie: ''
  };

  ngOnInit(): void {
    this.preiaDate();
  }

  preiaDate(): void {
    this.getData();
  }

  getData() {
    setTimeout(() => {
      this.raspuns = this.shareDataService.getRaspuns();
      console.log("Uite ce am primit" + this.raspuns);
      this.raspunsObj = JSON.parse(this.shareDataService.getRaspuns());
      this.pozitieActuala = Object.keys(this.raspunsObj).length;
      this.progresAdaugat = 100 / this.pozitieActuala;
      this.progres = this.progresAdaugat ;
      console.log("Dimensiune "+this.i)
    }, 3000);
  }
  

  async updateProgres() {
    const endpoint = 'http://localhost:8083/';
    const jsonObj = JSON.stringify(this.formData);
    const accessToken = localStorage.getItem('access_token');
    
    if (accessToken) {
      this.refresh.refresh();
      axios.put(endpoint, jsonObj, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${accessToken}`
        }
      })
      .then(response => {
        console.log('JSON trimis cu succes:', response.data);
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

  open() {
    const dialogRefresh = new MatDialogConfig();
    dialogRefresh.width = "60%";
    this.dialog.open(PopupComponent);
  }

  actualizeazapozitiaInainte(){
    if(this.pozitieActuala>1 && this.pozitieActuala<=4){
      this.pozitieActuala--;
      this.progres += this.progresAdaugat;
    }
  }

  actualizeazapozitiaInapoi(){
    if(this.pozitieActuala>=1 && this.pozitieActuala<4){
      this.pozitieActuala++;
      this.progres -= this.progresAdaugat;
    }
  }
}
