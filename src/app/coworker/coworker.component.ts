import { CoworkersListComponent } from './../coworkers-list/coworkers-list.component';
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Coworker } from '../Model/Coworker';
import { ApiService } from '../Service/api.service';
import {Message} from '../Model/Message'
import { SharedDataService } from '../Service/shared-data.service';
import { CoworkerService } from '../Service/coworker.service';


@Component({
  selector: 'app-coworker',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './coworker.component.html',
  styleUrl: './coworker.component.css'
})
export class CoworkerComponent {
  coworkerList:Coworker[]=[];
  isHighlighted:boolean =false;
 clickedItemIndex:number =0;
 constructor(private apiService:ApiService , private sharedDataService:SharedDataService,private coworkerService:CoworkerService ){

   this.sharedDataService.coworkerList.subscribe(data =>{
this.coworkerList=data;
   })
   this.coworkerService.coworkerList.subscribe(data =>{
    this.coworkerList=data;
    this.coworkerService.currentCoworker.next(this.coworkerList[this.clickedItemIndex])

       })
 }
 bringMessages(i:number){
  //sending messages to chat or making it static
//this.sharedDataService.updateChat(this.coworkerList[i].MessagesList);

this.sharedDataService.updateCurrentCoworker(this.coworkerList[i]);

this.isHighlighted=true;
this.clickedItemIndex=i;


 }
}
