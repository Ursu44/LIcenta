import { Component, OnInit } from '@angular/core';
import { ShareDataService } from '../../services/share-data.service';
import axios from 'axios';

@Component({
  selector: 'app-cursuri',
  templateUrl: './cursuri.component.html',
  styleUrls: ['./cursuri.component.css']
})
export class CursuriComponent implements OnInit {
  raspuns: any = null;

  constructor(private shareDataService:ShareDataService) {}

  formData: any = {
    nume: '',
    prenume: '',
    mail: '',
    parola: '',
    materie: ''
  };

  ngOnInit(): void {
    setTimeout(() => {
      this.raspuns = this.shareDataService.getRaspuns();
      console.log("Uite ce am primit"+this.raspuns);
      console.log("Raspuns primit 11"+ this.raspuns);
    }, 1000);
    
  }

  async updateProgres(){
    const endpoint = 'http://localhost:8083/';
    const jsonObj = JSON.stringify(this.formData);
    const accessToken = localStorage.getItem('access_token');

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
