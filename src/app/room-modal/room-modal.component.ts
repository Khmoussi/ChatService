import { Coworker } from './../Model/Coworker';
import { SharedDataService } from './../Service/shared-data.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field'; // Import MatFormFieldModule
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ChatRoom } from '../Model/ChatRoom';
import { ChatroomServiceService } from '../Service/chatroom-service.service';
import { ApiService } from '../Service/api.service';
import { MatInputModule } from '@angular/material/input';


@Component({
  selector: 'app-room-modal',
  standalone: true,
  imports: [CommonModule,MatFormFieldModule,FormsModule,MatInputModule],
  templateUrl: './room-modal.component.html',
  styleUrl: './room-modal.component.css'
})
export class RoomModalComponent {
  roomName:string="";

  constructor(private sharedDataService:SharedDataService,
    private apiService :ApiService,private chatRoomService :ChatroomServiceService, public dialogRef: MatDialogRef<RoomModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onAddRoomClick(roomName: string): void {


    this.apiService.createRoom(roomName).then((room) => {
      console.log("aaaaaaaaaaaaaaaaaaaaaaaaaaaa "+room)
      let createdRoomId=this.apiService.createdRoom;
      let list=this.chatRoomService.chatroomList.value;
      if(createdRoomId!=""){
    //  let room=new ChatRoom(roomName,[],createdRoomId,this.apiService.email,[]);

    list.unshift(room);

      this.chatRoomService.currentChatroom.next(room);
      this.chatRoomService.chatroomList.next(list);
      this.apiService.subscribeTotopic(this.apiService.createdRoom)
      }else{throw new Error("room wasn't created or id not retreived in front")}
    }).catch(()=>{
      console.log("room cannot be created")})

    // Add logic to submit room name
    this.dialogRef.close(roomName);
  }
}
