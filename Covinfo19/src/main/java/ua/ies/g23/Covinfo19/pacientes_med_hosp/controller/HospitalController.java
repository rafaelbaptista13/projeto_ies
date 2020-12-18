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

import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Hospital;
import ua.ies.g23.Covinfo19.exception.ResourceNotFoundException;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.HospitalRepository;


@RestController
@RequestMapping("/api/v1")
public class HospitalController {
    @Autowired
    private HospitalRepository hospitalRepository;

    @GetMapping("/hospitais")
    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    @GetMapping("/hospitais/{id}")
    public ResponseEntity<Hospital> getHospitalById(@PathVariable(value = "id") Long hospitalId)
        throws ResourceNotFoundException {
        Hospital hospital = hospitalRepository.findById(hospitalId)
          .orElseThrow(() -> new ResourceNotFoundException("Hospital not found for this id :: " + hospitalId));
        return ResponseEntity.ok().body(hospital);
    }
    
    @PostMapping("/hospitais")
    public Hospital createHospital(@Valid @RequestBody Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    @PutMapping("/hospitais/{id}")
    public ResponseEntity<Hospital> updateHospital(@PathVariable(value = "id") Long hospitalId,
         @Valid @RequestBody Hospital hospitalDetails) throws ResourceNotFoundException {
        Hospital hospital = hospitalRepository.findById(hospitalId)
        .orElseThrow(() -> new ResourceNotFoundException("Hospital not found for this id :: " + hospitalId));

        hospital.setNome(hospitalDetails.getNome());
        hospital.setConcelho(hospitalDetails.getConcelho());
        hospital.setRegiao(hospitalDetails.getRegiao());
        hospital.setNumero_camas(hospitalDetails.getNumero_camas());
        hospital.setNumero_camas_ocupadas(hospitalDetails.getNumero_camas_ocupadas());
        final Hospital updatedHospital = hospitalRepository.save(hospital);
        return ResponseEntity.ok(updatedHospital);
    }

    @DeleteMapping("/hospitais/{id}")
    public Map<String, Boolean> deleteHospital(@PathVariable(value = "id") Long hospitalId)
         throws ResourceNotFoundException {
        Hospital hospital = hospitalRepository.findById(hospitalId)
       .orElseThrow(() -> new ResourceNotFoundException("Hospital not found for this id :: " + hospitalId));

        hospitalRepository.delete(hospital);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}