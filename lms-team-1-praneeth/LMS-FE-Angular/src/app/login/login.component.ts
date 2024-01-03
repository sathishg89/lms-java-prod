import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { faEye } from '@fortawesome/free-solid-svg-icons';
import { faEyeSlash } from '@fortawesome/free-solid-svg-icons';
import { User, login } from '../user';
import { HttpClient } from '@angular/common/http';
import { EmailService } from '../email.service';
import { AuthService } from '../auth.service';
import { startWith } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  userEmail: string = '';
  password: string = '';
  status: string = '';
  warning: string = '';
  // role: string = 'user';
  // capitalizedEmail: string = '';

  constructor(
    private router: Router,
    private http: HttpClient,
    private emailService: EmailService,
    private auth: AuthService
  ) {}

  faEye = faEye;
  faEyeSlash = faEyeSlash;

  protector = false;

  forgotPwd() {
    this.router.navigate(['forgot-password']);
  }

  eye() {
    if (this.protector === false) {
      this.protector = true;
    } else {
      this.protector = false;
    }
  }

  home() {}

  login(loginData: login) {
    // if (loginData.userEmail.startsWith('@')) {
    //   this.status = 'login Failed';
    //   this.warning = 'email cannot start with an address';
    // console.log(loginData);

    // } else {
    this.auth.Login(loginData);
    this.auth.status.subscribe((result) => {
      if (result) {
        this.status = 'login Failed';
        this.warning = this.auth.errorMessage;
      } else {
        this.status = '';
      }
    });
    // }
  }
}
