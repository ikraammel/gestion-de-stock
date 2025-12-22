import { Injectable } from '@angular/core';
import { User } from '../user/user';
import { ArticleDto, ArticlesService } from '../../../gs-api';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Article {
  constructor(
    private userService: User,
    private articleService: ArticlesService
  ){}

  enregistrerArticle(article:ArticleDto):Observable<ArticleDto>{
    article.entrepriseId = this.userService.getConnectedUser().entreprise.id;
    return this.articleService.save8(article)
  }

  findAllArticles():Observable<Array<ArticleDto>>{
    return this.articleService.findAll8();
  }

  findArticleById(articleId:number):Observable<ArticleDto>{
    if(articleId){
      return this.articleService.findById8(articleId);
    }
    return of();
  }

  findArticleByCode(codeArticle:string):Observable<ArticleDto>{
    if(codeArticle){
      return this.articleService.findByCodeArticle(codeArticle);
    }
    return of();
  }

  deleteArticle(articleId:number):Observable<void>{
    if(articleId){
      return this.articleService.delete8(articleId);
    }
    return of();
  }
}
