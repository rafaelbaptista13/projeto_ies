package ua.ies.g23.Covinfo19.relatorios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.ies.g23.Covinfo19.relatorios.model.Relatorio_Paciente;


@Repository
public interface Relatorio_PacienteRepository extends JpaRepository<Relatorio_Paciente, Long>{

}
