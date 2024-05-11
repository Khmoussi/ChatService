import { UserRequest } from './../Model/UserRequest';
import { ApiService } from './../Service/api.service';
import { SignService } from './../Service/sign.service';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { RouterLinkActive } from '@angular/router';
import { RouterOutlet } from '@angular/router';
import { LoginComponent } from '../login/login.component';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';


@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [RouterLink,RouterLinkActive,RouterOutlet,LoginComponent,FormsModule],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {
  firstname="";
  lastname="";
  email="";
  password="";
  confirmPassword="";
  constructor(private signService : SignService,private router:Router,private apiService:ApiService){
  }

  verifySignUp():void{
    if(this.signService.verifySignUp(this.firstname,this.lastname,this.email,this.password,this.confirmPassword) ){

      this.apiService.signUp(new UserRequest(this.email,this.firstname, this.lastname,  this.password))
      .then(() => {
          console.log("Sign up successful!");
          this.router.navigate(["main-component"]);
          // Handle success
      })
      .catch(error => {
          console.error("Sign up failed:", error);
          // Handle error
      });
    }


  }


}
