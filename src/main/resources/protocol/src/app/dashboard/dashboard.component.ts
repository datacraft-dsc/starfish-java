import { Component, OnInit } from '@angular/core';
import { DashboardService } from './dashboard.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  assets:any;
  errorMessage:string;
  email:string;
  result:any;
  slidename:string='firstSlide';
  constructor(private dashboardService:DashboardService) { }

  ngOnInit() {
      this.email=JSON.parse(localStorage.getItem('email'));
  }

  selectItem(name){
     this.dashboardService.getAsset(name,this.email).subscribe((response) => {
      console.log(response);
      this.assets=response;
       error => this.errorMessage = <any>error;
    });  
  }

  tabClose(){
    this.result="";
  }
  downloadAsset(id){
    this.dashboardService.downloadAsset(id).subscribe((response) => {
      this.result = response['result'];
      console.log(response);
       error => this.errorMessage = <any>error;
    }); 
  }

  getActiveSlide(slideName){
    console.log(slideName);
    this.slidename=slideName;
  }
}
