package ua.ies.g23.Covinfo19.relatorios.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.ls.LSInput;

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
    @GetMapping("/public/casos/grafico/curva_diaria")
    public Map<String,Integer> getAllCasosGraficoCurvaEvolucao(
        @RequestParam(required = false) String genero,
        @RequestParam(required = false) Integer idademax,
        @RequestParam(required = false) Integer idademin,
        @RequestParam(required = false) String concelho,
        @RequestParam(required = false) String regiao,
        @RequestParam(required = false) String nacionalidade,
        @RequestParam(required = false) Integer alturamin,
        @RequestParam(required = false) Integer alturamax,
        @RequestParam(required = false) Double pesomin,
        @RequestParam(required = false) Double pesomax ) {
        
        Map<String,Integer> dicionarioCasos = new HashMap<String, Integer>();
        Map<Date,Date> dicionarioDias = new HashMap<Date,Date>();
        int somaCasosTodos = 0;

        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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
            strpesomin = pesomin.toString() ;
        } else {
            strpesomin = "0";
            pesomin = Double.valueOf(0);
        }

        String strpesomax = "";
        if (pesomax != null) {
            strpesomax = pesomax.toString() ;
        } else {
            strpesomax = "500";
            pesomax = Double.valueOf(500);
        }

        
        for (Date dia: dicionarioDias.keySet()) {
            List<Paciente> pacientes;
            Date data_fim_dia = dicionarioDias.get(dia);
            
            pacientes = pacienteRepository.findAllFilters(strgenero, stridademin, stridademax, strconcelho, strregiao, strnacionalidade, stralturamin, stralturamax, strpesomin, strpesomax);   
            List<Caso> casos = new ArrayList<Caso>();
            for (Paciente paciente : pacientes) {
                Caso caso = CasoRepository.findByPacienteId(paciente.getPacienteId());
                if (caso.getEstado_atual().equals("Confinamento Domiciliário") || caso.getEstado_atual().equals("Internado") || caso.getEstado_atual().equals("Cuidados Intensivos") ) {
                    Relatorio_Paciente relatorio = relatorioRepository.findRecentByCaso(caso.getId());
                    if (relatorio.getData().after(dia) && relatorio.getData().before(data_fim_dia)) {
                        casos.add(caso);
                    }
                }
            }
            
            String mes =  dia.toString().split(" ")[1];
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

            String chave = dia.toString().split(" ")[5] + "-" + mes + "-" + dia.toString().split(" ")[2] ;

            dicionarioCasos.put(chave, casos.size());
            
               
        }
        return dicionarioCasos ;
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/public/casos/grafico/peso")
    public List<Double> getAllCasosGraficoPeso(
        @RequestParam(required = false) String genero,
        @RequestParam(required = false) Integer idademax,
        @RequestParam(required = false) Integer idademin,
        @RequestParam(required = false) String concelho,
        @RequestParam(required = false) String regiao,
        @RequestParam(required = false) String nacionalidade,
        @RequestParam(required = false) Integer alturamin,
        @RequestParam(required = false) Integer alturamax,
        @RequestParam(required = false) Double pesomin,
        @RequestParam(required = false) Double pesomax ) {
        
        List<Integer> listaPesos = new ArrayList<Integer>();
        List<Double> listaPesosPercentagem = new ArrayList<Double>();
        int somaCasosTodos = 0;

       System.out.println();
            
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
            strpesomin = pesomin.toString() ;
        } else {
            strpesomin = "0";
            pesomin = Double.valueOf(0);
        }

        String strpesomax = "";
        if (pesomax != null) {
            strpesomax = pesomax.toString() ;
        } else {
            strpesomax = "500";
            pesomax = Double.valueOf(500);
        }



        for (int pesominima = 11; pesominima <= 91; pesominima += 10) {
            String strpesominima = (pesominima == 11) ? "0" : String.valueOf(pesominima); 
            String strpesomaxima = (pesominima == 91) ? "300" : String.valueOf(pesominima + 9); 

            List<Paciente> pacientes;
            
            pacientes = pacienteRepository.findAllFilters(strgenero, stridademin, stridademax, strconcelho, strregiao, strnacionalidade, stralturamin, stralturamax, strpesominima, strpesomaxima);   
            List<Caso> casos = new ArrayList<Caso>();
            for (Paciente paciente : pacientes) {
                Caso caso = CasoRepository.findByPacienteId(paciente.getPacienteId());
                if (caso.getEstado_atual().equals("Confinamento Domiciliário") || caso.getEstado_atual().equals("Internado") || caso.getEstado_atual().equals("Cuidados Intensivos") ) {
                    if (paciente.getPeso() >= pesomin && paciente.getPeso() <= pesomax) {
                        casos.add(caso);
                    }
                }
            }
                
            listaPesos.add(casos.size());
            somaCasosTodos += casos.size();
        }
        
        if (somaCasosTodos == 0) {
            for (Integer integer : listaPesos) {
                listaPesosPercentagem.add( (double) 0 );
            }
        } else {
            for (Integer integer : listaPesos) {
                listaPesosPercentagem.add( (double) integer / somaCasosTodos*100 );
            }
        }
        System.out.println(listaPesosPercentagem);
        return listaPesosPercentagem;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/public/casos/grafico/altura")
    public List<Double> getAllCasosGraficoAltura(
        @RequestParam(required = false) String genero,
        @RequestParam(required = false) Integer idademax,
        @RequestParam(required = false) Integer idademin,
        @RequestParam(required = false) String concelho,
        @RequestParam(required = false) String regiao,
        @RequestParam(required = false) String nacionalidade,
        @RequestParam(required = false) Integer alturamin,
        @RequestParam(required = false) Integer alturamax,
        @RequestParam(required = false) Double pesomin,
        @RequestParam(required = false) Double pesomax ) {
        
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
            strpesomin = pesomin.toString() ;
        } else {
            strpesomin = "0";
        }

        String strpesomax = "";
        if (pesomax != null) {
            strpesomax = pesomax.toString() ;
        } else {
            strpesomax = "500";
        }



        for (int alturaminima = 141; alturaminima <= 201; alturaminima += 10) {
            String stralturaminima = (alturaminima == 141) ? "0" : String.valueOf(alturaminima); 
            String stralturamaxima = (alturaminima == 201) ? "300" : String.valueOf(alturaminima + 9); 

            List<Paciente> pacientes;
            
            pacientes = pacienteRepository.findAllFilters(strgenero, stridademin, stridademax, strconcelho, strregiao, strnacionalidade, stralturaminima, stralturamaxima, strpesomin, strpesomax);   
            List<Caso> casos = new ArrayList<Caso>();
            for (Paciente paciente : pacientes) {
                Caso caso = CasoRepository.findByPacienteId(paciente.getPacienteId());
                if (caso.getEstado_atual().equals("Confinamento Domiciliário") || caso.getEstado_atual().equals("Internado") || caso.getEstado_atual().equals("Cuidados Intensivos") ) {
                    if (paciente.getAltura() >= alturamin && paciente.getAltura() <= alturamax) {
                        casos.add(caso);
                    }
                }
            }
                
            listaAlturas.add(casos.size());
            somaCasosTodos += casos.size();
        }
        
        if (somaCasosTodos == 0) {
            for (Integer integer : listaAlturas) {
                listaAlturasPercentagem.add( (double) 0 );
            }
        } else {
            for (Integer integer : listaAlturas) {
                listaAlturasPercentagem.add( (double) integer / somaCasosTodos*100 );
            }
        }
        System.out.println(listaAlturasPercentagem);
        return listaAlturasPercentagem;
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/public/casos/grafico/genero")
    public List<Double> getAllCasosGraficoGenero(
        @RequestParam(required = false) String genero,
        @RequestParam(required = false) Integer idademax,
        @RequestParam(required = false) Integer idademin,
        @RequestParam(required = false) String concelho,
        @RequestParam(required = false) String regiao,
        @RequestParam(required = false) String nacionalidade,
        @RequestParam(required = false) Integer alturamin,
        @RequestParam(required = false) Integer alturamax,
        @RequestParam(required = false) Double pesomin,
        @RequestParam(required = false) Double pesomax ) {
        
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
            strpesomin = pesomin.toString() ;
        } else {
            strpesomin = "0";
        }

        String strpesomax = "";
        if (pesomax != null) {
            strpesomax = pesomax.toString() ;
        } else {
            strpesomax = "500";
        }

        ArrayList<String> generos = new ArrayList<String>(); 
        generos.add("Masculino");
        generos.add("Feminino");

        for (String genero_nome : generos) {
            List<Paciente> pacientes;
            
            pacientes = pacienteRepository.findAllFilters(genero_nome, stridademin, stridademax, strconcelho, strregiao, strnacionalidade, stralturamin, stralturamax, strpesomin, strpesomax);   
            List<Caso> casos = new ArrayList<Caso>();
            for (Paciente paciente : pacientes) {
                Caso caso = CasoRepository.findByPacienteId(paciente.getPacienteId());
                if (caso.getEstado_atual().equals("Confinamento Domiciliário") || caso.getEstado_atual().equals("Internado") || caso.getEstado_atual().equals("Cuidados Intensivos") ) {
                    if (strgenero.equals("%") || strgenero.equals(genero_nome)) { 
                       casos.add(caso);
                    }
                }
            }
                
            listaCasosGenero.add(casos.size());
            somaCasosTodos += casos.size();
        }

        
        
        if (somaCasosTodos == 0) {
            for (Integer integer : listaCasosGenero) {
                listaCasosGeneroPercentagem.add( (double) 0 );
            }
        } else {
            for (Integer integer : listaCasosGenero) {
                listaCasosGeneroPercentagem.add( (double) integer / somaCasosTodos*100 );
            }
        }
        System.out.println(listaCasosGeneroPercentagem);
        return listaCasosGeneroPercentagem;
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/public/casos/grafico/regiao")
    public List<Double> getAllCasosGraficoRegiao(
        @RequestParam(required = false) String genero,
        @RequestParam(required = false) Integer idademax,
        @RequestParam(required = false) Integer idademin,
        @RequestParam(required = false) String concelho,
        @RequestParam(required = false) String regiao,
        @RequestParam(required = false) String nacionalidade,
        @RequestParam(required = false) Integer alturamin,
        @RequestParam(required = false) Integer alturamax,
        @RequestParam(required = false) Double pesomin,
        @RequestParam(required = false) Double pesomax ) {
        
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
            strpesomin = pesomin.toString() ;
        } else {
            strpesomin = "0";
        }

        String strpesomax = "";
        if (pesomax != null) {
            strpesomax = pesomax.toString() ;
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
            
            pacientes = pacienteRepository.findAllFilters(strgenero, stridademin, stridademax, strconcelho, regiao_nome, strnacionalidade, stralturamin, stralturamax, strpesomin, strpesomax);   
            List<Caso> casos = new ArrayList<Caso>();
            for (Paciente paciente : pacientes) {
                Caso caso = CasoRepository.findByPacienteId(paciente.getPacienteId());
                if (caso.getEstado_atual().equals("Confinamento Domiciliário") || caso.getEstado_atual().equals("Internado") || caso.getEstado_atual().equals("Cuidados Intensivos") ) {
                    if (strregiao.equals("%") || strregiao.equals(regiao_nome)) { 
                       casos.add(caso);
                    }
                }
            }
                
            listaCasosRegioes.add(casos.size());
            somaCasosTodos += casos.size();
        }

        
        
        if (somaCasosTodos == 0) {
            for (Integer integer : listaCasosRegioes) {
                listaCasosRegioesPercentagem.add( (double) 0 );
            }
        } else {
            for (Integer integer : listaCasosRegioes) {
                listaCasosRegioesPercentagem.add( (double) integer / somaCasosTodos*100 );
            }
        }
        System.out.println(listaCasosRegioesPercentagem);
        return listaCasosRegioesPercentagem;
    }
    
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/public/casos/grafico/idade")
    public List<Double> getAllCasosGraficoIdade(
        @RequestParam(required = false) String genero,
        @RequestParam(required = false) Integer idademax,
        @RequestParam(required = false) Integer idademin,
        @RequestParam(required = false) String concelho,
        @RequestParam(required = false) String regiao,
        @RequestParam(required = false) String nacionalidade,
        @RequestParam(required = false) Integer alturamin,
        @RequestParam(required = false) Integer alturamax,
        @RequestParam(required = false) Double pesomin,
        @RequestParam(required = false) Double pesomax ) {
        System.out.println("Estou aqui\n\n");
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
            strpesomin = pesomin.toString() ;
        } else {
            strpesomin = "0";
        }

        String strpesomax = "";
        if (pesomax != null) {
            strpesomax = pesomax.toString() ;
        } else {
            strpesomax = "500";
        }



        for (int idademinima = 1; idademinima <= 81; idademinima += 10) {
            String stridademinima = String.valueOf(idademinima);
            String stridademaxima = (idademinima == 81) ? "300" : String.valueOf(idademinima + 9); 

            List<Paciente> pacientes;
            
            pacientes = pacienteRepository.findAllFilters(strgenero, stridademinima, stridademaxima, strconcelho, strregiao, strnacionalidade, stralturamin, stralturamax, strpesomin, strpesomax);   
            List<Caso> casos = new ArrayList<Caso>();
            for (Paciente paciente : pacientes) {
                Caso caso = CasoRepository.findByPacienteId(paciente.getPacienteId());
                if (caso.getEstado_atual().equals("Confinamento Domiciliário") || caso.getEstado_atual().equals("Internado") || caso.getEstado_atual().equals("Cuidados Intensivos") ) {
                    if (paciente.getIdade() >= idademin && paciente.getIdade() <= idademax) {
                        casos.add(caso);
                    }
                }
            }
                
            listaIdades.add(casos.size());
            somaCasosTodos += casos.size();
        }
        
        if (somaCasosTodos == 0) {
            for (Integer integer : listaIdades) {
                listaIdadesPercentagem.add( (double) 0 );
            }
        } else {
            for (Integer integer : listaIdades) {
                listaIdadesPercentagem.add( (double) integer / somaCasosTodos*100 );
            }
        }
        System.out.println(listaIdadesPercentagem);
        return listaIdadesPercentagem;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/public/casos/count")
    public int getAllCasosCount(
        @RequestParam(required = false) String estado,
        @RequestParam(required = false) String genero,
        @RequestParam(required = false) Integer idademax,
        @RequestParam(required = false) Integer idademin,
        @RequestParam(required = false) String concelho,
        @RequestParam(required = false) String regiao,
        @RequestParam(required = false) String nacionalidade,
        @RequestParam(required = false) Integer alturamin,
        @RequestParam(required = false) Integer alturamax,
        @RequestParam(required = false) Double pesomin,
        @RequestParam(required = false) Double pesomax,
        @RequestParam(required = false) Long hospital ) {

        if (estado == null && genero == null && idademin == null && idademax == null && concelho == null && regiao == null && nacionalidade == null && alturamin == null && alturamax == null && pesomin == null && pesomax == null && hospital == null ) {
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
            strpesomin = pesomin.toString() ;
        } else {
            strpesomin = "0";
        }

        String strpesomax = "";
        if (pesomax != null) {
            strpesomax = pesomax.toString() ;
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
            for (Medico m: medicos) {
                strmedicos.add(String.valueOf(m.getNumero_medico()));
            }
        } 
        
        List<Paciente> pacientes;
        if (medicos.size() > 0) {
            pacientes = pacienteRepository.findAllFilters(strgenero, stridademin, stridademax, strconcelho, strregiao, strnacionalidade, stralturamin, stralturamax, strpesomin, strpesomax, strmedicos);   
        } else {
            pacientes = pacienteRepository.findAllFilters(strgenero, stridademin, stridademax, strconcelho, strregiao, strnacionalidade, stralturamin, stralturamax, strpesomin, strpesomax);   
        }
        List<Caso> casos = new ArrayList<Caso>();
        if (estado.equals("ativos")) {
            for (Paciente paciente : pacientes) {
                Caso caso = CasoRepository.findByPacienteId(paciente.getPacienteId());
                if (caso.getEstado_atual().equals("Confinamento Domiciliário") || caso.getEstado_atual().equals("Internado") || caso.getEstado_atual().equals("Cuidados Intensivos") ) {
                    casos.add(caso);
                }
            }
            return casos.size();
        } else {
            for (Paciente paciente : pacientes) {
                Caso caso = CasoRepository.findByPacienteId(paciente.getPacienteId());
                if (caso.getEstado_atual().equals(estado) || estado.equals("semestado")) {
                    casos.add(caso);
                }
            }
            return casos.size();
        }

    }

    
    @GetMapping("/public/casos")
    public List<Caso> getAllCasos(
        @RequestParam(required = false) String estado,
        @RequestParam(required = false) String genero,
        @RequestParam(required = false) Integer idademax,
        @RequestParam(required = false) Integer idademin,
        @RequestParam(required = false) String concelho,
        @RequestParam(required = false) String regiao,
        @RequestParam(required = false) String nacionalidade,
        @RequestParam(required = false) Integer alturamin,
        @RequestParam(required = false) Integer alturamax,
        @RequestParam(required = false) Double pesomin,
        @RequestParam(required = false) Double pesomax ) {
        if (estado == null && genero == null && idademin == null && idademax == null && concelho == null && regiao == null && nacionalidade == null && alturamin == null && alturamax == null && pesomin == null && pesomax == null ) {
            return CasoRepository.findAll();
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
            strpesomin = pesomin.toString() ;
        } else {
            strpesomin = "0";
        }

        String strpesomax = "";
        if (pesomax != null) {
            strpesomax = pesomax.toString() ;
        } else {
            strpesomax = "500";
        }

        
        List<Paciente> pacientes = pacienteRepository.findAllFilters(strgenero, stridademin, stridademax, strconcelho, strregiao, strnacionalidade, stralturamin, stralturamax, strpesomin, strpesomax);   

        List<Caso> casos = new ArrayList<Caso>();
        
        for (Paciente paciente : pacientes) {
            Caso caso = CasoRepository.findByPacienteId(paciente.getPacienteId());
            if (caso.getEstado_atual().equals(estado) || estado == null) {
                casos.add(caso);
            }
        }
        return casos;
    }
    

    @GetMapping("/public/casos/{id}")
    public ResponseEntity<Caso> getCasoById(@PathVariable(value = "id") Long CasoId)
        throws ResourceNotFoundException {
        Caso Caso = CasoRepository.findById(CasoId)
          .orElseThrow(() -> new ResourceNotFoundException("Caso not found for this id :: " + CasoId));

        return ResponseEntity.ok().body(Caso);
    }

    @PostMapping("/private/casos")
    public Caso createCaso(@Valid @RequestBody Caso caso)
            throws ResourceNotFoundException {
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
