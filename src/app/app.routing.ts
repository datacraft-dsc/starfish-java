import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import{RegisterComponent} from './register/register.component';
import {RouterLink} from '@angular/router';
import {SideNavComponent} from './side-nav/side-nav.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {Ver1Component} from './api/ver1/ver1.component';
export const appRoutes: Routes = [
    { path: '', component: HomeComponent },  
    {path:'register', component: RegisterComponent},
    {path:'API', component: Ver1Component},
    { path: 'ocean', component: SideNavComponent,
    children: [
     { path: 'dashboard', component: DashboardComponent },
     { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]}
];