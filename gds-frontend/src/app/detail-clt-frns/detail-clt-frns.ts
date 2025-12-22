import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ClientDto } from '../../gs-api';
import { Router } from '@angular/router';
import { Cltfrs } from '../services/cltfrs/cltfrs';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-detail-clt-frns',
  imports: [FormsModule,CommonModule],
  templateUrl: './detail-clt-frns.html',
  styleUrl: './detail-clt-frns.scss',
})
export class DetailCltFrns {
  showDeleteModal = false;
  errorMsgs: Array<string> = [];

  constructor(
    private router:Router,
    private cltFrsService: Cltfrs
  ) {}

  @Input()
  origin:any;

  @Input()
  clientFournisseur:ClientDto={}

  @Output()
  suppressionResult  = new EventEmitter()

  modifierCltFrs(){
    if(this.origin === "client"){
      this.router.navigate(['nouveauclient',this.clientFournisseur.id]);
    }else if(this.origin === "fournisseur"){
      this.router.navigate(['nouveaufournisseur',this.clientFournisseur.id]);
    }
  }

  ouvrirModalDelete() {
    this.showDeleteModal = true;
  }
    
      fermerModal() {
        this.showDeleteModal = false;
        this.errorMsgs = [];
      }
    
      confirmerDelete() {
    if (!this.clientFournisseur.id) return;

    const deleteObservable = this.origin === 'client' 
      ? this.cltFrsService.deleteClient(this.clientFournisseur.id)
      : this.cltFrsService.deleteFournisseur(this.clientFournisseur.id);

    deleteObservable.subscribe({
      next: () => {
        this.fermerModal();
        this.suppressionResult.emit('success');
      },
      error: (error) => {
        this.handleError(error);
      }
    });
  }
  private handleError(error: any) {
    if (error.error instanceof Blob) {
      const reader = new FileReader();
      reader.onload = () => {
        try {
          const parsed = JSON.parse(reader.result as string);
          this.errorMsgs = parsed.errors || [parsed.message] || ['Erreur inconnue'];
        } catch {
          this.errorMsgs = ['Erreur inconnue'];
        }
        this.suppressionResult.emit(this.errorMsgs);
      };
      reader.readAsText(error.error);
    } else {
      this.errorMsgs = error.error?.errors || [error.error?.message] || ['Erreur inconnue'];
      this.suppressionResult.emit(this.errorMsgs);
    }
  }
}