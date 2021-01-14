import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Paciente} from '../paciente';
import {ActivatedRoute, Router} from '@angular/router';
import {MedicService} from '../medic.service';
import {PacienteService} from '../paciente.service';

@Component({
  selector: 'app-inserirpaciente',
  templateUrl: './inserirpaciente.component.html',
  styleUrls: ['./inserirpaciente.component.css']
})
export class InserirpacienteComponent implements OnInit {
  medicoLogado: boolean;
  medicoId: number;

  formPaciente: FormGroup;
  nacionalidades = ['Alemã', 'Espanhola', 'Francesa', 'Italiana', 'Inglesa', 'Brasileira', 'Belga', 'Russa', 'Americana', 'Chinesa', 'Angolana',
    'Moçambicana', 'Holandesa', 'Polaca', 'Cabo-Verdiana', 'Albanesa', 'Austríaca', 'Búlgara', 'Croata', 'Dinamarquesa', 'Eslovaca',
    'Eslovena', 'Finlandesa', 'Grega', 'Húngara', 'Islandesa', 'Irlandesa', 'Lituana', 'Luxemburguesa', 'Norueguesa', 'Romena', 'Sueca',
    'Suíça', 'Turca', 'Ucraniana', 'Argentina', 'Canadiana', 'Mexicana', 'Japonesa', 'Portuguesa'].sort();
  regioes = ['Norte', 'Lisboa e Vale do Tejo', 'Centro', 'Alentejo', 'Algarve', 'Açores', 'Madeira'];
  formdic = {nome: 0, idade: 1, nacionalidade: 2, regiao: 3, peso: 4, altura: 5, estado: 6  };
  concelhos = ["Caminha","Melgaço","Ponte de Lima","Viana do Castelo","Vila Nova de Cerveira","Monção","Barcelos","Braga","Esposende","Fafe","Guimarães","Vizela","Vila Nova de Famalicão","Arouca","Espinho","Gondomar","Maia","Matosinhos","Porto","Póvoa de Varzim","Santa Maria da Feira","Oliveira de Azeméis","Santo Tirso","São João da Madeira","Trofa","Vila Nova de Gaia","Chaves","Montalegre","Amarante","Celorico de Basto","Lousada","Paços de Ferreira","Penafiel","Lamego","Peso da Régua","Vila Real","Bragança","Miranda do Douro","Mirandela",
    "Alcobaça","Alenquer","Óbidos","Nazaré","Peniche","Torres Vedras","Águeda","Albergaria-a-Velha","Aveiro","Estarreja","Ílhavo","Ovar","Sever do Vouga","Coimbra","Lousã","Mealhada","Mortágua","Oliveira do Hospital","Batalha","Leiria","Pombal","Mangualde","Nelas","Tondela","Vizeu","Vouzela","Castelo Branco","Oleiros","Abrantes","Entroncamento","Sertã","Tomar","Torres Novas","Vila de Rei","Belmonte","Covilhã","Fundão","Guarda","Manteigas","Seia","Sabugal",
    "Alcochete", "Almada", "Amadora", "Barreiro", "Cascais", "Lisboa", "Loures", "Mafra", "Montijo", "Odivelas", "Oeiras", "Palmela", "Seixal", "Sesimbra", "Setúbal", "Sintra", "Vila Franca de Xira",
    "Alcácer do Sal", "Odemira", "Sines", "Aljustrel", "Beja","Ferreira do Alentejo","Mértola","Ourique","Serpa","Azambuja","Almeirim","Benavente","Rio Maior","Santarém","Coruche","Cartaxo","Arronches","Campo Maior","Crato","Elvas","Marvão","Nisa","Portalegre","Estremoz","Évora","Mora","Redondo","Vendas Novas","Reguengos de Monsaraz","Viana do Alentejo",
    "Albufeira","Alcoutim","Aljezur","Castro Marim","Faro","Lagoa","Lagos","Loulé","Monchique","Olhão","Portimão","São Brás de Alportel","Silves","Tavira","Vila do Bispo","Vila Real de Santo António",
    "Vila do Porto","Ponta Delgada","Ribeira Grande","Vila Franca do Campo","Angra do Heroísmo","Vila da Praia da Vitória", "Velas", "Lajes do Pico","São Roque do Pico", "Horta", "Lajes das Flores", "Santa Cruz das Flores", "Corvo",
    "Câmara de Lobos", "Funchal", "Machico", "Ponta do Sol", "Porto Moniz", "São Vicente", "Santa Cruz", "Porto Santo"];
  generos = ['Masculino', 'Feminino'];
  concelhosdict = {'Norte': ["Caminha","Melgaço","Ponte de Lima","Viana do Castelo","Vila Nova de Cerveira","Monção","Barcelos","Braga","Esposende","Fafe","Guimarães","Vizela","Vila Nova de Famalicão","Arouca","Espinho","Gondomar","Maia","Matosinhos","Porto","Póvoa de Varzim","Santa Maria da Feira","Oliveira de Azeméis","Santo Tirso","São João da Madeira","Trofa","Vila Nova de Gaia","Chaves","Montalegre","Amarante","Celorico de Basto","Lousada","Paços de Ferreira","Penafiel","Lamego","Peso da Régua","Vila Real","Bragança","Miranda do Douro","Mirandela"],
  'Centro': ["Alcobaça","Alenquer","Óbidos","Nazaré","Peniche","Torres Vedras","Águeda","Albergaria-a-Velha","Aveiro","Estarreja","Ílhavo","Ovar","Sever do Vouga","Coimbra","Lousã","Mealhada","Mortágua","Oliveira do Hospital","Batalha","Leiria","Pombal","Mangualde","Nelas","Tondela","Vizeu","Vouzela","Castelo Branco","Oleiros","Abrantes","Entroncamento","Sertã","Tomar","Torres Novas","Vila de Rei","Belmonte","Covilhã","Fundão","Guarda","Manteigas","Seia","Sabugal"],
  'Lisboa e Vale do Tejo': ["Alcochete", "Almada", "Amadora", "Barreiro", "Cascais", "Lisboa", "Loures", "Mafra", "Montijo", "Odivelas", "Oeiras", "Palmela", "Seixal", "Sesimbra", "Setúbal", "Sintra", "Vila Franca de Xira"],
  'Alentejo': ["Alcácer do Sal", "Odemira", "Sines", "Aljustrel", "Beja","Ferreira do Alentejo","Mértola","Ourique","Serpa","Azambuja","Almeirim","Benavente","Rio Maior","Santarém","Coruche","Cartaxo","Arronches","Campo Maior","Crato","Elvas","Marvão","Nisa","Portalegre","Estremoz","Évora","Mora","Redondo","Vendas Novas","Reguengos de Monsaraz","Viana do Alentejo"],
  'Algarve': ["Albufeira","Alcoutim","Aljezur","Castro Marim","Faro","Lagoa","Lagos","Loulé","Monchique","Olhão","Portimão","São Brás de Alportel","Silves","Tavira","Vila do Bispo","Vila Real de Santo António"],
  'Açores': ["Vila do Porto","Ponta Delgada","Ribeira Grande","Vila Franca do Campo","Angra do Heroísmo","Vila da Praia da Vitória", "Velas", "Lajes do Pico","São Roque do Pico", "Horta", "Lajes das Flores", "Santa Cruz das Flores", "Corvo"],
  'Madeira': ["Câmara de Lobos", "Funchal", "Machico", "Ponta do Sol", "Porto Moniz", "São Vicente", "Santa Cruz", "Porto Santo"]};
  estados = ["Confinamento Domiciliário", "Internado", "Cuidados Intensivos"];

