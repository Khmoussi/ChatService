export class MessageResponse {
    idMessage: number = 0;
    sendDate: Date | null = null;
    messageType: string | null = null;
    senderId: string | null = null;
    roomId: number = 0;
    content: string |null;

    constructor(idMessage: number, sendDate: Date | null, messageType: string | null, senderId: string | null, roomId: number, content: string |null) {
        this.idMessage = idMessage;
        this.sendDate = sendDate;
        this.messageType = messageType;
        this.senderId = senderId;
        this.roomId = roomId;
        this.content = content;
    }
}
