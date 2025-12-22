import { Component, OnInit } from '@angular/core';
import { BouttonAction } from '../boutton-action/boutton-action';
import { Pagination } from '../pagination/pagination';
import { DetailCmd } from '../detail-cmd/detail-cmd';
import { DetailCmdCltFrs } from '../detail-cmd-clt-frs/detail-cmd-clt-frs';
import { ActivatedRoute, Router } from '@angular/router';
import { Cmdcltfrs } from '../services/commandecltfrs/cmdcltfrs';
import { CommandeClientDto } from '../../gs-api';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-page-cmd-clt-frs',
  imports: [BouttonAction,Pagination,DetailCmd,DetailCmdCltFrs,CommonModule,FormsModule],
  templateUrl: './page-cmd-clt-frs.html',
  styleUrl: './page-cmd-clt-frs.scss',
})
export class PageCmdCltFrs implements OnInit{

  origin='';
  listCommandes: any[] = [];
  constructor(
    public router:Router,
    private activatedRoute:ActivatedRoute,
    private cmdCltFrsService: Cmdcltfrs
  ){}

  ngOnInit(): void {
      this.activatedRoute.data.subscribe(data => {
        this.origin = data['origin'];
        this.findAllCommandes();
      })
  }

    nouvelleCommande(){
      if(this.origin=='client'){
        this.router.navigate(['nouvelle-commande-client'])
      }else if(this.origin=='fournisseur'){
        this.router.navigate(['nouvelle-commande-fournisseur'])
      }
    }

    findAllCommandes() {
  // On définit l'appel selon l'origine
  const call$: Observable<any> = this.origin === 'client' 
    ? this.cmdCltFrsService.findAllCommandeClients() 
    : this.cmdCltFrsService.findAllCommandeFournisseurs();

  // On souscrit directement
  call$.subscribe({
    next: (res: any) => { // Type 'any' explicite pour corriger l'erreur 7006
      if (res instanceof Blob) {
        const reader = new FileReader();
        reader.onload = () => {
          try {
            this.listCommandes = JSON.parse(reader.result as string);
            console.log('Commandes chargées (Blob) :', this.listCommandes);
          } catch (e) {
            console.error('Erreur de parsing JSON', e);
          }
        };
        reader.readAsText(res);
      } else {
        this.listCommandes = res;
        console.log('Commandes chargées (JSON direct) :', this.listCommandes);
      }
    },
    error: (err: any) => {
      console.error('Erreur lors de la récupération des commandes', err);
    }
  });
}
  // Calcule le nombre total d'articles pour une commande donnée
calculerNombreArticles(cmd: any): number {
  const lignes = cmd.ligneCommandeClient || cmd.ligneCommandeFournisseurs || [];
  return lignes.reduce((acc: number, lig: any) => acc + (lig.quantite || 0), 0);
}

// Calcule le montant total TTC de la commande
calculerTotalCommande(cmd: any): number {
  const lignes = cmd.ligneCommandeClient || cmd.ligneCommandeFournisseurs || [];
  return lignes.reduce((acc: number, lig: any) => {
    return acc + ((lig.quantite || 0) * (lig.prixUnitaire || 0));
  }, 0);
}

}
