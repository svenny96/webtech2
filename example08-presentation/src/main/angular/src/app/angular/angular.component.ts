import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { NewsService } from './news.service';
import { News } from '../news';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'app-angular',
  templateUrl: './angular.component.html',
  styleUrls: ['./angular.component.sass'],
  providers: [NewsService]
})
export class AngularComponent implements OnInit {


  public filtered: boolean;	
  public latest: News;
  public news: News[] = [];

  constructor(protected newsService: NewsService) {
  }

  ngOnInit() {
	this.load();
	this.filtered = false;
  }

  load(): void {
    this.newsService.getNewest().subscribe(
      news => this.latest = news,
      console.error
    );
    this.newsService.getAll().subscribe(
      news => this.news = news,
      console.error
	);
    this.filtered = false;
  }

  loadByAuthorAng(author: string): void {
	 this.newsService.getNewestByAuthor(author).subscribe(
      news => this.latest = news,
      console.error
	);
	 this.newsService.getAllByAuthor(author).subscribe(
      news => this.news = news,
      console.error
	);
	this.filtered = true;
  }
}
