import { TestBed } from '@angular/core/testing';

import { ShareCatalogService } from './share-catalog.service';

describe('ShareCatalogService', () => {
  let service: ShareCatalogService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ShareCatalogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
