import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SemicolonBreakPipe } from '../pipes/semicolon-break-pipe';
import { AsyncPipe } from '@angular/common';

@Component({
  selector: 'app-combos',
  imports: [SemicolonBreakPipe, AsyncPipe],
  templateUrl: './combos.html',
  styleUrl: './combos.css',
})
export class Combos implements OnInit {

  table: any = [];

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.table = this.http.get<any[]>('/api/getCombos');
  }

  edit(id: any) {
    this.router.navigate(['/combo-management', id]);
  }
}