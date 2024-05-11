export class LoginRequest{

  private email:string;
  private password:string;
  constructor(email:string,password:string){
this.email=email;
this.password=password;
  }

  get Email(){
    return this.email;
  }
  get Password(){
    return this.password;
  }
}
