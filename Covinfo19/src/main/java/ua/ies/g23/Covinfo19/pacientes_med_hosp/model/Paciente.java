package ua.ies.g23.Covinfo19.pacientes_med_hosp.model;

import java.util.Date;

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
    private long paciente_id;

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

    @Column(name = "estado_atual", nullable = false)
    private String estado_atual;
    
    @Column(name = "data_insercao", nullable = false)
    private Date data_insercao;
    
    // é acompanhado por um medico
    @ManyToOne
    private Medico medico;


    public Paciente() {

    }

    public Paciente(long paciente_id, String nome, String genero, int idade, String concelho, String regiao, String nacionalidade, int altura, float peso, String estado_atual, Date data_insercao, Medico medico) {
        this.paciente_id = paciente_id;
        this.nome = nome;
        this.genero = genero;
        this.idade = idade;
        this.concelho = concelho;
        this.regiao = regiao;
        this.nacionalidade = nacionalidade;
        this.altura = altura;
        this.peso = peso;
        this.estado_atual = estado_atual;
        this.medico = medico;
        this.data_insercao = data_insercao;
    }
 
    
    public long getPacienteId() {
        return paciente_id;
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

    public String getEstado_atual() {
        return estado_atual;
    }

    public void setEstado_atual(String estado_atual) {
        this.estado_atual = estado_atual;
    }

    
    public Date getData_insercao() {
        return data_insercao;
    }

    public void setData_insercao(Date data_insercao) {
        this.data_insercao = data_insercao;
    }

    public Medico getMedico() {
        return medico;
    }
    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    @Override
    public String toString() {
        return "Paciente [paciente_id=" + paciente_id + ", Nome=" + nome + ", Genero=" 
                + genero + ", Idade=" + idade + ", Concelho=" + concelho + ", Regiao=" + regiao
                + ", Nacionalidade=" + nacionalidade + ", Altura=" + altura + ", Peso=" + peso
                + ", Medico=" + medico +"]";
    }

 
}