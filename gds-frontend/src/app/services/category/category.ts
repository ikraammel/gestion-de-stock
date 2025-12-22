import { Injectable } from '@angular/core';
import { User } from '../user/user';
import { CategoryDto, CatgoriesService } from '../../../gs-api';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Category {
  constructor(
    private userService:User,
    private categoryService:CatgoriesService
  ) {}

  enregistrerCategorie(category:CategoryDto):Observable<CategoryDto>{
    category.entrepriseId = this.userService.getConnectedUser().entreprise.id;
    return this.categoryService.save7(category);
  }

  findAll(): Observable<CategoryDto[]> {
    return new Observable<CategoryDto[]>((observer) => {
      this.categoryService.findAll7().subscribe({
        next: (resp) => {
          if (resp instanceof Blob) {
            const reader = new FileReader();
            reader.onload = () => {
              try {
                const parsed = JSON.parse(reader.result as string);
                observer.next(parsed);
                observer.complete();
              } catch (err) {
                observer.error(err);
              }
            };
            reader.readAsText(resp);
          } else {
            observer.next(resp); // si déjà un tableau
            observer.complete();
          }
        },
        error: (err) => observer.error(err),
      });
    });
  }

  findById(categoryId:number):Observable<CategoryDto>{
    return this.categoryService.findById7(categoryId);
  }

  categoryToDelete(categoryId:number):Observable<void>{
    return this.categoryService.delete7(categoryId);
  }
}