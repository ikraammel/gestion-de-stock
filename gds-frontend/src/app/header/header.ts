import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { UtilisateurDto } from '../../gs-api';
import { User } from '../services/user/user';

@Component({
  selector: 'app-header',
  imports: [RouterModule],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header implements OnInit{

  connectedUser: UtilisateurDto | null = null;
  constructor(
    private userService: User,
    private router:Router
  ) {}

  ngOnInit(): void {
      this.connectedUser = this.userService.getConnectedUser();
  }

  logout(): void {
  // 1. Supprimer le token et les infos utilisateur du localStorage
  localStorage.removeItem('accessToken');
  localStorage.removeItem('connectedUser');

  // 2. Rediriger vers la page de login
  this.router.navigate(['login']);
}
}
