import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {
  logout:boolean=false;
  constructor() { }

  ngOnInit() {
  }
  logoutShow(){
    this.logout=true;
  }
}
