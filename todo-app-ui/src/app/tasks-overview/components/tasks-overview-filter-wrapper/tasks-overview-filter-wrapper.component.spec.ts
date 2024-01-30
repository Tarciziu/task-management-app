import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TasksOverviewFilterWrapperComponent } from './tasks-overview-filter-wrapper.component';

describe('TasksOverviewFilterWrapperComponent', () => {
  let component: TasksOverviewFilterWrapperComponent;
  let fixture: ComponentFixture<TasksOverviewFilterWrapperComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TasksOverviewFilterWrapperComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TasksOverviewFilterWrapperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
