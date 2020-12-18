package ua.ies.g23.Covinfo19.relatorios.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.ls.LSInput;

import ua.ies.g23.Covinfo19.relatorios.model.Caso;
import ua.ies.g23.Covinfo19.exception.ResourceNotFoundException;
import ua.ies.g23.Covinfo19.relatorios.repository.CasoRepository;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Paciente;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.PacienteRepository;

@RestController
@RequestMapping("/api/v1")
public class CasoController {
    @Autowired
    private CasoRepository CasoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/casos/count")
    public int getAllCasosCount(@RequestParam String estado) {
        if (estado.equals("ativos")) {
            return (int) CasoRepository.findAllAtivos().toArray()[0];
        }
        String estado_atual = "";
        switch (estado) {
            case "confinamento":
                estado_atual = "Confinamento Domiciliário";
                break;
            case "internado":
                estado_atual = "Internado";
                break;
            case "morto":
                estado_atual = "Óbito";
                break;
            case "cuidadosintensivos":
                estado_atual = "Cuidados Intensivos";
                break;
            case "recuperados":
                estado_atual = "Recuperado";
                break;
            default:
                throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "estado '" + estado + "' doesn't exist"
                );
        }

        return (int) CasoRepository.findAllByEstado(estado_atual).toArray()[0];
    }

    
    @GetMapping("/casos")
    public List<Caso> getAllCasos(
        @RequestParam(required = false) String estado,
        @RequestParam(required = false) String genero,
        @RequestParam(required = false) Integer idademax,
        @RequestParam(required = false) Integer idademin,
        @RequestParam(required = false) String concelho,
        @RequestParam(required = false) String regiao,
        @RequestParam(required = false) String nacionalidade,
        @RequestParam(required = false) Integer alturamin,
        @RequestParam(required = false) Integer alturamax,
        @RequestParam(required = false) Integer pesomin,
        @RequestParam(required = false) Integer pesomax ) {
        if (estado == null && genero == null && idademin == null && idademax == null && concelho == null && regiao == null && nacionalidade == null && alturamin == null && alturamax == null && pesomin == null && pesomax == null ) {
            return CasoRepository.findAll();
        }
            
        String strgenero = "";
        if (genero != null) {
            strgenero = genero;
        } else {
            strgenero = "%";
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

        String strnacionalidade = "";
        if (nacionalidade != null) {
            strnacionalidade = nacionalidade;
        } else {
            strnacionalidade = "%";
        }

        String stralturamin = "";
        if (alturamin != null) {
            stralturamin = alturamin.toString();
        } else {
            stralturamin = "0";
        }

        String stralturamax = "";
        if (alturamax != null) {
            stralturamax = alturamax.toString();
        } else {
            stralturamax = "300";
        }

        String strpesomin = "";
        if (pesomin != null) {
            strpesomin = pesomin.toString() ;
        } else {
            strpesomin = "0";
        }

        String strpesomax = "";
        if (pesomax != null) {
            strpesomax = pesomax.toString() ;
        } else {
            strpesomax = "500";
        }

        System.out.println("tou aqui");
        List<Paciente> pacientes = pacienteRepository.findAllFilters(strgenero, stridademin, stridademax, strconcelho, strregiao, strnacionalidade, stralturamin, stralturamax, strpesomin, strpesomax);   

        System.out.println(pacientes);

        List<Caso> casos = new ArrayList<Caso>();
        
        for (Paciente paciente : pacientes) {
            Caso caso = CasoRepository.findByPacienteId(paciente.getPacienteId());
            if (caso.getEstado_atual().equals(estado) || estado == null) {
                casos.add(caso);
            }
        }
        return casos;
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
