import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NouvelleCategorie } from './nouvelle-categorie';

describe('NouvelleCategorie', () => {
  let component: NouvelleCategorie;
  let fixture: ComponentFixture<NouvelleCategorie>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NouvelleCategorie]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NouvelleCategorie);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
