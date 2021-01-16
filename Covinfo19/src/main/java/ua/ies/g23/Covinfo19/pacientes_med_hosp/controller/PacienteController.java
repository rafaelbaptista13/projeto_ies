package ua.ies.g23.Covinfo19.pacientes_med_hosp.controller;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonTypeInfo.None;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import net.bytebuddy.implementation.bytecode.Throw;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Hospital;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Medico;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Paciente;
import ua.ies.g23.Covinfo19.exception.ResourceNotFoundException;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.HospitalRepository;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.MedicoRepository;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.PacienteRepository;
import ua.ies.g23.Covinfo19.relatorios.model.Caso;
import ua.ies.g23.Covinfo19.relatorios.model.Relatorio_Paciente;
import ua.ies.g23.Covinfo19.relatorios.repository.CasoRepository;
import ua.ies.g23.Covinfo19.relatorios.repository.Relatorio_PacienteRepository;


@RestController
@RequestMapping("/api/v1/private")
public class PacienteController {
    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private CasoRepository casoRepository;

    @Autowired
    private Relatorio_PacienteRepository relatorioPacienteRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    public static final SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/pacientesbymedic")
    public ArrayList<Object> getAllByMedico(
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
            ArrayList<Object> retorno = new ArrayList<>();
            for (Paciente p : pacientes) {
                if (estado.equals("Ativos")) {
                    if (p.getEstado_atual().equals("Confinamento Domiciliário") || p.getEstado_atual().equals("Internado") || p.getEstado_atual().equals("Cuidados Intensivos")) {
                        retorno.add(p);
                    }
                } else if (p.getEstado_atual().equals(estado) || estado.equals("%")) {
                    retorno.add(p);
                }
            }
            return retorno;
        }

