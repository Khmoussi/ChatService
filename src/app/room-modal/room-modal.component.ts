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

  constructor(
    private apiService :ApiService,private chatRoomService :ChatroomServiceService, public dialogRef: MatDialogRef<RoomModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onAddRoomClick(roomName: string): void {
    let room=new ChatRoom(roomName,[],"",this.apiService.email);

    this.apiService.createRoom(roomName).then(() => {
      this.chatRoomService.currentChatroom.next(room);
      this.chatRoomService.chatroomList.value.push(room);
    }).catch(()=>{
      console.log("room cannot be created")})

    // Add logic to submit room name
    this.dialogRef.close(roomName);
  }
}
