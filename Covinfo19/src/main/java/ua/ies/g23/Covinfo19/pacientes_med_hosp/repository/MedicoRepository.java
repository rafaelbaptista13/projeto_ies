package ua.ies.g23.Covinfo19.pacientes_med_hosp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Medico;


@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long>{
    //Seleção de todos os médicos através de filtros pelos seus campos
    @Query(value = "Select * from medicos where nome like :strnome and hospital_id like :strhospital_id and idade between :stridademin and :stridademax", nativeQuery = true)
	List<Medico> findAllByFilters(@Param("strnome") String strnome,@Param("strhospital_id") String strhospital_id,@Param("stridademin") String stridademin,@Param("stridademax") String stridademax);
	

}
