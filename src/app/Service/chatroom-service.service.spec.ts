import { TestBed } from '@angular/core/testing';

import { ChatroomServiceService } from './chatroom-service.service';

describe('ChatroomServiceService', () => {
  let service: ChatroomServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChatroomServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
