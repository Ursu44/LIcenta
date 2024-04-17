import { TestBed } from '@angular/core/testing';

import { ServiciuService } from './serviciu.service';

describe('ServiciuService', () => {
  let service: ServiciuService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiciuService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
