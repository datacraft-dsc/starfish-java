import { Component, OnInit } from '@angular/core';
import{ Router } from '@angular/router';

import{ SnackBarService } from '../_services/snackbar.service';

import{ RegisterService } from './register.service';
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  user:any;
  name:string;
  username:string;
  password :string;
  email:string;
  phone:number;
  cpassword:string;
  country:string;
  dob:Date;
  sex:string;
  walletId:string;
  userDetailsObj = new FormData();

  errorMessage:String;

  

  constructor(private router:Router,private registerService:RegisterService, private notify:SnackBarService) { }

  ngOnInit() {
  }

  registerUser() {
    console.log("registerUser");
      this.user = {

        name: this.name,
        userName: this.username,
        password:this.password,
        email:this.email,
        phone:this.phone,
        country:this.country,
        dob:this.dob,
        sex:this.sex,
        walletId:this.walletId,
        actorId:101

      };
      console.log(JSON.stringify(this.user));
      this.userDetailsObj.append('actor', JSON.stringify(this.user))

      this.registerService.registerUser(this.userDetailsObj).subscribe((response) => {
        console.log(response);
        if (response) {
          console.log("success");
          this.notify.openSnackBar("Success!", "");
        } else {
          console.log("failed");
          this.notify.openSnackBar("Failed!", "");
        }
         error => this.errorMessage = <any>error
      });

  }

}
