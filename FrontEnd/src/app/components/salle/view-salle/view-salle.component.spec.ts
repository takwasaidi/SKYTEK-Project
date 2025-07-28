import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewSalleComponent } from './view-salle.component';

describe('ViewSalleComponent', () => {
  let component: ViewSalleComponent;
  let fixture: ComponentFixture<ViewSalleComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewSalleComponent]
    });
    fixture = TestBed.createComponent(ViewSalleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
