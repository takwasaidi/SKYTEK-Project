import { TestBed } from '@angular/core/testing';

import { ConfigurationHoraireService } from './configuration-horaire.service';

describe('ConfigurationHoraireService', () => {
  let service: ConfigurationHoraireService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConfigurationHoraireService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
