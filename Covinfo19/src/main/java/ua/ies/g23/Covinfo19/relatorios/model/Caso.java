package ua.ies.g23.Covinfo19.relatorios.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "casos")
public class Caso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "estado_atual", nullable = false)
    private String estado_atual;

    @Column(name = "paciente_id", nullable = false)
    private long paciente_id;

    public Caso() {
    }

    public Caso(String estado_atual, long paciente_id) {
        this.estado_atual = estado_atual;
        this.paciente_id = paciente_id;
    }

    public long getId() {
        return this.id;
    }

    public String getEstado_atual() {
        return estado_atual;
    }

    public void setEstado_atual(String estado_atual) {
        this.estado_atual = estado_atual;
    }

    public long getPaciente_id() {
        return paciente_id;
    }

    public void setPaciente_id(long paciente_id) {
        this.paciente_id = paciente_id;
    }
    

    @Override
    public String toString() {
        return "Caso [id=" + this.id + ", Estado atual=" + this.estado_atual + ", paciente_id=" 
                + this.paciente_id + "]";
    }

    

    
}
