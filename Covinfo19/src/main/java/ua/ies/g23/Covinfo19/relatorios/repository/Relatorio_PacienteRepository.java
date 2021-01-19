package ua.ies.g23.Covinfo19.relatorios.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.ies.g23.Covinfo19.relatorios.model.Relatorio_Paciente;


@Repository
public interface Relatorio_PacienteRepository extends JpaRepository<Relatorio_Paciente, Long>{

    @Query(value = "Select * from relatorio_pacientes where caso_id = :caso_id order by data limit 1", nativeQuery = true)
    Relatorio_Paciente findRecentByCaso(@Param("caso_id") long caso_id);
    
    @Query(value = "Select * from relatorio_pacientes where caso_id = :caso_id order by data /* #pageReq */", nativeQuery = true)
    Page<Relatorio_Paciente> findAllByCaso(@Param("caso_id") long caso_id, @Param("pageReq") PageRequest pageRequest);

    @Query(value = "Select * from relatorio_pacientes where caso_id = :caso_id order by data", nativeQuery = true)
    List<Relatorio_Paciente> findAllByCaso(@Param("caso_id") long caso_id);
}
