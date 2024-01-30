import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TasksOverviewListComponent } from './tasks-overview-list.component';

describe('TasksOverviewListComponent', () => {
  let component: TasksOverviewListComponent;
  let fixture: ComponentFixture<TasksOverviewListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TasksOverviewListComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TasksOverviewListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
