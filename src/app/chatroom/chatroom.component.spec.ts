import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatroomComponent } from './chatroom.component';

describe('ChatroomComponent', () => {
  let component: ChatroomComponent;
  let fixture: ComponentFixture<ChatroomComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChatroomComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ChatroomComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
