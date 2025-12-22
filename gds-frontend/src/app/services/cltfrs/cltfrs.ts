import { Injectable } from '@angular/core';
import { User } from '../user/user';
import { ClientControllerService, ClientDto, FournisseurControllerService, FournisseurDto } from '../../../gs-api';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Cltfrs {
  constructor(
    private userService: User,
    private clientService: ClientControllerService,
    private fournisseurService: FournisseurControllerService
  ) {}

  enregistrerClient(client:ClientDto):Observable<ClientDto>{
    client.entrepriseId = this.userService.getConnectedUser().entreprise.id;
    return this.clientService.save6(client)
  }

  enregistrerFournisseur(fournisseurDto:FournisseurDto):Observable<FournisseurDto>{
    fournisseurDto.entrepriseId = this.userService.getConnectedUser().entreprise.id;
    return this.fournisseurService.save2(fournisseurDto)
  }

  findAllClients():Observable<Array<ClientDto>>{
    return this.clientService.findAll6();
  }

  findAllFournisseurs():Observable<Array<FournisseurDto>>{
    return this.fournisseurService.findAll2();
  }

  findClientById(id:number):Observable<ClientDto>{
    if(id){
      return this.clientService.findById6(id);
    }
    return of();
  }
  findFournisseurById(id:number):Observable<FournisseurDto>{
    if(id){
      return this.fournisseurService.findById2(id);
    }
    return of();
  }

  deleteClient(id:number):Observable<any>{
    if(id){
      return this.clientService.delete6(id);
    }
    return of();
  }

  deleteFournisseur(id:number):Observable<any>{
    if(id){
      return this.fournisseurService.delete2(id);
    }
    return of();
  }

}
