import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";

//Define mensagem em JSON e o URL é de segurança
const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json', 'access-control-allow-origin': 'http://localhost:8080/'})
};

@Injectable({
  providedIn: 'root'
})
export class MedicService {

  //URL inicial da api
  private baseURL = 'http://localhost:8080/api/v1/';
  constructor(private http: HttpClient) { }

  getPacientsFilter(num: string, nome: string, estado: string): any {
    let url = this.baseURL + 'public/hospitais?';
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
