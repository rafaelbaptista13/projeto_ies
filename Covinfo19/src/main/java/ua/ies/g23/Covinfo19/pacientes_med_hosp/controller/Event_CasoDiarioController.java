package ua.ies.g23.Covinfo19.pacientes_med_hosp.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import ua.ies.g23.Covinfo19.exception.ResourceNotFoundException;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Event_CasoDiario;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Event_Hospitais;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.Event_CasoDiarioRepository;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.Event_HospitaisRepository;


@RestController
@RequestMapping("/api/v1")
public class Event_CasoDiarioController {
    
    @Autowired
    private Event_CasoDiarioRepository eventRepository;

    @CrossOrigin(origins = "http://192.168.160.215:4200")
    @GetMapping("/public/allevent_casodiario")
    public ResponseEntity<List<Event_CasoDiario>> getEventById()
        throws ResourceNotFoundException {
        List<Event_CasoDiario> event_CasoDiario = eventRepository.findAll();
        return ResponseEntity.ok().body(event_CasoDiario);
    }

    @CrossOrigin(origins = "http://192.168.160.215:4200")
    @GetMapping("/public/event_casodiario")
    public Event_CasoDiario getRecentEvent()
        throws ResourceNotFoundException {
        Event_CasoDiario event_CasoDiario = null;
        try {
          event_CasoDiario = eventRepository.findRecent();
        } catch (Exception e) {

        }
        return event_CasoDiario;
    }
}
