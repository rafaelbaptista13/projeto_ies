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
import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Hospital;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Medico;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Paciente;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.HospitalRepository;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.MedicoRepository;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.PacienteRepository;

import org.json.JSONException;
import org.json.JSONObject;



@Component
public class ReceiverUpdateData {

  @Autowired
  private CasoRepository casoRepository;
  @Autowired
  private Relatorio_PacienteRepository relatorioPacienteRepository;
  @Autowired
  private PacienteRepository pacienteRepository;
  @Autowired
  private MedicoRepository medicoRepository;
  @Autowired
  private HospitalRepository hospitalRepository;

    
  private CountDownLatch latch = new CountDownLatch(1);

  //Receiver utilizado na recepção das mensagens enviadãs pelo script atualização estado de paciente.
  public void receiveMessage(String message) throws JSONException, ResourceNotFoundException {
    JSONObject json = new JSONObject(message);
    System.out.println("-------- Novo Paciente -----------");
    System.out.println("Received <" + json.toString() + ">");
    Long id = json.getLong("id");

    //Pesquisa caso associado à mensagem
    Caso caso = casoRepository.findById(id) .orElseThrow(() -> new ResourceNotFoundException("Caso not found for this id :: " + id));
    //Pesquisa paciente associado ao caso
    Paciente paciente = pacienteRepository.findById(caso.getPaciente_id()).orElseThrow(() -> new ResourceNotFoundException("Paciente not found for this id :: " + String.valueOf(id)));
    //Pesquisa medico associado ao paciente.
    Medico medico = medicoRepository.findById(paciente.getMedico().getNumero_medico()).orElseThrow(() -> new ResourceNotFoundException("Medico not found for this id :: " + String.valueOf(id)));
    //Pesquisa do hospital associado ao medico
    Hospital hospital = hospitalRepository.findById(medico.getHospital().getId()).orElseThrow(() -> new ResourceNotFoundException("Hospital not found for this id :: " + String.valueOf(id)));
    
    //Atualização do numero de camas do hospital tendo em conta a alteração de estado efetuada.
    if (caso.getEstado_atual().equals("Confinamento Domiciliário") && (json.getString("estado_atual").equals("Internado") || json.getString("estado_atual").equals("Cuidados Intensivos") )  ){
      hospital.setNumero_camas_ocupadas(hospital.getNumero_camas_ocupadas() + 1);
      hospitalRepository.save(hospital);
    } else if ( (caso.getEstado_atual().equals("Internado") || caso.getEstado_atual().equals("Cuidados Intensivos") ) && (json.getString("estado_atual").equals("Óbito") || json.getString("estado_atual").equals("Recuperado") || json.getString("estado_atual").equals("Confinamento Domiciliário") )  ) {
      hospital.setNumero_camas_ocupadas(hospital.getNumero_camas_ocupadas() - 1);
      hospitalRepository.save(hospital);
    }
    
    //Atualização do estado atual no caso e paciente em questão.
    paciente.setEstado_atual(json.getString("estado_atual"));
    pacienteRepository.save(paciente);
    caso.setEstado_atual(json.getString("estado_atual"));
    System.out.println(caso);
    casoRepository.save(caso);

    DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date d2;
    try {
      d2 = df2.parse(json.getString("time").split(" ")[0] + " " + json.getString("time").split(" ")[2] );

      //Criação novo relatório que irá armazenar o estado e data da mensagem.
      Relatorio_Paciente relatorio_paciente = new Relatorio_Paciente();
      relatorio_paciente.setCaso(caso);
      relatorio_paciente.setEstado(json.getString("estado_atual"));
      relatorio_paciente.setData(d2);
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