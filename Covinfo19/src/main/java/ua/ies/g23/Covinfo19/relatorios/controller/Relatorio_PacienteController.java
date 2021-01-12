package ua.ies.g23.Covinfo19.relatorios.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ua.ies.g23.Covinfo19.relatorios.model.Relatorio_Paciente;
import ua.ies.g23.Covinfo19.exception.ResourceNotFoundException;
import ua.ies.g23.Covinfo19.relatorios.repository.Relatorio_PacienteRepository;

@RestController
@RequestMapping("/api/v1/private")
public class Relatorio_PacienteController {
    @Autowired
    private Relatorio_PacienteRepository relatorio_PacienteRepository;


    @GetMapping("/relatorio_pacientes")
    public Page<Relatorio_Paciente> getAllRelatorio_Pacientes(@RequestParam(required = false) Integer page,
        @RequestParam(required=false) Integer size) {
        if (page==null) page=0;
        if (size==null) size=30;
        PageRequest pageRequest = PageRequest.of(page, size);
        return relatorio_PacienteRepository.findAll(pageRequest);
    }

    @GetMapping("/relatorio_pacientes/{id}")
    public ResponseEntity<Relatorio_Paciente> getHospitalById(@PathVariable(value = "id") Long relatorio_pacienteId)
        throws ResourceNotFoundException {
        Relatorio_Paciente relatorio_Paciente = relatorio_PacienteRepository.findById(relatorio_pacienteId)
          .orElseThrow(() -> new ResourceNotFoundException("Relatorio_Paciente not found for this id :: " + relatorio_pacienteId));

        return ResponseEntity.ok().body(relatorio_Paciente);
    }

    @PostMapping("/relatorio_pacientes")
    public Relatorio_Paciente createRelatorio_Paciente(@Valid @RequestBody Relatorio_Paciente Relatorio_Paciente)
            throws ResourceNotFoundException {
        return relatorio_PacienteRepository.save(Relatorio_Paciente);
    }

    @PutMapping("/relatorio_pacientes/{id}")
    public ResponseEntity<Relatorio_Paciente> updateRelatorio_Paciente(@PathVariable(value = "id") Long Relatorio_PacienteID,
            @Valid @RequestBody Relatorio_Paciente Relatorio_PacienteDetails) throws ResourceNotFoundException {

            Relatorio_Paciente relatorio_Paciente = relatorio_PacienteRepository.findById(Relatorio_PacienteID)
            .orElseThrow(() -> new ResourceNotFoundException("Relatorio_Paciente not found for this id :: " + Relatorio_PacienteID));
    
            relatorio_Paciente.setEstado(Relatorio_PacienteDetails.getEstado());
            relatorio_Paciente.setData(Relatorio_PacienteDetails.getData());
            //relatorio_Paciente.setPaciente(Relatorio_PacienteDetails.getPaciente());
            final Relatorio_Paciente updatedRelatorio_Paciente = relatorio_PacienteRepository.save(relatorio_Paciente);
            return ResponseEntity.ok(updatedRelatorio_Paciente);
    }

    @DeleteMapping("/relatorio_pacientes/{id}")
    public Map<String, Boolean> deleteRelatorio_Paciente(@PathVariable(value = "id") Long Relatorio_PacienteID) 
            throws ResourceNotFoundException, ParseException {

        Relatorio_Paciente Relatorio_Paciente = relatorio_PacienteRepository.findById(Relatorio_PacienteID)
        .orElseThrow(() -> new ResourceNotFoundException("Relatorio_Paciente not found for this id :: " + Relatorio_PacienteID));
    
        relatorio_PacienteRepository.delete(Relatorio_Paciente);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}