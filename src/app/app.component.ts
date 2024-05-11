import { NavigationComponent } from './navigation/navigation.component';
import { CoworkerComponent } from './coworker/coworker.component';
import { CoworkersListComponent } from './coworkers-list/coworkers-list.component';
import { ChatComponent } from './chat/chat.component';
import { MainComponent } from './main/main.component';
import { LoginComponent } from './login/login.component';
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { Router } from '@angular/router';
import { ApiService } from './Service/api.service';
import { HttpClient } from '@angular/common/http';
import { UserRequest } from './Model/UserRequest';
import { LoginRequest } from './Model/LoginRequest';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule,LoginComponent, RouterOutlet,MainComponent,ChatComponent,CoworkersListComponent,CoworkerComponent,NavigationComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {

  constructor(private router:Router,private apiService:ApiService ,private http:HttpClient){


  }

   ngOnInit(){

    this.router.navigate(["login-component"]);




}
  title = 'Messenger';
  callback():void{
    console.log("subscribing from main page");
      }
}
