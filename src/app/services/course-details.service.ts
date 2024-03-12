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
     srcImg:"https://timisplus.ro/wp-content/uploads/2020/02/matematica.jpg"
    },

    {
      id:2,
     courseName:"Fizica",
     srcImg:"https://mariusdavidesco.files.wordpress.com/2018/09/fizica-buna.jpg?w=1197"
    },

    {
      id:3,
      courseName:"Limba si literatura romana",
      srcImg:"https://theofania.ro/wp-content/uploads/2023/09/e333b5775e77a526dc3e24b8bc2737fd.jpg"
    },

    {
      id:4,
      courseName:"Istorie",
      srcImg:"https://oradeistorie.ro/wp-content/uploads/timpul-istoric.jpg"
    },
    {
      id:5,
      courseName:"Geografie",
      srcImg:"https://abi.de/cdn/37/63/Ein-Kompass-liegt-auf-einer-Weltkarte-3763569309d89f0f5e5cbc45e684cd70da444b4f.jpg"
    },
    {
      id:6,
      courseName:"Limba engleza",
      srcImg:"https://ltni.ro/wp-content/uploads/2023/05/english-british-england-language-education-concept-58368527-770x400.jpg"
    }
  ];

}
