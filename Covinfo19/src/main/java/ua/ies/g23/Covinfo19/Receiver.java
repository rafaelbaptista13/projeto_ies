package ua.ies.g23.Covinfo19;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.json.JSONException;
import org.json.JSONObject;

import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.*;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.*;
import ua.ies.g23.Covinfo19.relatorios.model.Caso;
import ua.ies.g23.Covinfo19.relatorios.model.Relatorio_Paciente;
import ua.ies.g23.Covinfo19.relatorios.repository.CasoRepository;
import ua.ies.g23.Covinfo19.relatorios.repository.Relatorio_PacienteRepository;

@Component
public class Receiver {

  private CountDownLatch latch = new CountDownLatch(1);

  @Autowired
  private PacienteRepository pacienteRepository;
  private CasoRepository casoRepository;
  private Relatorio_PacienteRepository relatorioPacienteRepository;

  public void receiveMessage(String message) throws JSONException {
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
    paciente.setPeso((float)Double.parseDouble(json.getString("peso")));
    System.out.println(paciente);
    //paciente.setMedico(pacienteDetails.getMedico());
    pacienteRepository.save(paciente);
    Caso caso = new Caso();
    caso.setEstado_atual(json.getString("estado"));
    caso.setPaciente_id(paciente.getPacienteId());
    casoRepository.save(caso);
    System.out.println(caso);
    Relatorio_Paciente relatorio_paciente = new Relatorio_Paciente();
    relatorio_paciente.setCaso(caso);
    relatorioPacienteRepository.save(relatorio_paciente);
    System.out.println(relatorio_paciente);
    System.out.println(json.get("time"));
    latch.countDown();
  }

  public CountDownLatch getLatch() {
    return latch;
  }

}