import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class EmailService {
  constructor() {}

  private emailData: string = '';

  setEmailData(data: string) {
    this.emailData = data;
  }
  getEmailData() {
    return this.emailData;
  }
}
