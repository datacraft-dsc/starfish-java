import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { MatDialogRef, MatDialog } from '@angular/material';
import { UploadDialogComponent } from '../upload-dialog/upload-dialog.component';
@Injectable()
export class DialogsService {

    constructor(private dialog: MatDialog) { }

    public confirm(): Observable<boolean> {

        let dialogRef: MatDialogRef<UploadDialogComponent>;

        dialogRef = this.dialog.open(UploadDialogComponent);

        return dialogRef.afterClosed();
    }
         
}