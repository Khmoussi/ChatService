import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CoworkersListComponent } from './coworkers-list.component';

describe('CoworkersListComponent', () => {
  let component: CoworkersListComponent;
  let fixture: ComponentFixture<CoworkersListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CoworkersListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CoworkersListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
