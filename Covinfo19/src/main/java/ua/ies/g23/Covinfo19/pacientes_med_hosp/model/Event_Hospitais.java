package ua.ies.g23.Covinfo19.pacientes_med_hosp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "event_hospitais")
public class Event_Hospitais {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Column(name = "data", nullable = false)
    private Date data;

    @Column(name = "hospital_id", nullable = false)
    private Long hospital_id;

    public Event_Hospitais() {
    }

    public Event_Hospitais(Long hospital_id, Date data) {
        this.hospital_id = hospital_id;
        this.data = data;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Long getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(Long hospital_id) {
        this.hospital_id = hospital_id;
    }

	@Override
	public String toString() {
		return "Event_Hospitais [data=" + data + ", hospital_id=" + hospital_id + "]";
	}


    

    
}
