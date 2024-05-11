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
import { BehaviorSubject, Observable, map, tap } from 'rxjs';
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



  constructor(private http:HttpClient ,private cookieService:CookieService,private chatroomService :ChatroomServiceService,private coworkerService:CoworkerService) {


    this.isLoggedIn$=this.coworker$.pipe(map(coworker=>!!coworker))
    this.isLoggedOut$=this.isLoggedIn$.pipe(map(loggedIn=> !loggedIn))
      this.getCoworkers = [
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


  async login(loginRequest: LoginRequest): Promise<void> {
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
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const result = await response.json();
        this.accessToken=result.accessToken;
        this.email=loginRequest.Email;

        console.log(result);
        // Process the result further if needed
    } catch (error) {
        console.error(error);
        // Handle errors, display error messages, etc.
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
this.getCoworkers =result.map((result:any) => new Coworker(result.firstName,result.lastName,result.email,result.messages));
this.coworkerService.currentCoworker.next(this.getCoworkers[0]);
console.log("fetched coworkers List " + this.getCoworkers[0].MessagesList);

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
console.log("feteched chatrooms +" +result)
this.getChatrooms=result;
console.log("feteched wiw +" +this.getChatrooms)


this.getChatrooms.forEach((room)=>{
  console.log(room);
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
async createRoom(roomName:string):Promise<void>{

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

}



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
 this.stompClient.subscribe("/topic/"+topic,((message: Stomp.Message) => {
 let m=JSON.parse(message.body);
 console.log("receive Message"+" " +m.content );
 let msg=new MessageResponse(0,new Date(),m.senderId,m.messageType,m.sendDate,m.content);
let list=this.chatroomService.chatroomList.value;
console.log("list list "+list.length)
list.forEach(chatroom=>{
  console.log("comaprison "+chatroom.id +" "+(m.roomId as string))

  if(chatroom.id== (m.roomId as string))
    {
      chatroom.chatroomMessages.push(msg);
      this.chatroomService.chatroomList.next(list);
    }
})

}
 ))
}
}
public subscribeToUser(){
this.stompClient.subscribe("/user/queue/hello",((message: Stomp.Message) => {
  let list=this.coworkerService.coworkerList.value;
  console.log("list list "+list.length)
  let m=JSON.parse(message.body);
  list.forEach(coworker=>{
    if((coworker.Email=== m.senderId ) )
      {
        coworker.MessagesList?.push(m);
        this.coworkerService.coworkerList.next(list);
      }
  })
}));
}



}
