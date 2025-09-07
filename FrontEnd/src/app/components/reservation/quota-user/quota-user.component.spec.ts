import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuotaUserComponent } from './quota-user.component';

describe('QuotaUserComponent', () => {
  let component: QuotaUserComponent;
  let fixture: ComponentFixture<QuotaUserComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [QuotaUserComponent]
    });
    fixture = TestBed.createComponent(QuotaUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
