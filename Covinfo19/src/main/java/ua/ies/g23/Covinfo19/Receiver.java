package ua.ies.g23.Covinfo19;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.json.JSONException;
import org.json.JSONObject;

import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.*;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.*;
import ua.ies.g23.Covinfo19.relatorios.model.Caso;
import ua.ies.g23.Covinfo19.relatorios.model.Relatorio_Paciente;
import ua.ies.g23.Covinfo19.relatorios.repository.*;

@Component
public class Receiver {

  private CountDownLatch latch = new CountDownLatch(1);
  private static Random rand = new Random();

  @Autowired
  private PacienteRepository pacienteRepository;
  @Autowired
  private CasoRepository casoRepository;
  @Autowired
  private Relatorio_PacienteRepository relatorioPacienteRepository;
  @Autowired
  private HospitalRepository hospitalRepository;

  public void receiveMessage(String message) throws JSONException {
    System.out.println("\n \n NEW MESSAGE:");
    JSONObject json = new JSONObject(message);
    System.out.println("-------- Novo Paciente -----------");
    System.out.println("Received <" + json.toString() + ">");
    Paciente paciente = new Paciente();
    paciente.setNome(json.getString("nome"));
    paciente.setGenero(json.getString("genero"));
    paciente.setIdade(Integer.parseInt(json.getString("idade")));
    paciente.setConcelho(json.getString("cidade"));
    paciente.setRegiao(json.getString("regiao"));
    paciente.setNacionalidade(json.getString("nacionalidade"));
    paciente.setAltura(Integer.parseInt(json.getString("altura")));
    paciente.setPeso((float) Double.parseDouble(json.getString("peso")));
    System.out.println(paciente);
    List<Hospital> listaHospitais = hospitalRepository.findAllFilters("%", "%", json.getString("regiao"), "0", "5000", "0", "5000");
    if (json.getString("estado").equals("Confinamento Domiciliário")) {
      int indexhospital = rand.nextInt((listaHospitais.size()-1 - 0) + 1) + 0;
      Hospital hospital = listaHospitais.get(indexhospital);
      System.out.println(hospital);
    }
    // paciente.setMedico(pacienteDetails.getMedico());
    pacienteRepository.save(paciente);
    Caso caso = new Caso();
    caso.setEstado_atual(json.getString("estado"));
    caso.setPaciente_id(paciente.getPacienteId());
    System.out.println(caso);
    casoRepository.save(caso);

    DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date d2;
    try {
      d2 = df2.parse(json.getString("time").split(" ")[0] + " " + json.getString("time").split(" ")[2] );
      Relatorio_Paciente relatorio_paciente = new Relatorio_Paciente();
      relatorio_paciente.setCaso(caso);
      relatorio_paciente.setEstado(json.getString("estado"));
      relatorio_paciente.setData(d2);
      System.out.println(relatorio_paciente);
      relatorioPacienteRepository.save(relatorio_paciente);
      
    } catch (ParseException e) {
      e.printStackTrace();
    }
    
    latch.countDown();
  }

  public CountDownLatch getLatch() {
    return latch;
  }

}