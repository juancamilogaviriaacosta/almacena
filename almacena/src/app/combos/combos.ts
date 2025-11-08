import { HttpClient } from '@angular/common/http';
import { SemicolonBreakPipe } from "../pipes/semicolon-break.pipe";
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-combos',
  imports: [SemicolonBreakPipe],
  templateUrl: './combos.html',
  styleUrl: './combos.css'
})
export class Combos {

  table:any;  

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    let tmp = this.http.get(environment.apiUrl+'/api/getCombos', { responseType: 'json' });
    tmp.subscribe(table => {
      this.table = table;
    });
  }

  edit(id: any) {
    this.router.navigate(['/combo-management', id]);
  }

}
