import { Injectable } from '@angular/core';
import { Caso} from './caso';
import { Observable } from 'rxjs/internal/Observable';
import { HttpClient, HttpHeaders} from '@angular/common/http';

//Define mensagem em JSON e o URL é de segurança
const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json',
    'access-control-allow-origin': 'http://192.168.160.215:8080/'})
};

@Injectable({
  providedIn: 'root'
})
export class CasosService {
  //URL inicial da api
  private baseURL = 'http://192.168.160.215:8080/api/v1/';

  constructor(private http: HttpClient) { }

  getNumerosPandemia(idadeMin: any, idadeMax: any, genero: string, regiao: string, nacionalidade: string, alturaMin: any,
                     alturaMax: any, pesoMin: any, pesoMax: any): Observable<any> {
    let url = this.baseURL + 'public/casos/numerospandemia?';

    //Cadeia de ifs que verifica os filtros especificados e constroi o URL de acordo com as seleções
    if (idadeMin !== '' && idadeMin !== null) {
      url += '&idademin=' + idadeMin;
    }

    if (idadeMax !== '' && idadeMax !== null) {
      url += '&idademax=' + idadeMax;
    }

    if (genero !== '' && genero !== null) {
      url += '&genero=' + genero;
    }

    if (regiao !== '' && regiao !== null) {
      url += '&regiao=' + regiao;
    }
    if (nacionalidade !== '' && nacionalidade !== null) {
      url += '&nacionalidade=' + nacionalidade;
    }
    if (alturaMax !== '' && alturaMax !== null) {
      url += '&alturamax=' + alturaMax;
    }
    if (alturaMin !== '' && alturaMin !== null) {
      url += '&alturamin=' + alturaMin;
    }
    if (pesoMax !== '' && pesoMax !== null) {
      url += '&pesomax=' + pesoMax;
    }
    if (pesoMin !== '' && pesoMin !== null) {
      url += '&pesomin=' + pesoMin;
    }

    return this.http.get(url, httpOptions);
  }

  getCasoByPaciente(id: number): Observable<any> {
    let url = this.baseURL + 'private/casobypaciente/' + id;

    return this.http.get(url, httpOptions);
  }

  //Função que retorna numero de casos de acordo com os filtros (ou sem filtros caso nao sejam especificados)
  getNumeroCasos(estado: string, idadeMin: any, idadeMax: any, genero: string, regiao: string, nacionalidade: string, alturaMin: any,
                 alturaMax: any, pesoMin: any, pesoMax: any): Observable<any> {
    let url = this.baseURL + 'public/casos/count?estado=' + estado;

    //Cadeia de ifs que verifica os filtros especificados e constroi o URL de acordo com as seleções
    if (idadeMin !== '' && idadeMin !== null) {
      url += '&idademin=' + idadeMin;
    }

    if (idadeMax !== '' && idadeMax !== null) {
      url += '&idademax=' + idadeMax;
    }

    if (genero !== '' && genero !== null) {
      url += '&genero=' + genero;
    }

    if (regiao !== '' && regiao !== null) {
      url += '&regiao=' + regiao;
    }
    if (nacionalidade !== '' && nacionalidade !== null) {
      url += '&nacionalidade=' + nacionalidade;
    }
    if (alturaMax !== '' && alturaMax !== null) {
      url += '&alturamax=' + alturaMax;
    }
    if (alturaMin !== '' && alturaMin !== null) {
      url += '&alturamin=' + alturaMin;
    }
    if (pesoMax !== '' && pesoMax !== null) {
      url += '&pesomax=' + pesoMax;
    }
    if (pesoMin !== '' && pesoMin !== null) {
      url += '&pesomin=' + pesoMin;
    }

    return this.http.get(url, httpOptions);
  }

  getProbabilidadeGraficoIdades(idadeMin: any, idadeMax: any, genero: string, regiao: string, nacionalidade: string, alturaMin: any,
                                alturaMax: any, pesoMin: any, pesoMax: any): Observable<number[]> {
    let url = this.baseURL + 'public/casos/grafico/idade?';

    //Cadeia de ifs que verifica os filtros especificados e constroi o URL de acordo com as seleções
    if (idadeMin !== '' && idadeMin !== null) {
      url += '&idademin=' + idadeMin;
    }

    if (idadeMax !== '' && idadeMax !== null) {
      url += '&idademax=' + idadeMax;
    }

    if (genero !== '' && genero !== null) {
      url += '&genero=' + genero;
    }

    if (regiao !== '' && regiao !== null) {
      url += '&regiao=' + regiao;
    }
    if (nacionalidade !== '' && nacionalidade !== null) {
      url += '&nacionalidade=' + nacionalidade;
    }
    if (alturaMax !== '' && alturaMax !== null) {
      url += '&alturamax=' + alturaMax;
    }
    if (alturaMin !== '' && alturaMin !== null) {
      url += '&alturamin=' + alturaMin;
    }
    if (pesoMax !== '' && pesoMax !== null) {
      url += '&pesomax=' + pesoMax;
    }
    if (pesoMin !== '' && pesoMin !== null) {
      url += '&pesomin=' + pesoMin;
    }

    return this.http.get<number[]>(url);
  }

