package ua.ies.g23.Covinfo19.pacientes_med_hosp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Hospital;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Medico;
import ua.ies.g23.Covinfo19.exception.ResourceNotFoundException;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.HospitalRepository;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.MedicoRepository;

@RestController
@RequestMapping("/api/v1/private")
public class MedicoController {
    @Autowired
    private MedicoRepository medicoRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/medicos")
    public List<Medico> getAllMedicos(@RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer hospital_id, @RequestParam(required = false) Integer idademin,
            @RequestParam(required = false) Integer idademax, @RequestParam(required=false) Integer page, @RequestParam(required=false) Integer size) {
        
        if (page==null) page=0;
        if (size==null) size=30;
        PageRequest pageRequest = PageRequest.of(page, size);

        if (nome == null && hospital_id == null && idademin == null && idademax == null) {
            List<Medico> l = new ArrayList<>();
            for (Medico m: medicoRepository.findAll(pageRequest)) {
                l.add(m);
            }
            return l;
        }

        String strnome = "";
        if (nome != null) {
            strnome = nome + "%";
        } else {
            strnome = "%";
        }

        String strhospital_id = "";
        if (hospital_id != null) {
            strhospital_id = hospital_id.toString();
        } else {
            strhospital_id = "%";
        }

        String stridademin = "";
        if (idademin != null) {
            stridademin = idademin.toString();
        } else {
            stridademin = "0";
        }

        String stridademax = "";
        if (idademax != null) {
            stridademax = idademax.toString();
        } else {
            stridademax = "300";
        }
        System.out.println(strnome);
        return medicoRepository.findAllByFilters(strnome, strhospital_id, stridademin, stridademax, pageRequest);
    }

    @GetMapping("/medicos/{id}")
    public ResponseEntity<Medico> getMedicoById(@PathVariable(value = "id") Long MedicoId)
            throws ResourceNotFoundException {
        Medico Medico = medicoRepository.findById(MedicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Medico not found for this id :: " + MedicoId));
        return ResponseEntity.ok().body(Medico);
    }

    @PostMapping("/medicos")
    @Transactional(rollbackFor = Exception.class)
    public Medico createMedico(@Valid @RequestBody Medico medico) {
        medico.setCodigo_acesso(bCryptPasswordEncoder.encode(medico.getCodigo_acesso()));
        return medicoRepository.save(medico);
    }

    @PutMapping("/medicos/{id}")
    public ResponseEntity<Medico> updateMedico(@PathVariable(value = "id") Long MedicoId,
         @Valid @RequestBody Medico MedicoDetails) throws ResourceNotFoundException {
        Medico Medico = medicoRepository.findById(MedicoId)
        .orElseThrow(() -> new ResourceNotFoundException("Medico not found for this id :: " + MedicoId));

        Medico.setNome(MedicoDetails.getNome());
        Medico.setCodigo_acesso(bCryptPasswordEncoder.encode(MedicoDetails.getCodigo_acesso()));
        Medico.setIdade(MedicoDetails.getIdade());
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