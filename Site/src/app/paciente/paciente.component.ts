import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {FormControl, FormGroup, Validators} from "@angular/forms";

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
  formdic = {nome: 0, idade: 1, nacionalidade: 2, regiao: 3, peso: 4, altura: 5, estado: 6  };
  private b_nome: HTMLElement;
  private b_idade: HTMLElement;
  private b_nacionalidade: HTMLElement;
  private b_regiao: HTMLElement;
  private b_peso: HTMLElement;
  private b_altura: HTMLElement;
  private b_estado: HTMLElement;
  private i_nome: HTMLElement;
  private i_idade: HTMLElement;
  private i_nacionalidade: HTMLElement;
  private i_regiao: HTMLElement;
  private i_peso: HTMLElement;
  private i_altura: HTMLElement;
  private i_estado: HTMLElement;

  constructor(private route: ActivatedRoute) {}

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
  }

  addPaciente(): void{
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
        nacionalidade: new FormControl('', [Validators.required]),
        regiao: new FormControl('', [Validators.required]),
        peso: new FormControl('', [Validators.required]),
        altura: new FormControl('', [Validators.required]),
        estado: new FormControl('', [Validators.required]),
      }
    );
  }

  enableinput(input_name: string): void{
    this.formPaciente.controls[this.formdic[input_name]].enable();
  }
}
