import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SemicolonBreakPipe } from '../pipes/semicolon-break.pipe';
import { environment } from '../../environments/environment';
import { DatePipe, DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-manual-movement',
  imports: [SemicolonBreakPipe, FormsModule, DecimalPipe, DatePipe],
  templateUrl: './manual-movement.html',
  styleUrl: './manual-movement.css'
})
export class ManualMovement {
  filterDate : string = '';
  id:string | null = null;
  table: any;
  uploading: boolean = false;

  constructor(private http: HttpClient, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
    const today = new Date();
    const offsetDate = new Date(today.getTime() - today.getTimezoneOffset() * 60000);
    this.filterDate = offsetDate.toISOString().split('T')[0];
    const params = {
      filterDate: this.filterDate,
      warehouseId: '-1',
      productId: '-1'
    };

    let tmp = this.http.get(environment.apiUrl + '/api/getInventory', { params, responseType: 'json'});
    tmp.subscribe(table => {
      this.table = table;
    });
  }

  save() {
    if(this.table != null && this.table.length && this.id != null) {
      this.uploading = true;
      this.http.post(environment.apiUrl+'/api/manualMovement/'+this.id, this.table, { responseType: 'text' })
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
