import { Component } from '@angular/core';
import{ RegisterService } from './register/register.service';
import{ HomeService } from './home/home.service';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers:[RegisterService,HomeService]
})
export class AppComponent {
  title = 'app';
}
