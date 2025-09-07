import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationCalenderComponent } from './reservation-calender.component';

describe('ReservationCalenderComponent', () => {
  let component: ReservationCalenderComponent;
  let fixture: ComponentFixture<ReservationCalenderComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReservationCalenderComponent]
    });
    fixture = TestBed.createComponent(ReservationCalenderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
