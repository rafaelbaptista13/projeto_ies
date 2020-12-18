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
    @Query(value = "Select count(*) from casos where estado_atual = 'Cuidados Intensivos' or estado_atual = 'Internado' or estado_atual = 'Confinamento Domiciliário'", nativeQuery = true)
	Collection<Integer> findAllAtivos();

    @Query(value = "Select count(*) from casos where estado_atual = :strestado", nativeQuery = true)
	Collection<Integer> findAllByEstado(@Param("strestado") String estado_atual);

    @Query(value = "Select * from casos where paciente_id = :pacienteId", nativeQuery = true)
	Caso findByPacienteId(@Param("pacienteId") long pacienteId);

    /*
    @Query(value = "Select * from casos where :strestado :strgenero :stridade :strconcelho :strregiao :strnacionalidade :straltura :strpeso", nativeQuery = true)
    //@Query(value = "Select * from casos where " +strestado + strgenero + stridade + strconcelho + strregiao + strnacionalidade + straltura + strpeso, nativeQuery = true)
    //@Query(value = "Select * from casos where estado_atual like '%:strestado%' and genero like :strgenero :stridade :strconcelho :strregiao :strnacionalidade :straltura :strpeso", nativeQuery = true)
    List<Caso> findAllFilters(@Param("strestado") String strestado,@Param("strgenero") String strgenero,@Param("stridade") String stridade,@Param("strconcelho") String strconcelho,@Param("strregiao") String strregiao,
            @Param("strnacionalidade") String strnacionalidade,@Param("straltura") String straltura,@Param("strpeso") String strpeso);
       */
}
