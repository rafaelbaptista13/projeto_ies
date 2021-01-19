import { Injectable } from '@angular/core';
import { Caso} from './caso';
import { Observable } from 'rxjs/internal/Observable';
import { HttpClient, HttpHeaders} from '@angular/common/http';

//Define mensagem em JSON e o URL é de segurança
const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json', 'access-control-allow-origin': 'http://192.168.160.215:8080/'})
};
@Injectable({
  providedIn: 'root'
})
export class RelatoriopacienteService {
  //URL inicial da api
  private baseURL = 'http://192.168.160.215:8080/api/v1/';

  constructor(private http: HttpClient) { }

  getRelatoriosByCaso(id: number): Observable<any> {
    let url = this.baseURL + 'private/relatorio_pacientes_bycaso/' + id;

    return this.http.get(url, httpOptions);
  }
}
