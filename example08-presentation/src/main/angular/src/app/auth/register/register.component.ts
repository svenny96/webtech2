import { Component, EventEmitter, Input, Output, SystemJsNgModuleLoader } from '@angular/core';
import { AuthService } from '../auth.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.sass']
})
export class RegisterComponent {

  @Input()
  public authService: AuthService;

  @Output()
  public registered = new EventEmitter<void>();

  public username: string = "";
  public password: string = "";
  public passwordRepeat: string = "";
  public errorMessage: string;
  private existingUsers: string[] = ["admin"];

  register(f: NgForm) {
	this.errorMessage = null;
	
    if (this.username != "" && this.password != "" && this.passwordRepeat != "") {
	  if(this.password === this.passwordRepeat)
		{
			if(this.existingUsers.includes(this.username))
			{
				this.errorMessage="Nutzername existiert bereits"
			}
			else {
				this.authService.register(this.username, this.password).subscribe(
					  () => this.registered.emit);
					  this.existingUsers.push(this.username);
					  f.reset();
			}         
		}
		else
		{this.errorMessage = "Passwörter stimmen nicht überein"}
     
    }
  }
}
