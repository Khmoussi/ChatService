import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CoworkerComponent } from './coworker.component';

describe('CoworkerComponent', () => {
  let component: CoworkerComponent;
  let fixture: ComponentFixture<CoworkerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CoworkerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CoworkerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
