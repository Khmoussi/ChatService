import { Message } from './../Model/Message';
import { ChangeDetectorRef, Component } from '@angular/core';
import { SharedDataService } from '../Service/shared-data.service';
import { CommonModule } from '@angular/common';
import { ChatRoom } from '../Model/ChatRoom';
import { ApiService } from '../Service/api.service';
import { CookieService } from 'ngx-cookie-service';
import { FormsModule } from '@angular/forms';
import SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import { BehaviorSubject } from 'rxjs';
import { ChatroomServiceService } from '../Service/chatroom-service.service';
import { MessageResponse } from '../Model/MessageResponse';
import { Coworker } from '../Model/Coworker';

@Component({
  selector: 'app-chat-group',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './chat-group.component.html',
  styleUrl: './chat-group.component.css'
})
export class ChatGroupComponent {


  userId:number;
  messageList: MessageResponse[]=[];
  message:string="";
  currentChatRoom:ChatRoom|null =null;
  currentCoworker:Coworker|null=null;
  constructor(private sharedDataService : SharedDataService,private apiService :ApiService,private coockieService:CookieService,private chatroomService : ChatroomServiceService){
    this.userId=parseInt(this.coockieService.get("username").split(',')[2]);
    this.currentChatRoom=this.sharedDataService.currentChatroom.value;
    this.messageList=this.currentChatRoom.chatroomMessages;
    this.currentCoworker=this.apiService.coworkerSubj.value;
   /* this.sharedDataService.currentChatroom$.subscribe(chatRoom =>{
    this.currentChatRoom=this.sharedDataService.currentChatroom.value;
    console.log("current chatrrom" +this.currentChatRoom.messageList.length);

    this.messageList=this.currentChatRoom.messageList;
    console.log("messageList" +this.messageList[0].Content)

    });*/
    this.chatroomService.currentChatroom$.subscribe(chatRoom =>{
      this.currentChatRoom=this.chatroomService.currentChatroom.value!;

      this.messageList=this.currentChatRoom.chatroomMessages;
      console.log("current Room " +this.currentChatRoom.chatroomMessages)

      });

  }

  ngOnInit() {





// sending file through socket


  }
  sendMessage(){
let roomId=this.currentChatRoom?.id as string;
    this.apiService.send(roomId,{},this.message);
    console.log("messageList" +this.messageList)

  }
  subscribeToRoomById(roomId:string){
    this.apiService.subscribeTotopic(roomId);

}
}
