package ua.ies.g23.Covinfo19.pacientes_med_hosp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Paciente;


@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>{
    
        //Seleção de pacientes através filtros por todos os seus campos
        @Query(value = "Select * from pacientes where genero like :strgenero and idade between :stridademin and :stridademax and concelho like :strconcelho and regiao like :strregiao and nacionalidade like :strnacionalidade and altura between :stralturamin and :stralturamax and peso between :strpesomin and :strpesomax", nativeQuery = true)
        List<Paciente> findAllFilters(@Param("strgenero") String strgenero,@Param("stridademin") String stridademin, @Param("stridademax") String stridademax ,@Param("strconcelho") String strconcelho,@Param("strregiao") String strregiao,
                @Param("strnacionalidade") String strnacionalidade,@Param("stralturamin") String stralturamin,@Param("stralturamax") String stralturamax,@Param("strpesomin") String strpesomin,@Param("strpesomax") String strpesomax);


        @Query(value = "Select * from pacientes where genero like :strgenero and idade between :stridademin and :stridademax and concelho like :strconcelho and regiao like :strregiao and nacionalidade like :strnacionalidade and altura between :stralturamin and :stralturamax and peso between :strpesomin and :strpesomax /* #pageReq */", nativeQuery = true)
        Page<Paciente> findAllFilters(@Param("strgenero") String strgenero,@Param("stridademin") String stridademin, @Param("stridademax") String stridademax ,@Param("strconcelho") String strconcelho,@Param("strregiao") String strregiao,
                @Param("strnacionalidade") String strnacionalidade,@Param("stralturamin") String stralturamin,@Param("stralturamax") String stralturamax,@Param("strpesomin") String strpesomin,@Param("strpesomax") String strpesomax, @Param("pageReq") PageRequest pageRequest);

                //Seleção de pacientes através filtros por todos os seus campos
        @Query(value = "Select * from pacientes where genero like :strgenero and idade between :stridademin and :stridademax and concelho like :strconcelho and regiao like :strregiao and nacionalidade like :strnacionalidade and altura between :stralturamin and :stralturamax and peso between :strpesomin and :strpesomax and medico_numero_medico in (:strmedico) ", nativeQuery = true)
        List<Paciente> findAllFilters(@Param("strgenero") String strgenero,@Param("stridademin") String stridademin, @Param("stridademax") String stridademax ,@Param("strconcelho") String strconcelho,@Param("strregiao") String strregiao,
                @Param("strnacionalidade") String strnacionalidade,@Param("stralturamin") String stralturamin,@Param("stralturamax") String stralturamax,@Param("strpesomin") String strpesomin,@Param("strpesomax") String strpesomax,@Param("strmedico") List<String> strmedico);
    
        //Seleção de pacientes através do num medico
    @Query(value = "Select * from pacientes where medico_numero_medico = :strmedico and nome like :strnome and paciente_id like :strnum_paciente ", nativeQuery = true)
    List<Paciente> findByMedico(@Param("strmedico") String strmedico, @Param("strnum_paciente") String strnum_paciente, @Param("strnome") String strnome);
    
}
