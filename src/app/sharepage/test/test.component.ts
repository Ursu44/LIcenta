import { Component } from '@angular/core';
import { AppComponent } from 'src/app/app.component';
import { SendTestService } from 'src/app/services/send-test.service';
import { ServiciuService } from 'src/app/services/serviciu.service';

@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.css']
})
export class TestComponent {
  constructor(private appComponent: AppComponent, private testComponent: SendTestService, private servicu:ServiciuService) { }
  public test: any  ;
  public test1: any  ;
  public selectedOptions: { [key: string]: string | string[] } = {};
  public intrebari :any = [];
  
   ngOnInit() {
    //this.appComponent.raspuns1 = false;
    this.getData();
    //this.appComponent.raspuns1 = false;
    //this.preiaDate();
    
  }


  async getData(): Promise<void>  {
    try {
      await new Promise<void>((resolve) => {
          setTimeout(() => {
            console.log("Am intrat in initializare 123");
      
            //this.test = JSON.parse(this.testComponent.getRaspunsTest());
           // console.log("Raspuns " +typeof this.test);
            console.log("Raspuns din local storage "+localStorage.getItem("intrebari"));
            this.test1 =localStorage.getItem("intrebari");
            this.test = JSON.parse(this.test1) ;
            console.log("Raspuns " +typeof this.test);
            for (const cheie in this.test) {
              if (this.test.hasOwnProperty(cheie)) {
                console.log(" "+this.test[cheie].variante)
                this.intrebari.push({
                  intrebare: this.test[cheie].intrebare,
                  variante: this.test[cheie].variante.map((varianta: string) => varianta.trim()),
                  tip :this.test[cheie].tip,
                  punctaj: this.test[cheie].punctaj
                });
              }
            }
            this.appComponent.apasat = false;
            
            //console.log("Raspuns 123" +Object.keys(this.test["Intrebare1"]));

              resolve(); 
          }, 2100);
      });
  } catch (error) {
      console.error('Eroare în preluarea datelor:', error);
  }
  }

  sendRaspunsuri() {

  }
}