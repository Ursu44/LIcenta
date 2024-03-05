import { Injectable } from '@angular/core';
import axios from 'axios';
import { PopupComponent } from '../sharepage/popup/popup.component';
import { MatDialog } from '@angular/material/dialog';

@Injectable({
  providedIn: 'root'
})
export class RefreshService {

  constructor(private dialog:MatDialog) { }

  async refresh(){
    const accessToken = localStorage.getItem('access_token');
    if(accessToken){
      const decodedClaims = this.decodeJwtClaims(accessToken);
      const expirationTime = decodedClaims.exp * 1000; 

      if (expirationTime < Date.now()) {
        console.log("Da i cu refresh");
        this.open();
      }
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

  async refreshAccessToken() {
    const refreshToken = localStorage.getItem('refresh_token');
    const accessToken = localStorage.getItem('access_token');
    console.log("TOken "+accessToken);
    console.log("Refresh folosit "+refreshToken);
    const backendEndpoint = 'http://localhost:8081/oauth/access_token';

  const requestData = {
    grant_type: 'refresh_token',
    refresh_token: refreshToken,
  };

  try {
    const response = await axios.post(backendEndpoint, requestData, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Authorization': `Bearer ${accessToken}`,
      },
    });

    const newAccessToken = response.data.access_token;
    console.log('Noul access token:', newAccessToken);
    localStorage.clear();
    localStorage.setItem('access_token', newAccessToken);
    localStorage.setItem('refresh_token', response.data.refresh_token);

    return newAccessToken;
    } catch (error) {
      console.error('Eroare la reîmprospătarea token-ului de acces:', error);
      throw error; 
    }
  }

  open(){
    this.dialog.open(PopupComponent);
  }
  
}
