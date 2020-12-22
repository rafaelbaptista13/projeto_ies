import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { HttpClient, HttpHeaders} from '@angular/common/http';

// Define mensagem em JSON e o URL é de segurança
const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json', 'access-control-allow-origin': 'http://localhost:8080/'})
};

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  // URL inicial da api
  private URL = 'http://localhost:8080/api/v1/loginValidation';

  constructor(private http: HttpClient) { }

  // Função que retorna se foi possivel dar login com os dados indicados
  LoginValidation(numeroutente: any, codigoacesso: any): Observable<any> {

    const loginInfo =
      {
        "numero_utente": numeroutente,
        "codigo_acesso": codigoacesso
      };

    /*
    var logged = this.http.post(this.URL, loginInfo).toPromise().then((data: any) => {
      console.log(data);
    });
    */

    /*
    const logged = this.http.post(this.URL, loginInfo).then((logged: any) => response.json());
    */

    // return logged;

    return this.http.post(this.URL, loginInfo);
  }

}
