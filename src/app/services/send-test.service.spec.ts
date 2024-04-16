import { TestBed } from '@angular/core/testing';

import { SendTestService } from './send-test.service';

describe('SendTestService', () => {
  let service: SendTestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SendTestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
