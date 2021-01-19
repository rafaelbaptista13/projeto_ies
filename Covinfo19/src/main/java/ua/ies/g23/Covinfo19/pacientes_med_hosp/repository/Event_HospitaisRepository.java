package ua.ies.g23.Covinfo19.pacientes_med_hosp.repository;

import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Event_Hospitais;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Event_HospitaisRepository extends JpaRepository<Event_Hospitais, Long> {

    @Query(value = "Select * from event_hospitais where hospital_id = :strid order by data", nativeQuery = true)
    List<Event_Hospitais> findByHospitalId(@Param("strid") String strid);
    
}
