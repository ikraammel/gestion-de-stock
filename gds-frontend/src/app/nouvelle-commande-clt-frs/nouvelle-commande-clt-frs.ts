import { Component, OnInit } from '@angular/core';
import { DetailArticle } from '../articles/detail-article/detail-article';
import { DetailCmd } from '../detail-cmd/detail-cmd';
import { ActivatedRoute, Router } from '@angular/router';
import { ArticleDto, ClientDto, CommandeClientControllerService, CommandeClientDto, CommandeFournisseurDto, LigneCommandeClientDto } from '../../gs-api';
import { Cltfrs } from '../services/cltfrs/cltfrs';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Article } from '../services/article/article';
import { Cmdcltfrs } from '../services/commandecltfrs/cmdcltfrs';
import e from 'express';

@Component({
  selector: 'app-nouvelle-commande-clt-frs',
  imports: [DetailCmd,CommonModule,FormsModule],
  templateUrl: './nouvelle-commande-clt-frs.html',
  styleUrl: './nouvelle-commande-clt-frs.scss',
})
export class NouvelleCommandeCltFrs implements OnInit{
  origin = ''
  clientFournisseur:ClientDto = {}
  listCltFrs: ClientDto[] = []
  searchedArticle:ArticleDto={}
  errorMsgs :Array<string> = [];
  codeArticle:string='';
  quantite:number=0;
  lignesCommande: Array<any>=[]
  listArticles: ArticleDto[]=[];
  listArticlesFiltered: ArticleDto[] = [];
  codeCommande='';

  constructor(
    private router:Router,
    private activatedRoute:ActivatedRoute,
    private clientService:Cltfrs,
    private articleService:Article,
    private commandeClientService:CommandeClientControllerService,
    private cmdCltFrsService:Cmdcltfrs
  ){}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.origin = data['origin'];
      // On lance les appels seulement quand l'origin est connue
      this.findAll();
      this.findAllArticles();
    });
  }

  cancel(){
    if(this.origin=='client'){
      this.router.navigate(['/commandeclient'])
    }else if(this.origin=='fournisseur'){
      this.router.navigate(['/commandefournisseur'])
    }
  }


  findAll() {
    const call = this.origin === 'client' 
      ? this.clientService.findAllClients() 
      : this.clientService.findAllFournisseurs();

    call.subscribe(res => this.handleGenericResponse(res, 'listCltFrs'));
  }

  findAllArticles() {
    this.articleService.findAllArticles()
      .subscribe(res => this.handleGenericResponse(res, 'listArticles'));
  }

  searchArticle() {
  const search = this.codeArticle.toLowerCase();
  if (search.length === 0) {
    this.listArticlesFiltered = []; // Cache l'autocomplete si vide
    return;
  }
  // On filtre à partir de la liste complète sans jamais la modifier
  this.listArticlesFiltered = this.listArticles.filter(article =>
    article.codeArticle?.toLowerCase().includes(search) ||
    article.designation?.toLowerCase().includes(search)
  );
}

  selectedArticle(article:ArticleDto){
    this.searchedArticle = article
    this.codeArticle = article.codeArticle!
    this.listArticlesFiltered = [];
  }

  // MÉTHODE UNIQUE pour transformer n'importe quel Blob de liste en données utilisables
  private handleGenericResponse(res: any, targetVariable: 'listCltFrs' | 'listArticles') {
    if (res instanceof Blob) {
      const reader = new FileReader();
      reader.onload = () => {
        try {
          this[targetVariable] = JSON.parse(reader.result as string);
        } catch (e) {
          console.error(`Erreur lors du parsing de ${targetVariable}`, e);
        }
      };
      reader.readAsText(res);
    } else {
      this[targetVariable] = res;
    }
  }

  findArticleByCode(codeArticle: string) {
  if (codeArticle) {
    this.articleService.findArticleByCode(codeArticle).subscribe({
      next: (res) => {
        if (res instanceof Blob) {
          const reader = new FileReader();
          reader.onload = () => {
            this.searchedArticle = JSON.parse(reader.result as string);
          };
          reader.readAsText(res);
        } else {
          this.searchedArticle = res;
        }
      },
      error: (error) => {
        this.errorMsgs = error.error?.errors || ['Article non trouvé'];
      }
    });
  }
}

