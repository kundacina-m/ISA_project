import { TestBed } from '@angular/core/testing';

import { AvioCompanyService } from './avio-company.service';

describe('AvioCompanyService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AvioCompanyService = TestBed.get(AvioCompanyService);
    expect(service).toBeTruthy();
  });
});
