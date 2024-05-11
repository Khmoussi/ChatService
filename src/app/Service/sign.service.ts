import { BehaviorSubject, Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { Coworker } from '../Model/Coworker';

@Injectable({
  providedIn: 'root'
})
export class SignService {
  coockieValue :string[]=[];
  userId="4";






  constructor(private cookieService:CookieService) { }


  verifyLogin(email: string,  password: string):boolean{
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const phoneRegex = /^\d{8,}$/;
    if ( !email || !password ) {
      console.error("All fields must be filled in.");
      return false;
    }
    if (!email.match(emailRegex) && !email.match(phoneRegex)) {
      console.error("Email or phone is not valid.");
      return false;
    }
  //creating coockie
  this.coockieValue.push(email,password);

  this.cookieService.set('user',this.coockieValue.join(','),7);
    return true;

  }

  verifySignUp( firstname: string,  lastname: string,  email: string,  password: string,  confirmPassword: string): boolean {
    // Add your verification logic here
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const phoneRegex = /^\d{8,}$/;

    if (!firstname || !lastname || !email || !password || !confirmPassword) {
      console.error("All fields must be filled in.");
      return false;
    }

    if (password !== confirmPassword) {
      console.error("Password and confirm password do not match.");
      return false;
    }

    if (!email.match(emailRegex) && !email.match(phoneRegex)) {
      console.error("Email or phone is not valid.");
      return false;
    }

    // Additional verification logic can be added based on your requirements

    // If no issues are found, you can proceed with sign-up logic or return true, etc.

  //creating coockie
this.coockieValue.push(email,password,this.userId);

    this.cookieService.set('user',this.coockieValue.join(','),7);
    console.log("Sign-up verification passed. Proceed with sign-up logic.");
return true;
  }

  signOut():void{
    this.cookieService.set('user'," ",-1);

  }






}
