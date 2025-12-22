import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailMvmtStockArticle } from './detail-mvmt-stock-article';

describe('DetailMvmtStockArticle', () => {
  let component: DetailMvmtStockArticle;
  let fixture: ComponentFixture<DetailMvmtStockArticle>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailMvmtStockArticle]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailMvmtStockArticle);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
