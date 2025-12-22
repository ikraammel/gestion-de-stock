import { Component, OnInit } from '@angular/core';
import { MenuModel } from './MenuModel';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-menu',
  imports: [CommonModule],
  templateUrl: './menu.html',
  styleUrl: './menu.scss',
})
export class Menu implements OnInit{

  public MenuProperties:Array<MenuModel> = [
  {
    id:'1',
    titre:'Tableau de bord ',
    icon:'fa-solid fa-chart-area',
    url:'',
    sousMenu: [
      {
        id: '11',
        titre: 'Vue d\'ensemble',
        icon:'fa-solid fa-chart-pie',
        url:''
      },
      {
        id: '12',
        titre: 'Statistiques',
        icon:'fa-solid fa-chart-bar',
        url:'statistiques'
      },      
    ]
  },
   {
    id:'2',
    titre:'Articles',
    icon:'fa-solid fa-newspaper',
    url:'articles',
    sousMenu: [
      {
        id: '21',
        titre: 'Articles',
        icon:'fa-solid fa-newspaper',
        url:'articles'
      },
      {
        id: '22',
        titre: 'Mouvements du stock',
        icon:'fa-brands fa-stack-overflow',
        url:'mouvement-stock'
      },      
    ]
  }  ,
  {
    id:'3',
    titre:'Clients',
    icon:'fa-solid fa-users',
    url:'clients',
    sousMenu: [
      {
        id: '31',
        titre: 'Clients',
        icon:'fa-solid fa-users',
        url:'clients'
      },
      {
        id: '32',
        titre: 'Commandes clients',
        icon:'fa-solid fa-cart-shopping',
        url:'commandeclient'
      },      
    ]
  },
  {
    id:'4',
    titre:'Fournisseurs',
    icon:'fa-solid fa-users',
    url:'fournisseurs',
    sousMenu: [
      {
        id: '41',
        titre: 'Fournisseurs',
        icon:'fa-solid fa-users',
        url:'fournisseurs'
      },
      {
        id: '42',
        titre: 'Commandes Fournisseurs',
        icon:'fa-solid fa-truck',
        url:'commandefournisseur'
      },      
    ]
  },{
    id:'5',
    titre:'Paramétrage',
    icon:'fa-solid fa-gears',
    url:'',
    sousMenu: [
      {
        id: '51',
        titre: 'Catégories',
        icon:'fas fa-tools',
        url:'categories'
      },
      {
        id: '52',
        titre: 'Utilisateurs',
        icon:'fa-solid fa-user-gear',
        url:'utilisateurs'
      },      
    ]
  },
  ]

  constructor(public router:Router){}

  ngOnInit(): void {
      
  }

  navigate(url:string){
    this.router.navigate([url]);
  }
}
