import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { CookieService } from 'ngx-cookie-service';

import { CoworkerService } from './../Service/coworker.service';
import { SharedDataService } from '../Service/shared-data.service';
import { ApiService } from '../Service/api.service';
import { ChatroomServiceService } from '../Service/chatroom-service.service';

import { Coworker } from './../Model/Coworker';
import { MessageResponse } from './../Model/MessageResponse';
import { ChatRoom } from '../Model/ChatRoom';

@Component({
  selector: 'app-chat-group',
  standalone: true,
  imports: [CommonModule, FormsModule, MatListModule, MatIconModule],
  templateUrl: './chat-group.component.html',
  styleUrls: ['./chat-group.component.css']
})
export class ChatGroupComponent implements OnInit {
  userId: number | undefined;
  messageList: MessageResponse[] = [];
  message: string = "";
  currentChatRoom: ChatRoom | null = null;
  currentCoworker: Coworker | null = null;
  showList: boolean = false;
  coworkerList: Coworker[] = [];
  showRoomUsers: boolean = false;
  currentUserEmail: string = "";
  usersNotInCurrentRoom: Coworker[] = [];

  constructor(
    private sharedDataService: SharedDataService,
    private apiService: ApiService,
    private cookieService: CookieService,
    private chatroomService: ChatroomServiceService,
    private coworkerService: CoworkerService
  ) {
    const username = this.cookieService.get("username");
    if (username) {
      this.userId = parseInt(username.split(',')[2]);
    }
  }

  ngOnInit() {
    console.log("onInit chat group");

    this.chatroomService.currentChatroom$.subscribe(chatRoom => {
      this.currentChatRoom = chatRoom!;
      if (this.currentChatRoom) {
        this.messageList = this.currentChatRoom.chatroomMessages;
        this.calculateUsersNotInCurrentRoom();
        console.log("current Room messages: ", this.currentChatRoom.chatroomMessages);
      }
    });

    this.sharedDataService.coworkerList.subscribe(data => {
      this.coworkerList = data;
      this.calculateUsersNotInCurrentRoom();
    });

    this.coworkerService.coworkerList.subscribe(data => {
      this.coworkerList = data;
      this.calculateUsersNotInCurrentRoom();
    });

    this.currentChatRoom = this.sharedDataService.currentChatroom.value;
    if (this.currentChatRoom) {
      this.messageList = this.currentChatRoom.chatroomMessages;
    }
    this.currentCoworker = this.apiService.coworkerSubj.value;
    this.currentUserEmail = this.apiService.email!;
    console.log("Current user: " + this.currentUserEmail);
  }

  addUser() {
    throw new Error('Method not implemented.');
  }

  sendMessage() {
    const roomId = this.currentChatRoom?.id as string;
    if (roomId) {
      this.apiService.send(roomId, {}, this.message);
      console.log("Sent message: " + this.message);
      this.message = "";
    }
  }

  subscribeToRoomById(roomId: string) {
    this.apiService.subscribeTotopic(roomId);
  }

  toggleList() {
    this.showList = !this.showList;
    this.showRoomUsers = false;
  }

  toggleRemoveList() {
    this.showRoomUsers = !this.showRoomUsers;
    this.showList = false;
  }

  closeList() {
    this.showList = false;
  }

  closeRoomUsersList() {
    this.showRoomUsers = false;
  }

  itemClicked(coworker: Coworker) {
    this.closeList();
    this.apiService.addCoworkerToChatroom(coworker.Email, Number.parseInt(this.currentChatRoom?.id!))
      .then(() => {
        console.log("User added to chat room: " + coworker.Email);
      })
      .catch(error => {
        console.log(error);
      });
  }

  itemClickedRemovedUser(coworker: Coworker) {
    this.closeList();
    this.apiService.removeUserFromRoom(coworker.Email, this.currentChatRoom?.id!)
      .then(() => {
        console.log("User removed from chat room: " + coworker.Email);
      })
      .catch(error => {
        console.log(error);
      });
  }

  calculateUsersNotInCurrentRoom(): void {
    this.usersNotInCurrentRoom = this.coworkerList.filter(user => this.userNotInList(user));
  }

  userNotInList(user: Coworker): boolean {
    if (!this.currentChatRoom || !this.currentChatRoom._userResponses) {
      return true;
    }
    for (const coworker of this.currentChatRoom._userResponses) {
      if (coworker.Email === user.Email) {
        return false;
      }
    }
    return true;
  }
}
