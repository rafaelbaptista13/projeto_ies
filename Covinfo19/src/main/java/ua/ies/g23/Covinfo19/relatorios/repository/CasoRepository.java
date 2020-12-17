package ua.ies.g23.Covinfo19.relatorios.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ua.ies.g23.Covinfo19.relatorios.model.Caso;


@Repository
public interface CasoRepository extends JpaRepository<Caso, Long>{
    @Query(value = "Select count(*) from casos where estado_atual = 'Confinamento Domiciliário'", nativeQuery = true)
	Collection<Integer> findAllConfinamento();

    @Query(value = "Select count(*) from casos where estado_atual = 'Internamento'", nativeQuery = true)
	Collection<Integer> findAllInternado();

    @Query(value = "Select count(*) from casos where estado_atual = 'Morto'", nativeQuery = true)
	Collection<Integer> findAllMorto();

    @Query(value = "Select count(*) from casos where estado_atual = 'Cuidados Intensivos'", nativeQuery = true)
	Collection<Integer> findAllCuidados();

    @Query(value = "Select count(*) from casos where estado_atual = 'Cuidados Intensivos' or estado_atual = 'Internamento' or estado_atual = 'Confinamento Domiciliário'", nativeQuery = true)
	Collection<Integer> findAllAtivos();

    @Query(value = "Select count(*) from casos where estado_atual = 'Recuperado'", nativeQuery = true)
	Collection<Integer> findAllRecuperados();
   

}
