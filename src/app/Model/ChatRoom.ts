import { Coworker } from './Coworker';
import { MessageResponse } from './MessageResponse';

export class ChatRoom {
  private _roomName: string;
  private _id?: string; // Renamed property to avoid conflict
  private _admin?: string; // Renamed property to avoid conflict
  public chatroomMessages: MessageResponse[];
  public userResponses:Coworker[];

  constructor(
    name: string,
    messageList: MessageResponse[] = [],
    roomId?: string,
    adminId?: string,
    userResponses:Coworker[]=[]
  ) {
    this._roomName = name;
    this.chatroomMessages = messageList;
    this._id = roomId; // Assign to renamed property
    this._admin = adminId; // Assign to renamed property
    this.userResponses=userResponses;
  }

  // Getter and Setter for 'roomName'
  get roomName(): string {
    return this._roomName;
  }
  get _userResponses(){
    return this.userResponses;
  }
  set _userResponses(value:Coworker[]){
    this.userResponses=value;
  }

  set roomName(value: string) {
    this._roomName = value;
  }

  // Getter and Setter for 'id'
  get id(): string | undefined {
    return this._id;
  }

  set id(value: string | undefined) {
    this._id = value;
  }

  // Getter and Setter for 'admin'
  get admin(): string | undefined {
    return this._admin;
  }

  set admin(value: string | undefined) {
    this._admin = value;
  }
}
