package ua.ies.g23.Covinfo19.pacientes_med_hosp.controller;

import java.util.ArrayList;
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

import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Paciente;
import ua.ies.g23.Covinfo19.exception.ResourceNotFoundException;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.PacienteRepository;
import ua.ies.g23.Covinfo19.relatorios.model.Caso;
import ua.ies.g23.Covinfo19.relatorios.repository.CasoRepository;


@RestController
@RequestMapping("/api/v1/private")
public class PacienteController {
    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private CasoRepository casoRepository;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/pacientesbymedic")
    public HashMap<Long, List<Object>> getAllByMedico(
        @RequestParam(required = true) Integer medico,
        @RequestParam(required = false) String num_paciente,
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) String estado ) {
            if (num_paciente == null) {
                num_paciente = "%";
            }

            if (nome == null) {
                nome = "%";
            } else {
                nome = "%" + nome + "%";
            }

            if (estado == null) {
                estado = "%";
            }

            List<Paciente> pacientes = pacienteRepository.findByMedico(String.valueOf(medico), num_paciente, nome);
            HashMap<Long, List<Object>> retorno = new HashMap<>();
            for (Paciente p : pacientes) {
                Caso caso = casoRepository.findByPacienteId(p.getPacienteId());
                if (caso.getEstado_atual().equals(estado) || estado.equals("%")) {
                    List<Object> dados = new ArrayList<>();
                    dados.add(p);
                    dados.add(caso);
                    retorno.put(p.getPacienteId(), dados );
                }
            }
            return retorno;
        }

    @GetMapping("/pacientes")
    public List<Paciente> getAllCasos(
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
        if ( genero == null && idademin == null && idademax == null && concelho == null && regiao == null && nacionalidade == null && alturamin == null && alturamax == null && pesomin == null && pesomax == null) {
            return pacienteRepository.findAll();
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

        return pacienteRepository.findAllFilters(strgenero, stridademin, stridademax, strconcelho, strregiao, strnacionalidade, stralturamin, stralturamax, strpesomin, strpesomax);
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