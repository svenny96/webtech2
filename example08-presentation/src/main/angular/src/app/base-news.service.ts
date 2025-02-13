import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { News } from './news';

export abstract class BaseNewsService {
  protected defaultHeaders = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  protected constructor(protected http: HttpClient) {
  }

  abstract getNewest(): Observable<News>;

  abstract getNewestByAuthor(author: string): Observable<News>;

  abstract getAll(): Observable<News[]>;

  abstract getAllByAuthor(author: string): Observable<News[]>;

  abstract create(headline: string, content: string): Observable<News>;

  abstract change(id: number, headline: string, content: string): Observable<News[]>;

  abstract delete(id: number): Observable<News[]>;
}
