import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { SemicolonBreakPipe } from '../pipes/semicolon-break.pipe';
import { environment } from '../../environments/environment';
import { DatePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  imports: [SemicolonBreakPipe, DecimalPipe, DatePipe, FormsModule],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {

  filterDate: string = new Date().toISOString().split('T')[0];
  table:any;  

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    let tmp = this.http.get(environment.apiUrl+'/api/getInventory?filterDate=' + this.filterDate, { responseType: 'json' });
    tmp.subscribe(table => {
      this.table = table;
    });
  }
}
