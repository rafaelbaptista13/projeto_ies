package ua.ies.g23.Covinfo19.model;

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

    public Hospital(){
        
    }

    public Hospital(long id, String nome, String concelho, String regiao, int numero_camas) {
        this.id = id;
        this.nome = nome;
        this.concelho = concelho;
        this.regiao = regiao;
        this.numero_camas = numero_camas;
    }
 
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Hospital [id=" + id + ", Nome=" + ", Concelho=" + concelho + ", Regiao=" + regiao
                + ", Numero_camas=" + numero_camas
                + "]";
    }
 
}