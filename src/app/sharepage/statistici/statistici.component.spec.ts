import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StatisticiComponent } from './statistici.component';

describe('StatisticiComponent', () => {
  let component: StatisticiComponent;
  let fixture: ComponentFixture<StatisticiComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StatisticiComponent]
    });
    fixture = TestBed.createComponent(StatisticiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
