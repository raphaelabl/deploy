import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeploymentCreationComponent } from './deployment-creation.component';

describe('DeploymentCreationComponent', () => {
  let component: DeploymentCreationComponent;
  let fixture: ComponentFixture<DeploymentCreationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DeploymentCreationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeploymentCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
