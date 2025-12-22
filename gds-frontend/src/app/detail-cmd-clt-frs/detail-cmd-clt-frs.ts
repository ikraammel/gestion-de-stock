import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ClientDto, CommandeClientDto } from '../../gs-api';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-detail-cmd-clt-frs',
  standalone: true, // Assurez-vous d'avoir CommonModule pour les pipes
  imports: [CommonModule],
  templateUrl: './detail-cmd-clt-frs.html',
  styleUrl: './detail-cmd-clt-frs.scss',
})
export class DetailCmdCltFrs implements OnChanges {

  @Input() origin = '';
  @Input() commande: any = {}; // Type 'any' car les champs varient entre clt et frs
  
  cltFrs: ClientDto = {};

  // Ce hook se déclenche dès que 'commande' ou 'origin' change
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['commande'] || changes['origin']) {
      this.extractClientFournisseur();
    }
  }

  extractClientFournisseur() {
  // On vérifie d'abord quel objet est présent dans la commande
  if (this.commande.client) {
    this.cltFrs = this.commande.client;
  } else if (this.commande.fournisseur) {
    this.cltFrs = this.commande.fournisseur;
  } else {
    this.cltFrs = {};
  }
}
} 