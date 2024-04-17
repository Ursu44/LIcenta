import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { RefreshService } from 'src/app/services/refresh.service';


@Component({
  selector: 'app-popup',
  templateUrl: './popup.component.html',
  styleUrls: ['./popup.component.css']
})
export class PopupComponent {

 constructor(private refreshService:RefreshService){}
 
  async refresh(){
    this.refreshService.refreshAccessToken();
  }

}
