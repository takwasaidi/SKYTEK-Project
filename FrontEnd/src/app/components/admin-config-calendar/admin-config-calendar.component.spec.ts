import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminConfigCalendarComponent } from './admin-config-calendar.component';

describe('AdminConfigCalendarComponent', () => {
  let component: AdminConfigCalendarComponent;
  let fixture: ComponentFixture<AdminConfigCalendarComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdminConfigCalendarComponent]
    });
    fixture = TestBed.createComponent(AdminConfigCalendarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
