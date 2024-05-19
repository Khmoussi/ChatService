import { Component } from '@angular/core';
import { Routes } from '@angular/router';
import { MainComponent } from './main/main.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { UploadImagesComponent } from './upload-images/upload-images.component';

export const routes: Routes = [
  {path:'main-component',component: MainComponent},
  {path:'login-component',component:LoginComponent},
  {path:'signup-component',component:SignupComponent}

];
