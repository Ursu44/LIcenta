import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CourseDetailsService {

  constructor() { }

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
