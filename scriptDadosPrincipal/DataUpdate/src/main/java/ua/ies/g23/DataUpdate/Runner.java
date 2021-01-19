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
import java.util.ArrayList;
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

  //Conjunto variáveis estáticas necessárias para criação mensagem update.
  private final RabbitTemplate rabbitTemplate;
  private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
  public static final String[] var_estado = {"Confinamento Domiciliário", "Internado", "Cuidados Intensivos", "Óbito", "Recuperado"};
	public static final String[] var_regioes = {"Norte", "Lisboa e Vale do Tejo", "Centro", "Alentejo", "Algarve", "Açores", "Madeira"};
  public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
  public static Random rand = new Random();

  public Runner(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void run(String... args) throws Exception {
      for (int l = 0; l<20; l++) {
        HttpResponse<String> response;
        int contador = 0;

        //Execução Pedido HTTP de um paciente segundo conjunto filtros (estado_paciente, regiao_paciente, idademin e idademax) até que o pedido devolta um paciente com um limite de 10 pedidos.
        while (true) {
          //Definição aleatório dos campos utilizados nos filtros
          String estado_paciente = getEstadoValido();
          String regiao_paciente = getRegiaoValida();
          int idade_paciente = getIdadeValida();
          int idademin = idade_paciente-5;
          int idademax = idade_paciente+5;

          //Criação URL para pedido http
          String url = "http://localhost:8080/api/v1/public/casos?estado="+estado_paciente+"&regiao="+regiao_paciente+"&idademin="+idademin+"&idademax="+idademax;
          url = url.replace(" ", "+");
          HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).setHeader("User-Agent", "Java 11 HttpClient Bot").build();

          response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
          
          //Verificação pedido contêm um caso
          if (response.body().equals("[]")) {
            contador += 1;

            //Implementação limite 10 pedidos
            if (contador >= 10) {
              System.exit(1);
            }
          } else {
            break;
          }
        } 
        
        // Conversão resposta http num arraylist de objetos JSON (1 para cada paciente/caso)
        String[] texto = response.body().split("\\{");
        ArrayList<JSONObject> jsonPacientes = new ArrayList<JSONObject>();
        for (int i = 1; i<texto.length; i++) {
          String novotexto = '{'+texto[i];
          novotexto = novotexto.substring(0, novotexto.length()-1);
          JSONObject myPacient = new JSONObject(novotexto);
          jsonPacientes.add(myPacient);
        }
        
        HashMap<String, Object> mensagem = new HashMap<String, Object>();
        //Criação nova mensagem. Utilização do paciente indice 0 do arrayobjetos.
        mensagem = construcaoMensagem(jsonPacientes.get(0));

        //Atribuição tempo atual á mensagem
        mensagem.put("time", formatter.format(new Date(System.currentTimeMillis())));
        System.out.println("\n \n--------- Nova Mensagem : ---------");
        for (String item : mensagem.keySet()) {
          System.out.println(item + " -> " + mensagem.get(item));
        }
        JSONObject json = new JSONObject(mensagem);

        //Envio da mensagem
        rabbitTemplate.convertAndSend(DataUpdateApplication.topicExchangeName, "filaupdate", json.toString());
      }
  }


  ////
	//	Função criação mensagem de update de estado do paciente.
	////
  public static HashMap<String, Object> construcaoMensagem(JSONObject myPacient) throws JSONException {
    HashMap<String, Object> mensagem = new HashMap<String, Object>();
    mensagem.put("id", myPacient.get("id"));
    mensagem.put("paciente_id", myPacient.get("paciente_id"));
    mensagem.put("estado_atual", atualizacaoEstado(myPacient.get("estado_atual").toString()));
		return mensagem;
	} 


  ////
	//	Função atualização do estado do paciente consoante o estado atual.
	////
  public static String atualizacaoEstado (String estado_atual) {
    double randomNum = rand.nextDouble();
    if (estado_atual.equals("Confinamento Domiciliário")) {
      if (randomNum <= 0.85) {
        return var_estado[4];
      } else if (randomNum <= 0.92) {
        return var_estado[1];
      } else if (randomNum <= 0.97) {
        return var_estado[2];
      } else {
        return var_estado[3];
      }
    } else if (estado_atual.equals("Internado")) {
      if (randomNum <= 0.5) {
        return var_estado[4];
      } else if (randomNum <= 0.8) {
        return var_estado[0];
      } else if (randomNum <= 0.95) {
        return var_estado[2];
      } else {
        return var_estado[3];
      }
    } else {
      if (randomNum <= 0.05) {
        return var_estado[4];
      } else if (randomNum <= 0.15) {
        return var_estado[0];
      } else if (randomNum <= 0.75) {
        return var_estado[1];
      } else {
        return var_estado[3];
      }
    }
  }

  ////
	//	Função utilizada para obtenção de um estado para executar filtragem paciente.
	////
  public static String getEstadoValido() {
		double randomNum = rand.nextDouble();
		if (randomNum <= 0.92) {
			return var_estado[0];
		} else if (randomNum <= 0.98) {
			return var_estado[1];
		} else {
			return var_estado[2];
		}
	}

  ////
	//	Função utilizada para obtenção de idade para executar filtragem paciente.
	////
	public static Integer getIdadeValida() {
		int val = -1;
		do {
			val = (int) (50f + 50f * (1 + rand.nextGaussian())/2);
		  } while (val >= 105 || val < 1);
		return val;
	}

  ////
	//	Função utilizada para obtenção de uma região para executar filtragem paciente.
	////
	public static String getRegiaoValida() {
		double randomNum = rand.nextDouble();
		if (randomNum <= 0.359) {
			return var_regioes[0];
		} else if (randomNum <= 0.623) {
			return var_regioes[1];
		} else if (randomNum <= 0.844) {
			return var_regioes[2];
		} else if (randomNum <= 0.917){
			return var_regioes[3];
		} else if (randomNum <= 0.954) {
			return var_regioes[4];
		} else if (randomNum <= 0.977) {
			return var_regioes[5];
		} else {
			return var_regioes[6];
		}
	}

}
