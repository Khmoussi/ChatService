import { SharedDataService } from './../Service/shared-data.service';
import { ApiService } from './../Service/api.service';
import { Message } from '../Model/Message';
import { ChatRoom } from './../Model/ChatRoom';
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChatroomServiceService } from '../Service/chatroom-service.service';
@Component({
  selector: 'app-chatroom',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './chatroom.component.html',
  styleUrl: './chatroom.component.css'
})
export class ChatroomComponent {

  clickedItemIndex:number=0;

  chatRoomsList:ChatRoom[]=[];
constructor(private apiService :ApiService,private sharedDataService:SharedDataService,private chatroomService:ChatroomServiceService){

  this.chatRoomsList=this.sharedDataService.chatroomList.value
  this.sharedDataService.updateCurrentChatRoom(this.chatRoomsList[this.clickedItemIndex]);
this.chatroomService.currentChatroom.next(this.chatRoomsList[this.clickedItemIndex]);

  this.sharedDataService.chatroomList.subscribe(data=>{
    this.chatRoomsList=data;
    this.sharedDataService.updateCurrentChatRoom(this.chatRoomsList[this.clickedItemIndex]);


  })
 // this.sharedDataService.updateCurrentChatRoom(this.chatRoomsList[0]);//idk why but if don't update the changes on the allchat doesn't save unless it's updated


}

 selectChatroom(index:number):void{
  this.clickedItemIndex=index;
  this.sharedDataService.updateCurrentChatRoom(this.chatRoomsList[index]);
  this.chatroomService.currentChatroom.next(this.chatRoomsList[index]);


}


}
