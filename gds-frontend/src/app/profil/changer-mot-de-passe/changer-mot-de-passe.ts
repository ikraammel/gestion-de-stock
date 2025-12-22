import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ChangerMotDePasseUtilisateurDto } from '../../../gs-api';
import { User } from '../../services/user/user';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-changer-mot-de-passe',
  imports: [FormsModule],
  templateUrl: './changer-mot-de-passe.html',
  styleUrl: './changer-mot-de-passe.scss',
})
export class ChangerMotDePasse implements OnInit{
  changerMotDePasseDto: ChangerMotDePasseUtilisateurDto = {};
  ancienMotDePasse = "";
  constructor(
    private router: Router,
    private userService: User
  ){}

  ngOnInit(): void {
      if(localStorage.getItem("origin") && localStorage.getItem("origin") == "inscription"){
        this.ancienMotDePasse = "som3R@nd0mP@$$word";
        localStorage.removeItem("origin");
      }
  }

  changerMotDePasseUtilisateur() {
  this.userService.changerMotDePasse(this.changerMotDePasseDto)
    .subscribe(() => this.router.navigate(['/profil']));
}


  cancel(){
    this.router.navigate(['/profil']);
  }
}
