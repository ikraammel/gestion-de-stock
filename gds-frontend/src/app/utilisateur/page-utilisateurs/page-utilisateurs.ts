import { Component } from '@angular/core';
import { Pagination } from '../../pagination/pagination';
import { BouttonAction } from '../../boutton-action/boutton-action';
import { Router } from '@angular/router';
import { DetailUtilisateur } from '../detail-utilisateur/detail-utilisateur';

@Component({
  selector: 'app-page-utilisateurs',
  imports: [Pagination,BouttonAction,DetailUtilisateur],
  templateUrl: './page-utilisateurs.html',
  styleUrl: './page-utilisateurs.scss',
})
export class PageUtilisateurs {
  constructor(
    public router: Router
  ){}

  nouveauUtilisateur(){
    this.router.navigate(['nouvel-utilisateur'])
  }
}
