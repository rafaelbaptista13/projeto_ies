import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from 'rxjs/internal/Observable';
import {Paciente} from './paciente';

//Define mensagem em JSON e o URL é de segurança
let httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': 'http://localhost:8080/',
  })
};


@Injectable({
  providedIn: 'root'
})
export class PacienteService {

  //URL inicial da api
  private baseURL = 'http://localhost:8080/api/v1/';
  constructor(private http: HttpClient) { }

  getPacientById(paciente_id: string): Observable<Paciente> {
    let url = this.baseURL + 'private/pacientes/' + paciente_id;
    return this.http.get<Paciente>(url, httpOptions);
  }

  createPacient(mensagem: any): Observable<any> {
    let url = this.baseURL + 'private/pacientes';
    return this.http.post(url, mensagem, httpOptions);
  }

  updatePacient(id: number, data: any): Observable<any>{
    const url = this.baseURL + 'private/pacientes/' + id;
    return this.http.put(url, data, httpOptions);
  }

  deletePacient(id: number): Observable<any>{
    const url = this.baseURL + 'private/pacientes/' + id;
    return this.http.delete(url, httpOptions);
  }

}
