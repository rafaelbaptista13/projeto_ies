import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {MedicService} from '../medic.service';

@Component({
  selector: 'app-aboutus',
  templateUrl: './aboutus.component.html',
  styleUrls: ['./aboutus.component.css']
})
export class AboutusComponent implements OnInit {
  medicoLogado: boolean;
  medicoId: number;
  nomeMedico: string;

  constructor(private router: Router, private medicoService: MedicService) { }

  ngOnInit(): void {
    if (localStorage.getItem('codigo_acesso') != null) {
      this.medicoLogado = true;
      this.medicoId = Number(localStorage.getItem('codigo_acesso'));
      this.medicoService.getMedicoById(this.medicoId).subscribe(resultado => {
        this.nomeMedico = 'Dr. ' + resultado.nome.split(' ')[0] + ' ' + resultado.nome.split(' ')[resultado.nome.split(' ').length - 1];
      });
    } else {
      this.medicoLogado = false;
      this.medicoLogado = false;
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
  }

  toggleDropdown(): void{
    const dropdownContent = document.getElementById('c_dropdown');

    if (dropdownContent.style.display === 'block') {
      dropdownContent.style.display = 'none';
    } else {
      dropdownContent.style.display = 'block';
    }
  }

  logout(): void {
    localStorage.removeItem('codigo_acesso');
    localStorage.removeItem('token');
    this.router.navigate(['/home']);
  }
}
