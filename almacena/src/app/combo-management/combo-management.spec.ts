import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComboManagement } from './combo-management';

describe('ComboManagement', () => {
  let component: ComboManagement;
  let fixture: ComponentFixture<ComboManagement>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComboManagement]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ComboManagement);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
