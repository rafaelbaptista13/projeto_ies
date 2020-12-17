package ua.ies.g23.Covinfo19.pacientes_med_hosp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hospitais")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "concelho", nullable = false)
    private String concelho;

    @Column(name = "regiao", nullable = false)
    private String regiao;

    @Column(name = "numero_camas", nullable = false)
    private int numero_camas;

    @Column(name = "numero_camas_ocupadas", nullable = false)
    private int numero_camas_ocupadas;

    public Hospital(){
        
    }

    public Hospital(String nome, String concelho, String regiao, int numero_camas, int numero_camas_ocupadas) {
        this.nome = nome;
        this.concelho = concelho;
        this.regiao = regiao;
        this.numero_camas = numero_camas;
        this.numero_camas_ocupadas = numero_camas_ocupadas;
    }
 
    
    public long getId() {
        return id;
    }
 
    
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    
    public String getConcelho() {
        return concelho;
    }
    public void setConcelho(String concelho) {
        this.concelho = concelho;
    }
    

    public String getRegiao() {
        return regiao;
    }
    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }
    

    public int getNumero_camas() {
        return numero_camas;
    }
    public void setNumero_camas(int numero_camas) {
        this.numero_camas = numero_camas;
    }

    public int getNumero_camas_ocupadas() {
        return numero_camas_ocupadas;
    }
    public void setNumero_camas_ocupadas(int numero_camas_ocupadas) {
        this.numero_camas_ocupadas = numero_camas_ocupadas;
    }

    @Override
    public String toString() {
        return "Hospital [id=" + id + ", Nome=" + nome + ", Concelho=" + concelho + ", Regiao=" + regiao
                + ", Numero_camas=" + numero_camas + ", Numero_camas_ocupadas=" + numero_camas_ocupadas
                + "]";
    }
 
}