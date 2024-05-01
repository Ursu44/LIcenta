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

  ngOnInit() {
    this.appComponent.apasat = false;
    this.getData();
    this.generateChart();
  }

  async getData(): Promise<void>  {
    try {
      await new Promise<void>((resolve) => {
          setTimeout(() => {
            console.log("Am intrat in initializare 123");
            this.intrebari = localStorage.getItem("noteGrafic");
            console.log("Am intrat in initializare 123456 "+this.intrebari);
            //console.log("Raspuns 123" +Object.keys(this.test["Intrebare1"]));

              resolve(); 
          }, 500);
      });
  } catch (error) {
      console.error('Eroare în preluarea datelor:', error);
  }
}
  

generateChart(): void {
  const labels = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'];
  const data = [0.02, 0.05, 0.1, 0.15, 0.2, 0.15, 0.1, 0.08, 0.03, 0.02];

  const ctx = document.getElementById('myChart') as HTMLCanvasElement;

  this.chart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: labels,
      datasets: [{
        label: 'Distribuția normală',
        data: data,
        fill: false,
        borderColor: 'rgb(75, 192, 192)',
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

}
