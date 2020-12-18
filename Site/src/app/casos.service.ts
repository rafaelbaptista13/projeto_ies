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

  getNumeroCasos(estado: string, idadeMin: any, idadeMax: any, genero: string, regiao: string, nacionalidade: string, alturaMin: any,
                 alturaMax: any, pesoMin: any, pesoMax: any): Observable<any> {
    let url = this.baseURL + 'casos/count?estado=' + estado;

    if (idadeMin !== '') {
      url += '&idademin=' + idadeMin;
    }

    if (idadeMax !== '') {
      url += '&idademax=' + idadeMax;
    }

    if (genero !== '') {
      url += '&genero=' + genero;
    }

    if (regiao !== '') {
      url += '&regiao' + regiao;
    }
    if (nacionalidade !== '') {
      url += '&nacionalidade' + nacionalidade;
    }
    if (alturaMax !== '') {
      url += '&alturaMax=' + alturaMax;
    }
    if (alturaMin !== '') {
      url += '&alturamin=' + alturaMin;
    }
    if (pesoMax !== '') {
      url += '&pesomax=' + pesoMax;
    }
    if (pesoMin !== '') {
      url += '&pesomin=' + pesoMin;
    }

    return this.http.get(url);
  }

  /*
  getCasos(estado: string, idadeMin: any, idadeMax: any, genero: string, regiao: string, nacionalidade: string, alturaMin: any,
           alturaMax: any, pesoMin: any, pesoMax: any): Observable<any> {
    let url = this.baseURL + 'casos?estado=' + estado;

    if (idadeMin !== '') {
      url += '&idademin=' + idadeMin;
    }

    if (idadeMax !== '') {
      url += '&idademax=' + idadeMax;
    }

    if (genero !== '') {
      url += '&genero=' + genero;
    }

    if (regiao !== '') {
      url += '&regiao' + regiao;
    }
    if (nacionalidade !== '') {
      url += '&nacionalidade' + nacionalidade;
    }
    if (alturaMax !== '') {
      url += '&alturaMax=' + alturaMax;
    }
    if (alturaMin !== '') {
      url += '&alturamin=' + alturaMin;
    }
    if (pesoMax !== '') {
      url += '&pesomax=' + pesoMax;
    }
    if (pesoMin !== '') {
      url += '&pesomin=' + pesoMin;
    }

    return this.http.get(url);
  }
   */
}
