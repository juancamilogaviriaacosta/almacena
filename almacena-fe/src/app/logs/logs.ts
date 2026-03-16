import { AsyncPipe, DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-logs',
  imports: [DatePipe, AsyncPipe],
  templateUrl: './logs.html',
  styleUrl: './logs.css',
})
export class Logs implements OnInit {

  table: any;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.table = this.http.get('/api/getRegister');
  }

}
