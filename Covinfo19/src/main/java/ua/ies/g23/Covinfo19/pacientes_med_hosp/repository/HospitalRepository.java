package ua.ies.g23.Covinfo19.pacientes_med_hosp.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Hospital;


@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long>{
    //Seleção de hospitais através de filtros por todos os seus campos
    @Query(value = "Select * from hospitais where nome like :strnome and concelho like :strconcelho and regiao like :strregiao and numero_camas between :strnumero_camasmin and :strnumero_camasmax and numero_camas_ocupadas between :strnumero_camas_ocupadasmin and :strnumero_camas_ocupadasmax", nativeQuery = true)
    List<Hospital> findAllFilters(@Param("strnome") String strnome,@Param("strconcelho") String strconcelho,@Param("strregiao") String strregiao,@Param("strnumero_camasmin") String strnumero_camasmin,@Param("strnumero_camasmax") String strnumero_camasmax,@Param("strnumero_camas_ocupadasmin") String strnumero_camas_ocupadasmin,
                                    @Param("strnumero_camas_ocupadasmax") String strnumero_camas_ocupadasmax);
    
    @Query(value = "Select * from hospitais where nome like :strnome and concelho like :strconcelho and regiao like :strregiao and numero_camas between :strnumero_camasmin and :strnumero_camasmax and numero_camas_ocupadas between :strnumero_camas_ocupadasmin and :strnumero_camas_ocupadasmax /* #pageReq */", nativeQuery = true)
    List<Hospital> findAllFilters(@Param("strnome") String strnome,@Param("strconcelho") String strconcelho,@Param("strregiao") String strregiao,@Param("strnumero_camasmin") String strnumero_camasmin,@Param("strnumero_camasmax") String strnumero_camasmax,@Param("strnumero_camas_ocupadasmin") String strnumero_camas_ocupadasmin,
                                    @Param("strnumero_camas_ocupadasmax") String strnumero_camas_ocupadasmax, @Param("pageReq") PageRequest pageRequest);

    @Query(value = "Select * from hospitais where id = strid", nativeQuery = true)
    Hospital findById(@Param("strid") String strid);
}
