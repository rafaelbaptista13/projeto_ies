package ua.ies.g23.Covinfo19;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.json.JSONException;
import org.json.JSONObject;

import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.*;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.*;

@Component
public class Receiver {

  private CountDownLatch latch = new CountDownLatch(1);

  @Autowired
  private PacienteRepository pacienteRepository;

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
    latch.countDown();
  }

  public CountDownLatch getLatch() {
    return latch;
  }

}