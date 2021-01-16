import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from 'rxjs/internal/Observable';

//Define mensagem em JSON e o URL é de segurança
let httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
    'access-control-allow-origin': 'http://localhost:8080/',
  })
};

@Injectable({
  providedIn: 'root'
})
export class MedicService {

  //URL inicial da api
  private baseURL = 'http://localhost:8080/api/v1/';
  constructor(private http: HttpClient) { }


  getMedicoById(id: number): Observable<any> {
    let url = this.baseURL + 'private/medicos/' + id;
    return this.http.get(url, httpOptions);
  }

  getPacientsFilter(medico: string, num: string, nome: string, estado: string): Observable<any> {
    let url = this.baseURL + 'private/pacientesbymedic?medico=' + medico;

    if (num !== '' && num !== null) {
      url += '&num_paciente=' + num;
    }
    if (nome !== '' && nome !== null) {
      url += '&nome=' + nome;
    }
    if (estado !== '' && nome !== null) {
      url += '&estado=' + estado;
    }

    return this.http.get(url, httpOptions);
  }

}
