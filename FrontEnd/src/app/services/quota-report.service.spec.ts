import { TestBed } from '@angular/core/testing';

import { QuotaReportService } from './quota-report.service';

describe('QuotaReportService', () => {
  let service: QuotaReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuotaReportService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
