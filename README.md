Real-Time Chat Application

Welcome to the Real-Time Chat Application, built using Spring Boot and Angular! This project supports seamless one-to-one and group messaging, leveraging STOMP over WebSockets for real-time interactions. Additionally, it integrates Azure Storage for secure and scalable file handling.

Key Features

1. Real-Time Messaging

One-to-One Chat: Connect with individuals in real-time with instant message delivery.

Group Chat: Engage in group conversations with multiple users simultaneously.

STOMP over WebSockets: Ensures reliable and efficient message transmission.
Exchange of Images and Voice Messages

2. Secure File Handling

Azure Storage Integration: Upload and share files securely within chats.

Scalable and Reliable: Uses Azure’s robust infrastructure to handle file storage and retrieval.

3. Modern Tech Stack

Backend: Spring Boot for efficient and maintainable server-side logic.

Frontend: Angular for a dynamic and responsive user experience.

WebSocket Protocol: Real-time bidirectional communication using STOMP.

Technology Stack

Backend: Spring Boot, STOMP, WebSockets, Azure Storage SDK

Frontend: Angular, TypeScript

Database: [Your preferred database, e.g., MySQL/PostgreSQL]

Cloud Storage: Azure Blob Storage

Prerequisites

To set up this project locally, you will need:

Java 17+

Node.js 14+

Angular CLI

Azure Account for storage

Getting Started

1. Clone the Repository

$ git clone https://github.com/yourusername/realtime-chat-app.git
$ cd realtime-chat-app

2. Backend Setup

Navigate to the backend directory and build the project:

$ cd backend
$ ./mvnw spring-boot:run

3. Frontend Setup

Navigate to the frontend directory and serve the Angular app:

$ cd frontend
$ npm install
$ ng serve

4. Access the Application

Visit http://localhost:4200 in your web browser to start chatting!

Architecture Overview

This project follows a client-server architecture:

Angular Frontend communicates with the Spring Boot Backend over WebSockets.

STOMP Protocol is used for handling message transfer.

Azure Storage ensures that all file uploads are stored securely in the cloud.



Screenshots

Login Page
![Capture d'écran 2024-07-16 185236](https://github.com/user-attachments/assets/32c27973-effe-4786-85f3-a026104025f7)




Chat Interface
One to One
![image](https://github.com/user-attachments/assets/742a568a-b8c1-4870-a2b3-b116ba2b37b0)

![image](https://github.com/user-attachments/assets/02c43d6c-0a0d-4b41-90c8-a507d878b73d)

Room chat
![image](https://github.com/user-attachments/assets/bf30347f-8ebc-4dc1-ae42-6d75d787b844)






Future Enhancements
Design Improvements.

Notification System for new messages.

Read Receipts to indicate when messages are seen.

User Presence Indicators for online/offline status.

