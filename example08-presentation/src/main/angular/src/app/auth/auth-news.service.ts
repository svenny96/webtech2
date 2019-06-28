import { Injectable } from '@angular/core';
import { BaseNewsService } from '../base-news.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { News } from '../news';
import { map } from 'rxjs/operators';
import { BasicAuthService } from './basic-auth.service';
import { AuthService } from './auth.service';
import { environment as env } from '../../environments/environment';

@Injectable()
export class AuthNewsService extends BaseNewsService {

  private _authService: AuthService;

  constructor(http: HttpClient) {
    super(http);
    this._authService = new BasicAuthService(http);
  }

  getAll(): Observable<News[]> {
    return this.http.get<any[]>(`${this._authService.getBaseUrl()}/news`, {headers: this._authService.getAuthHeaders()}).pipe(
      map(body => body.map(n => News.fromObject(n)))
    );
  }

  getAllByAuthor(author: string): Observable<News[]> {
    return this.http.post<any[]>(`${this._authService.getBaseUrl()}/news/byAuthor`, {author}, {headers: this._authService.getAuthHeaders()}).pipe(
      map(body => body.map(n => News.fromObject(n)))
    );
  }

  getNewest(): Observable<News> {
    return this.http.get<any>(`${this._authService.getBaseUrl()}/news/newest`, {headers: this._authService.getAuthHeaders()}).pipe(
      map(body => News.fromObject(body))
    );
  }

  getNewestByAuthor(author: string): Observable<News> {
	  return this.http.post<any>(`${this._authService.getBaseUrl()}/news/newest/byAuthor`, {author}, {headers: this._authService.getAuthHeaders()}).pipe(
      map(body => News.fromObject(body))
    );
  }

  getNewestBase(): Observable<News> {
    return this.http.get<any>(`${env.apiUrl}/news/newest`, {headers: this.defaultHeaders}).pipe(
      map(body => News.fromObject(body))
    );
  }

  create(headline: string, content: string): Observable<News> {
    let author: string = this._authService.getUsername();
    return this.http.post<any>(`${this._authService.getBaseUrl()}/news`, {headline, content, author}, {headers: this._authService.getAuthHeaders()}).pipe(
      map(body => News.fromObject(body))
    );
  }

  change(author: string, headline: string, content: string): Observable<News[]> {
    return this.http.post<any>(`${this._authService.getBaseUrl()}/news/changeNews`, {headline, content, author}, {headers: this._authService.getAuthHeaders()}).pipe(
       map(body => body.map(n => News.fromObject(n)))
    );
  }

  delete(author: string, headline: string, content: string): Observable<News[]> {
    return this.http.post<any>(`${this._authService.getBaseUrl()}/news/deleteNews`, {headline, content, author}, {headers: this._authService.getAuthHeaders()}).pipe(
       map(body => body.map(n => News.fromObject(n)))
    );
  }

  set authService(value: AuthService) {
    this._authService = value;
  }
}
