package ua.ies.g23.Covinfo19.pacientes_med_hosp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Paciente;


@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>{
    
    /*
    //@Query(value = "Select * from demo.pacientes where :strgenero :stridade :strconcelho :strregiao :strnacionalidade :straltura :strpeso", nativeQuery = true)
    @Query(value = "Select * from pacientes", nativeQuery = true)
    List<Paciente> findAllFilters(@Param("strgenero") String strgenero,@Param("stridade") String stridade,@Param("strconcelho") String strconcelho,@Param("strregiao") String strregiao,
            @Param("strnacionalidade") String strnacionalidade,@Param("straltura") String straltura,@Param("strpeso") String strpeso);
          
    @Query(value = "Select * from pacientes", nativeQuery = true)
    List<Paciente> findAllF();
    */
}
