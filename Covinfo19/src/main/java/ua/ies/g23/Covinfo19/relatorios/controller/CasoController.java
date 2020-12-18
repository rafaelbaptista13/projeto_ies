package ua.ies.g23.Covinfo19.relatorios.controller;

import java.text.ParseException;
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
    private PacienteRepository pacienteRepository;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/casos/count")
    public int getAllCasosCount(@RequestParam String estado) {
        int number = 0;
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
    public List<Caso> getAllCasos() {
        return CasoRepository.findAll();
    }

    /*
    @GetMapping("/casos")
    public List<Caso> getAllCasos(
        @RequestParam(required = false) String estado,
        @RequestParam(required = false) String genero,
        @RequestParam(required = false) Integer idade,
        @RequestParam(required = false) String concelho,
        @RequestParam(required = false) String regiao,
        @RequestParam(required = false) String nacionalidade,
        @RequestParam(required = false) Integer altura,
        @RequestParam(required = false) Integer peso ) {
            if (estado == null && genero == null && idade == null && concelho == null && regiao == null && nacionalidade == null && altura == null && peso == null ) {
                return CasoRepository.findAll();
            }
            
            String strestado = "";
            if (estado != null) {
                strestado = "estado_atual = '" + estado + "'";
            } else {
                strestado = "1 = 1 ";
            }

            String strgenero = "";
            if (genero != null) {
                strgenero = "genero = '" + genero + "'";
            } else {
                strestado = "1 = 1";
            }

            String stridade = "";
            if (idade != null) {
                stridade = " and idade = " + idade ;
            }

            String strconcelho = "";
            if (concelho != null) {
                strconcelho = " concelho = '" + concelho + "'";
            }

            String strregiao = "";
            if (regiao != null) {
                strregiao = " regiao = '" + regiao + "'";
            }

            String strnacionalidade = "";
            if (nacionalidade != null) {
                strnacionalidade = " nacionalidade = '" + nacionalidade + "'";
            }

            String straltura = "";
            if (altura != null) {
                straltura = " altura = " + altura ;
            }

            String strpeso = "";
            if (peso != null) {
                strpeso = " peso = " + peso ;
            }

            
            System.out.println("Select * from pacientes where " + strgenero + stridade + strconcelho + strregiao + strnacionalidade + straltura + strpeso);
            List<Paciente> pacientes = pacienteRepository.findAllFilters(strgenero, stridade, strconcelho, strregiao, strnacionalidade, straltura, strpeso);

            //List<Paciente> pacientes = pacienteRepository.findAllF();
            System.out.println("AAAAAAA");
            System.out.println(pacientes);
            
            return CasoRepository.findAllFilters(strestado, strgenero, stridade, strconcelho, strregiao, strnacionalidade, straltura, strpeso);
    }
    */

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