  constructor(private router: Router, private route: ActivatedRoute, private medicService: MedicService, private pacienteService: PacienteService) {}

  ngOnInit(): void {
    if (localStorage.getItem('codigo_acesso') != null) {
      this.medicoLogado = true;
      this.medicoId = Number(localStorage.getItem('codigo_acesso'));
    } else {
      this.router.navigate(['/login']);
    }

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
    this.addPaciente();
  }

  addPaciente(): void{
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

  inserir() {
    if (this.formPaciente.valid) {
      if (this.concelhosdict[this.formPaciente.controls.regiao.value].indexOf(this.formPaciente.controls.concelho.value) > -1) {
        const mensagem = {
          'nome': this.formPaciente.controls.nome.value,
          'genero': this.formPaciente.controls.genero.value,
          'idade': this.formPaciente.controls.idade.value,
          'concelho': this.formPaciente.controls.concelho.value,
          'regiao': this.formPaciente.controls.regiao.value,
          'nacionalidade': this.formPaciente.controls.nacionalidade.value,
          'altura': this.formPaciente.controls.altura.value,
          'peso': this.formPaciente.controls.peso.value,
          'medico_numero_medico': localStorage.getItem('codigo_acesso'),
          'estado': this.formPaciente.controls.estado.value
        }
        this.pacienteService.createPacient(mensagem).subscribe(result => console.log(result));
      }
    }
  }
}
