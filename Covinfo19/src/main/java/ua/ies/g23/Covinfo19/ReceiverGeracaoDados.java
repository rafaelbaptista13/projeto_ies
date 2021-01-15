package ua.ies.g23.Covinfo19;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.json.JSONException;
import org.json.JSONObject;

import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.*;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.*;
import ua.ies.g23.Covinfo19.relatorios.model.Caso;
import ua.ies.g23.Covinfo19.relatorios.model.Relatorio_Paciente;
import ua.ies.g23.Covinfo19.relatorios.repository.*;

@Component
public class ReceiverGeracaoDados {

  private CountDownLatch latch = new CountDownLatch(1);
  private static Random rand = new Random();

  @Autowired
  private PacienteRepository pacienteRepository;
  @Autowired
  private CasoRepository casoRepository;
  @Autowired
  private Relatorio_PacienteRepository relatorioPacienteRepository;
  @Autowired
  private HospitalRepository hospitalRepository;
  @Autowired
  private MedicoRepository medicoRepository;


  //Receiver utilizada na recepção das mensagens enviados pelo script geração de novos pacientes.
  public void receiveMessage(String message) throws JSONException {
    JSONObject json = new JSONObject(message);
    System.out.println("-------- Novo Paciente -----------");
    System.out.println("Received <" + json.toString() + ">");

    //Criação de novo paciente e atribuição informação relativa ao mesmo.
    Paciente paciente = new Paciente();
    paciente.setNome(json.getString("nome"));
    paciente.setGenero(json.getString("genero"));
    paciente.setIdade(Integer.parseInt(json.getString("idade")));
    paciente.setConcelho(json.getString("cidade"));
    paciente.setRegiao(json.getString("regiao"));
    paciente.setNacionalidade(json.getString("nacionalidade"));
    paciente.setAltura(Integer.parseInt(json.getString("altura")));
    paciente.setPeso((float) Double.parseDouble(json.getString("peso")));
    paciente.setEstado_atual(json.getString("estado"));

    //Obtenção lista de hospitais da região do paciente utilizada para atribuição medico ao paciente.
    List<Hospital> listaHospitais = hospitalRepository.findAllFilters("%", "%", json.getString("regiao"), "0", "5000", "0", "5000");
    
    if (json.getString("estado").equals("Confinamento Domiciliário")) {
      //Seleção aleatória hospital
      int indexhospital = rand.nextInt((listaHospitais.size()-1 - 0) + 1) + 0;
      Hospital hospital = listaHospitais.get(indexhospital);

      //Obtenção lista de medicos do hospital selecionado
      List<Medico> listaMedicos = medicoRepository.findAllByFilters("%", String.valueOf(hospital.getId()), "0", "300");

      //Seleção aleatória medico
      int indexMedico = rand.nextInt((listaMedicos.size()-1 - 0) + 1) + 0;
      Medico medico = listaMedicos.get(indexMedico);

      //Atribuição do médico
      paciente.setMedico(medico);
    } else {
      Boolean medicoInserido = false;
      do {
        //Seleção aleatória hospital
        int indexhospital = rand.nextInt((listaHospitais.size()-1 - 0) + 1) + 0;
        Hospital hospital = listaHospitais.get(indexhospital);

        //Verificação hospital possuí camas disponíveis
        if (hospital.getNumero_camas_ocupadas() < hospital.getNumero_camas()) {
          //Obtenção lista de medicos do hospital selecionado
          List<Medico> listaMedicos = medicoRepository.findAllByFilters("%", String.valueOf(hospital.getId()), "0", "300");
          //Seleção aleatória medico
          int indexMedico = rand.nextInt((listaMedicos.size()-1 - 0) + 1) + 0;
          //Atribuição do médico
          Medico medico = listaMedicos.get(indexMedico);
          paciente.setMedico(medico);
          //Atualização número de camas do hospital
          hospital.setNumero_camas_ocupadas(hospital.getNumero_camas_ocupadas() + 1);
          hospitalRepository.save(hospital);
          medicoInserido = true;
          break;
        } else {
          listaHospitais.remove(hospital);
        }
      } while (listaHospitais.size() > 0);

      //Bloco código utilizado para atribuição médico ao paciente caso todos os hospitais da região do paciente estejam cheios. Vai ser atribuido um hospital de outra região de portugal. Lógica semelhante ao anterior 
      if (!medicoInserido) {
        listaHospitais = hospitalRepository.findAll();
        do {
          int indexhospital = rand.nextInt((listaHospitais.size()-1 - 0) + 1) + 0;
          Hospital hospital = listaHospitais.get(indexhospital);
          if (hospital.getNumero_camas_ocupadas() < hospital.getNumero_camas()) {
            List<Medico> listaMedicos = medicoRepository.findAllByFilters("%", String.valueOf(hospital.getId()), "0", "300");
            int indexMedico = rand.nextInt((listaMedicos.size()-1 - 0) + 1) + 0;
            Medico medico = listaMedicos.get(indexMedico);
            paciente.setMedico(medico);
            hospital.setNumero_camas_ocupadas(hospital.getNumero_camas_ocupadas() + 1);
            hospitalRepository.save(hospital);
            medicoInserido = true;
            break;
          } else {
            listaHospitais.remove(hospital);
          }
        } while (listaHospitais.size() > 0);
      }

      //Bloco código utilizado para atribuição médico ao paciente caso todos os hospitais de Portugal estejam cheios. Vai ser atribuido um hospital de região ficando o mesmo sobrelotado. Lógica semelhante ao anterior 
      if (!medicoInserido) {
        listaHospitais = hospitalRepository.findAllFilters("%", "%", json.getString("regiao"), "0", "5000", "0", "5000");
        int indexhospital = rand.nextInt((listaHospitais.size()-1 - 0) + 1) + 0;
        Hospital hospital = listaHospitais.get(indexhospital);
        List<Medico> listaMedicos = medicoRepository.findAllByFilters("%", String.valueOf(hospital.getId()), "0", "300");
        int indexMedico = rand.nextInt((listaMedicos.size()-1 - 0) + 1) + 0;
        Medico medico = listaMedicos.get(indexMedico);
        paciente.setMedico(medico);
        hospital.setNumero_camas_ocupadas(hospital.getNumero_camas_ocupadas() + 1);
        hospitalRepository.save(hospital);
        medicoInserido = true;
      }
    }
    pacienteRepository.save(paciente);
    System.out.println(paciente);

    //Criação novo caso para o paciente. Atribuição dos valores dos campos do mesmo.
    Caso caso = new Caso();
    caso.setEstado_atual(json.getString("estado"));
    caso.setPaciente_id(paciente.getPacienteId());
    casoRepository.save(caso);

    DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date d2;
    try {
      d2 = df2.parse(json.getString("time").split(" ")[0] + " " + json.getString("time").split(" ")[2] );

      //Criação relatorio para guardar o estado e tempo associado à mensagem recebida neste momento. Atribuição desse relatorio ao caso do paciente.
      Relatorio_Paciente relatorio_paciente = new Relatorio_Paciente();
      relatorio_paciente.setCaso(caso);
      relatorio_paciente.setEstado(json.getString("estado"));
      relatorio_paciente.setData(d2);
      relatorioPacienteRepository.save(relatorio_paciente);
      
    } catch (ParseException e) {
      e.printStackTrace();
    }
    
    latch.countDown();
  }

  public CountDownLatch getLatch() {
    return latch;
  }

}