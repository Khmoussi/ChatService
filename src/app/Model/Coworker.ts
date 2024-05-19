import { Message } from "./Message";
import { MessageResponse } from "./MessageResponse";
import {OneMessageResponse } from "./OneMessageResponse";

export class Coworker {

  private firstname: string;
  private lastname: string;
  private email: string;
  private messagesList?: OneMessageResponse[];
  private photoUrl?: string;


  constructor(firstname: string, lastname: string, email: string, messagesList?: OneMessageResponse[],photoUrl?: string) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.messagesList = messagesList;
    this.photoUrl = photoUrl;

  }
  get PhotoUrl(): string|undefined {
    return this.photoUrl;
  }
  set PhotoUrl(value: string) {
    this.PhotoUrl = value;
  }
  // Getter and Setter for 'firstname'
  get Firstname(): string {
    return this.firstname;
  }
  set Firstname(value: string) {
    this.firstname = value;
  }

  // Getter and Setter for 'lastname'
  get Lastname(): string {
    return this.lastname;
  }
  set Lastname(value: string) {
    this.lastname = value;
  }

  // Getter and Setter for 'email'
  get Email(): string {
    return this.email;
  }
  set Email(value: string) {
    this.email = value;
  }




  // Getter and Setter for 'messagesList'
  get MessagesList(): OneMessageResponse[] | undefined {
    return this.messagesList;
  }
  set MessagesList(value: OneMessageResponse[] | undefined) {
    this.messagesList = value;
  }
}
