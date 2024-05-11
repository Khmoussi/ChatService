import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Coworker } from '../Model/Coworker';

@Injectable({
  providedIn: 'root'
})
export class CoworkerService {

  public currentCoworker: BehaviorSubject<Coworker|null> =new BehaviorSubject<Coworker|null>(null);
  public currentCoworker$ = this.currentCoworker.asObservable();
  public coworkerList :BehaviorSubject<Coworker[]> = new BehaviorSubject<Coworker[]>([]);
  public coworkerList$=this.coworkerList.asObservable();
  constructor() { }
  
}
