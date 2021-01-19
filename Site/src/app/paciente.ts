export class Paciente {
  pacienteId: number;
  genero: string;
  idade: number;
  nome: string;
  concelho: string;
  regiao: string;
  nacionalidade: string;
  altura: number;
  peso: number;
  estado_atual: string;
  medico_numero_medico: number;

  constructor(nome: string, genero: string, idade: number, concelho: string,  regiao: string, nacionalidade: string, altura: number, peso: number, estado_atual:string, medico_numero_medico:number) {
    this.nome = nome;
    this.genero = genero;
    this.idade = idade;
    this.concelho = concelho;
    this.regiao = regiao;
    this.nacionalidade = nacionalidade;
    this.altura = altura;
    this.peso = peso;
    this.estado_atual = estado_atual;
    this.medico_numero_medico = medico_numero_medico;
  }
}
