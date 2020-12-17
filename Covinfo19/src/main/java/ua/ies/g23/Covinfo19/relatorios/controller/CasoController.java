package ua.ies.g23.Covinfo19.relatorios.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

import ua.ies.g23.Covinfo19.relatorios.model.Caso;
import ua.ies.g23.Covinfo19.exception.ResourceNotFoundException;
import ua.ies.g23.Covinfo19.relatorios.repository.CasoRepository;

@RestController
@RequestMapping("/api/v1")
public class CasoController {
    @Autowired
    private CasoRepository CasoRepository;

    @GetMapping("/casos/count")
    public int getAllCasosCount(@RequestParam String estado) {
        int number = 0;
        switch (estado) {
            case "confinamento":
                number = (int) CasoRepository.findAllConfinamento().toArray()[0];
                break;
            case "internado":
                number = (int) CasoRepository.findAllInternado().toArray()[0];
                break;
            case "morto":
                number = (int) CasoRepository.findAllMorto().toArray()[0];
                break;
            case "cuidadosintensivos":
                number = (int) CasoRepository.findAllCuidados().toArray()[0];
                break;
            case "ativos":
                number = (int) CasoRepository.findAllAtivos().toArray()[0];
                break;
            case "recuperados":
                number = (int) CasoRepository.findAllRecuperados().toArray()[0];
                break;
            default:
                throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "estado '" + estado + "' doesn't exist"
                );
            }
        return number;
    }

    @GetMapping("/casos")
    public List<Caso> getAllCasos() {
        return CasoRepository.findAll();
    }

    @GetMapping("/casos/{id}")
    public ResponseEntity<Caso> getCasoById(@PathVariable(value = "id") Long CasoId)
        throws ResourceNotFoundException {
        Caso Caso = CasoRepository.findById(CasoId)
          .orElseThrow(() -> new ResourceNotFoundException("Caso not found for this id :: " + CasoId));

        return ResponseEntity.ok().body(Caso);
    }

    @PostMapping("/casos")
    public Caso createCaso(@Valid @RequestBody Caso caso)
            throws ResourceNotFoundException {
        return CasoRepository.save(caso);
    }

    @PutMapping("/casos/{id}")
    public ResponseEntity<Caso> updateCaso(@PathVariable(value = "id") Long CasoID,
            @Valid @RequestBody Caso CasoDetails) throws ResourceNotFoundException {

            Caso Caso = CasoRepository.findById(CasoID)
            .orElseThrow(() -> new ResourceNotFoundException("Caso not found for this id :: " + CasoID));
                
            Caso.setEstado_atual(CasoDetails.getEstado_atual());
            Caso.setPaciente_id(CasoDetails.getPaciente_id());
            final Caso updatedCaso = CasoRepository.save(Caso);
            return ResponseEntity.ok(updatedCaso);
    }

    @DeleteMapping("/casos/{id}")
    public Map<String, Boolean> deleteCaso(@PathVariable(value = "id") Long CasoID) 
            throws ResourceNotFoundException, ParseException {

        Caso Caso = CasoRepository.findById(CasoID)
        .orElseThrow(() -> new ResourceNotFoundException("Caso not found for this id :: " + CasoID));
    
        CasoRepository.delete(Caso);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
