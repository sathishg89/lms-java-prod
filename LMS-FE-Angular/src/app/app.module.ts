import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { LoginComponent } from './login/login.component';
import { FormsModule } from '@angular/forms';
import { ForgotPwdComponent } from './forgot-pwd/forgot-pwd.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { RegisterUserComponent } from './register-user/register-user.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NavbarComponent } from './navbar/navbar.component';
import { HomeComponent } from './home/home.component';
import { AdminPortalComponent } from './admin-portal/admin-portal.component';
import { HttpClientModule } from '@angular/common/http';
import { EmailService } from './email.service';
import { MyProfileComponent } from './my-profile/my-profile.component';
import { LearnersDashboardComponent } from './learners-dashboard/learners-dashboard.component';

@NgModule({
  declarations: [
    AppComponent,
    WelcomeComponent,
    LoginComponent,
    ForgotPwdComponent,
    RegisterUserComponent,
    DashboardComponent,
    NavbarComponent,
    HomeComponent,
    AdminPortalComponent,
    MyProfileComponent,
    LearnersDashboardComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    FontAwesomeModule,
    HttpClientModule,
  ],
  providers: [EmailService],
  bootstrap: [AppComponent],
})
export class AppModule {}
