package ua.ies.g23.Covinfo19.pacientes_med_hosp.controller;

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
import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Event_Hospitais;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.Event_HospitaisRepository;

@RestController
@RequestMapping("/api/v1")
public class Event_HospitaisController {
    
    @Autowired
    private Event_HospitaisRepository eventRepository;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/public/event_hospitais")
    public ResponseEntity<List<Event_Hospitais>> getAllEvents()
        throws ResourceNotFoundException {
        List<Event_Hospitais> event_hospital = eventRepository.findAll();
        return ResponseEntity.ok().body(event_hospital);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/public/event_hospitais/{id}")
    public ResponseEntity<Event_Hospitais> getEventById(@PathVariable(value = "id") Long eventId)
        throws ResourceNotFoundException {
        Event_Hospitais event_hospital = eventRepository.findById(eventId)
          .orElseThrow(() -> new ResourceNotFoundException("Event not found for this id :: " + eventId));
        return ResponseEntity.ok().body(event_hospital);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/public/event_by_hospital/{id}")
    public List<Event_Hospitais> getEventByHospitalId(@PathVariable(value = "id") Long eventId)
        throws ResourceNotFoundException {
        List<Event_Hospitais> event_hospital = eventRepository.findByHospitalId(String.valueOf(eventId));
        return event_hospital;
    }
}
