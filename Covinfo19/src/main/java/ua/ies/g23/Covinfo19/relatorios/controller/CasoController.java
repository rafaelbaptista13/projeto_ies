package ua.ies.g23.Covinfo19.relatorios.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ua.ies.g23.Covinfo19.relatorios.model.Caso;
import ua.ies.g23.Covinfo19.relatorios.model.Relatorio_Paciente;
import ua.ies.g23.Covinfo19.exception.ResourceNotFoundException;
import ua.ies.g23.Covinfo19.relatorios.repository.CasoRepository;
import ua.ies.g23.Covinfo19.relatorios.repository.Relatorio_PacienteRepository;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Medico;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Paciente;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.MedicoRepository;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.PacienteRepository;

@RestController
@RequestMapping("/api/v1")
public class CasoController {
    @Autowired
    private CasoRepository CasoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private Relatorio_PacienteRepository relatorioRepository;
    @Autowired
    private MedicoRepository medicoRepository;


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/public/casos/numerospandemia")
    public HashMap<String,Integer> getNumerosPandemia(
            @RequestParam(required = false) String genero, @RequestParam(required = false) Integer idademax,
            @RequestParam(required = false) Integer idademin, @RequestParam(required = false) String concelho,
            @RequestParam(required = false) String regiao, @RequestParam(required = false) String nacionalidade,
            @RequestParam(required = false) Integer alturamin, @RequestParam(required = false) Integer alturamax,
            @RequestParam(required = false) Double pesomin, @RequestParam(required = false) Double pesomax) {

        String strgenero = "";
        if (genero != null) {
            strgenero = genero;
        } else {
            strgenero = "%";
        }

        String stridademin = "";
        if (idademin != null) {
            stridademin = idademin.toString();
        } else {
            stridademin = "0";
        }

        String stridademax = "";
        if (idademax != null) {
            stridademax = idademax.toString();
        } else {
            stridademax = "300";
        }

        String strconcelho = "";
        if (concelho != null) {
            strconcelho = concelho;
        } else {
            strconcelho = "%";
        }

        String strregiao = "";
        if (regiao != null) {
            strregiao = regiao;
        } else {
            strregiao = "%";
        }

        String strnacionalidade = "";
        if (nacionalidade != null) {
            strnacionalidade = nacionalidade;
        } else {
            strnacionalidade = "%";
        }

        String stralturamin = "";
        if (alturamin != null) {
            stralturamin = alturamin.toString();
        } else {
            stralturamin = "0";
        }

        String stralturamax = "";
        if (alturamax != null) {
            stralturamax = alturamax.toString();
        } else {
            stralturamax = "300";
        }

        String strpesomin = "";
        if (pesomin != null) {
            strpesomin = pesomin.toString();
        } else {
            strpesomin = "0";
        }

        String strpesomax = "";
        if (pesomax != null) {
            strpesomax = pesomax.toString();
        } else {
            strpesomax = "500";
        }

        List<Paciente> pacientes = pacienteRepository.findAllFilters(strgenero, stridademin, stridademax, strconcelho, strregiao,
                    strnacionalidade, stralturamin, stralturamax, strpesomin, strpesomax);

        int casosativos = 0;
        int casosinternados = 0;
        int casoscuidados = 0;
        int casosrecuperados = 0;
        int casosobito = 0;
        for (Paciente p : pacientes) {
            if (p.getEstado_atual().equals("Confinamento Domiciliário")) {
                casosativos +=1;
            } else if (p.getEstado_atual().equals("Internado")) {
                casosinternados +=1;
                casosativos +=1;
            } else if (p.getEstado_atual().equals("Cuidados Intensivos")) {
                casoscuidados +=1;
                casosativos +=1;
            } else if (p.getEstado_atual().equals("Recuperado")) {
                casosrecuperados +=1;
            } else if (p.getEstado_atual().equals("Óbito")) {
                casosobito +=1;
            }
        }
        HashMap<String, Integer> retorno = new HashMap<String, Integer>();
        retorno.put("Ativos", casosativos);
        retorno.put("Internados", casosinternados);
        retorno.put("Cuidados Intensivos", casoscuidados);
        retorno.put("Recuperados", casosrecuperados);
        retorno.put("Óbitos", casosobito);
        return retorno;

    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/public/casos/grafico/curva_diaria/daily")
    public Integer getDailyCasosGraficoCurvaEvolucao(@RequestParam(required = false) String genero,
            @RequestParam(required = false) Integer idademax, @RequestParam(required = false) Integer idademin,
            @RequestParam(required = false) String concelho, @RequestParam(required = false) String regiao,
            @RequestParam(required = false) String nacionalidade, @RequestParam(required = false) Integer alturamin,
            @RequestParam(required = false) Integer alturamax, @RequestParam(required = false) Double pesomin,
            @RequestParam(required = false) Double pesomax) {

        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal2.set(Calendar.HOUR_OF_DAY, 23);
        cal2.set(Calendar.MINUTE, 59);
        cal2.set(Calendar.SECOND, 59);

        Date date = cal.getTime();
        Date date_fim = cal2.getTime();
        String strgenero = "";
        if (genero != null) {
            strgenero = genero;
        } else {
            strgenero = "%";
        }

        String stridademin = "";
        if (idademin != null) {
            stridademin = idademin.toString();
        } else {
            stridademin = "0";
            idademin = 0;
        }

        String stridademax = "";
        if (idademax != null) {
            stridademax = idademax.toString();
        } else {
            stridademax = "300";
            idademax = 300;
        }

        String strconcelho = "";
        if (concelho != null) {
            strconcelho = concelho;
        } else {
            strconcelho = "%";
        }

        String strregiao = "";
        if (regiao != null) {
            strregiao = regiao;
        } else {
            strregiao = "%";
        }

        String strnacionalidade = "";
        if (nacionalidade != null) {
            strnacionalidade = nacionalidade;
        } else {
            strnacionalidade = "%";
        }

        String stralturamin = "";
        if (alturamin != null) {
            stralturamin = alturamin.toString();
        } else {
            stralturamin = "0";
            alturamin = 0;
        }

        String stralturamax = "";
        if (alturamax != null) {
            stralturamax = alturamax.toString();
        } else {
            stralturamax = "300";
            alturamax = 300;
        }

        String strpesomin = "";
        if (pesomin != null) {
            strpesomin = pesomin.toString();
        } else {
            strpesomin = "0";
            pesomin = Double.valueOf(0);
        }

        String strpesomax = "";
        if (pesomax != null) {
            strpesomax = pesomax.toString();
        } else {
            strpesomax = "500";
            pesomax = Double.valueOf(500);
        }
        

        List<Paciente> pacientes = pacienteRepository.findAllFilters(strgenero, stridademin, stridademax, strconcelho, strregiao,
                    strnacionalidade, stralturamin, stralturamax, strpesomin, strpesomax);
        List<Caso> casos_pacientes = new ArrayList<>();

        for (Paciente p: pacientes) {
            if (p.getEstado_atual().equals("Confinamento Domiciliário")
                        || p.getEstado_atual().equals("Internado")
                        || p.getEstado_atual().equals("Cuidados Intensivos")) {
                Caso caso = CasoRepository.findByPacienteId(p.getPacienteId());
                if (caso == null) continue;
                casos_pacientes.add(caso);
            }
        }


        int contador = 0;
        for (Caso c: casos_pacientes) {
            Relatorio_Paciente relatorio = relatorioRepository.findRecentByCaso(c.getId());
            if (relatorio.getData().after(date) && relatorio.getData().before(date_fim)) {
                contador += 1;
            }
        }

        return contador;
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/public/casos/grafico/curva_diaria")
    public Map<String, Integer> getAllCasosGraficoCurvaEvolucao(@RequestParam(required = false) String genero,
            @RequestParam(required = false) Integer idademax, @RequestParam(required = false) Integer idademin,
            @RequestParam(required = false) String concelho, @RequestParam(required = false) String regiao,
            @RequestParam(required = false) String nacionalidade, @RequestParam(required = false) Integer alturamin,
            @RequestParam(required = false) Integer alturamax, @RequestParam(required = false) Double pesomin,
            @RequestParam(required = false) Double pesomax) {

        Map<String, Integer> dicionarioCasos = new HashMap<String, Integer>();
        Map<Date, Date> dicionarioDias = new HashMap<Date, Date>();

        // DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal2.set(Calendar.HOUR_OF_DAY, 23);
        cal2.set(Calendar.MINUTE, 59);
        cal2.set(Calendar.SECOND, 59);

        Date date = cal.getTime();
        Date date_fim = cal2.getTime();
        dicionarioDias.put(date, date_fim);
        for (int i = 1; i <= 14; i++) {
            cal.add(Calendar.DATE, -1);
            cal2.add(Calendar.DATE, -1);
            date = cal.getTime();
            date_fim = cal2.getTime();
            dicionarioDias.put(date, date_fim);
        }

        String strgenero = "";
        if (genero != null) {
            strgenero = genero;
        } else {
            strgenero = "%";
        }

        String stridademin = "";
        if (idademin != null) {
            stridademin = idademin.toString();
        } else {
            stridademin = "0";
            idademin = 0;
        }

        String stridademax = "";
        if (idademax != null) {
            stridademax = idademax.toString();
        } else {
            stridademax = "300";
            idademax = 300;
        }

        String strconcelho = "";
        if (concelho != null) {
            strconcelho = concelho;
        } else {
            strconcelho = "%";
        }

        String strregiao = "";
        if (regiao != null) {
            strregiao = regiao;
        } else {
            strregiao = "%";
        }

        String strnacionalidade = "";
        if (nacionalidade != null) {
            strnacionalidade = nacionalidade;
        } else {
            strnacionalidade = "%";
        }

        String stralturamin = "";
        if (alturamin != null) {
            stralturamin = alturamin.toString();
        } else {
            stralturamin = "0";
            alturamin = 0;
        }

        String stralturamax = "";
        if (alturamax != null) {
            stralturamax = alturamax.toString();
        } else {
            stralturamax = "300";
            alturamax = 300;
        }

        String strpesomin = "";
        if (pesomin != null) {
            strpesomin = pesomin.toString();
        } else {
            strpesomin = "0";
            pesomin = Double.valueOf(0);
        }

        String strpesomax = "";
        if (pesomax != null) {
            strpesomax = pesomax.toString();
        } else {
            strpesomax = "500";
            pesomax = Double.valueOf(500);
        }
        

        List<Paciente> pacientes = pacienteRepository.findAllFilters(strgenero, stridademin, stridademax, strconcelho, strregiao,
                    strnacionalidade, stralturamin, stralturamax, strpesomin, strpesomax);

        List<Caso> casos_pacientes = new ArrayList<>();
        for (Paciente p: pacientes) {
            if (p.getEstado_atual().equals("Confinamento Domiciliário")
                        || p.getEstado_atual().equals("Internado")
                        || p.getEstado_atual().equals("Cuidados Intensivos")) {
                Caso caso = CasoRepository.findByPacienteId(p.getPacienteId());
                if (caso == null) continue;
                casos_pacientes.add(caso);
            }
        }

        HashMap<Caso, Relatorio_Paciente> caso_relatorio = new HashMap<>();
        for (Caso c: casos_pacientes) {
            Relatorio_Paciente relatorio = relatorioRepository.findRecentByCaso(c.getId());
            if (relatorio == null) continue;
            caso_relatorio.put(c, relatorio);
        }

        List<Caso> casos_verificados = new ArrayList<>();
        
        for (Date dia : dicionarioDias.keySet()) {
            Date data_fim_dia = dicionarioDias.get(dia);

            List<Caso> casos = new ArrayList<Caso>();
            for (Caso caso : caso_relatorio.keySet()) {
                Relatorio_Paciente relatorio = caso_relatorio.get(caso);
                if (relatorio.getData().after(dia) && relatorio.getData().before(data_fim_dia)) {
                    casos.add(caso);
                    casos_verificados.add(caso);
                }
            }

            for (Caso c: casos_verificados) {
                caso_relatorio.remove(c);
            }
            casos_verificados = new ArrayList<>();

            String mes = dia.toString().split(" ")[1];
            if (mes.equals("Jan")) {
                mes = "1";
            } else if (mes.equals("Feb")) {
                mes = "2";
            } else if (mes.equals("Mar")) {
                mes = "3";
            } else if (mes.equals("Apr")) {
                mes = "4";
            } else if (mes.equals("May")) {
                mes = "5";
            } else if (mes.equals("Jun")) {
                mes = "6";
            } else if (mes.equals("Jul")) {
                mes = "7";
            } else if (mes.equals("Aug")) {
                mes = "8";
            } else if (mes.equals("Set")) {
                mes = "9";
            } else if (mes.equals("Oct")) {
                mes = "10";
            } else if (mes.equals("Nov")) {
                mes = "11";
            } else if (mes.equals("Dec")) {
                mes = "12";
            }

            String chave = dia.toString().split(" ")[5] + "-" + mes + "-" + dia.toString().split(" ")[2];

            dicionarioCasos.put(chave, casos.size());
        }
        return dicionarioCasos;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/public/casos/grafico/peso")
    public List<Double> getAllCasosGraficoPeso(@RequestParam(required = false) String genero,
            @RequestParam(required = false) Integer idademax, @RequestParam(required = false) Integer idademin,
            @RequestParam(required = false) String concelho, @RequestParam(required = false) String regiao,
            @RequestParam(required = false) String nacionalidade, @RequestParam(required = false) Integer alturamin,
            @RequestParam(required = false) Integer alturamax, @RequestParam(required = false) Double pesomin,
            @RequestParam(required = false) Double pesomax) {

        List<Integer> listaPesos = new ArrayList<Integer>();
        List<Double> listaPesosPercentagem = new ArrayList<Double>();
        int somaCasosTodos = 0;

        String strgenero = "";
        if (genero != null) {
            strgenero = genero;
        } else {
            strgenero = "%";
        }

        String stridademin = "";
        if (idademin != null) {
            stridademin = idademin.toString();
        } else {
            stridademin = "0";
            idademin = 0;
        }

        String stridademax = "";
        if (idademax != null) {
            stridademax = idademax.toString();
        } else {
            stridademax = "300";
            idademax = 300;
        }

        String strconcelho = "";
        if (concelho != null) {
            strconcelho = concelho;
        } else {
            strconcelho = "%";
        }

        String strregiao = "";
        if (regiao != null) {
            strregiao = regiao;
        } else {
            strregiao = "%";
        }

        String strnacionalidade = "";
        if (nacionalidade != null) {
            strnacionalidade = nacionalidade;
        } else {
            strnacionalidade = "%";
        }

        String stralturamin = "";
        if (alturamin != null) {
            stralturamin = alturamin.toString();
        } else {
            stralturamin = "0";
            alturamin = 0;
        }

        String stralturamax = "";
        if (alturamax != null) {
            stralturamax = alturamax.toString();
        } else {
            stralturamax = "300";
            alturamax = 300;
        }

        String strpesomin = "";
        if (pesomin != null) {
            strpesomin = pesomin.toString();
        } else {
            strpesomin = "0";
            pesomin = Double.valueOf(0);
        }

        String strpesomax = "";
        if (pesomax != null) {
            strpesomax = pesomax.toString();
        } else {
            strpesomax = "500";
            pesomax = Double.valueOf(500);
        }

        for (int pesominima = 11; pesominima <= 91; pesominima += 10) {
            String strpesominima = (pesominima == 11) ? "0" : String.valueOf(pesominima);
            String strpesomaxima = (pesominima == 91) ? "300" : String.valueOf(pesominima + 9);

            List<Paciente> pacientes;

            pacientes = pacienteRepository.findAllFilters(strgenero, stridademin, stridademax, strconcelho, strregiao,
                    strnacionalidade, stralturamin, stralturamax, strpesominima, strpesomaxima);
            
            int contador = 0;
            for (Paciente p : pacientes) {
                if (p.getEstado_atual().equals("Confinamento Domiciliário")
                        || p.getEstado_atual().equals("Internado")
                        || p.getEstado_atual().equals("Cuidados Intensivos")) {
                    if (p.getPeso() >= pesomin && p.getPeso() <= pesomax) {
                        contador += 1;
                    }
                }
            }

            listaPesos.add(contador);
            somaCasosTodos += contador;
        }

        if (somaCasosTodos == 0) {
            for (Integer integer : listaPesos) {
                listaPesosPercentagem.add((double) 0);
            }
        } else {
            for (Integer integer : listaPesos) {
                listaPesosPercentagem.add((double) integer / somaCasosTodos * 100);
            }
        }

        return listaPesosPercentagem;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/public/casos/grafico/altura")
    public List<Double> getAllCasosGraficoAltura(@RequestParam(required = false) String genero,
            @RequestParam(required = false) Integer idademax, @RequestParam(required = false) Integer idademin,
            @RequestParam(required = false) String concelho, @RequestParam(required = false) String regiao,
            @RequestParam(required = false) String nacionalidade, @RequestParam(required = false) Integer alturamin,
            @RequestParam(required = false) Integer alturamax, @RequestParam(required = false) Double pesomin,
            @RequestParam(required = false) Double pesomax) {

        List<Integer> listaAlturas = new ArrayList<Integer>();
        List<Double> listaAlturasPercentagem = new ArrayList<Double>();
        int somaCasosTodos = 0;

        String strgenero = "";
        if (genero != null) {
            strgenero = genero;
        } else {
            strgenero = "%";
        }

        String stridademin = "";
        if (idademin != null) {
            stridademin = idademin.toString();
        } else {
            stridademin = "0";
            idademin = 0;
        }

        String stridademax = "";
        if (idademax != null) {
            stridademax = idademax.toString();
        } else {
            stridademax = "300";
            idademax = 300;
        }

        String strconcelho = "";
        if (concelho != null) {
            strconcelho = concelho;
        } else {
            strconcelho = "%";
        }

        String strregiao = "";
        if (regiao != null) {
            strregiao = regiao;
        } else {
            strregiao = "%";
        }

        String strnacionalidade = "";
        if (nacionalidade != null) {
            strnacionalidade = nacionalidade;
        } else {
            strnacionalidade = "%";
        }

        String stralturamin = "";
        if (alturamin != null) {
            stralturamin = alturamin.toString();
        } else {
            stralturamin = "0";
            alturamin = 0;
        }

        String stralturamax = "";
        if (alturamax != null) {
            stralturamax = alturamax.toString();
        } else {
            stralturamax = "300";
            alturamax = 300;
        }

        String strpesomin = "";
        if (pesomin != null) {
            strpesomin = pesomin.toString();
        } else {
            strpesomin = "0";
        }

        String strpesomax = "";
        if (pesomax != null) {
            strpesomax = pesomax.toString();
        } else {
            strpesomax = "500";
        }

        for (int alturaminima = 141; alturaminima <= 201; alturaminima += 10) {
            String stralturaminima = (alturaminima == 141) ? "0" : String.valueOf(alturaminima);
            String stralturamaxima = (alturaminima == 201) ? "300" : String.valueOf(alturaminima + 9);

            List<Paciente> pacientes;

            pacientes = pacienteRepository.findAllFilters(strgenero, stridademin, stridademax, strconcelho, strregiao,
                    strnacionalidade, stralturaminima, stralturamaxima, strpesomin, strpesomax);
            
            int contador = 0;
            for (Paciente p : pacientes) {
                if (p.getEstado_atual().equals("Confinamento Domiciliário")
                        || p.getEstado_atual().equals("Internado")
                        || p.getEstado_atual().equals("Cuidados Intensivos")) {
                    if (p.getAltura() >= alturamin && p.getAltura() <= alturamax) {
                        contador += 1;
                    }
                }
            }

            listaAlturas.add(contador);
            somaCasosTodos += contador;
        }

        if (somaCasosTodos == 0) {
            for (Integer integer : listaAlturas) {
                listaAlturasPercentagem.add((double) 0);
            }
        } else {
            for (Integer integer : listaAlturas) {
                listaAlturasPercentagem.add((double) integer / somaCasosTodos * 100);
            }
        }
        return listaAlturasPercentagem;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/public/casos/grafico/genero")
    public List<Double> getAllCasosGraficoGenero(@RequestParam(required = false) String genero,
            @RequestParam(required = false) Integer idademax, @RequestParam(required = false) Integer idademin,
            @RequestParam(required = false) String concelho, @RequestParam(required = false) String regiao,
            @RequestParam(required = false) String nacionalidade, @RequestParam(required = false) Integer alturamin,
            @RequestParam(required = false) Integer alturamax, @RequestParam(required = false) Double pesomin,
            @RequestParam(required = false) Double pesomax) {

        List<Integer> listaCasosGenero = new ArrayList<Integer>();
        List<Double> listaCasosGeneroPercentagem = new ArrayList<Double>();
        int somaCasosTodos = 0;

        String strgenero = "";
        if (genero != null) {
            strgenero = genero;
        } else {
            strgenero = "%";
        }

        String stridademin = "";
        if (idademin != null) {
            stridademin = idademin.toString();
        } else {
            stridademin = "0";
            idademin = 0;
        }

        String stridademax = "";
        if (idademax != null) {
            stridademax = idademax.toString();
        } else {
            stridademax = "300";
            idademax = 300;
        }

        String strconcelho = "";
        if (concelho != null) {
            strconcelho = concelho;
        } else {
            strconcelho = "%";
        }

        String strregiao = "";
        if (regiao != null) {
            strregiao = regiao;
        } else {
            strregiao = "%";
        }

        String strnacionalidade = "";
        if (nacionalidade != null) {
            strnacionalidade = nacionalidade;
        } else {
            strnacionalidade = "%";
        }

        String stralturamin = "";
        if (alturamin != null) {
            stralturamin = alturamin.toString();
        } else {
            stralturamin = "0";
        }

        String stralturamax = "";
        if (alturamax != null) {
            stralturamax = alturamax.toString();
        } else {
            stralturamax = "300";
        }

        String strpesomin = "";
        if (pesomin != null) {
            strpesomin = pesomin.toString();
        } else {
            strpesomin = "0";
        }

        String strpesomax = "";
        if (pesomax != null) {
            strpesomax = pesomax.toString();
        } else {
            strpesomax = "500";
        }

        ArrayList<String> generos = new ArrayList<String>();
        generos.add("Masculino");
        generos.add("Feminino");

        for (String genero_nome : generos) {
            List<Paciente> pacientes;

            pacientes = pacienteRepository.findAllFilters(genero_nome, stridademin, stridademax, strconcelho, strregiao,
                    strnacionalidade, stralturamin, stralturamax, strpesomin, strpesomax);

            int contador = 0;
            for (Paciente p : pacientes) {
                if (p.getEstado_atual().equals("Confinamento Domiciliário")
                        || p.getEstado_atual().equals("Internado")
                        || p.getEstado_atual().equals("Cuidados Intensivos")) {
                    if (strgenero.equals("%") || strgenero.equals(genero_nome)) {
                        contador += 1;
                    }
                }
            }

            listaCasosGenero.add(contador);
            somaCasosTodos += contador;
        }

        if (somaCasosTodos == 0) {
            for (Integer integer : listaCasosGenero) {
                listaCasosGeneroPercentagem.add((double) 0);
            }
        } else {
            for (Integer integer : listaCasosGenero) {
                listaCasosGeneroPercentagem.add((double) integer / somaCasosTodos * 100);
            }
        }

        return listaCasosGeneroPercentagem;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/public/casos/grafico/regiao")
    public List<Double> getAllCasosGraficoRegiao(@RequestParam(required = false) String genero,
            @RequestParam(required = false) Integer idademax, @RequestParam(required = false) Integer idademin,
            @RequestParam(required = false) String concelho, @RequestParam(required = false) String regiao,
            @RequestParam(required = false) String nacionalidade, @RequestParam(required = false) Integer alturamin,
            @RequestParam(required = false) Integer alturamax, @RequestParam(required = false) Double pesomin,
            @RequestParam(required = false) Double pesomax) {

        List<Integer> listaCasosRegioes = new ArrayList<Integer>();
        List<Double> listaCasosRegioesPercentagem = new ArrayList<Double>();
        int somaCasosTodos = 0;

        String strgenero = "";
        if (genero != null) {
            strgenero = genero;
        } else {
            strgenero = "%";
        }

        String stridademin = "";
        if (idademin != null) {
            stridademin = idademin.toString();
        } else {
            stridademin = "0";
            idademin = 0;
        }

        String stridademax = "";
        if (idademax != null) {
            stridademax = idademax.toString();
        } else {
            stridademax = "300";
            idademax = 300;
        }

        String strconcelho = "";
        if (concelho != null) {
            strconcelho = concelho;
        } else {
            strconcelho = "%";
        }

        String strregiao = "";
        if (regiao != null) {
            strregiao = regiao;
        } else {
            strregiao = "%";
        }

        String strnacionalidade = "";
        if (nacionalidade != null) {
            strnacionalidade = nacionalidade;
        } else {
            strnacionalidade = "%";
        }

        String stralturamin = "";
        if (alturamin != null) {
            stralturamin = alturamin.toString();
        } else {
            stralturamin = "0";
        }

        String stralturamax = "";
        if (alturamax != null) {
            stralturamax = alturamax.toString();
        } else {
            stralturamax = "300";
        }

        String strpesomin = "";
        if (pesomin != null) {
            strpesomin = pesomin.toString();
        } else {
            strpesomin = "0";
        }

        String strpesomax = "";
        if (pesomax != null) {
            strpesomax = pesomax.toString();
        } else {
            strpesomax = "500";
        }

        ArrayList<String> regioes = new ArrayList<String>();
        regioes.add("Norte");
        regioes.add("Centro");
        regioes.add("Lisboa e Vale do Tejo");
        regioes.add("Alentejo");
        regioes.add("Algarve");
        regioes.add("Açores");
        regioes.add("Madeira");

        for (String regiao_nome : regioes) {
            List<Paciente> pacientes;

            pacientes = pacienteRepository.findAllFilters(strgenero, stridademin, stridademax, strconcelho, regiao_nome,
                    strnacionalidade, stralturamin, stralturamax, strpesomin, strpesomax);

            int contador = 0;
            for (Paciente p : pacientes) {
                if (p.getEstado_atual().equals("Confinamento Domiciliário")
                        || p.getEstado_atual().equals("Internado")
                        || p.getEstado_atual().equals("Cuidados Intensivos")) {
                    if (strregiao.equals("%") || strregiao.equals(regiao_nome)) {
                        contador += 1;
                    }
                }
            }

            listaCasosRegioes.add(contador);
            somaCasosTodos += contador;
        }

        if (somaCasosTodos == 0) {
            for (Integer integer : listaCasosRegioes) {
                listaCasosRegioesPercentagem.add((double) 0);
            }
        } else {
            for (Integer integer : listaCasosRegioes) {
                listaCasosRegioesPercentagem.add((double) integer / somaCasosTodos * 100);
            }
        }

        return listaCasosRegioesPercentagem;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/public/casos/grafico/idade")
    public List<Double> getAllCasosGraficoIdade(@RequestParam(required = false) String genero,
            @RequestParam(required = false) Integer idademax, @RequestParam(required = false) Integer idademin,
            @RequestParam(required = false) String concelho, @RequestParam(required = false) String regiao,
            @RequestParam(required = false) String nacionalidade, @RequestParam(required = false) Integer alturamin,
            @RequestParam(required = false) Integer alturamax, @RequestParam(required = false) Double pesomin,
            @RequestParam(required = false) Double pesomax) {

        List<Integer> listaIdades = new ArrayList<Integer>();
        List<Double> listaIdadesPercentagem = new ArrayList<Double>();
        int somaCasosTodos = 0;

        String strgenero = "";
        if (genero != null) {
            strgenero = genero;
        } else {
            strgenero = "%";
        }

        String stridademin = "";
        if (idademin != null) {
            stridademin = idademin.toString();
        } else {
            stridademin = "0";
            idademin = 0;
        }

        String stridademax = "";
        if (idademax != null) {
            stridademax = idademax.toString();
        } else {
            stridademax = "300";
            idademax = 300;
        }

        String strconcelho = "";
        if (concelho != null) {
            strconcelho = concelho;
        } else {
            strconcelho = "%";
        }

        String strregiao = "";
        if (regiao != null) {
            strregiao = regiao;
        } else {
            strregiao = "%";
        }

        String strnacionalidade = "";
        if (nacionalidade != null) {
            strnacionalidade = nacionalidade;
        } else {
            strnacionalidade = "%";
        }

        String stralturamin = "";
        if (alturamin != null) {
            stralturamin = alturamin.toString();
        } else {
            stralturamin = "0";
        }

        String stralturamax = "";
        if (alturamax != null) {
            stralturamax = alturamax.toString();
        } else {
            stralturamax = "300";
        }

        String strpesomin = "";
        if (pesomin != null) {
            strpesomin = pesomin.toString();
        } else {
            strpesomin = "0";
        }

        String strpesomax = "";
        if (pesomax != null) {
            strpesomax = pesomax.toString();
        } else {
            strpesomax = "500";
        }

        for (int idademinima = 1; idademinima <= 81; idademinima += 10) {
            String stridademinima = String.valueOf(idademinima);
            String stridademaxima = (idademinima == 81) ? "300" : String.valueOf(idademinima + 9);

            List<Paciente> pacientes;

            pacientes = pacienteRepository.findAllFilters(strgenero, stridademinima, stridademaxima, strconcelho,
                    strregiao, strnacionalidade, stralturamin, stralturamax, strpesomin, strpesomax);

            int contador = 0;
            for (Paciente p : pacientes) {
                if (p.getEstado_atual().equals("Confinamento Domiciliário")
                        || p.getEstado_atual().equals("Internado")
                        || p.getEstado_atual().equals("Cuidados Intensivos")) {
                    if (p.getIdade() >= idademin && p.getIdade() <= idademax) {
                        contador += 1;
                    }
                }
            }

            listaIdades.add(contador);
            somaCasosTodos += contador;
        }

        if (somaCasosTodos == 0) {
            for (Integer integer : listaIdades) {
                listaIdadesPercentagem.add((double) 0);
            }
        } else {
            for (Integer integer : listaIdades) {
                listaIdadesPercentagem.add((double) integer / somaCasosTodos * 100);
            }
        }

        return listaIdadesPercentagem;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/public/casos/count")
    public int getAllCasosCount(@RequestParam(required = false) String estado,
            @RequestParam(required = false) String genero, @RequestParam(required = false) Integer idademax,
            @RequestParam(required = false) Integer idademin, @RequestParam(required = false) String concelho,
            @RequestParam(required = false) String regiao, @RequestParam(required = false) String nacionalidade,
            @RequestParam(required = false) Integer alturamin, @RequestParam(required = false) Integer alturamax,
            @RequestParam(required = false) Double pesomin, @RequestParam(required = false) Double pesomax,
            @RequestParam(required = false) Long hospital) {

        if (estado == null && genero == null && idademin == null && idademax == null && concelho == null
                && regiao == null && nacionalidade == null && alturamin == null && alturamax == null && pesomin == null
                && pesomax == null && hospital == null) {
            List<Caso> casos = CasoRepository.findAll();
            return casos.size();
        }

        String strgenero = "";
        if (genero != null) {
            strgenero = genero;
        } else {
            strgenero = "%";
        }

        String stridademin = "";
        if (idademin != null) {
            stridademin = idademin.toString();
        } else {
            stridademin = "0";
        }

        String stridademax = "";
        if (idademax != null) {
            stridademax = idademax.toString();
        } else {
            stridademax = "300";
        }

        String strconcelho = "";
        if (concelho != null) {
            strconcelho = concelho;
        } else {
            strconcelho = "%";
        }

        String strregiao = "";
        if (regiao != null) {
            strregiao = regiao;
        } else {
            strregiao = "%";
        }

        String strnacionalidade = "";
        if (nacionalidade != null) {
            strnacionalidade = nacionalidade;
        } else {
            strnacionalidade = "%";
        }

        String stralturamin = "";
        if (alturamin != null) {
            stralturamin = alturamin.toString();
        } else {
            stralturamin = "0";
        }

        String stralturamax = "";
        if (alturamax != null) {
            stralturamax = alturamax.toString();
        } else {
            stralturamax = "300";
        }

        String strpesomin = "";
        if (pesomin != null) {
            strpesomin = pesomin.toString();
        } else {
            strpesomin = "0";
        }

        String strpesomax = "";
        if (pesomax != null) {
            strpesomax = pesomax.toString();
        } else {
            strpesomax = "500";
        }

        if (estado == null) {
            estado = "semestado";
        }

        List<String> strmedicos = new ArrayList<String>();
        List<Medico> medicos = new ArrayList<Medico>();
        if (hospital != null) {
            medicos = medicoRepository.findAllByFilters("%", hospital.toString(), "0", "200");
            for (Medico m : medicos) {
                strmedicos.add(String.valueOf(m.getNumero_medico()));
            }
        }

        List<Paciente> pacientes;
        if (medicos.size() > 0) {
            pacientes = pacienteRepository.findAllFilters(strgenero, stridademin, stridademax, strconcelho, strregiao,
                    strnacionalidade, stralturamin, stralturamax, strpesomin, strpesomax, strmedicos);
        } else {
            pacientes = pacienteRepository.findAllFilters(strgenero, stridademin, stridademax, strconcelho, strregiao,
                    strnacionalidade, stralturamin, stralturamax, strpesomin, strpesomax);
        }

        int contador = 0;
        if (estado.equals("ativos")) {
            for (Paciente p : pacientes) {
                if (p.getEstado_atual().equals("Confinamento Domiciliário")
                        || p.getEstado_atual().equals("Internado")
                        || p.getEstado_atual().equals("Cuidados Intensivos")) {
                    contador += 1;
                }
            }
            return contador;
        } else {
            for (Paciente p : pacientes) {
                if (p.getEstado_atual().equals(estado) || estado.equals("semestado")) {
                    contador += 1;
                }
            }
            return contador;
        }
    }

    
    @GetMapping("/public/casos")
    public List<Caso> getAllCasos(@RequestParam(required = false) String estado,
            @RequestParam(required = false) String genero, @RequestParam(required = false) Integer idademax,
            @RequestParam(required = false) Integer idademin, @RequestParam(required = false) String concelho,
            @RequestParam(required = false) String regiao, @RequestParam(required = false) String nacionalidade,
            @RequestParam(required = false) Integer alturamin, @RequestParam(required = false) Integer alturamax,
            @RequestParam(required = false) Double pesomin, @RequestParam(required = false) Double pesomax,
            @RequestParam(required = false) Integer page, @RequestParam(required=false) Integer size) {
        
        if (page==null) page=0;
        if (size==null) size=30;
        PageRequest pageRequest = PageRequest.of(page, size);
        if (estado == null && genero == null && idademin == null && idademax == null && concelho == null
                && regiao == null && nacionalidade == null && alturamin == null && alturamax == null && pesomin == null
                && pesomax == null) {
            List<Caso> l = new ArrayList<Caso>();
            for (Caso c: CasoRepository.findAll(pageRequest)) l.add(c);
            return l;
        }

        String strgenero = "";
        if (genero != null) {
            strgenero = genero;
        } else {
            strgenero = "%";
        }

        String stridademin = "";
        if (idademin != null) {
            stridademin = idademin.toString();
        } else {
            stridademin = "0";
        }

        String stridademax = "";
        if (idademax != null) {
            stridademax = idademax.toString();
        } else {
            stridademax = "300";
        }

        String strconcelho = "";
        if (concelho != null) {
            strconcelho = concelho;
        } else {
            strconcelho = "%";
        }

        String strregiao = "";
        if (regiao != null) {
            strregiao = regiao;
        } else {
            strregiao = "%";
        }

        String strnacionalidade = "";
        if (nacionalidade != null) {
            strnacionalidade = nacionalidade;
        } else {
            strnacionalidade = "%";
        }

        String stralturamin = "";
        if (alturamin != null) {
            stralturamin = alturamin.toString();
        } else {
            stralturamin = "0";
        }

        String stralturamax = "";
        if (alturamax != null) {
            stralturamax = alturamax.toString();
        } else {
            stralturamax = "300";
        }

        String strpesomin = "";
        if (pesomin != null) {
            strpesomin = pesomin.toString();
        } else {
            strpesomin = "0";
        }

        String strpesomax = "";
        if (pesomax != null) {
            strpesomax = pesomax.toString();
        } else {
            strpesomax = "500";
        }

        Page<Paciente> pacientes = pacienteRepository.findAllFilters(strgenero, stridademin, stridademax, strconcelho,
                strregiao, strnacionalidade, stralturamin, stralturamax, strpesomin, strpesomax, pageRequest);

        List<Caso> casos = new ArrayList<Caso>();

        for (Paciente paciente : pacientes) {
            Caso caso = CasoRepository.findByPacienteId(paciente.getPacienteId());
            if (caso==null) continue;
            if (caso.getEstado_atual().equals(estado) || estado == null) {
                casos.add(caso);
            }
        }
        return casos;
    }



    @GetMapping("/private/casobypaciente/{id}")
    public Caso getCasoByPacienteId(@PathVariable(value = "id") Long PacienteId) throws ResourceNotFoundException {
        Caso Caso = CasoRepository.findByPacienteId(PacienteId);

        return Caso;
    }

    @GetMapping("/public/casos/{id}")
    public ResponseEntity<Caso> getCasoById(@PathVariable(value = "id") Long CasoId) throws ResourceNotFoundException {
        Caso Caso = CasoRepository.findById(CasoId)
                .orElseThrow(() -> new ResourceNotFoundException("Caso not found for this id :: " + CasoId));

        return ResponseEntity.ok().body(Caso);
    }

    @PostMapping("/private/casos")
    public Caso createCaso(@Valid @RequestBody Caso caso) throws ResourceNotFoundException {
        return CasoRepository.save(caso);
    }

    @PutMapping("/private/casos/{id}")
    public ResponseEntity<Caso> updateCaso(@PathVariable(value = "id") Long CasoID,
            @Valid @RequestBody Caso CasoDetails) throws ResourceNotFoundException {

        Caso Caso = CasoRepository.findById(CasoID)
                .orElseThrow(() -> new ResourceNotFoundException("Caso not found for this id :: " + CasoID));

        Caso.setEstado_atual(CasoDetails.getEstado_atual());
        Caso.setPaciente_id(CasoDetails.getPaciente_id());
        final Caso updatedCaso = CasoRepository.save(Caso);
        return ResponseEntity.ok(updatedCaso);
    }

    @DeleteMapping("/private/casos/{id}")
    public Map<String, Boolean> deleteCaso(@PathVariable(value = "id") Long CasoID)
            throws ResourceNotFoundException, ParseException {

        Caso Caso = CasoRepository.findById(CasoID)
                .orElseThrow(() -> new ResourceNotFoundException("Caso not found for this id :: " + CasoID));

        CasoRepository.delete(Caso);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
