import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-user',
  templateUrl: './register-user.component.html',
  styleUrls: ['./register-user.component.css'],
})
export class RegisterUserComponent {
  username: string = '';
  email: string = '';
  password: string = '';
  user: string = '';
  add = false;

  constructor(private http: HttpClient, private router: Router) {}

  register() {
    // console.warn(data);
    // console.log('Name:', this.username);
    const data = {
      username: this.username,
      email: this.email,
      password: this.password,
    };

    this.http
      .post('http://localhost:8080/addUser', data, {
        responseType: 'text',
      })
      .subscribe((response: string) => {
        if (response === 'Invalid details') {
          this.user = 'Enter all the fields';
          this.add = true;
        } else {
          this.user = response;
          this.add = true;
        }
      });
  }

  login() {
    this.router.navigate(['login']);
  }
}
