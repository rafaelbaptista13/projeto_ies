package ua.ies.g23.Covinfo19.relatorios.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.ies.g23.Covinfo19.relatorios.model.Caso;


@Repository
public interface CasoRepository extends JpaRepository<Caso, Long>{
    //Seleção caso de um determinado paciente.
    @Query(value = "Select * from casos where paciente_id = :pacienteId", nativeQuery = true)
	Caso findByPacienteId(@Param("pacienteId") long pacienteId);

}
