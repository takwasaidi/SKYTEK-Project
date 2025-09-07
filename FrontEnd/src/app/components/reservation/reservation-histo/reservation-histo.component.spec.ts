import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationHistoComponent } from './reservation-histo.component';

describe('ReservationHistoComponent', () => {
  let component: ReservationHistoComponent;
  let fixture: ComponentFixture<ReservationHistoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReservationHistoComponent]
    });
    fixture = TestBed.createComponent(ReservationHistoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
