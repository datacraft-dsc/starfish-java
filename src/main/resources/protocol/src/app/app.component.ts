import { Component } from '@angular/core';
import{ RegisterService } from './register/register.service';
import{ HomeService } from './home/home.service';
import{ DashboardService } from './dashboard/dashboard.service';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers:[RegisterService,HomeService,DashboardService]
})
export class AppComponent {
  title = 'app';
}
