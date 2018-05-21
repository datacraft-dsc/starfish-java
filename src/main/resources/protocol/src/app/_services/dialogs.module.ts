import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DialogsService } from './dialogs.service';
import { MatButtonModule, MatDialogModule, MatInputModule,MatSelectModule } from '@angular/material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UploadDialogComponent } from '../upload-dialog/upload-dialog.component';
import { UploadDialogService } from '../upload-dialog/upload-dialog.service';
@NgModule({
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    FormsModule,
    MatInputModule,
    ReactiveFormsModule,
    MatSelectModule
  ],
  declarations: [UploadDialogComponent],
  exports: [UploadDialogComponent],
  entryComponents: [UploadDialogComponent],
  providers: [DialogsService,UploadDialogService]
})
export class DialogsModule { }