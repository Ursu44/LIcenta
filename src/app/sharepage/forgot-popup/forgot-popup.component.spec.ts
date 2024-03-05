import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForgotPopupComponent } from './forgot-popup.component';

describe('ForgotPopupComponent', () => {
  let component: ForgotPopupComponent;
  let fixture: ComponentFixture<ForgotPopupComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ForgotPopupComponent]
    });
    fixture = TestBed.createComponent(ForgotPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
