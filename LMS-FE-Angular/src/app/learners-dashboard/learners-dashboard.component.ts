import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { SafeUrl, DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { faEdit } from '@fortawesome/free-solid-svg-icons';
import { faAngleDown } from '@fortawesome/free-solid-svg-icons';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-learners-dashboard',
  templateUrl: './learners-dashboard.component.html',
  styleUrls: ['./learners-dashboard.component.css'],
})
export class LearnersDashboardComponent {
  constructor(
    private sanitizer: DomSanitizer,
    private route: ActivatedRoute,
    private http: HttpClient,
    private auth: AuthService
  ) {}

  baseUrl: any = this.auth.getBaseUrl();

  edit = faEdit;
  darrow = faAngleDown;
  role = localStorage.getItem('role');
  email = localStorage.getItem('email');

  courseTitle: string = '';
  courseTrainer: string = '';

  courseList: any;
  modules: any;
  moduleLength: any;

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      const courseName = params['courseName'];
      const courseTrainer = params['courseTrainer'];

      this.courseTitle = courseName;
      this.courseTrainer = courseTrainer;

      this.http
        .get(
          `${this.baseUrl}/admin/course/${courseName}/${courseTrainer}/getvideos`
        )
        .subscribe((data: any) => {
          console.log(data);
          
          data.sort((a: any, b: any) => a.moduleNumber - b.moduleNumber);
          this.courseList = data;

          // Extract unique moduleNumber values from courseList
          const uniquemoduleNumbers = [
            ...new Set(
              data.map((module: { moduleNumber: any }) => module.moduleNumber)
            ),
          ];

          this.moduleLength = uniquemoduleNumbers.length;

          // Initialize modules with isShowTopic set to false for each moduleNumber
          this.modules = uniquemoduleNumbers.map((moduleNumber) => ({
            name: `Module ${moduleNumber}`,
            isShowTopic: false,
          }));
        });
    });
  }

  tabStates: { [key: string]: boolean } = {
    courseInfo: true,
    resources: false,
    projects: false,
    resume: false,
    askAI: false,
  };

  expand(index: number) {
    this.modules[index].isShowTopic = !this.modules[index].isShowTopic;
  }

  style(tab: string) {
    return {
      color: this.tabStates[tab] ? 'black' : 'grey',
      'border-bottom': this.tabStates[tab]
        ? '2px solid #1f2937'
        : '2px #8f949b solid',
    };
  }

  active(tab: string) {
    Object.keys(this.tabStates).forEach((key) => (this.tabStates[key] = false));
    this.tabStates[tab] = true;
  }

  isShowVideo = false;
  selectedTopic: any;
  safeVideoUrl: SafeUrl | undefined;

  showVideo(video: any) {
    console.log(video);

    this.isShowVideo = true;
    if (video) {
      this.safeVideoUrl = this.sanitizer.bypassSecurityTrustResourceUrl(
        `https://www.youtube.com/embed/${this.getYouTubeVideoId(video)}`
      );
    }
  }

  private getYouTubeVideoId(url: string): string {
    const match = url.match(
      /(?:youtu\.be\/|youtube\.com(?:\/embed\/|\/v\/|\/watch\?v=|\/videos\?.+v=))([^"&?\/\s]{11})/
    );
    return match ? match[1] : '';
  }
}
