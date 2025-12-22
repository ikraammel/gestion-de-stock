import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ArticleDto } from '../../../gs-api';
import { Router } from '@angular/router';
import { Article } from '../../services/article/article';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-detail-article',
  imports: [CommonModule],
  templateUrl: './detail-article.html',
  styleUrl: './detail-article.scss',
})
export class DetailArticle implements OnInit{
    listArticles:Array<ArticleDto> = [];
    articleToDelete?: ArticleDto;
    showDeleteModal = false;
    errorMsgs :Array<string> = [];

  @Input()
  article:ArticleDto= {}

  ngOnInit(): void {
  }

  @Output()
  suppressionResult  = new EventEmitter()

  constructor(
    private router:Router,
    private articleService: Article
  ){}

  modifierArticle(){
    this.router.navigate(['nouvel-article',this.article.id])
  }
  ouvrirModalDelete(article: ArticleDto) {
      this.articleToDelete = article;
      this.showDeleteModal = true;
    }
  
    fermerModal() {
      this.showDeleteModal = false;
      this.articleToDelete = undefined;
    }
  
    confirmerDelete() {
    if (this.articleToDelete?.id !== undefined) {
      this.articleService.deleteArticle(this.articleToDelete.id).subscribe(
        () => {
          this.fermerModal()
          this.suppressionResult.emit('success')
          this.fermerModal();
        },
        error => {
          if (error.error instanceof Blob) {
            const reader = new FileReader();
            reader.onload = () => {
              try {
                const parsed = JSON.parse(reader.result as string);
                this.errorMsgs =
                  parsed.errors ||
                  parsed.erros ||
                  (parsed.message ? [parsed.message] : ['Erreur inconnue']);
              } catch {
                this.errorMsgs = ['Erreur inconnue'];
              }
            };
            reader.readAsText(error.error);
          } else {
            this.errorMsgs =
              error.error?.errors ||
              error.error?.erros ||
              (error.error?.message ? [error.error.message] : ['Erreur inconnue']);
              this.suppressionResult.emit(this.errorMsgs)
          }
        }
      );
    }
  }
  
}
