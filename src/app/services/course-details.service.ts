import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CourseDetailsService {
  private jsonData: any;
  constructor(private http: HttpClient) { }

  courseDetails=[
    {
     id:1,
     courseName:"Matematica",
     srcImg:"https://www.startupcafe.ro/sites/default/files/matematica.jpeg"
    },

    {
      id:2,
     courseName:"Fizica",
     srcImg:"https://mariusdavidesco.files.wordpress.com/2018/09/fizica-buna.jpg?w=1197"
    },

    {
      id:3,
      courseName:"Limba Romana",
      srcImg:"https://theofania.ro/wp-content/uploads/2023/09/e333b5775e77a526dc3e24b8bc2737fd.jpg"
    },

    {
      id:4,
      courseName:"Istorie",
      srcImg:"https://oradeistorie.ro/wp-content/uploads/timpul-istoric.jpg"
    }
  ];

}
