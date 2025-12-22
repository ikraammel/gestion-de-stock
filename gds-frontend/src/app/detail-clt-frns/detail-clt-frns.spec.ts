import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailCltFrns } from './detail-clt-frns';

describe('DetailCltFrns', () => {
  let component: DetailCltFrns;
  let fixture: ComponentFixture<DetailCltFrns>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailCltFrns]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailCltFrns);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
