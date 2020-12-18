package ua.ies.g23.Covinfo19;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.ies.g23.Covinfo19.exception.ResourceNotFoundException;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Hospital;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Medico;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.HospitalRepository;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.MedicoRepository;

import org.json.JSONException;
import org.json.JSONObject;


@Component
public class ReceiverMedicosPacientes {

  @Autowired
  private HospitalRepository hospitalRepository;
  @Autowired
  private MedicoRepository medicoRepository;

  private CountDownLatch latch = new CountDownLatch(1);

  public void receiveMessage(String message) throws JSONException, ResourceNotFoundException {
    JSONObject json = new JSONObject(message);
    System.out.println("-------- Novo Hospital/Medico -----------");
    System.out.println("Received <" + json.toString() + ">");
    String entidade = json.getString("tipo");
    if (entidade.equals("hospital")) {
      Hospital hospital = new Hospital();
      hospital.setConcelho(json.getString("concelho"));
      hospital.setNome(json.getString("nome"));
      hospital.setNumero_camas(json.getInt("numero_camas"));
      hospital.setNumero_camas_ocupadas(0);
      hospital.setRegiao(json.getString("regiao"));
      hospitalRepository.save(hospital);
    } else {
      Medico medico = new Medico();
      medico.setCodigo_acesso(json.getString("codigo"));
      medico.setIdade(json.getInt("idade"));
      medico.setNome(json.getString("nome"));
      Long id = json.getLong("hospital");
      Hospital hospital = hospitalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hospital not found for this id :: " + String.valueOf(id)));
      medico.setHospital(hospital);
      medicoRepository.save(medico);
    }
    latch.countDown();
  }

  public CountDownLatch getLatch() {
    return latch;
  }

}