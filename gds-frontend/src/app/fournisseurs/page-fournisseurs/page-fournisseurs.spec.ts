import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageFournisseurs } from './page-fournisseurs';

describe('PageFournisseurs', () => {
  let component: PageFournisseurs;
  let fixture: ComponentFixture<PageFournisseurs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageFournisseurs]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageFournisseurs);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
