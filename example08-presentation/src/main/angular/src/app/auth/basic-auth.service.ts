import { Injectable } from '@angular/core';
import { Observable, of, throwError } from 'rxjs';
import { HttpHeaders } from '@angular/common/http';
import { AuthService } from './auth.service';
import { map } from 'rxjs/operators';
import { environment as env } from '../../environments/environment';

@Injectable()
export class BasicAuthService extends AuthService {

  private token: string;
  private newname: string;
 

  login(username: string, password: string): Observable<boolean> {
    var token = btoa(unescape(encodeURIComponent(username + ':' + password)));

    return this.http.head(`${env.apiUrl}/auth/basic/profile`, {headers: this.getAuthHeadersForToken(token), responseType: 'text'})
        .pipe(map(body => {
          this.token = token;
          return true;
        }));
  }

  register(name: string, password: string): Observable<boolean> {
	   return this.http.post<any[]>(`${env.apiUrl}/news/newAuthor`, { name, password }, {headers:new HttpHeaders({ 'Content-Type': 'application/json'
  })})
	   .pipe(map(body => {
			this.newname = name;
		   return true;
	   }
    ));
  }

  logout(): Observable<boolean> {
    this.token = null;
    return of(true);
  }

  getAuthHeaders(): HttpHeaders {
    return this.getAuthHeadersForToken(this.token);
  }

  getAuthHeadersForToken(token: string): HttpHeaders {
    return token == null ? new HttpHeaders() : new HttpHeaders({
      "Authorization": `Basic ${token}`
    });
  }

  getBaseUrl(): string {
    return `${env.apiUrl}/auth/basic`;
  }

  get isLoggedIn(): boolean {
    return this.token != null;
  }
  getUsername(): string {
	  return atob(this.token).split(':')[0];
  } 
}
