import { CoworkerService } from './coworker.service';
import { ChatroomServiceService } from './chatroom-service.service';
import { SharedDataService } from './shared-data.service';
import { Coworker } from './../Model/Coworker';
import { UserRequest } from './../Model/UserRequest';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Message } from '../Model/Message';
import { ChatRoom } from '../Model/ChatRoom';
import SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import { BehaviorSubject, Observable, Subscription, map, tap } from 'rxjs';
import { LoginRequest } from '../Model/LoginRequest';
import { error } from 'console';
import axios from 'axios';
import qs from 'qs';
import { LoginResponse } from '../Model/LoginResponse';
import { CookieService } from 'ngx-cookie-service';
import { MessageResponse } from '../Model/MessageResponse';
import { OneMessageResponse } from '../Model/OneMessageResponse';





@Injectable({
  providedIn: 'root'
})
export class ApiService {

  public accessToken : string|undefined;
  public firstname :string | undefined;
  public lastname :string | undefined;
  public email:string|undefined;
  socket = new SockJS('http://localhost:9000/messenger');
  public stompClient=Stomp.over(this.socket);
  connected: boolean=false;
 public getCoworkers:Coworker[];
 getChatrooms:ChatRoom[];
 // loggedin user
 public coworkerSubj:BehaviorSubject<Coworker|null>=new BehaviorSubject<Coworker |null>(null);
 public coworker$=this.coworkerSubj.asObservable();
 public isLoggedIn$: Observable<boolean>;
public isLoggedOut$: Observable<boolean>;
public sessionId :string =" aa";
public response:string="";
public createdRoom="";
public subsMap = new Map();



  constructor(private http:HttpClient ,private cookieService:CookieService,private chatroomService :ChatroomServiceService,private coworkerService:CoworkerService) {


    this.isLoggedIn$=this.coworker$.pipe(map(coworker=>!!coworker))
    this.isLoggedOut$=this.isLoggedIn$.pipe(map(loggedIn=> !loggedIn))
    this.getCoworkers = [];
    this.getChatrooms= [];
    /*  this.getCoworkers = [
        new Coworker("user1", "aouina", "aouina@gmail.com", [
            new OneMessageResponse(1, new Date(), "sent", "hello brother", "aouina@gmail.com", "2aouina@gmail.com"),
            new OneMessageResponse(2, new Date(), "sent", "hello daniel", "2aouina@gmail.com", "aouina@gmail.com"),
            new OneMessageResponse(3, new Date(), "sent", "how u doing", "aouina@gmail.com", "2aouina@gmail.com")
        ]),
        new Coworker("user2", "aouina", "aouina@gmail.com", [
            new OneMessageResponse(1, new Date(), "sent", "hello brother", "aouina@gmail.com", "2aouina@gmail.com"),
            new OneMessageResponse(2, new Date(), "sent", "wakawaka", "2aouina@gmail.com", "aouina@gmail.com"),
            new OneMessageResponse(3, new Date(), "sent", "nyess", "aouina@gmail.com", "2aouina@gmail.com")
        ]),
        new Coworker("user3", "aouina", "aouina@gmail.com", [
            new OneMessageResponse(1, new Date(), "sent", "hello brother", "aouina@gmail.com", "2aouina@gmail.com"),
            new OneMessageResponse(2, new Date(), "sent", "hello daniel", "2aouina@gmail.com", "aouina@gmail.com"),
            new OneMessageResponse(3, new Date(), "sent", "how u doing", "aouina@gmail.com", "2aouina@gmail.com")
        ]),
        new Coworker("user4", "aouina", "aouina@gmail.com", [
            new OneMessageResponse(1, new Date(), "sent", "hello brother", "khmoussiaouina@gmail.com", "aouina@gmail.com"),
            new OneMessageResponse(2, new Date(), "sent", "hello daniel", "2aouina@gmail.com", "aouina@gmail.com"),
            new OneMessageResponse(3, new Date(), "sent", "how u doing", "aouina@gmail.com", "2aouina@gmail.com")
        ])
    ];

    this.getChatrooms= [
      new ChatRoom('General', [
          new MessageResponse(1, new Date(), 'text', 'sender1', 1, 'Welcome to the chatroom!'),
          new MessageResponse(2, new Date(), 'text', 'sender2', 1, 'Feel free to ask any questions.')
      ], 'room1', 'admin1'),
      new ChatRoom('Random', [
          new MessageResponse(1, new Date(), 'text', 'sender1', 2, 'Let\'s discuss the project.'),
          new MessageResponse(2, new Date(), 'text', 'sender3', 2, 'Does anyone have any suggestions?')
      ], 'room2', 'admin2')
  ];
  */
  }
  //hello
  hello(): void {
    // Set the session cookie in the request headers
    console.log("session from hello"+" "+this.sessionId);
    const headers = new HttpHeaders().set('Cookie', 'JSESSIONID=MDliNjNkZWItZDQ3Ny00MTU1LTk2YmEtY2MxODA0ZDI2NDBh');

    // Make the HTTP GET request with the provided session cookie
    this.http.get('http://localhost:9000/hello', { headers , responseType : 'text' }).subscribe(result => {
      // Handle the response from the hello endpoint
      console.log("hello function result " +result);
    },
    (error)=>{
      console.error('Error occurred:', error);

    }
    )
  }
  //hello2
  hello2(): void {
    this.http.get('http://localhost:9000/hello2', { responseType: 'text' }).subscribe(
      (result: string) => {
        console.log(result);
      },
      (error) => {
        console.error('Error occurred:', error);
      }
    );
  }
  //signup
  async signUp(userRequest: UserRequest): Promise<void> {
    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    try {
        const raw = JSON.stringify({
            "email": userRequest.email,
            "firstName": userRequest.firstName,
            "lastName": userRequest.lastName,
            "password": userRequest.password
        });

        const requestOptions = {
            method: "POST",
            headers: myHeaders,
            body: raw
        };

        const response = await fetch("http://localhost:9000/api/v1/auth/signUp", requestOptions);

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const result = await response.json();
        console.log(result);
        this.accessToken=result.accessToken;
        let coworker =new Coworker(result.firstname,result.lastname,result.email);
        this.coworkerSubj.next(coworker);
        // Handle result if needed
    } catch (error) {
        console.error(error);
        // Handle errors, display error messages, etc.
    }
}


