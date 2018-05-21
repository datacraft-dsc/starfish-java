import { Component, OnInit } from '@angular/core';
import { UploadDialogService } from './upload-dialog.service';
import { SnackBarService } from '../_services/snackbar.service';
import { MatDialogRef } from '@angular/material';

@Component({
  selector: 'app-upload-dialog',
  templateUrl: './upload-dialog.component.html',
  styleUrls: ['./upload-dialog.component.css']
})
export class UploadDialogComponent implements OnInit {
  actorId:any;
  assets:any;
  fileObj = new FormData();
  errorMessage:string;
  assetId:any;
  file:any;
  fileUpload;
  cname:string;
  price:number;
  description:string;
  type:string;
  email:string;
  healthList:any=[ "ECG","MRI","Cancer","xray","scanning"];
  marketList:any=[ "Share Markets","Money Markets","Token Market","Coin Market"]
  accountingList:any=["Open Account","Closed Account"];
  bussinessList:any=["Small Scale","Large Scale"];
  weatherList:any=["rainy","cloudy"];
  financeList:any=["Private","Bussiness"];
  constructor(private uploadDialogService:UploadDialogService, private notify:SnackBarService, public dialogRef: MatDialogRef<UploadDialogComponent>) { }

  ngOnInit() {
    this.actorId= localStorage.getItem("actorId");
    this.email=JSON.parse(localStorage.getItem('email'));
  }
  
  fileAdd(event){ 
    this.fileUpload= event.target.files[0];
    this.file = event.target.files[0].name; 
    console.log(this.file);
   }
  registerAsset(): void {

    this.assets={
      publisherId:this.actorId,
      name:this.email,
      category:this.cname,
      type:this.type,
      description:this.description,
      price:this.price
    }
    this.fileObj.append("assets",JSON.stringify(this.assets));
    this.fileObj.append("email",this.email);
    this.uploadDialogService.registerAsset(this.fileObj).subscribe((response) => {
    this.assetId=response['result'].assetId;
    console.log(this.assetId);
    let formData = new FormData();
    formData.append('file', this.fileUpload);
    this.uploadDialogService.uploadFile(this.assetId,formData).subscribe((response) => {
      if (response['result'] = "Assets Successfuly Uploaded") {
        console.log(response);
        this.notify.openSnackBar("Successfully Uploaded!", "");      
      } else {
        this.notify.openSnackBar("Uploading Failed!", "");
      }
      error => this.errorMessage = <any>error;
    });
    error => this.errorMessage = <any>error;
  });
 }
}