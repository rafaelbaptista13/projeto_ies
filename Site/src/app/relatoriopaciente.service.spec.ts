import { TestBed } from '@angular/core/testing';

import { RelatoriopacienteService } from './relatoriopaciente.service';

describe('RelatoriopacienteService', () => {
  let service: RelatoriopacienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RelatoriopacienteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
