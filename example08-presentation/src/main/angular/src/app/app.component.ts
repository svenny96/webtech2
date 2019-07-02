import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent {
  title = 'example08';

  public auth: boolean = false;


  public setAuth(): void {
	  this.auth = !this.auth;
  }
}
