import { Component, OnInit } from '@angular/core';
import { HttpClientModule, HttpEventType, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FileUploadService } from '../file-upload.service';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { ApiService } from '../Service/api.service';
import { Coworker } from '../Model/Coworker';
import { ChatroomServiceService } from '../Service/chatroom-service.service';
import { SharedDataService } from './../Service/shared-data.service';
import { SignService } from '../Service/sign.service';
import { CoworkerService } from '../Service/coworker.service';
import { Router } from '@angular/router';

import { ToastrService } from 'ngx-toastr';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-upload-images',
  templateUrl: './upload-images.component.html',
  styleUrls: ['./upload-images.component.css'],
  standalone: true,
  imports: [
    CommonModule,
    HttpClientModule,
    MatToolbarModule,
    MatProgressBarModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatListModule,

  ],
  providers: [FileUploadService]
})
export class UploadImagesComponent implements OnInit {
  user :Coworker| null=null;

  selectedFiles?: FileList;
  selectedFileNames: string[] = [];

  progressInfos: any[] = [];
  message: string[] = [];

  previews: string[] = [];
  imageInfos?: Observable<any>;
toggleToolBar: boolean = true;
  constructor(private snackBar: MatSnackBar,private toastr: ToastrService,private router:Router,private uploadService : FileUploadService,private sharedDataService :SharedDataService,private signService :SignService, private apiService :ApiService,private chatRoomService :ChatroomServiceService,private coworkerService:CoworkerService) {
  }
  ngOnInit(): void {
    this.imageInfos = this.uploadService.getFiles();
    this.user=this.apiService.user;
  }

  selectFiles(event: any): void {
    this.message = [];
    this.progressInfos = [];
    this.selectedFileNames = [];
    this.selectedFiles = event.target.files;

    this.previews = [];
    if (this.selectedFiles && this.selectedFiles[0]) {
      const numberOfFiles = this.selectedFiles.length;
      for (let i = 0; i < numberOfFiles; i++) {
        const reader = new FileReader();

        reader.onload = (e: any) => {
          console.log(e.target.result);
          this.previews.push(e.target.result);
        };

        reader.readAsDataURL(this.selectedFiles[i]);

        this.selectedFileNames.push(this.selectedFiles[i].name);
      }
    }


  }

  upload(idx: number, file: File): void {
    this.progressInfos[idx] = { value: 0, fileName: file.name };

    if (file) {
      this.apiService.upload(file).then(
        (result: any) => {
          if(result)
this.showSuccess("upload success")
   else{this.showError("upload failed")}
            const msg = 'Uploaded the file successfully: ' + file.name;
            this.message.push(msg);
           // this.imageInfos = this.apiService.getFiles();

        }).catch((error)=>{
          this.showError("upload failed")
          console.log(error)});
    }
  }

  uploadFiles(): void {
    this.message = [];

    if (this.selectedFiles) {
      for (let i = 0; i < this.selectedFiles.length; i++) {
        this.upload(i, this.selectedFiles[i]);
      }
    }
  }

  logout():void{
    //this.signService.signOut();
    this.apiService.resetData();
    this.chatRoomService.resetData();
    this.sharedDataService.resetData();
    this.coworkerService.resetData();
    this.router.navigate(['login-component']);
  }
  showEdit() {
   this.toggleToolBar=false;
  }
 showSuccess(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000, // duration in milliseconds
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: ['success-snackbar']
    });
  }

  showError(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: ['error-snackbar']
    });
  }
}
