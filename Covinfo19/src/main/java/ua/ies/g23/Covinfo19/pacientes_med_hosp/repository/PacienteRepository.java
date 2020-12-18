package ua.ies.g23.Covinfo19.pacientes_med_hosp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Paciente;


@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>{
    
    
    @Query(value = "Select * from pacientes where genero like :strgenero and idade between :stridademin and :stridademax and concelho like :strconcelho and regiao like :strregiao and nacionalidade like :strnacionalidade and altura between :stralturamin and :stralturamax and peso between :strpesomin and :strpesomax", nativeQuery = true)
    List<Paciente> findAllFilters(@Param("strgenero") String strgenero,@Param("stridademin") String stridademin, @Param("stridademax") String stridademax ,@Param("strconcelho") String strconcelho,@Param("strregiao") String strregiao,
            @Param("strnacionalidade") String strnacionalidade,@Param("stralturamin") String stralturamin,@Param("stralturamax") String stralturamax,@Param("strpesomin") String strpesomin,@Param("strpesomax") String strpesomax);
    
}
