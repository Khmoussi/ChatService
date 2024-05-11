import { TestBed } from '@angular/core/testing';

import { CoworkerService } from './coworker.service';

describe('CoworkerService', () => {
  let service: CoworkerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CoworkerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
