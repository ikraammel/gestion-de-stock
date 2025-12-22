import { Component, Input } from '@angular/core';
import { LigneCommandeClientDto } from '../../gs-api';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-detail-cmd',
  imports: [CommonModule],
  templateUrl: './detail-cmd.html',
  styleUrl: './detail-cmd.scss',
})
export class DetailCmd {
   @Input()
  ligneCommande:LigneCommandeClientDto={};
}
