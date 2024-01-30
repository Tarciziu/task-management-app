import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddEditTaskWrapperComponent } from './add-edit-task-wrapper.component';

describe('AddEditTaskWrapperComponent', () => {
  let component: AddEditTaskWrapperComponent;
  let fixture: ComponentFixture<AddEditTaskWrapperComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddEditTaskWrapperComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AddEditTaskWrapperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
