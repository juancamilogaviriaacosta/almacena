import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { SemicolonBreakPipe } from '../pipes/semicolon-break.pipe';

@Component({
  selector: 'app-manual-movement',
  imports: [SemicolonBreakPipe, FormsModule],
  templateUrl: './manual-movement.html',
  styleUrl: './manual-movement.css'
})
export class ManualMovement {

  id:string | null = null;
  table: any;
  uploading: boolean = false;

  constructor(private http: HttpClient, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
    let tmp = this.http.get('http://localhost:8080/api/getInventory', { responseType: 'json' });
    tmp.subscribe(table => {
      this.table = table;
    });
  }

  save() {
    if(this.table != null && this.table.length && this.id != null) {
      this.uploading = true;
      this.http.post('http://localhost:8080/api/manualMovement/'+this.id, this.table, { responseType: 'text' })
        .subscribe({
          next: (response) => {
            setTimeout(() => {
              this.uploading = false;
              window.location.href = '/manual-movement/' + this.id;
            }, 500);
          },
          error: (error) => {
            setTimeout(() => {
              this.uploading = false;
            }, 500);
          }
        });
    }
  }
}
