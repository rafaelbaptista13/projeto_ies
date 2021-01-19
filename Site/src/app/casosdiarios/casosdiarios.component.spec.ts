import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CasosdiariosComponent } from './casosdiarios.component';

describe('CasosdiariosComponent', () => {
  let component: CasosdiariosComponent;
  let fixture: ComponentFixture<CasosdiariosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CasosdiariosComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CasosdiariosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
