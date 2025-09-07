import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuotaAlertsComponent } from './quota-alerts.component';

describe('QuotaAlertsComponent', () => {
  let component: QuotaAlertsComponent;
  let fixture: ComponentFixture<QuotaAlertsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [QuotaAlertsComponent]
    });
    fixture = TestBed.createComponent(QuotaAlertsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
