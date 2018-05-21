import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material';
import { UploadDialogComponent } from '../upload-dialog/upload-dialog.component';
import { DialogsService } from '../_services/dialogs.service';
@Component({
  selector: 'app-assets',
  templateUrl: './assets.component.html',
  styleUrls: ['./assets.component.css']
})
export class AssetsComponent implements OnInit {
  file:any;
  fileUpload:any;
  constructor(private dialog:MatDialog, private dialogsService:DialogsService) { }

  ngOnInit() {
  
  }
//   fileEvent(event){ 
//     this.fileUpload= event.target.files[0];
//     this.file = event.target.files[0].name; 
//     console.log(this.file);
// }
 
  upload(): void {
    this.dialogsService    
      .confirm()
      .subscribe(res => {
       
        console.log(JSON.stringify(res));
        
      });
  }

}
