import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReclamationhistoComponent } from './reclamationhisto.component';

describe('ReclamationhistoComponent', () => {
  let component: ReclamationhistoComponent;
  let fixture: ComponentFixture<ReclamationhistoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReclamationhistoComponent]
    });
    fixture = TestBed.createComponent(ReclamationhistoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