  getProbabilidadeGraficoRegiao(idadeMin: any, idadeMax: any, genero: string, regiao: string, nacionalidade: string, alturaMin: any,
                                alturaMax: any, pesoMin: any, pesoMax: any): Observable<number[]> {
    let url = this.baseURL + 'public/casos/grafico/regiao?';

    //Cadeia de ifs que verifica os filtros especificados e constroi o URL de acordo com as seleções
    if (idadeMin !== '' && idadeMin !== null) {
      url += '&idademin=' + idadeMin;
    }

    if (idadeMax !== '' && idadeMax !== null) {
      url += '&idademax=' + idadeMax;
    }

    if (genero !== '' && genero !== null) {
      url += '&genero=' + genero;
    }

    if (regiao !== '' && regiao !== null) {
      url += '&regiao=' + regiao;
    }
    if (nacionalidade !== '' && nacionalidade !== null) {
      url += '&nacionalidade=' + nacionalidade;
    }
    if (alturaMax !== '' && alturaMax !== null) {
      url += '&alturamax=' + alturaMax;
    }
    if (alturaMin !== '' && alturaMin !== null) {
      url += '&alturamin=' + alturaMin;
    }
    if (pesoMax !== '' && pesoMax !== null) {
      url += '&pesomax=' + pesoMax;
    }
    if (pesoMin !== '' && pesoMin !== null) {
      url += '&pesomin=' + pesoMin;
    }

    return this.http.get<number[]>(url);
  }


  getProbabilidadeGraficoGenero(idadeMin: any, idadeMax: any, genero: string, regiao: string, nacionalidade: string, alturaMin: any,
                                alturaMax: any, pesoMin: any, pesoMax: any): Observable<number[]> {
    let url = this.baseURL + 'public/casos/grafico/genero?';

    //Cadeia de ifs que verifica os filtros especificados e constroi o URL de acordo com as seleções
    if (idadeMin !== '' && idadeMin !== null) {
      url += '&idademin=' + idadeMin;
    }

    if (idadeMax !== '' && idadeMax !== null) {
      url += '&idademax=' + idadeMax;
    }

    if (genero !== '' && genero !== null) {
      url += '&genero=' + genero;
    }

    if (regiao !== '' && regiao !== null) {
      url += '&regiao=' + regiao;
    }
    if (nacionalidade !== '' && nacionalidade !== null) {
      url += '&nacionalidade=' + nacionalidade;
    }
    if (alturaMax !== '' && alturaMax !== null) {
      url += '&alturamax=' + alturaMax;
    }
    if (alturaMin !== '' && alturaMin !== null) {
      url += '&alturamin=' + alturaMin;
    }
    if (pesoMax !== '' && pesoMax !== null) {
      url += '&pesomax=' + pesoMax;
    }
    if (pesoMin !== '' && pesoMin !== null) {
      url += '&pesomin=' + pesoMin;
    }

    return this.http.get<number[]>(url);
  }

  getProbabilidadeGraficoAltura(idadeMin: any, idadeMax: any, genero: string, regiao: string, nacionalidade: string, alturaMin: any,
                                alturaMax: any, pesoMin: any, pesoMax: any): Observable<number[]> {
    let url = this.baseURL + 'public/casos/grafico/altura?';

    //Cadeia de ifs que verifica os filtros especificados e constroi o URL de acordo com as seleções
    if (idadeMin !== '' && idadeMin !== null) {
      url += '&idademin=' + idadeMin;
    }

    if (idadeMax !== '' && idadeMax !== null) {
      url += '&idademax=' + idadeMax;
    }

    if (genero !== '' && genero !== null) {
      url += '&genero=' + genero;
    }

    if (regiao !== '' && regiao !== null) {
      url += '&regiao=' + regiao;
    }
    if (nacionalidade !== '' && nacionalidade !== null) {
      url += '&nacionalidade=' + nacionalidade;
    }
    if (alturaMax !== '' && alturaMax !== null) {
      url += '&alturamax=' + alturaMax;
    }
    if (alturaMin !== '' && alturaMin !== null) {
      url += '&alturamin=' + alturaMin;
    }
    if (pesoMax !== '' && pesoMax !== null) {
      url += '&pesomax=' + pesoMax;
    }
    if (pesoMin !== '' && pesoMin !== null) {
      url += '&pesomin=' + pesoMin;
    }

    return this.http.get<number[]>(url);
  }

