import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GithubauthComponent } from './githubauth.component';

describe('GithubauthComponent', () => {
  let component: GithubauthComponent;
  let fixture: ComponentFixture<GithubauthComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GithubauthComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GithubauthComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
