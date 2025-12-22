import { Component, OnInit } from '@angular/core';
import { DetailArticle } from '../detail-article/detail-article';
import { Pagination } from '../../pagination/pagination';
import { BouttonAction } from '../../boutton-action/boutton-action';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ArticleDto } from '../../../gs-api';
import { Article } from '../../services/article/article';

@Component({
  selector: 'app-page-article',
  imports: [DetailArticle,Pagination,BouttonAction,CommonModule],
  templateUrl: './page-article.html',
  styleUrl: './page-article.scss',
})
export class PageArticle implements OnInit{

  listArticles: ArticleDto[] = []
  errorMsgs = '';

  constructor(
    private router:Router,
    private articleService:Article
  ){}

  ngOnInit(): void {
    this.findListArticles()
}

  findListArticles(){
    this.articleService.findAllArticles().subscribe(res => {

    if (res instanceof Blob) {
      const reader = new FileReader();
      reader.onload = () => {
        try {
          this.listArticles = JSON.parse(reader.result as string);
           console.log('Articles reçus du backend :', this.listArticles);
        } catch (e) {
          console.error('Erreur parsing articles', e);
          this.listArticles = [];
        }
      };
      reader.readAsText(res);
    } else {
      this.listArticles = res;
    }

  });
  }

  nouvelArticle(){
    this.router.navigate(['nouvel-article'])
  }

  handleSuppression(event:any){
    if(event === 'success'){
      this.findListArticles()
    }
    else{
      this.errorMsgs = event
    }
  }
}
