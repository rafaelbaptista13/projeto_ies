package ua.ies.g23.Covinfo19.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.ies.g23.Covinfo19.model.Paciente;


@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>{

}
