import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ChartConfiguration } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';

@Component({
  selector: 'app-analytics',
  imports: [FormsModule, BaseChartDirective],
  templateUrl: './analytics.html',
  styleUrl: './analytics.css',
})
export class Analytics implements OnInit {
  criteria: string = 'power bank';
  start: string = '';
  end: string = '';
  lineChartData: ChartConfiguration<'line'>['data'] = {
    labels: [],
    datasets: []
  };
  lineChartOptions = { scales: { y: { reverse: true } } };

  constructor(private http: HttpClient, private cd: ChangeDetectorRef) {
  }

  ngOnInit() {
    const now = new Date();
    const firstDay = new Date(now.getFullYear(), now.getMonth() - 1, 1);
    const lastDay = new Date(now.getFullYear(), now.getMonth() + 1, 0);
    this.start = firstDay.toLocaleDateString('en-CA');
    this.end = lastDay.toLocaleDateString('en-CA');
    this.loadData();
  }

  loadData() {
    const params = {
      criteria: this.criteria,
      start: this.start,
      end: this.end
    };
    
    this.http.get<any[]>('/api/getProductRanks', { params }).subscribe(data => {
      console.log(data);
      const apiData:any[] = [];
      for(let tmp of data) {
        apiData.push({ asin: tmp[2], date: tmp[1].split('T')[0], rank: tmp[3] });
      }
      const grouped = this.groupByProduct(apiData);
      const dates = [...new Set(apiData.map(d => d.date))];

      this.lineChartData = {
        labels: dates,
        datasets: Object.keys(grouped).map(product => ({
          label: product,
          data: dates.map(date => {
            const found = grouped[product].find((d: any) => d.date === date);
            return found ? found.rank : null;
          }),
          fill: false,
          tension: 0.3
        }))
      };      
      this.cd.detectChanges();
    });   
  }

  groupByProduct(data: any[]) {
    return data.reduce((acc, item) => {
      if (!acc[item.asin]) {
        acc[item.asin] = [];
      }
      acc[item.asin].push(item);
      return acc;
    }, {});
  }
}

