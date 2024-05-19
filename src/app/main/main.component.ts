import { Message } from './../Model/Message';
import { SharedDataService } from './../Service/shared-data.service';
import { Coworker } from './../Model/Coworker';
import { ApiService } from './../Service/api.service';
import { Component, OnInit } from '@angular/core';
import { NavigationComponent } from '../navigation/navigation.component';
import { CoworkerComponent } from '../coworker/coworker.component';
import { CoworkersListComponent } from '../coworkers-list/coworkers-list.component';
import { ChatComponent } from '../chat/chat.component';
import { AsyncPipe, CommonModule, NgComponentOutlet } from '@angular/common';
import { ChatRoomsListComponent } from '../chat-rooms-list/chat-rooms-list.component';
import { ChatGroupComponent } from '../chat-group/chat-group.component';
import * as Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import { ChatroomServiceService } from '../Service/chatroom-service.service';
import { CoworkerService } from '../Service/coworker.service';
import { UploadImagesComponent } from '../upload-images/upload-images.component';
import { error } from 'console';



@Component({
  selector: 'app-main',
  standalone: true,
  imports: [UploadImagesComponent,ChatComponent,CoworkersListComponent,CoworkerComponent,NavigationComponent,ChatRoomsListComponent,ChatGroupComponent,CommonModule,NgComponentOutlet,AsyncPipe],
  templateUrl: './main.component.html',
  styleUrl: './main.component.css'
})
export class MainComponent implements OnInit {
  username:string="";
  msgList:string[]=["a","b"];
  socket = new SockJS('http://localhost:9000/messenger');
  stompClient=Stomp.over(this.socket);
toggleEdit=false;
  ChatRoomSOrCoworkers:boolean=true;
  currentComponent:Component= new CoworkersListComponent;
constructor(private sharedDataService:SharedDataService,private apiService:ApiService ,private chatroomService:ChatroomServiceService,private coworkerService:CoworkerService){

this.username=this.apiService.firstname +" "+this.apiService.lastname;
}

  ngOnInit() {
    this.sharedDataService.toggleEdit.subscribe((data)=>{
      this.toggleEdit=data;
    })


  //test subscribing to a user
  this.stompClient.connect({token:this.apiService.accessToken},(): any=>{
    //subscribing outside the block doesn't work
    console.log("connected now")
this.apiService.stompClient=this.stompClient;
  //subscrinbing to addedToRoom
  this.apiService.subscribeToAddedToRoom();
  //subscrinbing to removedFromRoom
  this.apiService.subscribeRemovedFromRoom();
    this.apiService.subscribeToUser();
  //  this.apiService.sendToUser({"receiverId":"khmoussiaouina@gmail.com"},"be carefull");





    //subscriptions
  this.apiService.fetchCoworkers().then(()=>{
  console.log("coworkers list fteched")
  this.sharedDataService.updateCoworkerList();
this.coworkerService.coworkerList.next(this.apiService.getCoworkers);

  }).catch(()=>{
    console.log("coworkers list can't be fetched")})

  //fetching rooms
  this.apiService.getUserChatrooms().then(()=>{
    this.sharedDataService.chatroomList.next(this.apiService.getChatrooms);
    this.sharedDataService.currentChatroom.next(this.apiService.getChatrooms[0])
  this.chatroomService.chatroomList.next(this.apiService.getChatrooms)
    console.log("rooms fetched successfully")
    let list=this.sharedDataService.chatroomList.value;
    list.forEach((chatroom)=>{
      let roomId=chatroom.id
      if(roomId != null){
    this.apiService.subscribeTotopic(roomId);


    }
    });

  }).catch((error)=>{
    console.log(error)})
// subscribeToNewUser
this.apiService.subscribeToNewUser();

  });



  // Subscribe to changes in the shared data
 this.sharedDataService.ChatRoomSOrCoworkers$.subscribe(data => {
    // Update the local variable when data changes
    this.ChatRoomSOrCoworkers = data;



  });

}



}
