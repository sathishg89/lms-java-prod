import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from './user';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private baseURL = 'http://localhost:8080';
  constructor(private http: HttpClient) {}

  getUserName(email: String): Observable<any> {
    return this.http.get(`${this.baseURL}/getUser?email=${email}`);
  }

  editUser(data: User) {}
}
