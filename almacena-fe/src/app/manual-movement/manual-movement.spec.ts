import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManualMovement } from './manual-movement';

describe('ManualMovement', () => {
  let component: ManualMovement;
  let fixture: ComponentFixture<ManualMovement>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManualMovement],
    }).compileComponents();

    fixture = TestBed.createComponent(ManualMovement);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
