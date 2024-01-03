import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { WelcomeComponent } from './welcome/welcome.component';
import { LoginComponent } from './login/login.component';
import { ForgotPwdComponent } from './forgot-pwd/forgot-pwd.component';
import { RegisterUserComponent } from './register-user/register-user.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HomeComponent } from './home/home.component';
import { AdminPortalComponent } from './admin-portal/admin-portal.component';
import { MyProfileComponent } from './my-profile/my-profile.component';
import { LearnersDashboardComponent } from './learners-dashboard/learners-dashboard.component';
// import { authGuard } from './auth.guard';

const routes: Routes = [
  {
    path: '',
    component: WelcomeComponent,
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'forgot-password',
    component: ForgotPwdComponent,
  },
  {
    path: 'register',
    component: RegisterUserComponent,
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    // canActivate: [authGuard],
  },
  {
    path: 'home',
    component: HomeComponent,
    // canActivate: [authGuard],
  },
  {
    path: 'admin',
    component: AdminPortalComponent,
    // canActivate: [authGuard],
  },
  {
    path: 'my-profile',
    component: MyProfileComponent,
    // canActivate: [authGuard],
  },
  {
    path: 'learners',
    component: LearnersDashboardComponent,
    // canActivate: [authGuard],
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes,{useHash:true})],
  exports: [RouterModule],
})
export class AppRoutingModule {}
