import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Menu } from '../menu/menu';
import { Header } from '../header/header';
import { Loader } from '../loader/loader';

@Component({
  selector: 'app-page-dashboard',
  imports: [RouterOutlet,Menu,Header,Loader],
  templateUrl: './page-dashboard.html',
  styleUrl: './page-dashboard.scss',
})
export class PageDashboard {

}
