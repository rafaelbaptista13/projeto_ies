package ua.ies.g23.Covinfo19.pacientes_med_hosp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "event_casodiario")
public class Event_CasoDiario {

    @Id
    @Column(name = "data", nullable = false)
    private Date data;

    @Column(name = "numero_casos", nullable = false)
    private Long numero_casos;

    public Event_CasoDiario() {
    }

    public Event_CasoDiario(Long numero_casos, Date data) {
        this.numero_casos = numero_casos;
        this.data = data;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Long getNumero_casos() {
        return numero_casos;
    }

    public void setNumero_casos(Long numero_casos) {
        this.numero_casos = numero_casos;
    }

    @Override
    public String toString() {
        return "Event_CasoDiario [data=" + data + ", numero_casos=" + numero_casos + "]";
    }

    

    
}
