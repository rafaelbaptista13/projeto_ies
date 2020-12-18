/*
-----old code---------------------------
import { Injectable } from '@angular/core';
import { Paciente} from './paciente';
import { Observable } from 'rxjs/internal/Observable';
import { HttpClient, HttpHeaders} from '@angular/common/http';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json', 'access-control-allow-origin': 'http://localhost:8080/'})
};

@Injectable({
  providedIn: 'root'
})
export class PacientesService {
  private baseURL = 'http://localhost:8080/api/v1/';

  constructor(private http: HttpClient) { }

  getPaciente(numeroUtente: number): Observable<Paciente> {
    const url = this.baseURL + 'pacientes/' + numeroUtente;
    return this.http.get<Paciente>(url);
  }


  getPacientes(): Observable<Paciente[]> {
    const url = this.baseURL + 'pacientes/';
    return this.http.get<Paciente[]>(url);
  }

  updatePaciente(paciente: Paciente): Observable<any> {
    const url = this.baseURL + 'pacientes/' + paciente.numeroUtente;
    return this.http.put(url, paciente, httpOptions);
  }

  getNumeroCasos(tipo: string): Observable<any> {
    if (tipo === 'casosAtivos') {
      const url = this.baseURL + 'casosativos';
      return this.http.get(url);
    }
  }
}
*/
