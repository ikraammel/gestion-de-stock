import { Injectable } from '@angular/core';
import { AuthControllerService, AuthenticationRequest, AuthenticationResponse, ChangerMotDePasseUtilisateurDto, UtilisateurControllerService, UtilisateurDto } from '../../../gs-api';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';


@Injectable({
  providedIn: 'root',
})
export class User {
    constructor(
      private utilisateurService: UtilisateurControllerService,
      private authenticationService:AuthControllerService,
    ){}

    getUserByEmail(email: string): Observable<UtilisateurDto> {
  return new Observable(observer => {
    this.utilisateurService.findByEmail(email, 'body').subscribe({
      next: (resp) => {
        // Vérifie si c'est un Blob
        if (resp instanceof Blob) {
          const reader = new FileReader();
          reader.onload = () => {
            try {
              const user = JSON.parse(reader.result as string);
              observer.next(user);
              observer.complete();
            } catch (err) {
              observer.error(err);
            }
          };
          reader.readAsText(resp);
        } else {
          observer.next(resp);
          observer.complete();
        }
      },
      error: (err) => observer.error(err)
    });
  });
}


    login(authRequest:AuthenticationRequest) :Observable<AuthenticationResponse>{
      return this.authenticationService.authenticate(authRequest);
    }

    setConnectedUser(utilisateur:UtilisateurDto){
      localStorage.setItem("connectedUser", JSON.stringify(utilisateur));
    }

    getConnectedUser(){
      if(localStorage.getItem("connectedUser")){
        return JSON.parse(localStorage.getItem("connectedUser") as string);
      }
      return {};
    }

    setAccessToken(authResponse: any) {
  if (authResponse instanceof Blob) {
    const reader = new FileReader();
    reader.onload = () => {
      try {
        const tokenObj = JSON.parse(reader.result as string);
        if (tokenObj.jwtToken) {
          localStorage.setItem("accessToken", tokenObj.jwtToken);
          console.log("Token stored:", tokenObj.jwtToken);
        } else {
          console.error("No jwtToken found in parsed Blob", tokenObj);
        }
      } catch (err) {
        console.error("Error parsing token Blob:", err);
      }
    };
    reader.readAsText(authResponse);
  } else if (authResponse.jwtToken) {
    localStorage.setItem("accessToken", authResponse.jwtToken);
    console.log("Token stored:", authResponse.jwtToken);
  } else {
    console.error("No token found in response", authResponse);
  }
}



    getAccessToken(): string | null {
      return localStorage.getItem("accessToken");
    }


    setUtilisateur(utilisateur:UtilisateurDto){
      localStorage.setItem("connectedUser", JSON.stringify(utilisateur));
    }

    changerMotDePasse(changerMotDePasseDto:ChangerMotDePasseUtilisateurDto){
      return this.utilisateurService.changerMotDePasse(changerMotDePasseDto)
    }
}
