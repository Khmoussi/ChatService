import { CookieService } from 'ngx-cookie-service';
import { SharedDataService } from './../Service/shared-data.service';
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Message } from '../Model/Message';
import { Coworker } from '../Model/Coworker';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../Service/api.service';
import { OneMessageResponse } from '../Model/OneMessageResponse';
import { CoworkerService } from '../Service/coworker.service';
@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.css'
})
export class ChatComponent implements OnInit{
  messageList: OneMessageResponse[]=[];
  currentCoworker:Coworker|null=null;
    message:string="";
    userEmail:string="";
  constructor(private sharedDataService : SharedDataService,private coockieService:CookieService,private apiService:ApiService,private coworkerService:CoworkerService){



  }
  ngOnInit() {
this.userEmail=this.apiService.email!;
    // Subscribe to changes in the shared data

    this.sharedDataService.currentCoworker$.subscribe(data => {
      // Update the local variable when data changes
      //code gets excuted even with no change

      this.currentCoworker=data;
      this.messageList = data.MessagesList!;
      console.log(data.Firstname+" "+data.MessagesList?.length);
    });
    this.coworkerService.currentCoworker$.subscribe(data => {
      console.log("currentcworker changed")
   this.currentCoworker=data;
   this.messageList!=this.currentCoworker?.MessagesList;

    });

  }
  sendMessage(){

    this.apiService.sendToUser({"receiverId":this.currentCoworker?.Email},this.message);
    this.message="";

  }



}
