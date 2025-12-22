import { Injectable } from '@angular/core';
import { EntrepriseControllerService, EntrepriseDto } from '../../../gs-api';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class EntrepriseService {
  constructor(
    private entreprise: EntrepriseControllerService
  ){}

  register(entreprise:EntrepriseDto):Observable<EntrepriseDto>{
    return this.entreprise.save3(entreprise);
  }
}
