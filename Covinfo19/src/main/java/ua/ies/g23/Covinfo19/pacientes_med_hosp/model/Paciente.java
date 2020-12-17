package ua.ies.g23.Covinfo19.pacientes_med_hosp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.persistence.ManyToOne;

@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long numero_utente;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "genero", nullable = false)
    private String genero;

    @Column(name = "idade", nullable = false)
    private int idade;

    @Column(name = "concelho", nullable = false)
    private String concelho;

    @Column(name = "regiao", nullable = false)
    private String regiao;

    @Column(name = "nacionalidade", nullable = false)
    private String nacionalidade;

    @Column(name = "altura", nullable = false)
    private int altura ;

    @Column(name = "peso", nullable = false)
    private float peso;

    // é acompanhado por um medico
    @ManyToOne
    private Medico medico;


    public Paciente() {

    }

    public Paciente(long numero_utente, String nome, String genero, int idade, String concelho, String regiao, String nacionalidade, int altura, float peso, Medico medico) {
        this.numero_utente = numero_utente;
        this.nome = nome;
        this.genero = genero;
        this.idade = idade;
        this.concelho = concelho;
        this.regiao = regiao;
        this.nacionalidade = nacionalidade;
        this.altura = altura;
        this.peso = peso;
        this.medico = medico;
    }
 
    
    public long getNumero_utente() {
        return numero_utente;
    }
    public void setNumero_utente(long numero_utente) {
        this.numero_utente = numero_utente;
    }
    
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
 
    
    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
 
    
    public int getIdade() {
        return idade;
    }
    public void setIdade(int idade) {
        this.idade = idade;
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
    
   
    public String getNacionalidade() {
        return nacionalidade;
    }
    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }
    
   
    public int getAltura() {
        return altura;
    }
    public void setAltura(int altura) {
        this.altura = altura;
    }
    
    
    public float getPeso() {
        return peso;
    }
    public void setPeso(float peso) {
        this.peso = peso;
    }

    public Medico getMedico() {
        return medico;
    }
    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    @Override
    public String toString() {
        return "Paciente [numero_utente=" + numero_utente + ", Nome=" + nome + ", Genero=" 
                + genero + ", Idade=" + idade + ", Concelho=" + concelho + ", Regiao=" + regiao
                + ", Nacionalidade=" + nacionalidade + ", Altura=" + altura + ", Peso=" + peso
                + "]";
    }
 
}