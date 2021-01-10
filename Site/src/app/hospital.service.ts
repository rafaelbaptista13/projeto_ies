import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs/internal/Observable';
import {CasosService} from './casos.service';


//Define mensagem em JSON e o URL é de segurança
const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json', 'access-control-allow-origin': 'http://localhost:8080/'})
};

@Injectable({
  providedIn: 'root'
})
export class HospitalService {

  //URL inicial da api
  private baseURL = 'http://localhost:8080/api/v1/';

  constructor(private http: HttpClient, private casosService: CasosService) { }

  getHospitaisFilter(nome: string, regiao: string, taxaocupacao_min: any, taxaocupacao_max: any): any {
    let url = this.baseURL + 'public/hospitais?';
    if (regiao !== '' && regiao !== null) {
      url += '&regiao=' + regiao;
    }
    if (nome !== '' && nome !== null) {
      url += '&nome=' + nome;
    }
    if (taxaocupacao_min === '' || taxaocupacao_min === null) {
      taxaocupacao_min = -1;
    } else {
      taxaocupacao_min = parseFloat(taxaocupacao_min);
    }
    if (taxaocupacao_max === '' || taxaocupacao_max === null) {
      taxaocupacao_max = 101;
    } else {
      taxaocupacao_max = parseFloat(taxaocupacao_max);
    }
    let retorno = {};
    let hospitais;
    this.http.get(url).subscribe(result => {hospitais = result;
      hospitais.forEach((element) => {
        let urlci = this.baseURL + 'public/casos/count?estado=Cuidados Intensivos';
        urlci += '&hospital=' + element.id;
        let urlint = this.baseURL + 'public/casos/count?estado=Internado';
        urlint += '&hospital=' + element.id;
        this.http.get(urlci).subscribe(result1 => {
          this.http.get(urlint).subscribe(result2 => {
            let taxaocupacao: number = (element.numero_camas_ocupadas / element.numero_camas) * 100;
            if (taxaocupacao_min < taxaocupacao && taxaocupacao < taxaocupacao_max) {
              retorno[element.id] = [element.nome, result1, result2, ((element.numero_camas_ocupadas / element.numero_camas) * 100).toFixed(2)];
            }
          })
        })
      } )
    });
    return retorno;
  }
}
