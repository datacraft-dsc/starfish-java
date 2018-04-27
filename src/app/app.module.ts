import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {MatInputModule} from '@angular/material';
import { AppMaterialModule } from './app-material/app-material.module';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { RegisterComponent } from './register/register.component';
import{RouterModule} from '@angular/router';
import{appRoutes} from './app.routing';
import{HttpModule} from '@angular/http';

import { SnackBarService } from './_services/snackbar.service';
import { SideNavComponent } from './side-nav/side-nav.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { Ver1Component } from './api/ver1/ver1.component'

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    RegisterComponent,
    SideNavComponent,
    DashboardComponent,
    NavBarComponent,
    Ver1Component
  ],
  imports: [
    RouterModule.forRoot(appRoutes, { useHash: true }),
    BrowserModule,
    MatInputModule,
    AppMaterialModule,
    FormsModule,
    HttpModule
  ],
  providers: [SnackBarService],
  bootstrap: [AppComponent]
})
export class AppModule { }
