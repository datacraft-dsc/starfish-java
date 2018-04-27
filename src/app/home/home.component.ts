import { Component, OnInit } from '@angular/core';

import{Router} from '@angular/router';
import { HomeService } from './home.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
 email:string;
 password:string;
 errorMessage:string;
 userObj = new FormData();

  constructor(private homeService:HomeService,private router:Router) { }

  ngOnInit() {
  }
  login() {
    this.userObj.append("email",this.email)
    this.userObj.append("password",this.password)
  this.homeService.loginUser(this.userObj).subscribe((response) => {
    console.log(response);
    if (response['result'] == "Success") {
     this.router.navigate(['/ocean']);
    }
     error => this.errorMessage = <any>error
  });
}


}

