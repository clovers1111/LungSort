import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-img-select',
  imports: [],
  templateUrl: './img-select.html',
  styleUrl: './img-select.css',
})
export class ImgSelect {

  @Input() imgPaths: string[] = [];

  @Output() close = new EventEmitter<void>();
}
