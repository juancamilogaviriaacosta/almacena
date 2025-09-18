import { DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-log',
  imports: [DatePipe],
  templateUrl: './log.html',
  styleUrl: './log.css'
})
export class Log implements OnInit {

  table: any;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    let tmp = this.http.get('http://localhost:8080/api/getRegister', { responseType: 'json' });
    tmp.subscribe(table => {
      this.table = table;
    });
  }

}
