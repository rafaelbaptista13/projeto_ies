import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from 'rxjs/internal/Observable';
import {Paciente} from './paciente';

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
export class PacienteService {

  //URL inicial da api
  private baseURL = 'http://localhost:8080/api/v1/';
  constructor(private http: HttpClient) { }

  getPacientById(paciente_id: string): Observable<any> {
    let url = this.baseURL + 'private/pacientes/' + paciente_id;

    return this.http.get(url, httpOptions);
  }

}
