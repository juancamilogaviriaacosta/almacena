import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild} from '@angular/core';

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

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    let tmp = this.http.get('http://localhost:8080/api/getWarehouse', { responseType: 'json' });
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

      this.http.post('http://localhost:8080/api/uploadFile?fileId=' + this.fileId + '&warehouseId=' + this.warehouseId, formData)
        .subscribe({
          next: (response) => {
            this.uploading = false;
            (event.target as HTMLInputElement).value = '';
            window.location.href = '/home';
          },
          error: (error) => {
            this.uploading = false;
            (event.target as HTMLInputElement).value = '';
          }
        });
    }
  }

  manual(warehouseId: number) {
    window.location.href = '/manual-movement/' + warehouseId;
  }

}
