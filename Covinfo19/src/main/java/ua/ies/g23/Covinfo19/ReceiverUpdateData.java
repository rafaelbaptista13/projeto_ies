package ua.ies.g23.Covinfo19;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.json.JSONException;
import org.json.JSONObject;

import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.*;
import ua.ies.g23.Covinfo19.relatorios.model.*;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.*;
import ua.ies.g23.Covinfo19.relatorios.repository.*;

@Component
public class ReceiverUpdateData {

  private CountDownLatch latch = new CountDownLatch(1);

  public void receiveMessage(String message) throws JSONException {
    JSONObject json = new JSONObject(message);
    System.out.println("-------- Novo Paciente -----------");
    System.out.println("Received <" + json.toString() + ">");
    latch.countDown();
  }

  public CountDownLatch getLatch() {
    return latch;
  }

}