import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageUtilisateurs } from './page-utilisateurs';

describe('PageUtilisateurs', () => {
  let component: PageUtilisateurs;
  let fixture: ComponentFixture<PageUtilisateurs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageUtilisateurs]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageUtilisateurs);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
