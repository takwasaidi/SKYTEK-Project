import { TestBed } from '@angular/core/testing';

import { QuotaAlertService } from './quota-alert.service';

describe('QuotaAlertService', () => {
  let service: QuotaAlertService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuotaAlertService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
