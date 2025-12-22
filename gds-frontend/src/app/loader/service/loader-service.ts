import { Injectable } from '@angular/core';
import { LoaderState } from '../loader.model';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LoaderService {
  private loaderSubject = new Subject<LoaderState>();
  loaderState = this.loaderSubject.asObservable();

  show(){
    this.loaderSubject.next({show:true});
  }

  hide(){
    this.loaderSubject.next({show:false});
  }
}
