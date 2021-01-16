import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
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
  medicoLogado: boolean;
  medicoId: number;
  regioesError: boolean;
  pessoamasculina: boolean;
  pessoafeminina: boolean;

  private id: string;
  formPaciente: FormGroup = new FormGroup({});
  formReady: boolean;
  nacionalidades = ['Alemã', 'Espanhola', 'Francesa', 'Italiana', 'Inglesa', 'Brasileira', 'Belga', 'Russa', 'Americana', 'Chinesa', 'Angolana',
    'Moçambicana', 'Holandesa', 'Polaca', 'Cabo-Verdiana', 'Albanesa', 'Austríaca', 'Búlgara', 'Croata', 'Dinamarquesa', 'Eslovaca',
    'Eslovena', 'Finlandesa', 'Grega', 'Húngara', 'Islandesa', 'Irlandesa', 'Lituana', 'Luxemburguesa', 'Norueguesa', 'Romena', 'Sueca',
    'Suíça', 'Turca', 'Ucraniana', 'Argentina', 'Canadiana', 'Mexicana', 'Japonesa', 'Portuguesa'].sort();
  regioes = ['Norte', 'Lisboa e Vale do Tejo', 'Centro', 'Alentejo', 'Algarve', 'Açores', 'Madeira'];
  paciente_atual: Paciente;
  caso_atual: Caso;
  relatorios = [];
  concelhos = ["Caminha","Melgaço","Ponte de Lima","Viana do Castelo","Vila Nova de Cerveira","Monção","Barcelos","Braga","Esposende","Fafe","Guimarães","Vizela","Vila Nova de Famalicão","Arouca","Espinho","Gondomar","Maia","Matosinhos","Porto","Póvoa de Varzim","Santa Maria da Feira","Oliveira de Azeméis","Santo Tirso","São João da Madeira","Trofa","Vila Nova de Gaia","Chaves","Montalegre","Amarante","Celorico de Basto","Lousada","Paços de Ferreira","Penafiel","Lamego","Peso da Régua","Vila Real","Bragança","Miranda do Douro","Mirandela",
    "Alcobaça","Alenquer","Óbidos","Nazaré","Peniche","Torres Vedras","Águeda","Albergaria-a-Velha","Aveiro","Estarreja","Ílhavo","Ovar","Sever do Vouga","Coimbra","Lousã","Mealhada","Mortágua","Oliveira do Hospital","Batalha","Leiria","Pombal","Mangualde","Nelas","Tondela","Vizeu","Vouzela","Castelo Branco","Oleiros","Abrantes","Entroncamento","Sertã","Tomar","Torres Novas","Vila de Rei","Belmonte","Covilhã","Fundão","Guarda","Manteigas","Seia","Sabugal",
    "Alcochete", "Almada", "Amadora", "Barreiro", "Cascais", "Lisboa", "Loures", "Mafra", "Montijo", "Odivelas", "Oeiras", "Palmela", "Seixal", "Sesimbra", "Setúbal", "Sintra", "Vila Franca de Xira",
    "Alcácer do Sal", "Odemira", "Sines", "Aljustrel", "Beja","Ferreira do Alentejo","Mértola","Ourique","Serpa","Azambuja","Almeirim","Benavente","Rio Maior","Santarém","Coruche","Cartaxo","Arronches","Campo Maior","Crato","Elvas","Marvão","Nisa","Portalegre","Estremoz","Évora","Mora","Redondo","Vendas Novas","Reguengos de Monsaraz","Viana do Alentejo",
    "Albufeira","Alcoutim","Aljezur","Castro Marim","Faro","Lagoa","Lagos","Loulé","Monchique","Olhão","Portimão","São Brás de Alportel","Silves","Tavira","Vila do Bispo","Vila Real de Santo António",
    "Vila do Porto","Ponta Delgada","Ribeira Grande","Vila Franca do Campo","Angra do Heroísmo","Vila da Praia da Vitória", "Velas", "Lajes do Pico","São Roque do Pico", "Horta", "Lajes das Flores", "Santa Cruz das Flores", "Corvo",
    "Câmara de Lobos", "Funchal", "Machico", "Ponta do Sol", "Porto Moniz", "São Vicente", "Santa Cruz", "Porto Santo"].sort();
  concelhosdict = {'Norte': ["Caminha","Melgaço","Ponte de Lima","Viana do Castelo","Vila Nova de Cerveira","Monção","Barcelos","Braga","Esposende","Fafe","Guimarães","Vizela","Vila Nova de Famalicão","Arouca","Espinho","Gondomar","Maia","Matosinhos","Porto","Póvoa de Varzim","Santa Maria da Feira","Oliveira de Azeméis","Santo Tirso","São João da Madeira","Trofa","Vila Nova de Gaia","Chaves","Montalegre","Amarante","Celorico de Basto","Lousada","Paços de Ferreira","Penafiel","Lamego","Peso da Régua","Vila Real","Bragança","Miranda do Douro","Mirandela"],
    'Centro': ["Alcobaça","Alenquer","Óbidos","Nazaré","Peniche","Torres Vedras","Águeda","Albergaria-a-Velha","Aveiro","Estarreja","Ílhavo","Ovar","Sever do Vouga","Coimbra","Lousã","Mealhada","Mortágua","Oliveira do Hospital","Batalha","Leiria","Pombal","Mangualde","Nelas","Tondela","Vizeu","Vouzela","Castelo Branco","Oleiros","Abrantes","Entroncamento","Sertã","Tomar","Torres Novas","Vila de Rei","Belmonte","Covilhã","Fundão","Guarda","Manteigas","Seia","Sabugal"],
    'Lisboa e Vale do Tejo': ["Alcochete", "Almada", "Amadora", "Barreiro", "Cascais", "Lisboa", "Loures", "Mafra", "Montijo", "Odivelas", "Oeiras", "Palmela", "Seixal", "Sesimbra", "Setúbal", "Sintra", "Vila Franca de Xira"],
    'Alentejo': ["Alcácer do Sal", "Odemira", "Sines", "Aljustrel", "Beja","Ferreira do Alentejo","Mértola","Ourique","Serpa","Azambuja","Almeirim","Benavente","Rio Maior","Santarém","Coruche","Cartaxo","Arronches","Campo Maior","Crato","Elvas","Marvão","Nisa","Portalegre","Estremoz","Évora","Mora","Redondo","Vendas Novas","Reguengos de Monsaraz","Viana do Alentejo"],
    'Algarve': ["Albufeira","Alcoutim","Aljezur","Castro Marim","Faro","Lagoa","Lagos","Loulé","Monchique","Olhão","Portimão","São Brás de Alportel","Silves","Tavira","Vila do Bispo","Vila Real de Santo António"],
    'Açores': ["Vila do Porto","Ponta Delgada","Ribeira Grande","Vila Franca do Campo","Angra do Heroísmo","Vila da Praia da Vitória", "Velas", "Lajes do Pico","São Roque do Pico", "Horta", "Lajes das Flores", "Santa Cruz das Flores", "Corvo"],
    'Madeira': ["Câmara de Lobos", "Funchal", "Machico", "Ponta do Sol", "Porto Moniz", "São Vicente", "Santa Cruz", "Porto Santo"]};
  estados = ['Confinamento Domiciliário', 'Internado', 'Cuidados Intensivos', 'Recuperado', 'Óbito'];

  private b_editar: HTMLElement;
  private b_confirmar: HTMLElement;
  constructor(private router: Router, private route: ActivatedRoute, private medicService: MedicService, private pacienteService: PacienteService, private casoService: CasosService, private relatorioService: RelatoriopacienteService) {}

  ngOnInit(): void {
    this.regioesError = false;
    this.pessoafeminina = false;
    this.pessoamasculina = false;
    if (localStorage.getItem('codigo_acesso') != null) {
      this.medicoLogado = true;
      this.medicoId = Number(localStorage.getItem('codigo_acesso'));
      this.formReady = false;
    } else {
      this.router.navigate(['/login']);
    }
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
          if (this.paciente_atual.genero === 'Masculino') {
            this.pessoamasculina = true;
          } else {
            this.pessoafeminina = true;
          }
          this.casoService.getCasoByPaciente(this.paciente_atual.pacienteId).subscribe(result2 => {
            this.caso_atual = result2;
            this.relatorioService.getRelatoriosByCaso(this.caso_atual.id).subscribe(result3 => {
              this.relatorios = result3;
              this.editPaciente();
            },
              error => {
                this.router.navigate(['/homeMedic']);
              });
          },
            error => {
              this.router.navigate(['/homeMedic']);
            });
        },
        error => {
          this.router.navigate(['/homeMedic']);
        });
    }
    else{
      console.log("No ID was given.");
    }
  }

  toggleDropdown(): void{
    const dropdownContent = document.getElementById('c_dropdown');

    if (dropdownContent.style.display === 'block') {
      dropdownContent.style.display = 'none';
    } else {
      dropdownContent.style.display = 'block';
    }
  }

  editPaciente(): void{
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
        estado: new FormControl(this.caso_atual.estado_atual, [Validators.required]),
      }
    );
    this.formReady = true;
    setTimeout(() => {
      chartEstados(this.relatorios);
    }, 1000)

  }

  enableinput(): void{
    this.b_editar = document.getElementById('b_editar');
    this.b_confirmar = document.getElementById('b_confirmar');
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
      if (this.concelhosdict[this.formPaciente.controls.regiao.value].indexOf(this.formPaciente.controls.concelho.value) > -1) {

        const data = {
          'pacientId': this.paciente_atual.pacienteId,
          'nome': this.formPaciente.value.nome,
          'idade': this.formPaciente.value.idade,
          'genero': this.formPaciente.value.genero,
          'concelho': this.formPaciente.value.concelho,
          'regiao': this.formPaciente.value.regiao,
          'nacionalidade': this.formPaciente.value.nacionalidade,
          'altura': this.formPaciente.value.altura,
          'peso': this.formPaciente.value.peso,
          'estado': this.formPaciente.value.estado,
          'medico_numero_medico': this.medicoId,
        };
        this.pacienteService.updatePacient(this.paciente_atual.pacienteId, data).subscribe(result => {
          this.ngOnInit();
        },
          error => {
            this.router.navigate(['/homeMedic']);
          });
      } else {
        this.regioesError = true;
      }
    }
  }

  eliminar(): void{
    this.pacienteService.deletePacient(this.paciente_atual.pacienteId).subscribe(sucesso => {this.router.navigate(['/homeMedic'])},
      error => {
        this.router.navigate(['/homeMedic']);
      });
  }
}

