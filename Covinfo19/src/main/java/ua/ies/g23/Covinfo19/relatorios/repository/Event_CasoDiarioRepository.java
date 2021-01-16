package ua.ies.g23.Covinfo19.relatorios.repository;

import ua.ies.g23.Covinfo19.relatorios.model.Event_CasoDiario;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Event_CasoDiarioRepository extends JpaRepository<Event_CasoDiario, Date> {

    @Query(value = "Select * from event_casodiario order by data limit 1", nativeQuery = true)
	Event_CasoDiario findRecent();
    
}


