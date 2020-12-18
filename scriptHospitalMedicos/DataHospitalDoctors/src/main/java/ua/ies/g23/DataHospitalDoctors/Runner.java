package ua.ies.g23.DataHospitalDoctors;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
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
	public static Random rand = new Random();
  private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

  public static final String[] var_nomesproprios_masculinos = {"João","Rodrigo","Martim","Francisco","Santiago","Tomás","Guilherme","Afonso","Gonçalo","Miguel","Duarte","Tiago","Pedro","Gabriel","Diogo","Rafael","Gustavo","Dinis","David","Lucas","Salvador","Simão","José","Daniel","António","Lourenço","André","Diego","Vicente","Manuel","Henrique","Leonardo","Vasco","Bernardo","Mateus","Luís","Eduardo","Leandro","Alexandre","Rúben","Filipe","Ricardo","Samuel","Bruno","Matias","Nuno","Enzo","Rui","Hugo","Carlos","Xavier","Isaac","Fábio","Artur","Jorge","Sebastião","Paulo","Ivo","Marco","Frederico","Davi","Cristiano","Joaquim","Renato","Ângelo","Micael","Valentim","Ivan","Sérgio","Mário","Tomé","Joel","Jaime","Sandro","Lisandro","Márcio","Luca","Fernando"};
	public static final String[] var_nomesproprios_femininos = {"Maria","Matilde","Leonor","Mariana","Carolina","Beatriz","Ana","Inês","Lara","Margarida","Sofia","Joana","Francisca","Laura","Madalena","Luana","Diana","Rita","Mafalda","Sara","Bianca","Alice","Eva","Clara","Íris","Constança","Letícia","Mara","Catarina","Gabriela","Marta","Vitória","Yara","Camila","Ariana","Ema","Daniela","Núria","Iara","Rafaela","Benedita","Bruna","Filipa","Júlia","Bárbara","Jéssica","Victória","Carlota","Nicole","Alícia","Lia","Helena","Raquel","Teresa","Luísa","Isabel","Érica","Miriam","Kyara","Juliana","Alexandra","Yasmin","Luna","Mia","Débora","Adriana","Melissa","Carminho","Valentina","Tatiana","Fabiana","Soraia"};
	public static final String[] var_nomesfamilia = {"Silva","Santos","Ferreira","Pereira","Oliveira","Costa","Rodrigues","Martins","Jesus","Sousa","Fernandes","Gonçalves","Gomes","Lopes","Marques","Alves","Almeida","Ribeiro","Pinto","Carvalho","Teixeira","Moreira","Correia","Mendes","Nunes","Soares","Vieira","Monteiro","Cardoso","Rocha","Raposo","Neves","Coelho","Cruz","Cunha","Pires","Ramos","Reis","Simões","Antunes","Matos","Fonseca","Machado","Araújo","Barbosa","Tavares","Lourenço","Castro","Figueiredo","Azevedo"};
  public static final String carateres = "QWERTYUIOPASDFGHJKLÇZXCVBNMqwertyuiopasdfghjklçzxcvbnm0123456789";
  public static HashMap<String, List<String>> hospitais = new HashMap<String, List<String>>();
  public static HashMap<String, List<String>> medicos = new HashMap<String, List<String>>();


  public Runner(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void run(String... args) throws Exception {
    List<String> value = Arrays.asList("Braga", "Norte","105");
    hospitais.put("Hospital de Braga", value);
    value = Arrays.asList("Porto", "Norte", "210");
    hospitais.put("Centro Hospitalar Universitário de São João", value);
    value = Arrays.asList("Guimarães", "Norte", "80");
    hospitais.put("Hospital Da Senhora Da Oliveira", value);
    value = Arrays.asList("Porto", "Norte", "122");
    hospitais.put("Centro Hospitalar do Porto", value);
    value = Arrays.asList("Bragança", "Norte", "78");
    hospitais.put("Unidade Hospital de Bragança", value);

    value = Arrays.asList("Almada", "Lisboa e Vale do Tejo", "253");
    hospitais.put("Hospital Garcia de Orta", value);
    value = Arrays.asList("Lisboa", "Lisboa e Vale do Tejo", "224");
    hospitais.put("Hospital Dona Estefânia", value);
    value = Arrays.asList("Lisboa", "Lisboa e Vale do Tejo", "313");
    hospitais.put("Hospital da Luz", value);
    value = Arrays.asList("Setúbal", "Lisboa e Vale do Tejo", "134");
    hospitais.put("Centro Hospital de Setúbal", value);

    value = Arrays.asList("Viseu", "Centro", "110");
    hospitais.put("Hospital CUF Viseu",  value);
    value = Arrays.asList("Covilhã", "Centro", "75");
    hospitais.put("Centro Hospitalar Universitário Cova da Beira", value);
    value = Arrays.asList("Castelo Branco", "Centro", "125");
    hospitais.put("Unidade Local de Saúde de Castelo Branco", value);

    value = Arrays.asList("Évora", "Alentejo", "76");
    hospitais.put("Hospital do Espírito Santo de Évora", value);
    value = Arrays.asList("Beja", "Alentejo", "92");
    hospitais.put("Unidade Local de Saúde do Baixo Alentejo", value);

    value = Arrays.asList("Faro", "Algarve", "90");
    hospitais.put("Hospital de Faro", value);
    value = Arrays.asList("Portimão", "Algarve", "82");
    hospitais.put("Hospital de Portimão", value);

    value = Arrays.asList("Ilha do Faial", "Açores", "54");
    hospitais.put("Hospital da Horta", value);
    value = Arrays.asList("Lagoa", "Açores", "49");
    hospitais.put("Hospital Internacional Dos Açores", value);

    value = Arrays.asList("Funchal", "Madeira", "63");
    hospitais.put("Hospital Dr. Nélio Mendonça", value);

    JSONObject json;
    HashMap<String, Object> mensagem = new HashMap<String, Object>();
    for (String hospital: hospitais.keySet() ) {
      mensagem = construcaoMensagemHospital(hospitais.get(hospital));
      mensagem.put("nome", hospital);
      System.out.println("\n \n--------- Nova Mensagem : ---------");
      for (String item : mensagem.keySet()) {
        System.out.println(item + " -> " + mensagem.get(item));
      }
      json = new JSONObject(mensagem);
      rabbitTemplate.convertAndSend(DataHospitalDoctorsApplication.topicExchangeName, "filamedicospacientes", json.toString());
    }
    Thread.sleep(5000);
    HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/api/v1/hospitais")).setHeader("User-Agent", "Java 11 HttpClient Bot").build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    
    String[] texto = response.body().split("\\{");
    ArrayList<JSONObject> jsonHospitais = new ArrayList<JSONObject>();
    for (int i = 1; i<texto.length; i++) {
      String novotexto = '{'+texto[i];
      novotexto = novotexto.substring(0, novotexto.length()-1);
      JSONObject myHospital = new JSONObject(novotexto);
      jsonHospitais.add(myHospital);
    }

    for (String hospital: hospitais.keySet()) {
      for (int i = 0; i <= 9; i++) {
        mensagem = construcaoMensagemMedico(jsonHospitais, hospital);
        System.out.println("\n \n--------- Nova Mensagem : ---------");
        for (String item : mensagem.keySet()) {
          System.out.println(item + " -> " + mensagem.get(item));
        }
        json = new JSONObject(mensagem);
        rabbitTemplate.convertAndSend(DataHospitalDoctorsApplication.topicExchangeName, "filamedicospacientes", json.toString());
      }
    }
  }

  // geração de dados

  public static HashMap<String, Object> construcaoMensagemHospital(List<String> hospital) throws JSONException {
    HashMap<String, Object> mensagem = new HashMap<String, Object>();
    mensagem.put("tipo", "hospital");
    mensagem.put("concelho", hospital.get(0));
    mensagem.put("regiao", hospital.get(1));
    mensagem.put("numero_camas", hospital.get(2));
		return mensagem;
	} 

  public static HashMap<String, Object> construcaoMensagemMedico(ArrayList<JSONObject> jsonHospitais, String nomeHospital) throws JSONException {
    HashMap<String, Object> mensagem = new HashMap<String, Object>();
    mensagem.put("tipo", "medico");
    for (JSONObject json: jsonHospitais) {
      if (json.getString("nome").equals(nomeHospital)) {
        mensagem.put("hospital", json.getInt("id"));
      }
    }
    mensagem.put("nome", getNomeValido());
    mensagem.put("idade", getIdadeValida());
    String codigo = "";
    for (int i = 0; i <= 7; i++) {
      int randomNum = rand.nextInt((63 - 0) + 1) + 0;
      codigo = codigo + carateres.charAt(randomNum);
    }
    mensagem.put("codigo", codigo);
		return mensagem;
	} 


  public static Integer getIdadeValida() {
		int val = -1;
		do {
			val = (int) (50f + 50f * (1 + rand.nextGaussian())/2);
		  } while (val >= 70 || val < 25);
		return val;
	}

  public static String getNomeValido() {
		int numeronomes = -1;
		String nome = "";
		double randomNum = rand.nextDouble();
		if (randomNum <= 0.3) {
			numeronomes = 3;
		} else if (randomNum <= 0.7) {
			numeronomes = 4;
		} else {
			numeronomes = 5;
    }
    randomNum = rand.nextDouble();
		if (randomNum <= 0.5) {
			for (int i = 0; i<numeronomes-1; i++) {
				int proximonome = rand.nextInt((77 - 0) + 1) + 0;
				nome = nome + var_nomesproprios_masculinos[proximonome] + " ";
			}
			int ultimonome = rand.nextInt((49 - 0) + 1) + 0;
			return nome + var_nomesfamilia[ultimonome];
		} else {
			for (int i = 0; i<numeronomes-1; i++) {
				int proximonome = rand.nextInt((71 - 0) + 1) + 0;
				nome = nome + var_nomesproprios_femininos[proximonome] + " ";
			}
			int ultimonome = rand.nextInt((49 - 0) + 1) + 0;
			return nome + var_nomesfamilia[ultimonome];
		}

	}
}