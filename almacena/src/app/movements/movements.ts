import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild} from '@angular/core';
import { environment } from '../../environments/environment';
import { Router } from '@angular/router';

@Component({
  selector: 'app-movements',
  imports: [],
  templateUrl: './movements.html',
  styleUrl: './movements.css'
})
export class Movements implements OnInit {

  @ViewChild('mlInput') mlInput: any;
  table: any;
  fileId: string = '';
  warehouseId: number = 0;
  uploading = false;

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    let tmp = this.http.get(environment.apiUrl+'/api/getWarehouse', { responseType: 'json' });
    tmp.subscribe(table => {
      this.table = table;
    });
  }

  onMLClick(fileId: string, warehouseId: number) {
    this.fileId = fileId;
    this.warehouseId = warehouseId;
    this.mlInput.nativeElement.click();
  }

  onMLFileSelected(event: Event) {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      this.uploading = true;
      const formData = new FormData();
      formData.append('file', file);

      this.http.post(environment.apiUrl+'/api/uploadFile?fileId=' + this.fileId + '&warehouseId=' + this.warehouseId, formData)
        .subscribe({
          next: (response) => {
            this.uploading = false;
            (event.target as HTMLInputElement).value = '';
            this.router.navigate(['/home']);
          },
          error: (error) => {
            this.uploading = false;
            (event.target as HTMLInputElement).value = '';
          }
        });
    }
  }

  manual(warehouseId: number) {
    this.router.navigate(['/manual-movement', warehouseId]);
  }

}
