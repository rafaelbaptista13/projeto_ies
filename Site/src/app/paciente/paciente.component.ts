import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Paciente} from '../paciente';
import {PacienteService} from '../paciente.service';
import {MedicService} from "../medic.service";
import {CasosService} from '../casos.service';
import {Caso} from '../caso';
import {RelatoriopacienteService} from '../relatoriopaciente.service';

declare const CanvasJS: any;

@Component({
  selector: 'app-paciente',
  templateUrl: './paciente.component.html',
  styleUrls: ['./paciente.component.css']
})
export class PacienteComponent implements OnInit {
  private id: string;
  formPaciente: FormGroup = new FormGroup({});
  nacionalidades = ['Alemã', 'Espanhola', 'Francesa', 'Italiana', 'Inglesa', 'Brasileira', 'Belga', 'Russa', 'Americana', 'Chinesa', 'Angolana',
    'Moçambicana', 'Holandesa', 'Polaca', 'Cabo-Verdiana', 'Albanesa', 'Austríaca', 'Búlgara', 'Croata', 'Dinamarquesa', 'Eslovaca',
    'Eslovena', 'Finlandesa', 'Grega', 'Húngara', 'Islandesa', 'Irlandesa', 'Lituana', 'Luxemburguesa', 'Norueguesa', 'Romena', 'Sueca',
    'Suíça', 'Turca', 'Ucraniana', 'Argentina', 'Canadiana', 'Mexicana', 'Japonesa', 'Portuguesa'].sort();
  regioes = ['Norte', 'Lisboa e Vale do Tejo', 'Centro', 'Alentejo', 'Algarve', 'Açores', 'Madeira'];
  paciente_atual: Paciente;
  caso_atual: Caso;
  relatorios = [];
  concelhos = {'Norte': ["Caminha","Melgaço","Ponte de Lima","Viana do Castelo","Vila Nova de Cerveira","Monção","Barcelos","Braga","Esposende","Fafe","Guimarães","Vizela","Vila Nova de Famalicão","Arouca","Espinho","Gondomar","Maia","Matosinhos","Porto","Póvoa de Varzim","Santa Maria da Feira","Oliveira de Azeméis","Santo Tirso","São João da Madeira","Trofa","Vila Nova de Gaia","Chaves","Montalegre","Amarante","Celorico de Basto","Lousada","Paços de Ferreira","Penafiel","Lamego","Peso da Régua","Vila Real","Bragança","Miranda do Douro","Mirandela"]}
  estados = ['atvio', 'recuperado', 'internado', 'intensivo', 'obito'];

  private b_editar: HTMLElement;
  private b_confirmar: HTMLElement;
  constructor(private route: ActivatedRoute, private medicService: MedicService, private pacienteService: PacienteService, private casoService: CasosService, private relatorioService: RelatoriopacienteService) {}

  ngOnInit(): void {
    //chartEstados();
    this.b_editar = document.getElementById('b_editar');
    this.b_confirmar = document.getElementById('b_confirmar');
    // Função para colapsar navbar
    (function($) {
      $(document).ready(function () {
        $('#sidebarCollapse').on('click', function () {
          $('#sidebar').toggleClass('active');
          $(this).toggleClass('active');
        });
      });
    })(jQuery);

    // Função para colapsar aba de filtros
    (function($) {
      $(document).ready(function () {
        $('#filterCollapse').on('click', function () {
          $('#filters').toggle(500);
        });
      });
    })(jQuery);
    this.id = this.route.snapshot.paramMap.get('id');
    if (this.id){
      this.pacienteService.getPacientById(this.id).subscribe(
        result => {this.paciente_atual = result;
          this.casoService.getCasoByPaciente(this.paciente_atual.paciente_id).subscribe(result2 => {
            this.caso_atual = result2;
            this.relatorioService.getRelatoriosByCaso(this.caso_atual.id).subscribe(result3 => {
              this.relatorios = result3;
              console.log(this.relatorios);
              this.editPaciente();
            });
          });
        });
    }
    else{
      console.log("No ID was given.");
    }
  }

  editPaciente(): void{
    console.log(this.paciente_atual);
    this.formPaciente = new FormGroup(
      {
        nome: new FormControl(this.paciente_atual.nome, [Validators.required]),
        idade: new FormControl(this.paciente_atual.idade, [Validators.required]),
        genero: new FormControl(this.paciente_atual.genero, [Validators.required]),
        nacionalidade: new FormControl(this.paciente_atual.nacionalidade, [Validators.required]),
        regiao: new FormControl(this.paciente_atual.regiao, [Validators.required]),
        concelho: new FormControl(this.paciente_atual.concelho, [Validators.required]),
        peso: new FormControl(this.paciente_atual.peso, [Validators.required]),
        altura: new FormControl(this.paciente_atual.altura, [Validators.required]),
        estado: new FormControl(this.caso_atual.estadoAtual, [Validators.required]),
      }
    );
    //
    this.formPaciente.get('nome').disable();
    this.formPaciente.get('idade').disable();
    this.formPaciente.get('genero').disable();
    this.formPaciente.get('nacionalidade').disable();
    this.formPaciente.get('regiao').disable();
    this.formPaciente.get('concelho').disable();
    this.formPaciente.get('peso').disable();
    this.formPaciente.get('altura').disable();
    this.formPaciente.get('estado').disable();
  }

  enableinput(): void{
    this.b_editar.style.display = 'none';
    this.b_confirmar.style.display = 'block';
    this.formPaciente.get('nome').enable();
    this.formPaciente.get('idade').enable();
    this.formPaciente.get('genero').enable();
    this.formPaciente.get('nacionalidade').enable();
    this.formPaciente.get('regiao').enable();
    this.formPaciente.get('concelho').enable();
    this.formPaciente.get('peso').enable();
    this.formPaciente.get('altura').enable();
    this.formPaciente.get('estado').enable();
  }

  submit_editar(): void{
    if (this.formPaciente.valid){
      const data = {nome: this.formPaciente.value.nome, idade: this.formPaciente.value.idade, genero: this.formPaciente.value.genero,
        concelho: this.formPaciente.value.concelho, regiao: this.formPaciente.value.regiao,
        nacionalidade: this.formPaciente.value.nacionalidade, altura: this.formPaciente.value.altura,
        peso: this.formPaciente.value.peso, estado: this.formPaciente.value.estado};
      this.medicService.updatePacient(data);
    }
  }

  eliminar(): void{
    // Eliminar cliente
  }
}

// Função para especificar valores dos charts
function chartEstados() {

  // grafico todos os casos diarios

  var chart = new CanvasJS.Chart("estadossgraph", {
    animationEnabled: true,
    title:{
      text: ""
    },
    axisX:{
      valueFormatString: "DD MMM",
      crosshair: {
        enabled: true,
        snapToDataPoint: true
      }
    },
    axisY: {
      title: "Estado",
      valueFormatString: "##",
      crosshair: {
        enabled: true,
        snapToDataPoint: true,
        labelFormatter: function(e) {
          return CanvasJS.formatNumber(e.value, "##") + " infeções";
        }
      }
    },
    data: [{
      type: "area",
      xValueFormatString: "DD MMM",
      yValueFormatString: "##",
      dataPoints: ['Recuperado', 'Domiciliário', 'Internado', 'Intensivo', 'Óbito']
    }]
  });
  chart.render();

}

