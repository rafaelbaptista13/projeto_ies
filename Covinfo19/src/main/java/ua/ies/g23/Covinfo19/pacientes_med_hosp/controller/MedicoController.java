package ua.ies.g23.Covinfo19.pacientes_med_hosp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Medico;
import ua.ies.g23.Covinfo19.exception.ResourceNotFoundException;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.MedicoRepository;


@RestController
@RequestMapping("/api/v1")
public class MedicoController {
    @Autowired
    private MedicoRepository medicoRepository;

    @GetMapping("/medicos")
    public List<Medico> getAllMedicos() {
        return medicoRepository.findAll();
    }

    @GetMapping("/medicos/{id}")
    public ResponseEntity<Medico> getMedicoById(@PathVariable(value = "id") Long MedicoId)
        throws ResourceNotFoundException {
        Medico Medico = medicoRepository.findById(MedicoId)
          .orElseThrow(() -> new ResourceNotFoundException("Medico not found for this id :: " + MedicoId));
        return ResponseEntity.ok().body(Medico);
    }
    
    @PostMapping("/medicos")
    public Medico createMedico(@Valid @RequestBody Medico Medico) {
        return medicoRepository.save(Medico);
    }

    @PutMapping("/medicos/{id}")
    public ResponseEntity<Medico> updateMedico(@PathVariable(value = "id") Long MedicoId,
         @Valid @RequestBody Medico MedicoDetails) throws ResourceNotFoundException {
        Medico Medico = medicoRepository.findById(MedicoId)
        .orElseThrow(() -> new ResourceNotFoundException("Medico not found for this id :: " + MedicoId));

        Medico.setNome(MedicoDetails.getNome());
        Medico.setCodigo_acesso(MedicoDetails.getCodigo_acesso());
        Medico.setIdade(MedicoDetails.getIdade());
        Medico.setEspecialidade(MedicoDetails.getEspecialidade());
        Medico.setHospital(MedicoDetails.getHospital());
        final Medico updatedMedico = medicoRepository.save(Medico);
        return ResponseEntity.ok(updatedMedico);
    }

    @DeleteMapping("/medicos/{id}")
    public Map<String, Boolean> deleteMedico(@PathVariable(value = "id") Long MedicoId)
         throws ResourceNotFoundException {
        Medico Medico = medicoRepository.findById(MedicoId)
       .orElseThrow(() -> new ResourceNotFoundException("Medico not found for this id :: " + MedicoId));

        medicoRepository.delete(Medico);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}