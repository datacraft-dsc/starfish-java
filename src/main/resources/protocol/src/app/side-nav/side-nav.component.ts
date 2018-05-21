import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-side-nav',
  templateUrl: './side-nav.component.html',
  styleUrls: ['./side-nav.component.css']
})
export class SideNavComponent implements OnInit {
  actorId:any;
  selectedItem:string ='Home';
  constructor() { }

  ngOnInit() {
    this.actorId= localStorage.getItem("actorId");
  }
  listClick(item){
    this.selectedItem=item;
  }
}
