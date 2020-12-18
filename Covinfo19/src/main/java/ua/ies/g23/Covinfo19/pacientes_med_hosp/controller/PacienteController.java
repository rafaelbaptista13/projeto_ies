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

import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Paciente;
import ua.ies.g23.Covinfo19.exception.ResourceNotFoundException;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.PacienteRepository;


@RestController
@RequestMapping("/api/v1")
public class PacienteController {
    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping("/pacientes")
    public List<Paciente> getAllPacientes() {
        return pacienteRepository.findAll();
    }

    @GetMapping("/pacientes/{id}")
    public ResponseEntity<Paciente> getPacienteById(@PathVariable(value = "id") Long pacienteID)
        throws ResourceNotFoundException {
        Paciente paciente = pacienteRepository.findById(pacienteID)
          .orElseThrow(() -> new ResourceNotFoundException("Paciente not found for this id :: " + pacienteID));
        return ResponseEntity.ok().body(paciente);
    }
    
    @PostMapping("/pacientes")
    public Paciente createPaciente(@Valid @RequestBody Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    @PutMapping("/pacientes/{id}")
    public ResponseEntity<Paciente> updatePaciente(@PathVariable(value = "id") Long pacienteID,
         @Valid @RequestBody Paciente pacienteDetails) throws ResourceNotFoundException {
        Paciente paciente = pacienteRepository.findById(pacienteID)
        .orElseThrow(() -> new ResourceNotFoundException("Paciente not found for this id :: " + pacienteID));
        
        paciente.setNome(pacienteDetails.getNome());
        paciente.setGenero(pacienteDetails.getGenero());
        paciente.setIdade(pacienteDetails.getIdade());
        paciente.setConcelho(pacienteDetails.getConcelho());
        paciente.setRegiao(pacienteDetails.getRegiao());
        paciente.setNacionalidade(pacienteDetails.getNacionalidade());
        paciente.setAltura(pacienteDetails.getAltura());
        paciente.setPeso(pacienteDetails.getPeso());
        paciente.setMedico(pacienteDetails.getMedico());
        final Paciente updatedPaciente = pacienteRepository.save(paciente);
        return ResponseEntity.ok(updatedPaciente);
    }

    @DeleteMapping("/pacientes/{id}")
    public Map<String, Boolean> deletePaciente(@PathVariable(value = "id") Long pacienteID)
         throws ResourceNotFoundException {
        Paciente paciente = pacienteRepository.findById(pacienteID)
       .orElseThrow(() -> new ResourceNotFoundException("Paciente not found for this id :: " + pacienteID));

        pacienteRepository.delete(paciente);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}