  async login(loginRequest: LoginRequest): Promise<boolean> {
    try {
        const raw = JSON.stringify({
            "email": loginRequest.Email,
            "password": loginRequest.Password
        });

        const requestOptions = {
            method: "POST",
            body: raw,
            headers: {
                "Content-Type": "application/json" // Specify content type
            }
        };

        const response = await fetch("http://localhost:9000/api/v1/auth/login", requestOptions);

        if (!response.ok) {
          return false;
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const result = await response.json();
        this.accessToken=result.accessToken;
        this.email=loginRequest.Email;

        console.log(result);
        // Process the result further if needed
        return true;

    } catch (error) {
        console.error(error);
        // Handle errors, display error messages, etc.
        return false;

    }
}

  // getCoworkers
  async fetchCoworkers():Promise<void>{
    const myHeaders = new Headers();

    myHeaders.append("Authorization", "Bearer "+this.accessToken);
    console.log("token when fetchin "+ this.accessToken)

    const requestOptions = {
      method: "GET",
      headers: myHeaders

    };

    const response =await fetch("http://localhost:9000/chat/getUsers", requestOptions);
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
  }

  const result = await response.json();
this.getCoworkers =result.map((result:any) => new Coworker(result.firstName,result.lastName,result.email,result.messages,result.photoUrl));
this.coworkerService.currentCoworker.next(this.getCoworkers[0]);
console.log("fetched coworkers List " + this.getCoworkers[0].PhotoUrl);

  }



