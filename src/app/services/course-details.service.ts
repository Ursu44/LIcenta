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
     courseName:"Matematică",
     srcImg:"https://timisplus.ro/wp-content/uploads/2020/02/matematica.jpg",
     deAfisat:true
    },

    {
      id:2,
     courseName:"Fizică",
     srcImg:"https://mariusdavidesco.files.wordpress.com/2018/09/fizica-buna.jpg?w=1197",
     deAfisat:true
    },

    {
      id:3,
      courseName:"Limba si literatura romana",
      srcImg:"https://theofania.ro/wp-content/uploads/2023/09/e333b5775e77a526dc3e24b8bc2737fd.jpg",
      deAfisat:true
    },

    {
      id:4,
      courseName:"Istorie",
      srcImg:"https://oradeistorie.ro/wp-content/uploads/timpul-istoric.jpg",
      deAfisat:true
    },
    {
      id:5,
      courseName:"Geografie",
      srcImg:"https://abi.de/cdn/37/63/Ein-Kompass-liegt-auf-einer-Weltkarte-3763569309d89f0f5e5cbc45e684cd70da444b4f.jpg",
      deAfisat:true
    },
    {
      id:6,
      courseName:"Limba engleza",
      srcImg:"https://ltni.ro/wp-content/uploads/2023/05/english-british-england-language-education-concept-58368527-770x400.jpg",
      deAfisat:true
    }
  ];

  modifica(an:String){
    for(let curs of this.courseDetails){
        curs.courseName = curs.courseName+"_"+an;
    }
  }

  curataVector(){
    for(let curs of this.courseDetails){
      const [numeCurs, an] = curs.courseName.split('_');
      curs.courseName = numeCurs;
  }
  }
  curataVectorProf(){
    for(let curs of this.courseDetails){
      const [numeCurs, an] = curs.courseName.split('_');
      curs.courseName = numeCurs;
          curs.deAfisat = true;
      }
  }

  modificaProf(an:String){
    const materii:string[] = an.split(" ");
    let gasit:boolean=false;
    for(let curs of this.courseDetails){
      let gasit:boolean=false;
        for(let i = 0; i < materii.length; i++){
          if(materii[i].includes(curs.courseName)){
            curs.courseName = materii[i];
            gasit= true;
            break;
          }
        }
        if(!gasit){
          curs.deAfisat = false;
        }
    }
  }

}
