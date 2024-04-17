import { TestBed } from '@angular/core/testing';

import { SharenumeService } from './sharenume.service';

describe('SharenumeService', () => {
  let service: SharenumeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SharenumeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