  //getUserChatrooms
  async getUserChatrooms():Promise<void>{
    const myHeaders = new Headers();
    myHeaders.append("Authorization", "Bearer "+this.accessToken);

const requestOptions = {
  method: "GET",
  headers: myHeaders
};

const response =await fetch("http://localhost:9000/chat/getUserChatrooms", requestOptions)
if(!response.ok){
  throw new Error(`HTTP error! Status: ${response.status}`);

}
const result= await response.json();
console.log("feteched chatrooms +" +result.chatroomMessages)

this.getChatrooms=result.map((result:any) => new ChatRoom( result.roomName,result.chatroomMessages,result.id as string,result.admin,result.userResponses.map((result:any) =>new Coworker(result.firstName,result.lastName,result.email))));
console.log("feteched wiw +" +this.getChatrooms[0].userResponses?.[0].Email)


this.getChatrooms.forEach((room)=>{
  console.log(room._userResponses);
})

  }
  //logout
  logout() :void{
    this.http.get('http://localhost:9000/hello2', { responseType: 'text' }).subscribe(
  (result:string) => {
    console.log(result);

  },
  (error)=>{
    console.log(error);
  });
  }


//creating new room
async createRoom(roomName:string):Promise<ChatRoom>{

  const myHeaders = new Headers();
myHeaders.append("Content-Type", "application/json");
myHeaders.append("Authorization", "Bearer "+this.accessToken);

const raw = JSON.stringify({
  "name": roomName
});

const requestOptions = {
  method: "POST",
  headers: myHeaders,
  body: raw,
};

const response=await fetch("http://localhost:9000/chat/createChatroom", requestOptions)
if(!response.ok){
  throw new Error(`HTTP error! Status: ${response.status}`);

}
const result= await response.json();
console.log("new room created " + result.id);
let room=new ChatRoom(result.roomName,result.chatroomMessages,result.id,result.admin,result.userResponses);
this.createdRoom=result.id as string
return room;
}

//add user to chatroom
async addCoworkerToChatroom(addedCoworker:string ,roomId :number):Promise<void>{

  const myHeaders = new Headers();
  myHeaders.append("Authorization", "Bearer "+this.accessToken);
  myHeaders.append("Content-Type", "application/json");

const raw = JSON.stringify({
  "addedUserId": addedCoworker,
  "chatRoomId": roomId
});

const requestOptions = {
  method: "POST",
  headers: myHeaders,
  body: raw,
};


const response =await fetch("http://localhost:9000/chat/addUserToChatroom", requestOptions);
if (!response.ok) {
  console.log(response.text());

  throw new Error(`HTTP error! Status: ${response.status}`);
}

const result = await response.text();
console.log("added user response "+ result);

}

// remove user from chatroom
async removeUserFromRoom(removedUserId:string,chatRoomId :string):Promise<void>{

  const myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", "Bearer "+this.accessToken);

  const raw = JSON.stringify({
    "removedUserId": removedUserId,
    "chatRoomId": Number.parseInt(chatRoomId)
  });

  const requestOptions = {
    method: "DELETE",
    headers: myHeaders,
    body: raw
  };

  const response =await fetch("http://localhost:9000/chat/delete/removeUserFromRoom", requestOptions);
  if(!response.ok){
    console.log(response.text());

  throw new Error(`HTTP error! Status: ${response.status}`);
  }
  const result = await response.text();
console.log("removed user response "+ result);


}



//uploading photo
async upload(file: File): Promise<boolean> {
try{
  const myHeaders = new Headers();
  myHeaders.append("Authorization", "Bearer "+this.accessToken);
const formdata = new FormData();
formdata.append("file", file);

const requestOptions = {
  method: "POST",
  headers: myHeaders,
  body: formdata,
};

const response =await fetch("http://localhost:9000/file/", requestOptions)

if(!response.ok){
  throw new Error(`HTTP error! Status: ${response.status}`);

}
const result= await response.json();
return result;
}catch(error){
  console.log(error);
  return false;
}

}
// getFiles
getFiles(){

}
//




  //websockest

//connecting to websocket
initializeConnection():void{
  this.stompClient.connect({token:"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJraG1vdXNzaWFvdWluYUBnbWFpbC5jb20iLCJleHAiOjE3MTU3MTI3MjAsImlhdCI6MTcxNDg0ODcyMH0.qtq8I9v8g0tCvqs7OxLNqWNWPJ9EDKL0M6D94OcniX0"},(): any=>{
    console.log("connected now")

  });
}

