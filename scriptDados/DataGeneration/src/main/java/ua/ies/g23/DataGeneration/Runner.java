package ua.ies.g23.DataGeneration;

import java.util.*;
import java.text.*;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.json.JSONObject;

@Component
public class Runner implements CommandLineRunner {

  private final RabbitTemplate rabbitTemplate;

  public Runner(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void run(String... args) throws Exception {
	HashMap<String, Object> mensagem = new HashMap<String, Object>();
	
	// Construção 10 Mensagens Novos Pacientes Distintos
	for (int i = 0; i<= 500; i++) {
		mensagem = construcaoMensagem();

		//Atribuição tempo atual à mensagem inserção paciente
		mensagem.put("time",formatter.format(new Date(System.currentTimeMillis())));
		System.out.println("\n \n--------- Novo Paciente : ---------");
		for (String item: mensagem.keySet() ) {
			System.out.println(item + " -> " + mensagem.get(item));
		}
		JSONObject json = new JSONObject(mensagem);

		//Envio da Mensagem
		rabbitTemplate.convertAndSend(DataGenerationApplication.topicExchangeName, "filagerar", json.toString());
		rabbitTemplate.convertAndSend(DataGenerationApplication.topicExchangeName, "filagerar", json.toString());
	}
  }

  	//Conjunto variaveis finais utilizadas para geração aleatória de dados
  	public static int var_paciente_id = 1;
	public static final String[] var_estado = {"Confinamento Domiciliário", "Internado", "Cuidados Intensivos"};
	public static final String[] var_genero = {"Masculino", "Feminino"};
	public static final String[] var_nacionalidade_maior_probabilidade = {"Alemã","Espanhola","Francesa","Italiana","Inglesa","Brasileira"};
	public static final String[] var_nacionalidade_media_probabilidade = {"Belga","Russa","Americana","Chinesa","Angolana","Moçambicana","Holandesa","Cabo-Verdiana"};
	public static final String[] var_nacionalidade_menor_probabilidade = {"Albanesa", "Austríaca","Búlgara","Croata","Dinamarquesa","Eslovaca","Eslovena","Finlandesa","Grega","Húngara","Islandesa","Irlandesa","Lituana","Luxemburguesa","Norueguesa","Polaca","Romena","Sueca","Suíça","Turca","Ucraniana","Argentina","Canadiana","Mexicana","Japonesa"};
	public static final String[] var_nomesproprios_masculinos = {"João","Rodrigo","Martim","Francisco","Santiago","Tomás","Guilherme","Afonso","Gonçalo","Miguel","Duarte","Tiago","Pedro","Gabriel","Diogo","Rafael","Gustavo","Dinis","David","Lucas","Salvador","Simão","José","Daniel","António","Lourenço","André","Diego","Vicente","Manuel","Henrique","Leonardo","Vasco","Bernardo","Mateus","Luís","Eduardo","Leandro","Alexandre","Rúben","Filipe","Ricardo","Samuel","Bruno","Matias","Nuno","Enzo","Rui","Hugo","Carlos","Xavier","Isaac","Fábio","Artur","Jorge","Sebastião","Paulo","Ivo","Marco","Frederico","Davi","Cristiano","Joaquim","Renato","Ângelo","Micael","Valentim","Ivan","Sérgio","Mário","Tomé","Joel","Jaime","Sandro","Lisandro","Márcio","Luca","Fernando"};
	public static final String[] var_nomesproprios_femininos = {"Maria","Matilde","Leonor","Mariana","Carolina","Beatriz","Ana","Inês","Lara","Margarida","Sofia","Joana","Francisca","Laura","Madalena","Luana","Diana","Rita","Mafalda","Sara","Bianca","Alice","Eva","Clara","Íris","Constança","Letícia","Mara","Catarina","Gabriela","Marta","Vitória","Yara","Camila","Ariana","Ema","Daniela","Núria","Iara","Rafaela","Benedita","Bruna","Filipa","Júlia","Bárbara","Jéssica","Victória","Carlota","Nicole","Alícia","Lia","Helena","Raquel","Teresa","Luísa","Isabel","Érica","Miriam","Kyara","Juliana","Alexandra","Yasmin","Luna","Mia","Débora","Adriana","Melissa","Carminho","Valentina","Tatiana","Fabiana","Soraia"};
	public static final String[] var_nomesfamilia = {"Silva","Santos","Ferreira","Pereira","Oliveira","Costa","Rodrigues","Martins","Jesus","Sousa","Fernandes","Gonçalves","Gomes","Lopes","Marques","Alves","Almeida","Ribeiro","Pinto","Carvalho","Teixeira","Moreira","Correia","Mendes","Nunes","Soares","Vieira","Monteiro","Cardoso","Rocha","Raposo","Neves","Coelho","Cruz","Cunha","Pires","Ramos","Reis","Simões","Antunes","Matos","Fonseca","Machado","Araújo","Barbosa","Tavares","Lourenço","Castro","Figueiredo","Azevedo"};
	public static final String[] var_regioes = {"Norte", "Lisboa e Vale do Tejo", "Centro", "Alentejo", "Algarve", "Açores", "Madeira"};
	public static final String[] var_cidades_norte = {"Caminha","Melgaço","Ponte de Lima","Viana do Castelo","Vila Nova de Cerveira","Monção","Barcelos","Braga","Esposende","Fafe","Guimarães","Vizela","Vila Nova de Famalicão","Arouca","Espinho","Gondomar","Maia","Matosinhos","Porto","Póvoa de Varzim","Santa Maria da Feira","Oliveira de Azeméis","Santo Tirso","São João da Madeira","Trofa","Vila Nova de Gaia","Chaves","Montalegre","Amarante","Celorico de Basto","Lousada","Paços de Ferreira","Penafiel","Lamego","Peso da Régua","Vila Real","Bragança","Miranda do Douro","Mirandela"};
	public static final String[] var_cidades_centro = {"Alcobaça","Alenquer","Óbidos","Nazaré","Peniche","Torres Vedras","Águeda","Albergaria-a-Velha","Aveiro","Estarreja","Ílhavo","Ovar","Sever do Vouga","Coimbra","Lousã","Mealhada","Mortágua","Oliveira do Hospital","Batalha","Leiria","Pombal","Mangualde","Nelas","Tondela","Vizeu","Vouzela","Castelo Branco","Oleiros","Abrantes","Entroncamento","Sertã","Tomar","Torres Novas","Vila de Rei","Belmonte","Covilhã","Fundão","Guarda","Manteigas","Seia","Sabugal"};
	public static final String[] var_cidades_lisboa = {"Alcochete", "Almada", "Amadora", "Barreiro", "Cascais", "Lisboa", "Loures", "Mafra", "Montijo", "Odivelas", "Oeiras", "Palmela", "Seixal", "Sesimbra", "Setúbal", "Sintra", "Vila Franca de Xira"};
	public static final String[] var_cidades_alentejo = {"Alcácer do Sal", "Odemira", "Sines", "Aljustrel", "Beja","Ferreira do Alentejo","Mértola","Ourique","Serpa","Azambuja","Almeirim","Benavente","Rio Maior","Santarém","Coruche","Cartaxo","Arronches","Campo Maior","Crato","Elvas","Marvão","Nisa","Portalegre","Estremoz","Évora","Mora","Redondo","Vendas Novas","Reguengos de Monsaraz","Viana do Alentejo"};
	public static final String[] var_cidades_algarve = {"Albufeira","Alcoutim","Aljezur","Castro Marim","Faro","Lagoa","Lagos","Loulé","Monchique","Olhão","Portimão","São Brás de Alportel","Silves","Tavira","Vila do Bispo","Vila Real de Santo António"};
	public static final String[] var_cidades_acores = {"Vila do Porto","Ponta Delgada","Ribeira Grande","Vila Franca do Campo","Angra do Heroísmo","Vila da Praia da Vitória", "Velas", "Lajes do Pico","São Roque do Pico", "Horta", "Lajes das Flores", "Santa Cruz das Flores", "Corvo"};
	public static final String[] var_cidades_madeira = {"Câmara de Lobos", "Funchal", "Machico", "Ponta do Sol", "Porto Moniz", "São Vicente", "Santa Cruz", "Porto Santo"};
	public static final SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
	public static Random rand = new Random();


	////
	//	Função Criação Mensagem Novo Paciente. 
	////
	public static HashMap<String, Object> construcaoMensagem() {
		int paciente_id = var_paciente_id;
		var_paciente_id++;
		String estado = getEstadoValido();
		String genero = getGeneroValido();
		Integer altura = getAlturaValida(genero);
		Double peso = getPesoValido(genero);
		Integer idade = getIdadeValida();
		String nacionalidade = getNacionalidadeValida();
		String nome = getNomeValido(genero);
		String regiao = getRegiaoValida();
		String cidade = getCidadeValida(regiao);
		HashMap<String, Object> mensagem = new HashMap<String, Object>();
		mensagem.put("paciente_id",paciente_id);
		mensagem.put("estado",estado);
		mensagem.put("genero",genero);
		mensagem.put("altura",altura);
		mensagem.put("peso",peso);
		mensagem.put("idade",idade);
		mensagem.put("nacionalidade",nacionalidade);
		mensagem.put("nome",nome);
		mensagem.put("regiao", regiao);
		mensagem.put("cidade", cidade);
		return mensagem;
	}


	////
	//	Função seleção estado do novo paciente de forma random. Possiveis Estados: "Confinamento Domiciliário", "Internado", "Cuidados Intensivos"
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
	//	Função seleção genero do novo paciente de forma random. Possiveis Generos: "Masculino", "Feminino"
	////
	public static String getGeneroValido() {
		int randomNum = rand.nextInt((1 - 0) + 1) + 0;
		return var_genero[randomNum];
	}

	////
	//	Função seleção altura do novo paciente de forma random.
	////
	public static Integer getAlturaValida(String genero) {
		if (genero.equals("Masculino")) {
			return (int) (173f + 15f * (1 + rand.nextGaussian())/2);
		} else {
			return (int) (160f + 10f * (1 + rand.nextGaussian())/2);
		}
	}


	////
	//	Função seleção peso do novo paciente de forma random.
	////
	public static Double getPesoValido(String genero) {
		if (genero.equals("Masculino")) {
			double x =  (75f + 20f * (1 + rand.nextGaussian())/2);
			return (double)Math.round(x * 10d) / 10d;
		} else {
			double x =  (55f + 20f * (1 + rand.nextGaussian())/2);
			return (double)Math.round(x * 10d) / 10d;
		}
	}


	////
	//	Função seleção idade do novo paciente de forma random.
	////
	public static Integer getIdadeValida() {
		int val = -1;
		do {
			val = (int) (50f + 50f * (1 + rand.nextGaussian())/2);
		  } while (val >= 105 || val < 1);
		return val;
	}
	
	////
	//	Função seleção Nacionalidade do novo paciente de forma random. Possiveis Nacionalidades: "Portuguesa" ou qualquer outra nacionalidade presente nos arrays inicialiados emcima.
	////
	public static String getNacionalidadeValida() {
		double randomNum = rand.nextDouble();
		if (randomNum <= 0.9) {
			return "Portuguesa";
		} else if (randomNum <= 0.95) {
			int newrandomNum = rand.nextInt((5 - 0) + 1) + 0;
			return var_nacionalidade_maior_probabilidade[newrandomNum];
		} else if (randomNum <= 0.98) {
			int newrandomNum = rand.nextInt((7 - 0) + 1) + 0;
			return var_nacionalidade_media_probabilidade[newrandomNum];
		} else {
			int newrandomNum = rand.nextInt((24 - 0) + 1) + 0;
			return var_nacionalidade_menor_probabilidade[newrandomNum];
		}
	}


	////
	//	Função seleção Nome do novo paciente de forma random.
	////
	public static String getNomeValido(String genero) {
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
		if (genero.equals("Masculino")) {
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

	////
	//	Função seleção Regiao do novo paciente de forma random. Possiveis Regiões: "Norte", "Lisboa e Vale do Tejo", "Centro", "Alentejo", "Algarve", "Açores", "Madeira"
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

	////
	//	Função seleção Cidade do novo paciente de forma random. 
	////
	public static String getCidadeValida(String regiao) {
		if (regiao.equals(var_regioes[0])) {
			int randomNum = rand.nextInt((38 - 0) + 1) + 0;
			return var_cidades_norte[randomNum];
		} else if (regiao.equals(var_regioes[1])) {
			int randomNum = rand.nextInt((16 - 0) + 1) + 0;
			return var_cidades_lisboa[randomNum];
		} else if (regiao.equals(var_regioes[2])) {
			int randomNum = rand.nextInt((40 - 0) + 1) + 0;
			return var_cidades_centro[randomNum];
		} else if (regiao.equals(var_regioes[3])) {
			int randomNum = rand.nextInt((29 - 0) + 1) + 0;
			return var_cidades_alentejo[randomNum];
		} else if (regiao.equals(var_regioes[4])) {
			int randomNum = rand.nextInt((15 - 0) + 1) + 0;
			return var_cidades_algarve[randomNum];
		} else if (regiao.equals(var_regioes[5])) {
			int randomNum = rand.nextInt((12 - 0) + 1) + 0;
			return var_cidades_acores[randomNum];
		} else {
			int randomNum = rand.nextInt((7 - 0) + 1) + 0;
			return var_cidades_madeira[randomNum];
		}

	}

}