// Função para especificar valores dos charts
function chartEstados (relatorios: any[]) {
  var array_valores = [];
  for (let relatorio of relatorios) {
    let valor = 0;
    if (relatorio.estado === 'Recuperado') {
      valor = 20;
    } else if (relatorio.estado === 'Confinamento Domiciliário') {
      valor = 40;
    } else if (relatorio.estado === 'Internado') {
      valor = 60;
    } else if (relatorio.estado === 'Cuidados Intensivos') {
      valor = 80;
    } else if (relatorio.estado === 'Óbito') {
      valor = 100;
    }
    array_valores.push({x: new Date(relatorio.data), y: valor});
  }
  var labels = {
    20: 'Recuperado', 40: 'Confinamento Domiciliário', 60: 'Internado', 80: 'Cuidados Intensivos', 100: 'Óbito', 120: ' '
  };
  var chart = new CanvasJS.Chart("estadosgraph", {
    animationEnabled: true,
    exportEnabled: true,
    title: {
      text: "Evolução estado do paciente"
    },
    axisY:{
      interval: 20,
      maximum: 105,
      minimum: 1,
      labelFormatter: function(e) {
        return customLabel(e.value);
      }
    },
    data: [{
      type: "stepLine",
      yValueFormatString: " ",
      xValueFormatString: "DD MMM",
      markerSize: 10,
      lineThickness: 3,
      dataPoints: array_valores
    }]
  });
  chart.render();

  function customLabel (value) {
    if (value && labels[value])
      return labels[value];
  }

}
