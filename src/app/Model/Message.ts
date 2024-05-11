export class Message {
  private idMessage:number;
  private senderId: string;
  private content: string;
  private sendDate: Date;
  private contentType: string;
  private messageType : string;

  constructor(idMessage:number,sendDate: Date, messageType: string,  contentType: string, content: string,senderId: string) {
    this.senderId = senderId;
    this.content = content;
    this.sendDate = sendDate;
    this.contentType = contentType;
    this.messageType = messageType;
    this.idMessage = idMessage;
  }

  // Getter and Setter for 'senderId'
  get SenderId(): string {
    return this.senderId;
  }

  set SenderId(value: string) {
    this.senderId = value;
  }

  // Getter and Setter for 'content'
  get Content(): string {
    return this.content;
  }

  set Content(value: string) {
    this.content = value;
  }

  // Getter and Setter for 'state'


  // Getter and Setter for 'sendDate'
  get SendDate(): Date {
    return this.sendDate;
  }

  set SendDate(value: Date) {
    this.sendDate = value;
  }
}
