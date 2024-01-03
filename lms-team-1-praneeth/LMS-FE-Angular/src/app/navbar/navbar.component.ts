import { ChangeDetectorRef, Component } from '@angular/core';
import { Router } from '@angular/router';
import { faArrowUpRightFromSquare } from '@fortawesome/free-solid-svg-icons';
import { faCircleUser } from '@fortawesome/free-solid-svg-icons';
import { faSignOut } from '@fortawesome/free-solid-svg-icons';
import { EmailService } from '../email.service';
import { AuthService } from '../auth.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent {
  falink = faArrowUpRightFromSquare;
  fauser = faCircleUser;
  fasignout = faSignOut;

  emailData: string | any = '';

  authority: string | any = localStorage.getItem('role');
  
  myProfile: boolean = true;

  image: string | any = localStorage.getItem('image');

  constructor(
    public router: Router,
    private emailService: EmailService,
    private auth: AuthService,
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {
    this.emailData = localStorage.getItem('capitalizedEmail');
  }

  baseUrl: any = this.auth.getBaseUrl();

  ngOnInit(): void {
    if (this.image.length > 3) {
      this.myProfile = true;
    } else {
      this.myProfile = false;
      // console.log(this.myProfile);
      this.image = localStorage.getItem('image');
      // console.log(this.image);
    }
  }

  admin() {
    this.router.navigate(['admin']);
  }

  dashboard() {
    this.router.navigate(['dashboard']);
  }

  profile() {
    this.router.navigate(['my-profile']);
  }

  signOut() {
    this.router.navigate(['login']);
    localStorage.clear();
  }
}
