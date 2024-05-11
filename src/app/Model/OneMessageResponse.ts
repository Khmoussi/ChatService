export class OneMessageResponse {
  idMessage: number = 0;
  sendDate: Date | null = null;
  type: string | null = null;
  senderId: string | null = null;
  receiverId: string | null = null;

  message: string |null;

  constructor(idMessage: number, sendDate: Date | null, type: string | null, content: string |null, senderId: string | null, receiverId: string | null) {
      this.idMessage = idMessage;
      this.sendDate = sendDate;
      this.type = type;
      this.message = content;
      this.senderId = senderId;
      this.receiverId = receiverId;
  }
}
