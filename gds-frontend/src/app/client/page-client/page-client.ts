import { Component, OnInit } from '@angular/core';
import { Pagination } from '../../pagination/pagination';
import { BouttonAction } from '../../boutton-action/boutton-action';
import { DetailCltFrns } from '../../detail-clt-frns/detail-clt-frns';
import { Router } from '@angular/router';
import { Cltfrs } from '../../services/cltfrs/cltfrs';
import { ClientDto } from '../../../gs-api';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-page-client',
  imports: [Pagination,BouttonAction,DetailCltFrns,FormsModule,CommonModule],
  templateUrl: './page-client.html',
  styleUrl: './page-client.scss',
})
export class PageClient implements OnInit{
  listClients:ClientDto[]=[]
  errorMsg = '';
  constructor(
    public router: Router,
    private cltService:Cltfrs
  ){}

  ngOnInit(): void {
      this.findAllClients();
  }

  nouveauClient(){
    this.router.navigate(['nouveauclient'])
  }

  findAllClients() {
  this.cltService.findAllClients().subscribe(res => {

    if (res instanceof Blob) {
      const reader = new FileReader();
      reader.onload = () => {
        try {
          this.listClients = JSON.parse(reader.result as string);
          console.log('Clients reçus :', this.listClients);
        } catch (e) {
          console.error('Erreur parsing clients', e);
          this.listClients = [];
        }
      };
      reader.readAsText(res);
    } else {
      this.listClients = res;
    }

  });
}

  handleSuppression(event: any) {
    if (event === 'success') {
      this.findAllClients();
    }else{
      this.errorMsg = event;
    }
  }

}
