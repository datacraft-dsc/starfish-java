import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatToolbarModule, MatSidenavModule, MatButtonModule, MatIconModule, MatTableModule, MatSlideToggleModule, MatSelectModule, MatInputModule, MatCheckboxModule, MatDatepickerModule } from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Component, Inject } from '@angular/core';
import { MatExpansionModule, MatNativeDateModule  } from '@angular/material';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatProgressBarModule} from '@angular/material/progress-bar';
@NgModule({
  imports: [
    CommonModule,
    MatToolbarModule,
    MatSidenavModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    
    BrowserAnimationsModule,
    MatSlideToggleModule,
    MatSelectModule,
    MatInputModule,
    MatCheckboxModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatSnackBarModule,
    MatProgressBarModule
  ],
  exports: [
    MatToolbarModule,
    MatSidenavModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    
    MatCheckboxModule,
    BrowserAnimationsModule,
    MatSlideToggleModule,
    MatSelectModule,
    MatInputModule,
    MatExpansionModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatSnackBarModule,
    MatProgressBarModule
  ],
  declarations: []
})
export class AppMaterialModule { }
