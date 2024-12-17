import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LizenzBuyComponent } from './lizenz-buy.component';

describe('LizenzBuyComponent', () => {
  let component: LizenzBuyComponent;
  let fixture: ComponentFixture<LizenzBuyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LizenzBuyComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LizenzBuyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
