import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import {map} from 'rxjs/operators';

// Define mensagem em JSON e o URL é de segurança
const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json', 'access-control-allow-origin': 'http://localhost:8080/'})
};

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  // URL inicial da api
  private URL = 'http://localhost:8080/api/v1/';
  private httpOptions: { headers: HttpHeaders };
  constructor(private http: HttpClient) { }

  // Função que retorna se foi possivel dar login com os dados indicados
  LoginValidation(loginInfo: any): Observable<any> {
    const url = this.URL + 'public/login';
    return this.http.post(url, loginInfo, httpOptions).pipe(
      map(user => {
        if (user) {
          localStorage.setItem('codigo_acesso', user['codigo_acesso']);
          localStorage.setItem('token', user['token']);
        }
        return user;
      })
    );
  }

  Logout(): void {
    localStorage.removeItem('codigo_acesso');
    localStorage.removeItem('token');
  }
}
