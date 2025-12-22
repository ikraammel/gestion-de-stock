import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PagesStatistiques } from './pages-statistiques';

describe('PagesStatistiques', () => {
  let component: PagesStatistiques;
  let fixture: ComponentFixture<PagesStatistiques>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PagesStatistiques]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PagesStatistiques);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
