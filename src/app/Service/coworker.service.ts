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
  public clickedItemIndex :BehaviorSubject<number>=new BehaviorSubject<number>(0);
public clickedItemIndex$=this.clickedItemIndex.asObservable();
  constructor() { }
  resetData() {
    this.currentCoworker.next(null);
    this.coworkerList.next([]);
    this.clickedItemIndex.next(0);
  }
}
