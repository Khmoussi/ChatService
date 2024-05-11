import { CoworkerComponent } from './../coworker/coworker.component';
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-coworkers-list',
  standalone: true,
  imports: [CommonModule,CoworkerComponent],
  templateUrl: './coworkers-list.component.html',
  styleUrl: './coworkers-list.component.css'
})
export class CoworkersListComponent {

}
