import { ApiService } from './../Service/api.service';
import { SignService } from './../Service/sign.service';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RouterLink } from '@angular/router';
import { RouterLinkActive } from '@angular/router';
import { RouterOutlet } from '@angular/router';
import { SignupComponent } from '../signup/signup.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LoginRequest } from '../Model/LoginRequest';
import { UserRequest } from '../Model/UserRequest';
import SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [RouterLink,RouterLinkActive,RouterOutlet,SignupComponent,FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  email:string="";
  password:string="";
  socket = new SockJS('http://localhost:9000/messenger');
  stompClient=Stomp.over(this.socket);
  constructor(private apiService: ApiService,private signService: SignService ,private router:Router){

  }
   ngOnInit(){



    this.verifyLogin();


}
   async verifyLogin(){

      await this.apiService.login(new LoginRequest("khmoussiaouina@gmail.com", "123"))  .then((result) => {
        console.log("Login  successful!");

if(result)
        this.router.navigate(["main-component"]);
        // Handle success
    })
    .catch(error => {
        console.error("Loginfailed:", error);
        // Handle error
    });


 // console.log("session id after loging :" + this.apiService.sessionId)

  }


}



