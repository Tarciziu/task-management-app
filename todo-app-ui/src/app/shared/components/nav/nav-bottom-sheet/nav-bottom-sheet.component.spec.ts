import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavBottomSheetComponent } from './nav-bottom-sheet.component';

describe('NavBottomSheetComponent', () => {
  let component: NavBottomSheetComponent;
  let fixture: ComponentFixture<NavBottomSheetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavBottomSheetComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(NavBottomSheetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
