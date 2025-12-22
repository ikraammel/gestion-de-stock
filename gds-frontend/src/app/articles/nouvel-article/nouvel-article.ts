import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Article } from '../../services/article/article';
import { ArticleDto, CategoryDto, PhotoControllerService, UploadPhotoRequest } from '../../../gs-api';
import { Category } from '../../services/category/category';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-nouvel-article',
  imports: [CommonModule,FormsModule],
  templateUrl: './nouvel-article.html',
  styleUrl: './nouvel-article.scss',
})
export class NouvelArticle implements OnInit{

  articleDto:ArticleDto={};
  categoryDto: CategoryDto | null = null;
  listCategories:Array<CategoryDto>=[];
  errorMsg:Array<string>=[];
  imgUrl:string|ArrayBuffer|null = "product.png";
  file: File | null = null;

  constructor(
    private router:Router,
    private articleService:Article,
    private categoryService:Category,
    private activatedRoute:ActivatedRoute,
    private photoService:PhotoControllerService,
    private httpClient: HttpClient
  ){}

  ngOnInit(): void {
      this.categoryService.findAll()
      .subscribe(categories => {
        this.listCategories = categories
      })
      const articleId =this.activatedRoute.snapshot.params['articleId'];
      if(articleId){
        this.articleService.findArticleById(articleId)
        .subscribe(article => {
          if (article instanceof Blob) {
        const reader = new FileReader();
        reader.onload = () => {
            try {
                const parsed = JSON.parse(reader.result as string);
                console.log('Article récupéré :', parsed);
                this.articleDto = parsed;
            } catch (err) {
                console.error('Erreur parsing JSON :', err);
            }
        };
        reader.readAsText(article);
    }
          this.articleDto = article;
          this.categoryDto = this.articleDto.category ? this.articleDto.category : null;
        })
      }
  }
  cancelClick(){
    this.router.navigate(['articles'])
  }

  enregistrerArticle() {
  // Attribution de la catégorie
  this.articleDto.category = this.categoryDto ? this.categoryDto : undefined;

  this.articleService.enregistrerArticle(this.articleDto)
    .subscribe({
      next: (res) => {
        // Cas 1: Le backend renvoie un Blob (votre cas probable)
        if (res instanceof Blob) {
          const reader = new FileReader();
          reader.onload = () => {
            try {
              const articleSaved = JSON.parse(reader.result as string);
              this.procederEnregistrementPhoto(articleSaved.id);
            } catch (e) {
              console.error("Erreur lors de l'extraction de l'ID", e);
            }
          };
          reader.readAsText(res);
        } 
        // Cas 2: Le backend renvoie directement du JSON
        else if (res && res.id) {
          this.procederEnregistrementPhoto(res.id);
        }
      },
      error: (error) => {
        this.handleError(error); // Votre méthode existante pour gérer les erreurs Blob
      }
    });
}
private handleError(error: any) {
  if (error.error instanceof Blob) {
    const reader = new FileReader();
    reader.onload = () => {
      try {
        const parsed = JSON.parse(reader.result as string);
        console.error('Erreurs backend décodées :', parsed);
        // On récupère soit la liste d'erreurs, soit le message simple
        this.errorMsg = parsed.errors || (parsed.message ? [parsed.message] : ['Erreur inconnue']);
      } catch (e) {
        this.errorMsg = ['Erreur lors du décodage des erreurs serveur'];
      }
    };
    reader.readAsText(error.error);
  } else {
    // Si l'erreur n'est pas un Blob
    this.errorMsg = error.error?.errors || [error.error?.message] || ['Erreur inconnue'];
  }
}

// Nouvelle méthode pour centraliser la logique après enregistrement
private procederEnregistrementPhoto(id: number) {
  if (this.file && id) {
    this.savePhoto(id);
  } else {
    this.router.navigate(['articles']);
  }
}

  calculerTTC(){
  const ht = Number(this.articleDto.prixUnitaireHt) || 0;
  const tva = Number(this.articleDto.tauxTva) || 0;
  this.articleDto.prixUnitaireTtc = ht + (ht * tva / 100);
}

onFileInput(files: FileList | null){
  if(files){
    this.file = files.item(0);
    if(this.file){
      const fileReader = new FileReader()
      fileReader.readAsDataURL(this.file);
      fileReader.onload = (event)=> {
        if(fileReader.result){
          this.imgUrl = fileReader.result;
        }
      }
    }
  }
}
  savePhoto(articleId: number) {
  if (this.file && articleId) {
    const formData = new FormData();
    formData.append('photo', this.file); // Correspond au @RequestParam("photo") en Java

    // On construit l'URL manuellement
    const url = `http://localhost:8081/gestiondestock/v1/photos/article/${articleId}`;

    // On utilise directement httpClient pour éviter que le service OpenAPI ne force le JSON
    this.httpClient.post(url, formData).subscribe({
      next: () => {
        console.log('Succès : Photo enregistrée');
        this.router.navigate(['articles']);
      },
      error: (err) => {
        console.error("Erreur lors de l'upload", err);
        this.router.navigate(['articles']);
      }
    });
  }
}
}
