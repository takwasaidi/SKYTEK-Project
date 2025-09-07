import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FilterdSalleComponent } from './filterd-salle.component';

describe('FilterdSalleComponent', () => {
  let component: FilterdSalleComponent;
  let fixture: ComponentFixture<FilterdSalleComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FilterdSalleComponent]
    });
    fixture = TestBed.createComponent(FilterdSalleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
