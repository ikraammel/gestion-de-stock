import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AdresseDto, AuthenticationRequest, EntrepriseControllerService, EntrepriseDto } from '../../gs-api';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { User } from '../services/user/user';

@Component({
  selector: 'app-page-inscription',
  imports: [RouterModule, FormsModule, CommonModule],
  templateUrl: './page-inscription.html',
  styleUrl: './page-inscription.scss',
})
export class PageInscription implements OnInit{
  entrepriseDto: EntrepriseDto = {};
  adresse: AdresseDto = {};
  errors: Array<string> = [];

  constructor(
    private entrepriseService: EntrepriseControllerService,
    private userService: User,
    private router: Router
  ) {}

  ngOnInit() {
    localStorage.removeItem('accessToken');
  }

  register() {
    this.errors = [];
    this.entrepriseDto.adresse = this.adresse;

    // 1️⃣ Créer l'entreprise
    this.entrepriseService.save3(this.entrepriseDto).subscribe({
      next: (entrepriseDto: EntrepriseDto) => {
        const authRequest: AuthenticationRequest = {
          email: this.entrepriseDto.email!,
          password: 'som3R@nd0mP@$$word'
        };

        // 2️⃣ Se connecter automatiquement
        this.userService.login(authRequest).subscribe({
          next: (response) => {
            this.userService.setAccessToken(response);

            // 3️⃣ Récupérer l'utilisateur connecté
            this.userService.getUserByEmail(this.entrepriseDto.email!).subscribe({
              next: (user) => {
                this.userService.setConnectedUser(user);

                localStorage.setItem("origin",'inscription')
                // 4️⃣ Redirection vers la page de changement de mot de passe
                this.router.navigate(['changer-mot-de-passe']);
              },
             error: (err) => {
              console.error('Erreur backend complète :', err);
              
              if (err.error instanceof Blob) {
                const reader = new FileReader();
                reader.onload = () => {
                  try {
                    const parsed = JSON.parse(reader.result as string);
                    console.log('Erreur backend décodée :', parsed);

                    // Ajuste selon ce que renvoie ton backend, par ex. "errors" ou "erros"
                    this.errors = parsed.errors || parsed.erros || [parsed.message] || ['Erreur inconnue'];
                  } catch (e) {
                    console.error('Erreur parsing JSON du backend :', e);
                    this.errors = ['Erreur inconnue'];
                  }
                };
                reader.readAsText(err.error);
              } else if (err.error) {
                if (Array.isArray(err.error.errors)) {
                  this.errors = err.error.errors;
                } else if (typeof err.error.message === 'string') {
                  this.errors = [err.error.message];
                } else if (typeof err.error === 'string') {
                  this.errors = [err.error];
                } else {
                  this.errors = ['Erreur de validation'];
                }
              } else {
                this.errors = ['Une erreur est survenue'];
              }
            }

            });
          },
          
        });
      },
      
      
    });
  }
}