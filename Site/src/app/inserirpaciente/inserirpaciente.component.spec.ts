import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InserirpacienteComponent } from './inserirpaciente.component';

describe('InserirpacienteComponent', () => {
  let component: InserirpacienteComponent;
  let fixture: ComponentFixture<InserirpacienteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InserirpacienteComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InserirpacienteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
