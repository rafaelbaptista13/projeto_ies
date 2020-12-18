package ua.ies.g23.Covinfo19;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.ies.g23.Covinfo19.relatorios.controller.CasoController;
import ua.ies.g23.Covinfo19.relatorios.model.Caso;
import ua.ies.g23.Covinfo19.relatorios.model.Relatorio_Paciente;
import ua.ies.g23.Covinfo19.relatorios.repository.CasoRepository;
import ua.ies.g23.Covinfo19.relatorios.repository.Relatorio_PacienteRepository;
import ua.ies.g23.Covinfo19.exception.ResourceNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;



@Component
public class ReceiverUpdateData {

  @Autowired
  private CasoRepository casoRepository;
  @Autowired
  private Relatorio_PacienteRepository relatorioPacienteRepository;

    
  private CountDownLatch latch = new CountDownLatch(1);

  public void receiveMessage(String message) throws JSONException, ResourceNotFoundException {
    JSONObject json = new JSONObject(message);
    System.out.println("-------- Novo Paciente -----------");
    System.out.println("Received <" + json.toString() + ">");
    Long id = json.getLong("id");
    Caso caso = casoRepository.findById(id) .orElseThrow(() -> new ResourceNotFoundException("Caso not found for this id :: " + id));
    caso.setEstado_atual(json.getString("estado_atual"));
    System.out.println(caso);
    casoRepository.save(caso);

    DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date d2;
    try {
      d2 = df2.parse(json.getString("time").split(" ")[0] + " " + json.getString("time").split(" ")[2] );
      Relatorio_Paciente relatorio_paciente = new Relatorio_Paciente();
      relatorio_paciente.setCaso(caso);
      relatorio_paciente.setEstado(json.getString("estado_atual"));
      relatorio_paciente.setData(d2);
      System.out.println(relatorio_paciente);
      relatorioPacienteRepository.save(relatorio_paciente);
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    latch.countDown();
  }

  public CountDownLatch getLatch() {
    return latch;
  }

}