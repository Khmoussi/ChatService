import { Component } from '@angular/core';
import { ApiService } from './../Service/api.service';
import { SharedDataService } from './../Service/shared-data.service';
import { CommonModule } from '@angular/common';
import { ChatRoomsListComponent } from '../chat-rooms-list/chat-rooms-list.component';
import { CoworkersListComponent } from '../coworkers-list/coworkers-list.component';
import { SignService } from '../Service/sign.service';
import { Router } from '@angular/router';
import { ChatRoom } from '../Model/ChatRoom';
import { ChatroomServiceService } from '../Service/chatroom-service.service';
import { RoomModalComponent } from '../room-modal/room-modal.component';
import { MatDialog } from '@angular/material/dialog';
import { CoworkerService } from '../Service/coworker.service';
import { Coworker } from '../Model/Coworker';
import {  OnInit } from '@angular/core';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {
  user :Coworker| null=null;

  constructor(private sharedDataService :SharedDataService,private signService :SignService,private router:Router , private apiService :ApiService,private chatRoomService :ChatroomServiceService,private coworkerService:CoworkerService) {
  }
  ngOnInit(): void {

this.user=this.apiService.user  }


  logout():void{
    //this.signService.signOut();
    this.apiService.resetData();
    this.chatRoomService.resetData();
    this.sharedDataService.resetData();
    this.coworkerService.resetData();
    this.router.navigate(['login-component']);
  }
  showEdit() {
this.sharedDataService.toggleEdit.next(!this.sharedDataService.toggleEdit.value);
  }

}