// Optionnel : Calculer le total en temps réel
get totalCommande(): number {
  return this.lignesCommande.reduce((acc, ligne) => 
    acc + ((ligne.prixUnitaire ?? 0) * (ligne.quantite ?? 0)), 0);
}


  ajoutLigneCommande() {
  // 1. Vérifier si l'article est déjà présent dans la commande
  const index = this.lignesCommande.findIndex(
    lig => lig.article?.codeArticle === this.searchedArticle.codeArticle
  );

  if (index !== -1) {
    // 2. Si l'article existe, on met à jour la quantité de la ligne existante
    this.lignesCommande[index].quantite! += this.quantite;
  } else {
    // 3. Sinon, on crée une nouvelle ligne
    const ligneCmd: LigneCommandeClientDto = {
      article: this.searchedArticle,
      quantite: this.quantite,
      prixUnitaire: this.searchedArticle.prixUnitaireTtc
    };
    this.lignesCommande.push(ligneCmd);
  }

  // 4. Réinitialisation des champs de saisie
  this.searchedArticle = {};
  this.quantite = 0;
  this.codeArticle = '';
  this.listArticlesFiltered = [];
}

  enregistrerCommande() {
  // 1. Validations préliminaires
  if (!this.clientFournisseur || !this.clientFournisseur.id) {
    this.errorMsgs = ["Veuillez sélectionner un client/fournisseur."];
    return;
  }

  if (!this.lignesCommande || this.lignesCommande.length === 0) {
    this.errorMsgs = ["Veuillez ajouter au moins un article à la commande."];
    return;
  }

  // Générer un code automatique si le champ est resté vide
  if (!this.codeCommande || this.codeCommande.trim() === '') {
    this.codeCommande = 'CMD-' + Date.now();
  }

  const commande = this.preparerCommande();

  // 2. Envoi au service
  if (this.origin === "client") {
    this.cmdCltFrsService.enregistrerCommandeClient(commande as CommandeClientDto)
      .subscribe({
        next: (cmd) => this.router.navigate(['commandeclient']),
        error: (err) => this.handleBlobError(err) // Correction syntaxique ici
      });
  } else if (this.origin === "fournisseur") {
    this.cmdCltFrsService.enregistrerCommandeFournisseur(commande as CommandeFournisseurDto)
      .subscribe({
        next: (cmd) => this.router.navigate(['commandefournisseur']),
        error: (err) => this.handleBlobError(err) // Correction syntaxique ici
      });
  }
}

  private handleBlobError(error: any) {
    if (error.error instanceof Blob) {
      const reader = new FileReader();
      reader.onload = () => {
        const body = JSON.parse(reader.result as string);
        this.errorMsgs = body.errors || [body.message];
      };
      reader.readAsText(error.error);
    } else {
      this.errorMsgs = error.error?.errors || [error.error?.message] || ['Erreur inconnue'];
    }
  }

  private preparerCommande(): any {
  const finalCode = this.codeCommande && this.codeCommande.trim() !== '' 
                    ? this.codeCommande 
                    : 'CMD-' + Date.now();

  if (this.origin === "client") {
    return {
      client: this.clientFournisseur,
      code: finalCode,
      dateCommande: new Date().toISOString(),
      etatCommande: "EN_PREPARATION",
      entrepriseId: this.clientFournisseur.entrepriseId,
      ligneCommandeClient: this.lignesCommande 
    };
  } else if (this.origin === "fournisseur") {
    return {
      fournisseur: this.clientFournisseur,
      code: finalCode,
      dateCommande: new Date().toISOString(),
      etatCommande: "EN_PREPARATION",
      entrepriseId: this.clientFournisseur.entrepriseId,
      ligneCommandeFournisseurs: this.lignesCommande
    };
  }
}
}
