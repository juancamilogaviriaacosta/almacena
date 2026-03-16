import { Component } from '@angular/core';
import { SemicolonBreakPipe } from '../pipes/semicolon-break-pipe';
import { FormsModule } from '@angular/forms';
import { DatePipe, DecimalPipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-manual-movement',
  imports: [SemicolonBreakPipe, FormsModule, DecimalPipe, DatePipe],
  templateUrl: './manual-movement.html',
  styleUrl: './manual-movement.css',
})
export class ManualMovement {
  filterDate : string = '';
  id:string | null = null;
  table: any;
  uploading: boolean = false;

  constructor(private http: HttpClient,
    private ar: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.id = this.ar.snapshot.paramMap.get('id');
    this.table = this.ar.snapshot.data['preload'].table;
  }

  save() {
    if(this.table != null && this.table.length && this.id != null) {
      this.uploading = true;
      this.http.post('/api/manualMovement/'+this.id, this.table, { responseType: 'text' })
        .subscribe({
          next: (response) => {
            setTimeout(() => {
              this.uploading = false;
              this.router.navigate(['/home']);
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

