import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Cltfrs } from '../services/cltfrs/cltfrs';
import { AdresseDto, FournisseurDto, ClientDto } from '../../gs-api';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-nouveau-clt-frs',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './nouveau-clt-frs.html',
  styleUrl: './nouveau-clt-frs.scss',
})
export class NouveauCltFrs implements OnInit {
  origin = '';
  clientFrs: ClientDto = {};
  adresseDto: AdresseDto = {};
  errorMsgs: Array<string> = [];
  imgUrl: string | ArrayBuffer | null = "product.png";
  file: File | null = null;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private cltfrsService: Cltfrs,
    private httpClient: HttpClient
  ) { }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.origin = data['origin'];
      this.findObject();
    });
  }

  findObject() {
    const id = this.activatedRoute.snapshot.params['id'];
    if (id && this.origin) {
      const serviceCall = this.origin === "client"
        ? this.cltfrsService.findClientById(id)
        : this.cltfrsService.findFournisseurById(id);

      serviceCall.subscribe(res => {
        if (res instanceof Blob) {
          const reader = new FileReader();
          reader.onload = () => {
            const data = JSON.parse(reader.result as string);
            this.assignData(data);
          };
          reader.readAsText(res);
        } else {
          this.assignData(res);
        }
      });
    }
  }

  assignData(data: any) {
    this.clientFrs = data;
    this.adresseDto = data.adresse ? data.adresse : {};
    // Si l'objet a déjà une photo, on prépare l'URL d'affichage
    if (this.clientFrs.photo) {
      this.imgUrl = 'http://localhost:8081' + this.clientFrs.photo;
    }
  }

  enregistrer() {
    const observable = this.origin === "client"
      ? this.cltfrsService.enregistrerClient(this.mapToClient())
      : this.cltfrsService.enregistrerFournisseur(this.mapToFournisseur());

    observable.subscribe({
      next: (res) => {
        if (res instanceof Blob) {
          const reader = new FileReader();
          reader.onload = () => {
            const savedObj = JSON.parse(reader.result as string);
            this.procederFinalisation(savedObj.id);
          };
          reader.readAsText(res);
        } else {
          this.procederFinalisation(res.id!);
        }
      },
      error: (err) => this.handleError(err)
    });
  }

  private procederFinalisation(id: number) {
    if (this.file && id) {
      this.savePhoto(id);
    } else {
      this.cancelClick(); // Navigation immédiate si pas de fichier
    }
  }

  savePhoto(objectId: number) {
    const formData = new FormData();
    formData.append('photo', this.file!);

    // Utilisation de l'origin (client/fournisseur) dans l'URL
    const url = `http://localhost:8081/gestiondestock/v1/photos/${this.origin}/${objectId}`;

    this.httpClient.post(url, formData).subscribe({
      next: () => this.cancelClick(),
      error: (err) => {
        console.error("Erreur upload photo", err);
        this.cancelClick();
      }
    });
  }

  handleError(error: any) {
    if (error.error instanceof Blob) {
      const reader = new FileReader();
      reader.onload = () => {
        const parsed = JSON.parse(reader.result as string);
        this.errorMsgs = parsed.errors || [parsed.message] || ['Erreur inconnue'];
      };
      reader.readAsText(error.error);
    } else {
      this.errorMsgs = error.error?.errors || [error.error?.message] || ['Erreur inconnue'];
    }
  }

  onFileInput(files: FileList | null) {
    if (files && files.length > 0) {
      this.file = files.item(0);
      if (this.file) {
        const fileReader = new FileReader();
        fileReader.readAsDataURL(this.file);
        fileReader.onload = () => {
          this.imgUrl = fileReader.result;
        };
      }
    }
  }

  cancelClick() {
    const path = this.origin === "client" ? 'clients' : 'fournisseurs';
    this.router.navigate([path]);
  }

  mapToClient(): ClientDto {
    const dto: ClientDto = { ...this.clientFrs };
    dto.adresse = this.adresseDto;
    return dto;
  }

  mapToFournisseur(): FournisseurDto {
    const dto: FournisseurDto = { ...this.clientFrs };
    dto.adresse = this.adresseDto;
    return dto;
  }
}