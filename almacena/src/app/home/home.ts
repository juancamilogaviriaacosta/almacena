import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {

  table:any;  

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    let tmp = this.http.get('http://localhost:8080/api/getInventory', { responseType: 'json' });
    tmp.subscribe(table => {
      this.table = table;
    });
  }
}
