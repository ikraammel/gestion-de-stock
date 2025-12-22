import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MvmtStock } from './mvmt-stock';

describe('MvmtStock', () => {
  let component: MvmtStock;
  let fixture: ComponentFixture<MvmtStock>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MvmtStock]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MvmtStock);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
