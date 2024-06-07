import { AfterViewInit, Component, HostListener, OnInit } from '@angular/core';
import { ServiciuService } from 'src/app/services/serviciu.service';
import { Chart, ChartConfiguration } from 'chart.js';
import { range } from 'rxjs';

@Component({
  selector: 'app-statistici',
  templateUrl: './statistici.component.html',
  styleUrls: ['./statistici.component.css']
})
export class StatisticiComponent implements OnInit {
  raspuns: any;
  raspuns1: any;
  vectorElevi: any;
  myChart: any;
  raspunsJson: any;
  indici :any = [];
  materii :any = [];
  vectorMaterii :any = [];
  constructor(private serviciu: ServiciuService) { }

  chartWidth = 40; 
  chartHeight = 40;
  data1 = [25, 100, 75 ,50];
  i = 0;
  grafice: (Chart | null)[] = []; 
  dateElevi: { [indice: number]: number [] } = {};

  ngOnInit(): void {
    this.raspuns1 = localStorage.getItem("statistica");
    this.preiaDate();
  }

  async preiaDate() {
    try {
      await new Promise<void>((resolve) => {
        setTimeout(async () => {
          this.raspuns = this.serviciu.getStatistici();
          this.raspuns1 = localStorage.getItem("statistica");
          console.log("Raspunsul din noua pagina " + this.raspuns1);
          this.raspunsJson = JSON.parse(this.raspuns1);
          if(this.esteProfesor()){
            this.vectorElevi = Object.keys(this.raspunsJson );
            console.log("Raspusnul JSON "+ this.vectorElevi);
            let lungimeJSON = this.vectorElevi.length;
            for(let i =0 ;i <lungimeJSON ;i++){
              this.indici.push(i);
            }
          console.log(this.indici);
        }
        else if(!this.esteProfesor()){
          this.vectorMaterii = Object.keys(this.raspunsJson );
           console.log("Raspusnul JSON "+ this.vectorMaterii);
           let lungimeJSON = this.vectorMaterii.length;
          for(let i =0 ;i <lungimeJSON ;i++){

            this.materii.push(this.vectorMaterii[i]);
          }
        }
          resolve();
        },7500);
      });
    } catch (error) {
      console.error('Eroare în preluarea datelor:', error);
    }
  }

  generateChart(index: number, materie: string): void {

    let valori:any = [];
    let eticheta :string = "Progres capitole";
    if(this.esteProfesor()){
      valori.push(this.raspunsJson["elev"+(index+1)].progres1);
      valori.push(this.raspunsJson["elev"+(index+1)].progres2);
      valori.push(this.raspunsJson["elev"+(index+1)].progres3);
      valori.push(this.raspunsJson["elev"+(index+1)].progres4);
    }
    else if(!this.esteProfesor()){
      valori.push(this.raspunsJson[materie].progres1);
      valori.push(this.raspunsJson[materie].progres2);
      valori.push(this.raspunsJson[materie].progres3);
      valori.push(this.raspunsJson[materie].progres4);

    }
    let etichete = ['Capitolul 1', 'Capitolul 2', 'Capitolul 3', 'Capitolul 4']; 

    let eticheteMapate: { [eticheta: string]: number } = {};
    for (let i = 0; i < etichete.length; i++) {
      eticheteMapate[etichete[i]] = valori[i];
    }
  
    let data = {
      labels: etichete, 
      datasets: [{
        label: 'Progres capitole',
        data: etichete.map(eticheta => eticheteMapate[eticheta]), 
        backgroundColor: [
          'red',
          'blue',
          'yellow',
          'green'
        ],
        borderColor: [
          'red',
          'blue',
          'yellow',
          'green'
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
            min:0,
            max:100,
          }
        }
      }
    };
    console.log(index);
    let index1 = index+1;
    const canvasId = 'Chart'+index;
    const ctx = document.getElementById(canvasId) as HTMLCanvasElement;
    console.log("Id "+ctx);
    this.myChart = new Chart(ctx, config);
    this.grafice[index] = this.myChart;

}

ascundeGrafic(index: number) {
  const chart = this.grafice[index];
  if (chart) {
    chart.destroy();
    this.grafice[index] = null;
  }
}

decodeJwtClaims(token: string) {
  const base64Url = token.split('.')[1];
  const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
  const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
  }).join(''));

  return JSON.parse(jsonPayload);
}

esteProfesor() :boolean{
  var notaNoua = document.getElementById('element') as HTMLInputElement | null;
  const accessToken = localStorage.getItem('access_token');
  let ok =false;
  if(accessToken){
    const claimsDecodat = this.decodeJwtClaims(accessToken)
    let rol = claimsDecodat.roles;
    let rol1 = JSON.stringify(rol);
      const numeRol = rol1.trim();
      ok = (numeRol === '["ROLE_PROFESOR"]');
}
  return ok;
}
  @HostListener('change', ['$event.target.value'])
  onAnSelectatChange() {
    if(this.esteProfesor()){
      
    }
  }
  

}
