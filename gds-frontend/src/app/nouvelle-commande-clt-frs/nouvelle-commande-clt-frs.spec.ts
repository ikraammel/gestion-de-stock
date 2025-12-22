import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NouvelleCommandeCltFrs } from './nouvelle-commande-clt-frs';

describe('NouvelleCommandeCltFrs', () => {
  let component: NouvelleCommandeCltFrs;
  let fixture: ComponentFixture<NouvelleCommandeCltFrs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NouvelleCommandeCltFrs]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NouvelleCommandeCltFrs);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
