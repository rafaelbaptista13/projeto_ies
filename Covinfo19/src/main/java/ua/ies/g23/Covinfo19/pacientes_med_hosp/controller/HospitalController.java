package ua.ies.g23.Covinfo19.pacientes_med_hosp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import ua.ies.g23.Covinfo19.exception.ResourceNotFoundException;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.HospitalRepository;


@RestController
@RequestMapping("/api/v1")
public class HospitalController {
    @Autowired
    private HospitalRepository hospitalRepository;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/public/hospitais")
    public List<Hospital> getAllHospitals(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) String concelho,
        @RequestParam(required = false) String regiao,
        @RequestParam(required = false) Integer numero_camasmin,
        @RequestParam(required = false) Integer numero_camasmax,
        @RequestParam(required = false) Integer numero_camas_ocupadasmin,
        @RequestParam(required = false) Integer numero_camas_ocupadasmax ) {
        if (nome == null && concelho == null && regiao == null && numero_camasmin == null && numero_camasmax == null && numero_camas_ocupadasmin == null && numero_camas_ocupadasmax == null) {
            return hospitalRepository.findAll();
        }
        
        String strnome = "";
        if (nome != null) {
            strnome = "%" + nome + "%";
        } else {
            strnome = "%";
        }

        String strconcelho = "";
        if (concelho != null) {
            strconcelho = concelho;
        } else {
            strconcelho = "%";
        }
    
        String strregiao = "";
        if (regiao != null) {
            strregiao = regiao;
        } else {
            strregiao = "%";
        }

        String strnumero_camasmin = "";
        if (numero_camasmin != null) {
            strnumero_camasmin = numero_camasmin.toString();
        } else {
            strnumero_camasmin = "0";
        }

        String strnumero_camasmax = "";
        if (numero_camasmax != null) {
            strnumero_camasmax = numero_camasmax.toString();
        } else {
            strnumero_camasmax = "5000";
        }

        String strnumero_camas_ocupadasmin = "";
        if (numero_camas_ocupadasmin != null) {
            strnumero_camas_ocupadasmin = numero_camas_ocupadasmin.toString();
        } else {
            strnumero_camas_ocupadasmin = "0";
        }

        String strnumero_camas_ocupadasmax = "";
        if (numero_camas_ocupadasmax != null) {
            strnumero_camas_ocupadasmax = numero_camas_ocupadasmax.toString();
        } else {
            strnumero_camas_ocupadasmax = "5000";
        }

        return hospitalRepository.findAllFilters(strnome, strconcelho, strregiao, strnumero_camasmin, strnumero_camasmax, strnumero_camas_ocupadasmin, strnumero_camas_ocupadasmax);
    }

    @GetMapping("/public/hospitais/{id}")
    public ResponseEntity<Hospital> getHospitalById(@PathVariable(value = "id") Long hospitalId)
        throws ResourceNotFoundException {
        Hospital hospital = hospitalRepository.findById(hospitalId)
          .orElseThrow(() -> new ResourceNotFoundException("Hospital not found for this id :: " + hospitalId));
        return ResponseEntity.ok().body(hospital);
    }
    
    @PostMapping("/private/hospitais")
    public Hospital createHospital(@Valid @RequestBody Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    @PutMapping("/private/hospitais/{id}")
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

    @DeleteMapping("/private/hospitais/{id}")
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