import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-img-select',
  imports: [],
  templateUrl: './img-select.html',
  styleUrl: './img-select.css',
})
export class ImgSelect {

  boolean

  @Input() imgPaths: string[] = [];

  @Output() close = new EventEmitter<void>();

  @Output() selection = new EventEmitter<string>();

}
