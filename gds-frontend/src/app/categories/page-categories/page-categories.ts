import { Component, OnInit } from '@angular/core';
import { BouttonAction } from '../../boutton-action/boutton-action';
import { Pagination } from '../../pagination/pagination';
import { Router } from '@angular/router';
import { CategoryDto } from '../../../gs-api';
import { Category } from '../../services/category/category';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-page-categories',
  imports: [BouttonAction,Pagination,CommonModule],
  templateUrl: './page-categories.html',
  styleUrl: './page-categories.scss',
})
export class PageCategories implements OnInit{

  listCategories:Array<CategoryDto> = [];
  categoryToDelete?: CategoryDto;
  showDeleteModal = false;
  errorMsgs :Array<string> = [];

  constructor(
    private router:Router,
    private categoryService:Category
  ){}

  ngOnInit(): void {
      this.categoryService.findAll().subscribe(res =>{
        this.listCategories = res;
      });
  }

  nouvelleCategorie(){
    this.router.navigate(['nouvelle-categorie']);
  }

  modifierCategory(categoryId:number){
    this.router.navigate(['nouvelle-categorie', categoryId]);

  }
  ouvrirModalDelete(cat: CategoryDto) {
    this.categoryToDelete = cat;
    this.showDeleteModal = true;
  }

  fermerModal() {
    this.showDeleteModal = false;
    this.categoryToDelete = undefined;
  }

  confirmerDelete() {
  if (this.categoryToDelete?.id !== undefined) {
    this.categoryService.categoryToDelete(this.categoryToDelete.id).subscribe(
      () => {
        this.listCategories = this.listCategories.filter(
          c => c.id !== this.categoryToDelete!.id
        );
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
        }
      }
    );
  }
}

}
