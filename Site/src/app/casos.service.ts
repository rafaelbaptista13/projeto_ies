import { Injectable } from '@angular/core';
import { Caso} from './caso';
import { Observable } from 'rxjs/internal/Observable';
import { HttpClient, HttpHeaders} from '@angular/common/http';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json', 'access-control-allow-origin': 'http://localhost:8080/'})
};

@Injectable({
  providedIn: 'root'
})
export class CasosService {
  private baseURL = 'http://localhost:8080/api/v1/';

  constructor(private http: HttpClient) { }

  getNumeroCasos(tipo: string): Observable<any> {
    const url = this.baseURL + 'casos/count?estado=' + tipo;
    return this.http.get(url);
  }
}
