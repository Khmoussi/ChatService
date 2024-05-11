import { ApiService } from './../Service/api.service';
import { SharedDataService } from './../Service/shared-data.service';
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChatRoomsListComponent } from '../chat-rooms-list/chat-rooms-list.component';
import { CoworkersListComponent } from '../coworkers-list/coworkers-list.component';
import { SignService } from '../Service/sign.service';
import { Router } from '@angular/router';
import { ChatRoom } from '../Model/ChatRoom';
import { ChatroomServiceService } from '../Service/chatroom-service.service';
import { RoomModalComponent } from '../room-modal/room-modal.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-navigation',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './navigation.component.html',
  styleUrl: './navigation.component.css'
})
export class NavigationComponent {

  constructor(public dialog: MatDialog,private sharedDataService :SharedDataService,private signService :SignService,private router:Router , private apiService :ApiService,private chatRoomService :ChatroomServiceService){

  }


navigateToGroupChat():void{
this.sharedDataService.updateChatRoomSOrCoworkers(false)
}
navigateToCoworkerList():void{
  this.sharedDataService.updateChatRoomSOrCoworkers(true)
  }
  logout():void{
    //this.signService.signOut();
    this.apiService.logout();
    this.router.navigate(['login-component']);
  }
  createRoom() {


  }
  openRoomModal(): void {
    const dialogRef = this.dialog.open(RoomModalComponent, {
      width: '250px',
      data: {}
    });

    dialogRef.afterClosed().subscribe(roomName => {
      console.log('The dialog was closed');
      if (roomName) {

        // Handle room addition here
        console.log('Room added:', roomName);
      }
    });
  }
}
