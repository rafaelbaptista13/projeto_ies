package ua.ies.g23.Covinfo19;

import java.util.concurrent.CountDownLatch;

import org.springframework.stereotype.Component;
import org.json.JSONException;
import org.json.JSONObject;


@Component
public class ReceiverMedicosPacientes {

  private CountDownLatch latch = new CountDownLatch(1);

  public void receiveMessage(String message) throws JSONException {
    JSONObject json = new JSONObject(message);
    System.out.println("-------- Novo Medico -----------");
    System.out.println("Received <" + json.toString() + ">");
    latch.countDown();
  }

  public CountDownLatch getLatch() {
    return latch;
  }

}