  getProbabilidadeGraficoPeso(idadeMin: any, idadeMax: any, genero: string, regiao: string, nacionalidade: string, alturaMin: any,
                                alturaMax: any, pesoMin: any, pesoMax: any): Observable<number[]> {
    let url = this.baseURL + 'public/casos/grafico/peso?';

    //Cadeia de ifs que verifica os filtros especificados e constroi o URL de acordo com as seleções
    if (idadeMin !== '' && idadeMin !== null) {
      url += '&idademin=' + idadeMin;
    }

    if (idadeMax !== '' && idadeMax !== null) {
      url += '&idademax=' + idadeMax;
    }

    if (genero !== '' && genero !== null) {
      url += '&genero=' + genero;
    }

    if (regiao !== '' && regiao !== null) {
      url += '&regiao=' + regiao;
    }
    if (nacionalidade !== '' && nacionalidade !== null) {
      url += '&nacionalidade=' + nacionalidade;
    }
    if (alturaMax !== '' && alturaMax !== null) {
      url += '&alturamax=' + alturaMax;
    }
    if (alturaMin !== '' && alturaMin !== null) {
      url += '&alturamin=' + alturaMin;
    }
    if (pesoMax !== '' && pesoMax !== null) {
      url += '&pesomax=' + pesoMax;
    }
    if (pesoMin !== '' && pesoMin !== null) {
      url += '&pesomin=' + pesoMin;
    }

    return this.http.get<number[]>(url);
  }

  getProbabilidadeGraficoCurvatura(idadeMin: any, idadeMax: any, genero: string, regiao: string, nacionalidade: string, alturaMin: any,
                              alturaMax: any, pesoMin: any, pesoMax: any): Observable<number[]> {
    let url = this.baseURL + 'public/casos/grafico/curva_diaria?';

    //Cadeia de ifs que verifica os filtros especificados e constroi o URL de acordo com as seleções
    if (idadeMin !== '' && idadeMin !== null) {
      url += '&idademin=' + idadeMin;
    }

    if (idadeMax !== '' && idadeMax !== null) {
      url += '&idademax=' + idadeMax;
    }

    if (genero !== '' && genero !== null) {
      url += '&genero=' + genero;
    }

    if (regiao !== '' && regiao !== null) {
      url += '&regiao=' + regiao;
    }
    if (nacionalidade !== '' && nacionalidade !== null) {
      url += '&nacionalidade=' + nacionalidade;
    }
    if (alturaMax !== '' && alturaMax !== null) {
      url += '&alturamax=' + alturaMax;
    }
    if (alturaMin !== '' && alturaMin !== null) {
      url += '&alturamin=' + alturaMin;
    }
    if (pesoMax !== '' && pesoMax !== null) {
      url += '&pesomax=' + pesoMax;
    }
    if (pesoMin !== '' && pesoMin !== null) {
      url += '&pesomin=' + pesoMin;
    }

    return this.http.get<any>(url);
  }

  getProbabilidadeGraficoCurvaturaDaily(idadeMin: any, idadeMax: any, genero: string, regiao: string, nacionalidade: string, alturaMin: any,
                                   alturaMax: any, pesoMin: any, pesoMax: any): Observable<number> {
    let url = this.baseURL + 'public/casos/grafico/curva_diaria/daily?';

    //Cadeia de ifs que verifica os filtros especificados e constroi o URL de acordo com as seleções
    if (idadeMin !== '' && idadeMin !== null) {
      url += '&idademin=' + idadeMin;
    }

    if (idadeMax !== '' && idadeMax !== null) {
      url += '&idademax=' + idadeMax;
    }

    if (genero !== '' && genero !== null) {
      url += '&genero=' + genero;
    }

    if (regiao !== '' && regiao !== null) {
      url += '&regiao=' + regiao;
    }
    if (nacionalidade !== '' && nacionalidade !== null) {
      url += '&nacionalidade=' + nacionalidade;
    }
    if (alturaMax !== '' && alturaMax !== null) {
      url += '&alturamax=' + alturaMax;
    }
    if (alturaMin !== '' && alturaMin !== null) {
      url += '&alturamin=' + alturaMin;
    }
    if (pesoMax !== '' && pesoMax !== null) {
      url += '&pesomax=' + pesoMax;
    }
    if (pesoMin !== '' && pesoMin !== null) {
      url += '&pesomin=' + pesoMin;
    }

    return this.http.get<number>(url);
  }

  getLastCasosDiariosEvent(): Observable<any> {
    let url = this.baseURL + 'public/event_casodiario';

    return this.http.get(url, httpOptions);
  }
}