    @GetMapping("/pacientes")
    public Page<Paciente> getAllPacientes(
        @RequestParam(required = false) String genero,
        @RequestParam(required = false) Integer idademax,
        @RequestParam(required = false) Integer idademin,
        @RequestParam(required = false) String concelho,
        @RequestParam(required = false) String regiao,
        @RequestParam(required = false) String nacionalidade,
        @RequestParam(required = false) Integer alturamin,
        @RequestParam(required = false) Integer alturamax,
        @RequestParam(required = false) Integer pesomin,
        @RequestParam(required = false) Integer pesomax,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size) {
        
        if (page==null) page=0;
        if (size==null) size=30;
        PageRequest pageRequest = PageRequest.of(page, size);

        if ( genero == null && idademin == null && idademax == null && concelho == null && regiao == null && nacionalidade == null && alturamin == null && alturamax == null && pesomin == null && pesomax == null) {
            return pacienteRepository.findAll(pageRequest);
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

        return pacienteRepository.findAllFilters(strgenero, stridademin, stridademax, strconcelho, strregiao, strnacionalidade, stralturamin, stralturamax, strpesomin, strpesomax, pageRequest);
        }   

    @GetMapping("/pacientes/{id}")
    public ResponseEntity<Paciente> getPacienteById(@PathVariable(value = "id") Long paciente_id)
        throws ResourceNotFoundException {
        Paciente paciente = pacienteRepository.findById(paciente_id)
          .orElseThrow(() -> new ResourceNotFoundException("Paciente not found for this id :: " + paciente_id));
        return ResponseEntity.ok().body(paciente);
    }
    
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/pacientes")
    public Paciente createPaciente(@Valid @RequestBody Map<String, String> pacient_info)
            throws NumberFormatException, ResourceNotFoundException {
        Paciente paciente = new Paciente();
        paciente.setNome(pacient_info.get("nome"));
        paciente.setGenero(pacient_info.get("genero"));
        paciente.setIdade(Integer.parseInt(pacient_info.get("idade")));
        paciente.setConcelho(pacient_info.get("concelho"));
        paciente.setRegiao(pacient_info.get("regiao"));
        paciente.setNacionalidade(pacient_info.get("nacionalidade"));
        paciente.setAltura(Integer.parseInt(pacient_info.get("altura")));
        paciente.setPeso(Float.parseFloat(pacient_info.get("peso")));
        paciente.setEstado_atual(pacient_info.get("estado"));
        Medico medico = medicoRepository.findById(Long.parseLong(pacient_info.get("medico_numero_medico")))
        .orElseThrow(() -> new ResourceNotFoundException("Medico not found for this id :: " + pacient_info.get("medico_numero_medico")));
        paciente.setMedico(medico);
        pacienteRepository.save(paciente);
        //Criação novo caso para o paciente. Atribuição dos valores dos campos do mesmo.
        Caso caso = new Caso();
        caso.setEstado_atual(pacient_info.get("estado"));
        caso.setPaciente_id(paciente.getPacienteId());
        casoRepository.save(caso);

        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d2;
        try {
            String data = formatter.format(new Date(System.currentTimeMillis()));
            d2 = df2.parse(data.split(" ")[0] + " " + data.split(" ")[2] );

            //Criação relatorio para guardar o estado e tempo associado à mensagem recebida neste momento. Atribuição desse relatorio ao caso do paciente.
            Relatorio_Paciente relatorio_paciente = new Relatorio_Paciente();
            relatorio_paciente.setCaso(caso);
            relatorio_paciente.setEstado(pacient_info.get("estado"));
            relatorio_paciente.setData(d2);
            relatorioPacienteRepository.save(relatorio_paciente);
        
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        if (caso.getEstado_atual().equals("Internado") || caso.getEstado_atual().equals("Cuidados Intensivos") ) {
            Hospital hospital = medico.getHospital();
            hospital.setNumero_camas_ocupadas(hospital.getNumero_camas_ocupadas() + 1);
            hospitalRepository.save(hospital);
        }
        
        return paciente;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/pacientes/{id}")
    public ResponseEntity<Paciente> updatePaciente(@Valid @RequestBody Map<String, String> pacient_info) throws ResourceNotFoundException {
        
        System.out.println(pacient_info);
        
        Paciente paciente = pacienteRepository.findById(Long.parseLong(pacient_info.get("pacientId")))
        .orElseThrow(() -> new ResourceNotFoundException("Paciente not found for this id :: " + pacient_info.get("pacientId")));
        
        paciente.setNome(pacient_info.get("nome"));
        paciente.setGenero(pacient_info.get("genero"));
        paciente.setIdade(Integer.parseInt(pacient_info.get("idade")));
        paciente.setConcelho(pacient_info.get("concelho"));
        paciente.setRegiao(pacient_info.get("regiao"));
        paciente.setNacionalidade(pacient_info.get("nacionalidade"));
        paciente.setAltura(Integer.parseInt(pacient_info.get("altura")));
        paciente.setPeso(Float.parseFloat(pacient_info.get("peso")));
        paciente.setEstado_atual(pacient_info.get("estado"));
        pacienteRepository.save(paciente);
        
        Caso caso = casoRepository.findByPacienteId(Long.parseLong(pacient_info.get("pacientId")));
        String estado_passado = caso.getEstado_atual();
        caso.setEstado_atual(pacient_info.get("estado"));
        casoRepository.save(caso);
        
        if (!estado_passado.equals(caso.getEstado_atual())) {
            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d2;
            try {
                String data = formatter.format(new Date(System.currentTimeMillis()));
                d2 = df2.parse(data.split(" ")[0] + " " + data.split(" ")[2] );

                //Criação relatorio para guardar o estado e tempo associado à mensagem recebida neste momento. Atribuição desse relatorio ao caso do paciente.
                Relatorio_Paciente relatorio_paciente = new Relatorio_Paciente();
                relatorio_paciente.setCaso(caso);
                relatorio_paciente.setEstado(pacient_info.get("estado"));
                relatorio_paciente.setData(d2);
                relatorioPacienteRepository.save(relatorio_paciente);
            
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Medico medico = medicoRepository.findById(Long.parseLong(pacient_info.get("medico_numero_medico")))
            .orElseThrow(() -> new ResourceNotFoundException("Medico not found for this id :: " + pacient_info.get("medico_numero_medico")));
            Hospital hospital = medico.getHospital();
            if ((caso.getEstado_atual().equals("Internado") || caso.getEstado_atual().equals("Cuidados Intensivos")) && (estado_passado.equals("Confinamento Domiciliário") || estado_passado.equals("Recuperado")) ) {
                hospital.setNumero_camas_ocupadas(hospital.getNumero_camas_ocupadas() + 1);
                hospitalRepository.save(hospital);
            } else if ((caso.getEstado_atual().equals("Confinamento Domiciliário") || caso.getEstado_atual().equals("Recuperado") || caso.getEstado_atual().equals("Óbito")) && (estado_passado.equals("Internado") || estado_passado.equals("Cuidados Intensivos")) ) {
                hospital.setNumero_camas_ocupadas(hospital.getNumero_camas_ocupadas() - 1);
                hospitalRepository.save(hospital);
            }
        }

        
        return ResponseEntity.ok(paciente);
    }

    @DeleteMapping("/pacientes/{id}")
    public Map<String, Boolean> deletePaciente(@PathVariable(value = "id") Long pacienteID)
         throws ResourceNotFoundException {
        Paciente paciente = pacienteRepository.findById(pacienteID)
       .orElseThrow(() -> new ResourceNotFoundException("Paciente not found for this id :: " + pacienteID));
        
        Caso caso = casoRepository.findByPacienteId(pacienteID);
        List<Relatorio_Paciente> relatorios_Paciente = relatorioPacienteRepository.findAllByCaso(caso.getId());
        for (Relatorio_Paciente r: relatorios_Paciente) {
            relatorioPacienteRepository.delete(r);
        }
        casoRepository.delete(caso);
        pacienteRepository.delete(paciente);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}