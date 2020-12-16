package ua.ies.g23.DataUpdate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

  private final RabbitTemplate rabbitTemplate;
  private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

  public Runner(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void run(String... args) throws Exception {
    HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/api/v1/pacientes/1"))
        .setHeader("User-Agent", "Java 11 HttpClient Bot").build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    JSONObject myPacient = new JSONObject(response.body());

    HashMap<String, Object> mensagem = new HashMap<String, Object>();
    mensagem = construcaoMensagem(myPacient);
    mensagem.put("time", formatter.format(new Date(System.currentTimeMillis())));
    System.out.println("\n \n--------- Nova Mensagem : ---------");
    for (String item : mensagem.keySet()) {
      System.out.println(item + " -> " + mensagem.get(item));
    }
    JSONObject json = new JSONObject(mensagem);
    rabbitTemplate.convertAndSend(DataUpdateApplication.topicExchangeName, "filaupdate", json.toString());
  }

  // geração de dados
  public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
  public static Random rand = new Random();

  public static HashMap<String, Object> construcaoMensagem(JSONObject myPacient) throws JSONException {
    HashMap<String, Object> mensagem = new HashMap<String, Object>();
    mensagem.put("numero_utente", myPacient.get("numero_utente"));
		return mensagem;
	}


}