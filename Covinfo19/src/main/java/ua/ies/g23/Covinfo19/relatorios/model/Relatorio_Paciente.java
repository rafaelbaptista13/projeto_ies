package ua.ies.g23.Covinfo19.relatorios.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Paciente;

//import javax.persistence.ManyToOne;

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

    // Relatorios de casos
    @ManyToOne
    private Caso caso;
    
    public Relatorio_Paciente() {

    }

    public Relatorio_Paciente(String estado, Date data, Caso caso ) {
        this.estado = estado;
        this.data = data;
        this.caso = caso;
    }
 
    public long getId() {
        return id;
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
    
    
    public Caso getCaso() {     
        return this.caso;
    }
    public void setCaso(Caso caso) {
        this.caso = caso;
    }

    @Override
    public String toString() {
        return "Relatorio_Paciente [caso=" + caso + ", data=" + data + ", estado=" + estado + ", id=" + id + "]";
    }

}