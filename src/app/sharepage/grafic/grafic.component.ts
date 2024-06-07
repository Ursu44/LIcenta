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
        console.log("Media "+media);
        const dateGrafic = this.generareDateDistributie(media, deviatiaStandard, this.note.length);
       this.numarNote(this.note);

        const data = this.generateGaussianData(media, deviatiaStandard, this.note.length+2);
        this.generateChart(dateGrafic, data);
        this.generateBarChart(this.numarat);
              resolve(); 
          }, 800);
      });
  } catch (error) {
      console.error('Eroare în preluarea datelor:', error);
  }
}
  

generateChart(gaussianData:  Array<{x: number, y: number}>, idealData:  Array<{x: number, y: number}>): void {
  const labels = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'];

  const ctx = document.getElementById('myChart') as HTMLCanvasElement;

  this.chart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: labels,
      datasets: [{
        label: 'Distribuția normală a notelor ideala',
        data: idealData,
        fill: false,
        borderColor: 'rgb(75, 192, 192)',
        tension: 0.1
      },{
        label: 'Distribuția normală a notelor reala',
        data: gaussianData,
        fill: false,
        borderColor: 'rgb(255, 99, 132)',
        tension: 0.1
      }
    ]
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

generareDateDistributie(medie: number, deviatieStandard: number, lungime: number): { x: number, y: number }[] {
  const dateGrafic: { x: number, y: number }[] = [];
  for (let i = 0; i <= lungime; i++) {
    const valoare = (1 / (deviatieStandard * Math.sqrt(2 * Math.PI))) * Math.exp(-Math.pow(i - medie, 2) / (2 * Math.pow(deviatieStandard, 2)));
    dateGrafic.push({ x: i, y: valoare });
  }
  const sum = dateGrafic.reduce((acc, val) => acc + val.y, 0);
  return dateGrafic.map(val => ({ x: val.x, y: val.y / sum }));
}


gaussian(x: number, medie: number, sigma: number) {
  var constantaGaussian = 1 / (sigma * Math.sqrt(2 * Math.PI));
  x = (x - medie) / sigma;
  return constantaGaussian * Math.exp(-.5 * x * x);
}

genereazaGraficIdeal(lungime: number, valoareReferinta: number, medie: number, deviatie: number): Array<{x: number, y: number}> {
  const dateIdeale = [];
  const constantaGaussian = 1 / (deviatie * Math.sqrt(2 * Math.PI));

  for (let i = 0; i <= lungime; i++) {
    const x = i;
    const scaledX = (x - medie) / deviatie;
    const valoareGaussian = constantaGaussian * Math.exp(-0.5 * scaledX * scaledX);
    dateIdeale.push({x: x, y: valoareGaussian});
  }
  return dateIdeale;
}



numarNote(note: number[]) {
  for(let i=0;i< note.length; i++){
    this.numarat[note[i]]++;
  }
}

 gaussianPDF(x: number, mean: number, stdDev: number): number {
  return (1 / (stdDev * Math.sqrt(2 * Math.PI))) * Math.exp(-Math.pow(x - mean, 2) / (2 * Math.pow(stdDev, 2)));
}

 generateGaussianData(mean: number, stdDev: number, numPoints: number): { x: number, y: number }[] {
  console.log("Media grafic "+mean+" deviatia "+stdDev)
  const data = [];
  const minX = mean - 2.5 * stdDev;
    const maxX = mean + 2.5 * stdDev;
    const step = (maxX - minX) / (numPoints - 1);

    for (let x = minX; x <= maxX; x += step) {
        const y = this.gaussianPDF(x, mean, stdDev);
        data.push({ x, y });
        console.log("x:", x, "y:", y);
    }

  return data;
}

}