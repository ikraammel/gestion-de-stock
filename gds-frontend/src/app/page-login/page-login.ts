import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { User } from '../services/user/user';
import { AuthenticationRequest } from '../../gs-api';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-page-login',
  imports: [RouterModule,FormsModule,CommonModule],
  templateUrl: './page-login.html',
  styleUrl: './page-login.scss',
})
export class PageLogin {

  authRequest: AuthenticationRequest = {};
  errorMessage: string = '';

  constructor(private userService: User, private router: Router) {}

  login() {
    this.userService.login(this.authRequest).subscribe({
      next: (data) => {
        console.log("Login response:", data);
          this.userService.setAccessToken(data);
          this.userService.getUserByEmail(this.authRequest.email!).subscribe(user =>{
            console.log("User from API de pagelogin:", user);
            this.userService.setConnectedUser(user)
            this.router.navigate(['']); 
          });
      },
      error: (err) => {
        this.errorMessage = 'Email ou mot de passe incorrect';
      }
    });
  }

  
}