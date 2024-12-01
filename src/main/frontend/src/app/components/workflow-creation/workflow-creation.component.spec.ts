import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkflowCreationComponent } from './workflow-creation.component';

describe('WorkflowCreationComponent', () => {
  let component: WorkflowCreationComponent;
  let fixture: ComponentFixture<WorkflowCreationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WorkflowCreationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WorkflowCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
