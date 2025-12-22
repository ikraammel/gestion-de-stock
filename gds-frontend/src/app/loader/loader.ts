import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { LoaderService } from './service/loader-service';
import { LoaderState } from './loader.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-loader',
  imports: [CommonModule],
  templateUrl: './loader.html',
  styleUrl: './loader.scss',
})
export class Loader implements OnInit{
  show = false;
  subscription: Subscription | undefined ;

  constructor(
    private loaderService:LoaderService
  ) {}

  ngOnInit(): void {
      this.subscription = this.loaderService.loaderState
      .subscribe((state: LoaderState) => {
        this.show = state.show;
      });
  }

  ngOnDestroy(){
    this.subscription?.unsubscribe();
  }

}
