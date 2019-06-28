import { Component, Input, Output, EventEmitter } from '@angular/core';
import { News } from '../../news';
import { ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'news-list',
  templateUrl: './news-list.component.html',
  styleUrls: ['./news-list.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class NewsListComponent {

  public newHeadline: string ="";
  public newContent: string ="";

  @Input()
  public user: string;

  @Input()
  public news: News[] = [];

  @Output()
  filteredName = new EventEmitter<string>();
  
  @Output()
  changedNews = new EventEmitter<News>();

  @Output()
  deletedNews = new EventEmitter<News>();


  get reversedNews(): News[] {
	return this.news.slice().reverse();
  }

  

  edit(news: News): void {
	  news.editable = !news.editable;
  }

  openEditDialog(news: News): void {
	  news.editDialog = !news.editDialog;
  }
  
  onFilterByName(name: string) {
    this.filteredName.emit(name);
  }

  onSubmit(news: News): void {
	 
	  let nHeadline: string = this.newHeadline;
	  let nContent: string = this.newContent;

	  news.headline = news.headline+"@"+this.newHeadline;
	  news.content = news.content+"@"+this.newContent;
	  this.changedNews.emit(news);
	  
	  this.openEditDialog(news);	
	  this.newHeadline="";
	  this.newContent="";
  }

  onDelete(news: News): void {
	  this.deletedNews.emit(news);
  }
}
