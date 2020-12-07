package ua.ies.g23.Covinfo19.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToOne;

import java.util.Date;

@Entity
@Table(name = "relatorio_pacientes")
public class Relatorio_Paciente {
     
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "data", nullable = false)
    private Date data;

    // Relatorios de paciente
    @ManyToOne
    private Paciente paciente;
    
    public Relatorio_Paciente() {

    }

    public Relatorio_Paciente(String estado, Date data, Paciente paciente) {
        this.estado = estado;
        this.data = data;
        this.paciente = paciente;
    }
 
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }
    public void setData(Date data) {
        this.data = data;
    }
 

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public Paciente getPaciente() {
        return paciente;
    }
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    
    @Override
    public String toString() {
        return "Relatorio Paciente [Paciente=" + paciente + ", Estado=" + estado + ", Data=" 
                + data + "]";
    }
 
}