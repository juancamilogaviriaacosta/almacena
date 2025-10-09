import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { SemicolonBreakPipe } from '../pipes/semicolon-break.pipe';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-home',
  imports: [SemicolonBreakPipe],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {

  table:any;  

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    let tmp = this.http.get(environment.apiUrl+'/api/getInventory', { responseType: 'json' });
    tmp.subscribe(table => {
      this.table = table;
    });
  }
}
