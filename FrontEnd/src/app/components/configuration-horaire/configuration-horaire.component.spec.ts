import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigurationHoraireComponent } from './configuration-horaire.component';

describe('ConfigurationHoraireComponent', () => {
  let component: ConfigurationHoraireComponent;
  let fixture: ComponentFixture<ConfigurationHoraireComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ConfigurationHoraireComponent]
    });
    fixture = TestBed.createComponent(ConfigurationHoraireComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
