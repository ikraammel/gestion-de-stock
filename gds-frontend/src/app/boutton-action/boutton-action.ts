import { CommonModule } from '@angular/common';
import { Component, Input, OnInit, Output } from '@angular/core';
import { EventEmitter } from '@angular/core';

@Component({
  selector: 'app-boutton-action',
  imports: [CommonModule],
  templateUrl: './boutton-action.html',
  styleUrl: './boutton-action.scss',
})
export class BouttonAction implements OnInit{

  @Input()
  isNouveauVisible = true;
  @Input()
  isExporterVisible = true;
  @Input()
  isImporterVisible = true;

  @Output()
  clickEvent = new EventEmitter()

  ngOnInit(): void {
      
  }

  boutonNouveauClick(): void{
    this.clickEvent.emit()
  }
}
