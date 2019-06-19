import { Component, Input, Output, EventEmitter } from '@angular/core';
import { News } from '../../news';


@Component({
  selector: 'news-list',
  templateUrl: './news-list.component.html',
  styleUrls: ['./news-list.component.sass']
})
export class NewsListComponent {

  @Input()
  public news: News[] = [];

  @Output()
  filteredName = new EventEmitter<string>();

  get reversedNews(): News[] {
    return this.news.slice().reverse();
  }

  onFilterByName(name: string) {
    this.filteredName.emit(name);
  }
}
