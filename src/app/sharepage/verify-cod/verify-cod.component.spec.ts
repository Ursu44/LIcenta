import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerifyCodComponent } from './verify-cod.component';

describe('VerifyCodComponent', () => {
  let component: VerifyCodComponent;
  let fixture: ComponentFixture<VerifyCodComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VerifyCodComponent]
    });
    fixture = TestBed.createComponent(VerifyCodComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
