import { Component } from '@angular/core';
import axios from 'axios';
import { CourseDetailsService } from 'src/app/services/course-details.service';
import { RefreshService } from 'src/app/services/refresh.service';
import { ShareDataService } from 'src/app/services/share-data.service';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.css']
})
export class CoursesComponent {
    details : any[] = [];

    constructor(private courseDetalis:CourseDetailsService, private shareDataService:ShareDataService, private refresh:RefreshService) {}

    ngOnInit() : void{
      this.details = this.courseDetalis.courseDetails;
    }

    async afisezMateria(nume: string):Promise<void>{
      console.log("Aceasta este materia selectata "+nume);
      const accessToken = localStorage.getItem('access_token');
      const refreshToken = localStorage.getItem('refresh_token');
      if (accessToken && refreshToken) {
  
        const accessTokenPayload = this.decodeJwtPayload(accessToken);
        const expirationTime = accessTokenPayload.exp * 1000; 
  
        if (expirationTime > Date.now()) {
          const securedEndpoint = 'http://localhost:8082/'+nume;
          try {
            const response = await axios.get(securedEndpoint , {
              method: 'GET',
              headers: {
                'Content-Type': 'text/plain',
                'Authorization': `Bearer ${accessToken}`,
              },
            });
            let raspunsServer = response.data;
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

    decodeJwtPayload(token: string) {
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
  

}
