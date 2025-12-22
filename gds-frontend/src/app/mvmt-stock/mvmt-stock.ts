import { Component } from '@angular/core';
import { BouttonAction } from '../boutton-action/boutton-action';
import { Pagination } from '../pagination/pagination';
import { DetailMvmtStockArticle } from '../detail-mvmt-stock-article/detail-mvmt-stock-article';
import { DetailMvmtStock } from '../detail-mvmt-stock/detail-mvmt-stock';

@Component({
  selector: 'app-mvmt-stock',
  imports: [DetailMvmtStockArticle,BouttonAction,Pagination,DetailMvmtStock],
  templateUrl: './mvmt-stock.html',
  styleUrl: './mvmt-stock.scss',
})
export class MvmtStock {

}
