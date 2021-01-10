package ua.ies.g23.DataUpdate;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.json.JSONException;
import org.json.JSONObject;

@Component
public class Receiver {

  private CountDownLatch latch = new CountDownLatch(1);

  public void receiveMessage(String message) throws JSONException {
    latch.countDown();
  }

  public CountDownLatch getLatch() {
    return latch;
  }

}