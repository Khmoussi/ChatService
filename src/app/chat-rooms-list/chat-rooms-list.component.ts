import { Component, OnInit } from '@angular/core';
import { ChatroomComponent } from '../chatroom/chatroom.component';
import { ApiService } from '../Service/api.service';

@Component({
  selector: 'app-chat-rooms-list',
  standalone: true,
  imports: [ChatroomComponent],
  templateUrl: './chat-rooms-list.component.html',
  styleUrl: './chat-rooms-list.component.css'
})
export class ChatRoomsListComponent implements OnInit {
constructor(private apiService:ApiService){

}
ngOnInit(): void {

}


}
