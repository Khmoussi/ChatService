import { BehaviorSubject } from 'rxjs';
import { ChatRoom } from '../Model/ChatRoom';
import { SharedDataService } from './shared-data.service';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ChatroomServiceService {
  public chatroomList :BehaviorSubject<ChatRoom[]> = new BehaviorSubject<ChatRoom[]>([]);
  public chatroomList$=this.chatroomList.asObservable();
  public currentChatroom: BehaviorSubject<ChatRoom | undefined>=new BehaviorSubject<ChatRoom | undefined>(undefined);

  public currentChatroom$=this.currentChatroom.asObservable();
  constructor() {


   }
   resetData() {
    this.chatroomList.next([]);
    this.currentChatroom.next(undefined);
  }
}
