import { Component, OnInit } from '@angular/core';

import{Router} from '@angular/router';
import { HomeService } from './home.service';
import{ SnackBarService } from '../_services/snackbar.service';

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

  constructor(private homeService:HomeService,private router:Router, private notify:SnackBarService) { }

  ngOnInit() {
    localStorage.removeItem("actorId");
    localStorage.removeItem("email");
  }
  login() {
    this.userObj.append("email",this.email)
    this.userObj.append("password",this.password)
  this.homeService.loginUser(this.userObj).subscribe((response) => {
    console.log(response);
    if (response['result'] == "Success") {
     localStorage.setItem('actorId', JSON.stringify(response['actorId']));
     localStorage.setItem('email', JSON.stringify(this.email));
     this.router.navigate(['/ocean']);
    }else{
      this.notify.openSnackBar("Username or Password incorrect!", "");
    }
     error => this.errorMessage = <any>error
  });
}


}

