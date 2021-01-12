import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MedicService} from "../medic.service";

@Component({
  selector: 'app-paciente',
  templateUrl: './paciente.component.html',
  styleUrls: ['./paciente.component.css']
})
export class PacienteComponent implements OnInit {
  private id: string;
  formPaciente: FormGroup;
  nacionalidades = ['Alemã', 'Espanhola', 'Francesa', 'Italiana', 'Inglesa', 'Brasileira', 'Belga', 'Russa', 'Americana', 'Chinesa', 'Angolana',
    'Moçambicana', 'Holandesa', 'Polaca', 'Cabo-Verdiana', 'Albanesa', 'Austríaca', 'Búlgara', 'Croata', 'Dinamarquesa', 'Eslovaca',
    'Eslovena', 'Finlandesa', 'Grega', 'Húngara', 'Islandesa', 'Irlandesa', 'Lituana', 'Luxemburguesa', 'Norueguesa', 'Romena', 'Sueca',
    'Suíça', 'Turca', 'Ucraniana', 'Argentina', 'Canadiana', 'Mexicana', 'Japonesa', 'Portuguesa'].sort();
  regioes = ['Norte', 'Lisboa e Vale do Tejo', 'Centro', 'Alentejo', 'Algarve', 'Açores', 'Madeira'];
  concelhos = {'Norte': ["Caminha","Melgaço","Ponte de Lima","Viana do Castelo","Vila Nova de Cerveira","Monção","Barcelos","Braga","Esposende","Fafe","Guimarães","Vizela","Vila Nova de Famalicão","Arouca","Espinho","Gondomar","Maia","Matosinhos","Porto","Póvoa de Varzim","Santa Maria da Feira","Oliveira de Azeméis","Santo Tirso","São João da Madeira","Trofa","Vila Nova de Gaia","Chaves","Montalegre","Amarante","Celorico de Basto","Lousada","Paços de Ferreira","Penafiel","Lamego","Peso da Régua","Vila Real","Bragança","Miranda do Douro","Mirandela"]}
  estados = ['atvio', 'recuperado', 'internado', 'intensivo', 'obito'];
  private b_nome: HTMLElement;
  private b_idade: HTMLElement;
  private b_nacionalidade: HTMLElement;
  private b_regiao: HTMLElement;
  private b_peso: HTMLElement;
  private b_altura: HTMLElement;
  private b_estado: HTMLElement;
  private add: boolean;
  private update: boolean;
  constructor(private route: ActivatedRoute, private medicService: MedicService) {}

  ngOnInit(): void {
    //Função para colapsar navbar
    (function($) {
      $(document).ready(function () {
        $('#sidebarCollapse').on('click', function () {
          $('#sidebar').toggleClass('active');
          $(this).toggleClass('active');
        });
      });
    })(jQuery);

    //Função para colapsar aba de filtros
    (function($) {
      $(document).ready(function () {
        $('#filterCollapse').on('click', function () {
          $('#filters').toggle(500);
        });
      });
    })(jQuery);
    this.id = this.route.snapshot.paramMap.get('id');
    this.b_nome = document.getElementById('b_nome');
    this.b_idade = document.getElementById('b_idade');
    this.b_nacionalidade = document.getElementById('b_nacionalidade');
    this.b_regiao = document.getElementById('b_regiao');
    this.b_peso = document.getElementById('b_peso');
    this.b_altura = document.getElementById('b_altura');
    this.b_estado = document.getElementById('b_estado');
    if (this.id){
      this.editPaciente();
    }
    else{
      this.addPaciente();
    }
  }

  editPaciente(): void{
    this.add = false;
    this.update = true;
    this.b_nome.style.display = 'block';
    this.b_idade.style.display = 'block';
    this.b_regiao.style.display = 'block';
    this.b_nacionalidade.style.display = 'block';
    this.b_peso.style.display = 'block';
    this.b_altura.style.display = 'block';
    this.b_estado.style.display = 'block';
    // Chamar serviço e ir buscar os valores e inicializar
    this.formPaciente = new FormGroup(
      {
        nome: new FormControl('', [Validators.required]),
        idade: new FormControl('', [Validators.required]),
        nacionalidade: new FormControl('', [Validators.required]),
        regiao: new FormControl('', [Validators.required]),
        peso: new FormControl('', [Validators.required]),
        altura: new FormControl('', [Validators.required]),
        estado: new FormControl('', [Validators.required]),
      }
    );
    this.formPaciente.get('nome').disable();
    this.formPaciente.get('idade').disable();
    this.formPaciente.get('nacionalidade').disable();
    this.formPaciente.get('regiao').disable();
    this.formPaciente.get('peso').disable();
    this.formPaciente.get('altura').disable();
    this.formPaciente.get('estado').disable();
  }

  addPaciente(): void{
    this.add = true;
    this.update = false;
    this.b_nome.style.display = 'none';
    this.b_idade.style.display = 'none';
    this.b_regiao.style.display = 'none';
    this.b_nacionalidade.style.display = 'none';
    this.b_peso.style.display = 'none';
    this.b_altura.style.display = 'none';
    this.b_estado.style.display = 'none';
    this.formPaciente = new FormGroup(
      {
        nome: new FormControl('', [Validators.required]),
        idade: new FormControl('', [Validators.required]),
        genero: new FormControl('', [Validators.required]),
        nacionalidade: new FormControl('', [Validators.required]),
        regiao: new FormControl('', [Validators.required]),
        concelho: new FormControl('', [Validators.required]),
        peso: new FormControl('', [Validators.required]),
        altura: new FormControl('', [Validators.required]),
        estado: new FormControl('', [Validators.required]),
      }
    );
  }

  enableinput(input_name: string): void{
    this.formPaciente.get(input_name).enable();
  }

  setPacient(): void{
    if (this.formPaciente.valid){
      if (this.add){
        const medico_id = localStorage.getItem('codigo_acesso');
        const data = {nome: this.formPaciente.value.nome, idade: this.formPaciente.value.idade, genero: this.formPaciente.value.genero,
        concelho: this.formPaciente.value.concelho, regiao: this.formPaciente.value.regiao, nacionalidade: this.formPaciente.value.nacionalidade,
        altura: this.formPaciente.value.altura, peso: this.formPaciente.value.peso, estado: this.formPaciente.value.estado, medico: medico_id};
        this.medicService.addPacient(data);
      }
      else if (this.update){
        // Falta fazer este
        const data = {nome: this.formPaciente.value.nome, idade: this.formPaciente.value.idade, genero: this.formPaciente.value.genero,
          concelho: this.formPaciente.value.concelho, regiao: this.formPaciente.value.regiao, nacionalidade: this.formPaciente.value.nacionalidade,
          altura: this.formPaciente.value.altura, peso: this.formPaciente.value.peso, estado: this.formPaciente.value.estado};
        this.medicService.updatePacient(this.formPaciente.value);
      }
    }
  }
}