  //subscribing to websocket


subscribe(topic:string):void{
   this.connected =this.stompClient.connected;
  if(this.connected){
    console.log(" already connected")
    this.subscribeTotopic(topic);
    return;
  }
  this.stompClient.connect({},(): any=>{
    console.log("connected now")

    this.subscribeTotopic(topic);
  });
}

//send message to destiniation
send(destiniation:string,headers={},message:string):any{
  if(this.stompClient.connected){
  this.stompClient.send("/app/"+destiniation,headers,message);
  }else
console.log("can send because not connected");
}
sendToUser(headers:any,message:string):any{
  if(this.stompClient.connected)
    {

  this.stompClient.send("/app/chat",headers,message);
  console.log("send to user called");

  let m=new OneMessageResponse(0,new Date(),null,message,this.email!,this.coworkerService.currentCoworker.value?.Email!);
  let list=this.coworkerService.coworkerList.value;
  console.log("list list "+list.length)
  list.forEach(coworker=>{
    if((coworker.Email=== headers.receiverId ) )
      {
        coworker.MessagesList?.push(m);
        this.coworkerService.coworkerList.next(list);
      }
  })

    }else
  console.log("can send because not connected");

}
public subscribeTotopic(topic:string):void{
  if(this.stompClient.connected){
 let subscription=this.stompClient.subscribe("/topic/"+topic,((message: Stomp.Message) => {
 let m=JSON.parse(message.body);
 console.log("receive Message"+" " +m.content );

 let msg=new MessageResponse(0,new Date(),m.messageType,m.senderId,m.sendDate,m.content);
 console.log(" Message Type1"+" " +msg.messageType );

 if(msg.messageType=="join"){
  console.log(" Message Type2"+" " +m.messageType );

let room=this.chatroomService.currentChatroom.value;
if(room){
room.userResponses.push(new Coworker(m.userResponse.firstName,m.userResponse.lastName,m.userResponse.email));
room._userResponses.forEach((coworker:Coworker)=>{
console.log("new room coworker "+coworker.Email);
})
this.chatroomService.currentChatroom.next(room);
}

}
if(msg.messageType=="Leave"){
  console.log(" Message Type2"+" " +m.messageType );

let room=this.chatroomService.currentChatroom.value;
if(room){
  let removed=new Coworker(m.userResponse.firstName,m.userResponse.lastName,m.userResponse.email)
  room.userResponses=room.userResponses.filter(coworker =>coworker.Email!==removed.Email);
room.userResponses.forEach((coworker:Coworker)=>{
console.log("removed from  room coworker "+coworker.Email +"       "+removed.Email);
})
this.chatroomService.currentChatroom.next(room);
}

}
let list=this.chatroomService.chatroomList.value;
console.log("list list "+list.length)
list.forEach((chatroom,index)=>{
  console.log("comaprison "+chatroom.id +" "+(m.roomId as string))

  if(chatroom.id== (m.roomId as string))
    {
      chatroom.chatroomMessages.push(msg);
      list.splice(index,1);
      list.unshift(chatroom);
      this.chatroomService.chatroomList.next(list);
    }
})

}
 ))

 this.subsMap.set(topic,subscription);

}
}
public subscribeToUser(){
this.stompClient.subscribe("/user/queue/hello",((message: Stomp.Message) => {
  let list=this.coworkerService.coworkerList.value;
  console.log("list list "+list.length)
  let m=JSON.parse(message.body);
  list.forEach((coworker,index)=>{
    if((coworker.Email=== m.senderId ) )
      {
        coworker.MessagesList?.push(m);
        list.splice(index,1);
        list.unshift(coworker);

        this.coworkerService.coworkerList.next(list);

      }
  })
}));
}
public subscribeToNewUser():void{
  this.stompClient.subscribe("/topic/NewUser",(message:Stomp.Message)=>{
    let m=JSON.parse(message.body);
let list=this.coworkerService.coworkerList.value;
let addedUser=new Coworker(m.firstName,m.lastName,m.email,m.messages,m.photoUrl);
list.push(addedUser);
console.log("added user " +addedUser);
this.coworkerService.coworkerList.next(list);
  });
}
public subscribeToAddedToRoom():void{
  this.stompClient.subscribe("/user/queue/addUserToRoom",((message: Stomp.Message) => {

    let m=JSON.parse(message.body) as ChatRoom;
   let list= this.chatroomService.chatroomList.value;
   list.push(m);
   this.chatroomService.chatroomList.next(list);
    this.subscribeTotopic(m.id as string);
    if(list.length==1){
      this.chatroomService.currentChatroom.next(list[0]);

    }

  }));}

  public subscribeRemovedFromRoom():void{
    this.stompClient.subscribe("/user/queue/removedUserFromRoom",((message: Stomp.Message) => {

      let roomId=JSON.parse(message.body) ;
      roomId=roomId as string;
     let list= this.chatroomService.chatroomList.value;
     list = list.filter(room => room.id !== roomId);

     this.chatroomService.chatroomList.next(list);

      console.log("removed From  room "+roomId);
//unsubscribe
console.log("unsubscribe "+this.subsMap.get(roomId));
this.subsMap.get(roomId).unsubscribe();
this.subsMap.delete(roomId);

if(this.chatroomService.currentChatroom.value?.id==roomId)
  this.chatroomService.currentChatroom.next(this.chatroomService.chatroomList.value[0]);
    }));}
    resetData() {
      this.accessToken = undefined;
      this.firstname = undefined;
      this.lastname = undefined;
      this.email = undefined;
      this.getCoworkers = [];
      this.getChatrooms = [];
      this.coworkerSubj.next(null);

    }

}
