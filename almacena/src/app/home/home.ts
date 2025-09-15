import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {

  @ViewChild('mlInput') mlInput: any;
  inventory:any;  

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    let inventory = this.http.get('http://localhost:8080/api/getInventory', { responseType: 'json' });
    inventory.subscribe(inventory => {
      this.inventory = inventory;
    });
  }

  onMLClick() {
    this.mlInput.nativeElement.click();
  }

  onMLFileSelected(event: Event) {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      const formData = new FormData();
      formData.append('file', file);

      this.http.post('http://localhost:8080/api/uploadFile?id=ml', formData)
        .subscribe({
          next: (response) => {
            console.log('Archivo subido correctamente:', response);
            (event.target as HTMLInputElement).value = '';
          },
          error: (error) => {
            console.error('Error al subir el archivo:', error);
            (event.target as HTMLInputElement).value = '';
          }
        });
    }
  }
}
