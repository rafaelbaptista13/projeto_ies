package ua.ies.g23.Covinfo19;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

  private final Receiver receiver;

  public Runner(Receiver receiver) {
    this.receiver = receiver;
  }

  @Override
  public void run(String... args) throws Exception {
    receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
  }

}