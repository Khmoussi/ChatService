import { Message } from './../Model/Message';
import { ApiService } from './api.service';
import { Component, Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ChatRoom } from '../Model/ChatRoom';
import { Coworker } from '../Model/Coworker';
import { ChatroomServiceService } from './chatroom-service.service';
import { MessageResponse } from '../Model/MessageResponse';

@Injectable({
  providedIn: 'root'
})
export class SharedDataService {

  private behaviorSubject: BehaviorSubject<Message[]> = new BehaviorSubject<Message[]>([]);
  public data$ = this.behaviorSubject.asObservable();
  private behaviorSubject3: BehaviorSubject<Message[]> = new BehaviorSubject<Message[]>([]);
  public data2$ = this.behaviorSubject3.asObservable();

  private BehaviorSubject2:BehaviorSubject<boolean >=new BehaviorSubject<boolean>(true );
  public ChatRoomSOrCoworkers$=this.BehaviorSubject2.asObservable();
  public currentChatroom$;
  public currentChatroom: BehaviorSubject<ChatRoom>;
  public currentCoworker$;
  public currentCoworker: BehaviorSubject<Coworker>;
  public behaviorCurrentMessageList: BehaviorSubject<Message[]> = new BehaviorSubject<Message[]>([]);
  public currentMessageList$ = this.behaviorCurrentMessageList.asObservable();
  public coworkerList :BehaviorSubject<Coworker[]> = new BehaviorSubject<Coworker[]>([]);
  public coworkerList$=this.coworkerList.asObservable();
  public chatroomList :BehaviorSubject<ChatRoom[]> = new BehaviorSubject<ChatRoom[]>([]);
  public chatroomList$=this.chatroomList.asObservable();

  public testMessage :BehaviorSubject<string>=new BehaviorSubject<string>("");
public testMessage$=this.testMessage.asObservable();

  constructor( private apiService: ApiService ,private chatroomService: ChatroomServiceService) {

  this.currentChatroom= new BehaviorSubject(this.apiService.getChatrooms[0]);
  this.currentChatroom$=this.currentChatroom.asObservable();
  this.chatroomList$.subscribe(chatroom =>{
    this.chatroomList.next(chatroom);
  })
  this.chatroomService.chatroomList.subscribe(chatRoom =>{
    this.chatroomList.next(chatRoom);
  })

  this.currentCoworker= new BehaviorSubject(this.apiService.getCoworkers[0]);
  this.currentCoworker$=this.currentCoworker.asObservable();






}


updateCoworkerList() {
  let list=this.apiService.getCoworkers
  this.coworkerList.next(list);
  this.updateCurrentCoworker(list[0]);
 }
 updateCurrentCoworker(coworker:Coworker){
  this.currentCoworker.next(coworker);

 }
 updateCurrentChatRoom(chatRoom:ChatRoom){
this.currentChatroom.next(chatRoom);
 }
  updateChat(data:Message[]){
    this.behaviorSubject.next(data);
  }
updateChatRoomSOrCoworkers(ChatRoomSOrCoworkers:boolean){
this.BehaviorSubject2.next(ChatRoomSOrCoworkers);
}
updateMessageList(data:Message[]){
  this.behaviorCurrentMessageList.next(data);
}
sendMessage(message:MessageResponse){
  this.currentChatroom.value.chatroomMessages.push(message);
}
}

