import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthNewsService } from './auth-news.service';
import { AngularComponent } from '../angular/angular.component';
import { JwtAuthService } from './jwt-auth.service';
import { BasicAuthService } from './basic-auth.service';
import { AuthService } from './auth.service';
import { SessionAuthService } from './session-auth.service';
import { News } from '../news';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.sass'],
  providers: [AuthNewsService]
})
export class AuthComponent extends AngularComponent implements OnInit {

  private authService: AuthService;


  constructor(private http: HttpClient,
              private authNewsService: AuthNewsService) {
    super(authNewsService);
   
  }

  ngOnInit() {
	  this.authService = new BasicAuthService(this.http);
	this.authNewsService.authService = this.authService;
	
  }

  logout() {
    this.authService.logout().subscribe();
    this.news = [];
    this.latest = null;
  }

  load(): void {
    this.newsService.getNewest().subscribe(
      news => this.latest = news,
      console.error
	);
	this.newsService.getNewestByAuthor(this.currentUser).subscribe(
		news => this.latestOwned = news,
		console.error
	);
    this.newsService.getAll().subscribe(
      news => this.news = news,
      console.error
	);
	this.newsService.getAllByAuthor(this.currentUser).subscribe(
		news => this.newsOwned = news,
		console.error
	);
    this.filtered = false;
  }

  loadByAuthor(author: string): void {
    this.newsService.getNewest().subscribe(
      news => this.latest = news,
      console.error
    );
    this.newsService.getAllByAuthor(author).subscribe(
      news => this.news = news,
      console.error
	);
	this.filtered = true;
  }

  changeNews(newsC: News): void {
	this.newsService.change(newsC.author, newsC.headline, newsC.content).subscribe(
		news =>{ this.news = news;
		this.newsOwned = this.ownedNewsFromNews;
		this.latest = this.latestFromNews;
		this.latestOwned = this.latestOwnedFromNews	
		},
		console.error
	);
	
	this.load();
  }

  useBasicAuth(e?: Event) {
    if (e != null) e.preventDefault();
    this.authService = new BasicAuthService(this.http);
    this.authNewsService.authService = this.authService;
  }

  useJwtAuth(e?: Event) {
    if (e != null) e.preventDefault();
    this.authService = new JwtAuthService(this.http);
    this.authNewsService.authService = this.authService;
  }

  useSessionAuth(e?: Event) {
    if (e != null) e.preventDefault();
    this.authService = new SessionAuthService(this.http);
    this.authNewsService.authService = this.authService;
  }

  isBasicAuth(): boolean {
    return this.authService instanceof BasicAuthService;
  }

  isJwtAuth(): boolean {
    return this.authService instanceof JwtAuthService;
  }

  isSessionAuth(): boolean {
    return this.authService instanceof SessionAuthService;
  }

  get isLoggedIn(): boolean {
    return this.authService.isLoggedIn;
  }
  get currentUser(): string {
	  return this.authService.getUsername();
  }

 get ownedNewsFromNews(): News[] {
	 let newsList: News[] = Object.assign([], this.news);

	//if(this.currentUser === "admin") {
	//		return newsList;
	//}

	  for(let el of newsList) {
		  if(el.author === this.currentUser) {

		  } else {
			  newsList.splice(newsList.indexOf(el),1);
		  }
	  }
	 return newsList;
  }

  get latestFromNews(): News {
	  let newsList: News[] = Object.assign([], this.news);
	  return newsList.pop();
  }

  get latestOwnedFromNews(): News {
	  let newsList: News[] = Object.assign([], this.news);
	  newsList = newsList.slice().reverse();

	  for(let el of newsList) {
		  if(el.author === this.currentUser) {
			  return el;
		  }
	  }
  }
 
}
