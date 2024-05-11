import { Component } from '@angular/core';
import axios from 'axios';
import { AppComponent } from 'src/app/app.component';
import { CourseDetailsService } from 'src/app/services/course-details.service';
import { RefreshService } from 'src/app/services/refresh.service';
import { ShareCatalogService } from 'src/app/services/share-catalog.service';
import { ShareDataService } from 'src/app/services/share-data.service';
import { SharenumeService } from 'src/app/services/sharenume.service';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.css']
})
export class CoursesComponent {
    details : any[] = [];

    constructor(private appComponent:AppComponent,private courseDetalis:CourseDetailsService, private shareDataService:ShareDataService, private refresh:RefreshService, private shareNume:SharenumeService) {}

    ngOnInit() : void{
      //this.appComponent.raspuns1 = true;
      this.details = this.courseDetalis.courseDetails;
    }

    async afisezMateria(nume: string):Promise<void>{
      this.shareNume.sendNume(nume);
      console.log("Aceasta este materia selectata "+nume);
      const accessToken = localStorage.getItem('access_token');
      const refreshToken = localStorage.getItem('refresh_token');
      if (accessToken && refreshToken) {
  
        const accessTokenPayload = this.decodeJwtClaims(accessToken);
        const expirationTime = accessTokenPayload.exp * 1000; 
  
        if (expirationTime > Date.now()) {
          let securedEndpoint:string;
          if(this.esteProfesor()){
          securedEndpoint = 'http://localhost:8082/'+nume+'/profesor';
          }
          else{
            securedEndpoint = 'http://localhost:8082/'+nume+'/elev';
          }
          try {
            const response = await axios.get(securedEndpoint , {
              method: 'GET',
              headers: {
                'Content-Type': 'text/plain',
                'Authorization': `Bearer ${accessToken}`,
              },
            });
            let raspunsServer = response.data;
            console.log("Raspuns ani "+raspunsServer);
            let raspunsServerModficat = raspunsServer.slice(0, -1);
            const raspunsJSON = JSON.stringify(raspunsServerModficat);
            this.shareDataService.sendRaspuns(raspunsServerModficat);
             console.log("Raspuns server "+ raspunsServerModficat);
             console.log("Raspuns server json"+ raspunsJSON);
          } catch (error) {
            console.error('Eroare la cererea GET cu token valid:', error);
          }
        } else {
          console.log('Token-ul de acces a expirat. Solicităm o nouă autentificare.');
          this.refresh.refresh();
          //this.formLogin.show();
          //await this.refreshAccessToken();
        }
      } else {
        console.error('Token-uri lipsă.');
      }
    }

    decodeJwtClaims(token: string) {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));
  
      console.log("1 " + base64Url);
      console.log("2 " + base64);
      console.log("3 " + jsonPayload);
  
      return JSON.parse(jsonPayload);
    }
  
    esteProfesor() :boolean{
      var notaNoua = document.getElementById('element') as HTMLInputElement | null;
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
}
