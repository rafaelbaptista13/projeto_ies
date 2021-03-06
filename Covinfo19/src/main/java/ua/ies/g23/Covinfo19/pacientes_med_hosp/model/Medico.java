package ua.ies.g23.Covinfo19.pacientes_med_hosp.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.ManyToOne;

import ua.ies.g23.Covinfo19.Covinfo19Application;

@Entity
@Table(name = "medicos")
public class Medico implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long numero_medico;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "codigo_acesso", nullable = false)
    private String codigo_acesso;

    @Column(name = "idade", nullable = false)
    private int idade;

    // Trabalha num hospital
    @ManyToOne
    private Hospital hospital;

    public Medico() {

    }

    public Medico(long numero_medico, String nome, String codigo_acesso, int idade) {
        this.numero_medico = numero_medico;
        this.nome = nome;
        this.codigo_acesso = codigo_acesso;
        this.idade = idade;
    }

    public long getNumero_medico() {
        return numero_medico;
    }

    public void setNumero_medico(long numero_medico) {
        this.numero_medico = numero_medico;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo_acesso() {
        return codigo_acesso;
    }

    public void setCodigo_acesso(String codigo_acesso) {
        this.codigo_acesso = codigo_acesso;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public String getUsername() {
        return String.valueOf(this.numero_medico);
    }

    public String getPassword() {
        return this.codigo_acesso;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "Medico [numero_medico=" + numero_medico + ", Nome=" + nome + ", Codigo de acesso=" 
                + codigo_acesso + ", Idade=" + idade 
                + "]";
    }
 
}