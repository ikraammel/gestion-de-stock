import { Injectable } from '@angular/core';
import { CommandeClientControllerService, CommandeClientDto, CommandeFournisseurControllerService, CommandeFournisseurDto } from '../../../gs-api';
import { User } from '../user/user';

@Injectable({
  providedIn: 'root',
})
export class Cmdcltfrs {
  constructor(
    private commandeClientService:CommandeClientControllerService,
    private commandeFournisseurService: CommandeFournisseurControllerService,
    private userService: User
  ){}

  enregistrerCommandeClient(commandeClient:CommandeClientDto){
    commandeClient.entrepriseId = this.userService.getConnectedUser().entreprise.id;
    return this.commandeClientService.save5(commandeClient);
  }

  enregistrerCommandeFournisseur(commandeFournisseur:CommandeFournisseurDto){
    commandeFournisseur.entrepriseId = this.userService.getConnectedUser().entreprise.id;
    return this.commandeFournisseurService.save4(commandeFournisseur);
  }

  findAllCommandeClients(){
    return this.commandeClientService.findAll5();
  }

  findAllCommandeFournisseurs(){
	  return this.commandeFournisseurService.findAll4();
  }
  
}
