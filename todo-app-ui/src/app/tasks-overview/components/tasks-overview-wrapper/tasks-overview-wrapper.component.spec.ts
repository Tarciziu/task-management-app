import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TasksOverviewWrapperComponent } from './tasks-overview-wrapper.component';

describe('TasksOverviewWrapperComponent', () => {
  let component: TasksOverviewWrapperComponent;
  let fixture: ComponentFixture<TasksOverviewWrapperComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TasksOverviewWrapperComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TasksOverviewWrapperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
