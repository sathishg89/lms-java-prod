import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LearnersDashboardComponent } from './learners-dashboard.component';

describe('LearnersDashboardComponent', () => {
  let component: LearnersDashboardComponent;
  let fixture: ComponentFixture<LearnersDashboardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LearnersDashboardComponent]
    });
    fixture = TestBed.createComponent(LearnersDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
