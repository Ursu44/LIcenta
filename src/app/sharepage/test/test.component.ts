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
[x: string]: any;
  constructor(private appComponent: AppComponent, private testComponent: SendTestService, private servicu:ServiciuService) { }
  public test: any  ;
  public test1: any  ;
  public selectedOptions: { [key: string]: number[] } = {};
  public intrebari :any = [];
  display: any;
  
   ngOnInit() {
    //this.appComponent.raspuns1 = false;
    this.getData();
    //this.appComponent.raspuns1 = false;
    //this.preiaDate();
    this.timer(0, 12);
    
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
                  variante: this.test[cheie].variante.map((varianta: string) => {
                    return {
                    text:varianta.trim(),
                    checked: false
                  };
                  }),
                  tip :this.test[cheie].tip,
                  punctaj: this.test[cheie].punctaj
                });
              }
            }
            this.appComponent.apasat = false;
            var date = new Date().getTime();

            //console.log("Raspuns 123" +Object.keys(this.test["Intrebare1"]));

              resolve(); 
          }, 2100);
      });
  } catch (error) {
      console.error('Eroare în preluarea datelor:', error);
  }
  }

  sendRaspunsuri() {
    for (const intrebare of this.intrebari) {
      this.selectedOptions[intrebare.intrebare] = [];
      console.log("Intrebare 123 "+intrebare.intrebare);
      for (let j = 0; j < intrebare.variante.length; j++){
        const varianta1 = intrebare.variante[j];
        if(varianta1.checked){
        console.log("Varianta 1234 "+varianta1.text+" ");
        }
      }
        for (let i = 0; i < intrebare.variante.length; i++){
          const varianta = intrebare.variante[i];
          if(varianta.checked){
          //console.log("Varianta 123 "+i);
          //console.log("Varianta "+varianta.text+" "+varianta.checked+" ");
            this.selectedOptions[intrebare.intrebare].push(i);
            console.log("Aici123 "+this.selectedOptions[intrebare.intrebare]);
          }

      }
  }
  for (const intrebare of this.intrebari) {
    if (typeof this.selectedOptions !== 'undefined' && this.selectedOptions !== null) {
      console.log(this.selectedOptions[intrebare.intrebare]);
  } else {
      console.log("selectedOptions nu este definit sau este null.");
  }
  }
}

  timer(hour:number, minute: number) {

    let seconds = (hour % 24) * 3600 + (minute % 60) * 60;

    let oreRamase = 0;
    let minuteRamase = 0;
    let secundeRamas = 0; 
    
    let ora = "0";
    let minut = "0";
    let secunde = "0";

    const timer = setInterval(() => {
      seconds --;
     
      let oreRamase = Math.floor(seconds / 3600) ;
      let minuteRamase = Math.floor((seconds % 3600) / 60);
      let secundeRamas = seconds%60 ;

      if(oreRamase < 10){
        ora = "0"+oreRamase;
      } else{
        ora = oreRamase.toString();
      }

      if(minuteRamase < 10){
        minut = "0"+minuteRamase;
      } else{
        minut = minuteRamase.toString();
      }

      if(secundeRamas < 10){
        secunde = "0"+secundeRamas;
      } else{
        secunde = secundeRamas.toString();
      }

      this.display = ora+":"+minut+":"+secunde;

      if (seconds == 0) {
        window.close();
        console.log("finished");
        clearInterval(timer);
      }
    }, 1000);
  }

  deselectPreviousOption(intrebare: any, selectedIndex: number) {
    for (let j = 0; j < intrebare.variante.length; j++) {
      if (j !== selectedIndex) {
        intrebare.variante[j].checked = false;
      }
    }
  }

  selectCorect(intrebare: any, selectedIndex: number) {

    intrebare.variante[selectedIndex].checked = true;
  }

  apeleaza(intrebare: any, selectedIndex: number){
    this.deselectPreviousOption(intrebare, selectedIndex);
    this.selectCorect(intrebare, selectedIndex);
  }
}