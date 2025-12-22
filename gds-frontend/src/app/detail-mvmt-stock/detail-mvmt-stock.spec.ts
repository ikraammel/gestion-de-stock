import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailMvmtStock } from './detail-mvmt-stock';

describe('DetailMvmtStock', () => {
  let component: DetailMvmtStock;
  let fixture: ComponentFixture<DetailMvmtStock>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailMvmtStock]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailMvmtStock);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
