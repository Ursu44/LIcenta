import { Component, OnInit } from '@angular/core';
import { AppComponent } from 'src/app/app.component';
import Chart from 'chart.js/auto';



@Component({
  selector: 'app-grafic',
  templateUrl: './grafic.component.html',
  styleUrls: ['./grafic.component.css']
})
export class GraficComponent implements OnInit{
  constructor(private appComponent: AppComponent) { }

  intrebari:any= {};
  chart: any;
  note: any = [];
  numarat: any = new Array(11).fill(0);

  ngOnInit() {
    this.appComponent.apasat = false;
    this.getData();
  }

  async getData(): Promise<void>  {
    try {
      await new Promise<void>((resolve) => {
          setTimeout(() => {
            console.log("Am intrat in initializare 123");
            this.intrebari = localStorage.getItem("noteGrafic");
            console.log("Am intrat in initializare 123456 "+this.intrebari);
            let intrebariJSON = JSON.parse(this.intrebari);
            let intrebariJSON1 = Object.keys(intrebariJSON);
            for(let i in intrebariJSON){
              this.note.push(intrebariJSON[i].nota);
            }
            console.log("Note "+this.note);
            //console.log("Raspuns 123" +Object.keys(this.test["Intrebare1"]));

        const media = this.calculezaMedia(this.note);
        const deviatiaStandard = this.calculareDeviatieStandard(this.note, media);

        const dateGrafic = this.generareDateDistributie(media, deviatiaStandard, 10);
        const idealGaussianData = this.genereazaGraficIdeal(10, 0.9, media, deviatiaStandard);
        this.numarNote(this.note);
        console.log("Numarate "+this.numarat);
        
        this.generateChart(dateGrafic, idealGaussianData);
        this.generateBarChart(this.numarat);
              resolve(); 
          }, 500);
      });
  } catch (error) {
      console.error('Eroare în preluarea datelor:', error);
  }
}
  

generateChart(gaussianData: number[], idealData: number[]): void {
  const labels = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'];

  const ctx = document.getElementById('myChart') as HTMLCanvasElement;

  this.chart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: labels,
      datasets: [{
        label: 'Distribuția normală a notelor',
        data: gaussianData,
        fill: false,
        borderColor: 'rgb(75, 192, 192)',
        tension: 0.1
      },{
        label: 'Distributia normală a notelor ideală',
        data: idealData,
        fill: false,
        borderColor: 'rgb(255, 99, 132)',
        tension: 0.1
      }]
    },
    options: {
      scales: {
        y: {
          beginAtZero: true,
          title: {
            display: true,
            text: 'Proportia'
          }
        },
        x: {
          title: {
            display: true,
            text: 'Note'
          }
        }
      }
    }
  });
}

generateBarChart( data: number[] ): void {
  const ctx = document.getElementById('barChart') as HTMLCanvasElement;
  data = data.slice(1);
  new Chart(ctx, {
    type: 'bar',
    data: {
      labels:['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'],
      datasets: [{
        label: 'Număr de elevi',
        data: data,
        backgroundColor: 'rgba(75, 192, 192, 0.2)',
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 1
      }]
    },
    options: {
      scales: {
        y: {
          beginAtZero: true,
          title: {
            display: true,
            text: 'Număr de elevi'
          }
        },
        x: {
          title: {
            display: true,
            text: 'Note'
          }
        }
      }
    }
  });
}


calculezaMedia(note: number[]): number {
  let suma =0 ;
  for(let i=0;i<note.length;i++){
      suma += note[i];
  }
  return suma / note.length;
}

calculareDeviatieStandard(note: number[], medie: number): number {
  const variance = note.reduce((sum, value) => sum + Math.pow(value - medie, 2), 0) / note.length;
  let deviatie = 0;
  for(let i=0;i<note.length;i++){
    deviatie +=  Math.pow(note[i] - medie, 2);
  }
  let deviatieStd = deviatie / note.length ;
  return Math.sqrt(deviatieStd);
}

generareDateDistributie(medie: number, deviatieStandard: number, lungime: number): number[] {
  const dateGrafic = [];
  for (let i = 0; i <= lungime; i++) {
    const valoare = (1 / (deviatieStandard * Math.sqrt(2 * Math.PI))) * Math.exp(-Math.pow(i - medie, 2) / (2 * Math.pow(deviatieStandard, 2)));
    dateGrafic.push(valoare);
  }
  return dateGrafic;
}

genereazaGraficIdeal(lungime: number, valoareReferinta: number, medie: number, deviatie: number): number[] {
  const dateIdeale = [];
  for (let x = 1; x <= lungime; x++) {
    const exponent = -0.5 * Math.pow((x - medie) / deviatie, 2);
    const value = valoareReferinta * Math.exp(exponent);
    dateIdeale.push(value);
  }
  return dateIdeale;
}


numarNote(note: number[]) {
  for(let i=0;i< note.length; i++){
    this.numarat[note[i]]++;
  }
}

}
