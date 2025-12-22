import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoryDto } from '../../../gs-api';
import { FormsModule } from '@angular/forms';
import { Category } from '../../services/category/category';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-nouvelle-categorie',
  imports: [FormsModule,CommonModule],
  templateUrl: './nouvelle-categorie.html',
  styleUrl: './nouvelle-categorie.scss',
})
export class NouvelleCategorie implements OnInit{

  categoryDto: CategoryDto = {};
  errorMsgs :Array<string> = [];
  constructor(
    private router : Router,
    private activatedRoute: ActivatedRoute,
    private categoryService: Category,
  
  ){}

  ngOnInit(): void {
    const categoryId = +this.activatedRoute.snapshot.params['categoryId'];    
    if(categoryId){
      this.categoryService.findById(categoryId).subscribe(cat => {
        if (cat instanceof Blob) {
        const reader = new FileReader();
        reader.onload = () => {
            try {
                const parsed = JSON.parse(reader.result as string);
                console.log('Catégorie récupérée :', parsed);
                this.categoryDto = parsed;
            } catch (err) {
                console.error('Erreur parsing JSON :', err);
            }
        };
        reader.readAsText(cat);
    } else {
        this.categoryDto = cat;
        console.log('Catégorie récupérée :', cat);
    }
      })
    }
  }

enregistrerCategory(){
  this.categoryService.enregistrerCategorie(this.categoryDto).subscribe(
    res => {
      console.log('Catégorie enregistrée :', res);
      this.router.navigate(['/categories']);
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


  cancel(){
    this.router.navigate(['categories'])
  }
}
