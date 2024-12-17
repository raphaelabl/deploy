import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AutociComponent } from './autoci.component';

describe('AutociComponent', () => {
  let component: AutociComponent;
  let fixture: ComponentFixture<AutociComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AutociComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AutociComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
