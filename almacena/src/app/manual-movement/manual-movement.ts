import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { SemicolonBreakPipe } from '../pipes/semicolon-break.pipe';

@Component({
  selector: 'app-manual-movement',
  imports: [SemicolonBreakPipe],
  templateUrl: './manual-movement.html',
  styleUrl: './manual-movement.css'
})
export class ManualMovement {

  id:string | null = null;
  table: any;

  constructor(private http: HttpClient, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
    let tmp = this.http.get('http://localhost:8080/api/getProducts', { responseType: 'json' });
    tmp.subscribe(table => {
      this.table = table;
    });
  }

  save() {
    alert("Guardado");
  }
}
