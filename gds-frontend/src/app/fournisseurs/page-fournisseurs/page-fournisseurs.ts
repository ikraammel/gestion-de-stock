import { Component, OnInit } from '@angular/core';
import { DetailCltFrns } from '../../detail-clt-frns/detail-clt-frns';
import { BouttonAction } from '../../boutton-action/boutton-action';
import { Pagination } from '../../pagination/pagination';
import { Router } from '@angular/router';
import { Cltfrs } from '../../services/cltfrs/cltfrs';
import { FournisseurDto } from '../../../gs-api';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-page-fournisseurs',
  imports: [Pagination,BouttonAction,DetailCltFrns,FormsModule,CommonModule],
  templateUrl: './page-fournisseurs.html',
  styleUrl: './page-fournisseurs.scss',
})
export class PageFournisseurs implements OnInit{
  listFournisseurs:FournisseurDto[]=[]
  errorMsg = '';
  
  constructor(
    public router:Router,
    private fournisseurService:Cltfrs
  ){}

  ngOnInit(): void {
      this.findAllFournisseurs();
  }
  nouveauFournisseur(){
    this.router.navigate(['nouveaufournisseur'])
  }
findAllFournisseurs() {
  this.fournisseurService.findAllFournisseurs().subscribe(res => {

    if (res instanceof Blob) {
      const reader = new FileReader();
      reader.onload = () => {
        try {
          this.listFournisseurs = JSON.parse(reader.result as string);
          console.log('Fournisseurs reçus :', this.listFournisseurs);
        } catch (e) {
          console.error('Erreur parsing fournisseurs', e);
          this.listFournisseurs = [];
        }
      };
      reader.readAsText(res);
    } else {
      this.listFournisseurs = res;
    }

  });
}

  handleSuppression(event: any) {
    if (event === 'success') {
      this.findAllFournisseurs();
    }else{
      this.errorMsg = event;
    }
  }
}

