import { Component, OnInit } from '@angular/core';
import { ServiciuService } from 'src/app/services/serviciu.service';
import { Chart, ChartConfiguration } from 'chart.js';

@Component({
  selector: 'app-statistici',
  templateUrl: './statistici.component.html',
  styleUrls: ['./statistici.component.css']
})
export class StatisticiComponent implements OnInit {

  raspuns: any;
  myChart: any;
  constructor(private serviciu: ServiciuService) { }
  chartWidth = 40; 
  chartHeight = 40;
  i = 0;
  ngOnInit(): void {
    this.preiaDate();
  }

  async preiaDate() {
    try {
      await new Promise<void>((resolve) => {
        setTimeout(() => {
          this.raspuns = this.serviciu.getStatistici();
          console.log("Raspunsul din noua pagina " + this.raspuns);
          for(let i =0 ;i<5; i++){
            this.generateChart(i);
          }
          resolve();
        }, 8900);
      });
    } catch (error) {
      console.error('Eroare în preluarea datelor:', error);
    }
  }
  generateChart(index: number): void {
  let data = {
    labels: ['Capitolul 1', 'Capitolul 2', 'Capitolul 3', 'Capitolul 4'],
    datasets: [{
      label: 'My First Dataset',
      data: [0, 20, 25,40, 50, 60, 75, 100],
      backgroundColor: [
        'red',
        'blue',
        'yellow',
        'green',
        'purple',
        'orange'
      ],
      borderColor: [
        'red',
        'blue',
        'yellow',
        'green',
        'purple',
        'orange'
      ],
      borderWidth: 1
    }]
  };

  let config: ChartConfiguration<'bar'> = {
    type: 'bar',
    data: data,
    options: {
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  };

  let canvasId = 'myChart' + index;
  let ctx = document.getElementById(canvasId) as HTMLCanvasElement;
  this.myChart = new Chart(ctx, config);

  const graficeDiv = document.querySelector('.grafice') as HTMLElement;
  ctx.width = graficeDiv.clientWidth;
  ctx.height = graficeDiv.clientHeight;
  }
}
