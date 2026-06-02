import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Dimensions2d} from '../../../models/dimensions';

@Component({
  selector: 'app-img-select',
  imports: [],
  templateUrl: './img-select.html',
  styleUrl: './img-select.css',
})
export class ImgSelect {

  @Input() imgPaths: string[] = [];


  constructor() {

  }

  @Output() close = new EventEmitter<void>();

  @Output() selection = new EventEmitter<string>()

  // Sets the style width and the actual width.
  onImageLoad(img: HTMLImageElement, canvas: HTMLCanvasElement) {
    canvas.style.width = `${img.clientWidth}px`;
    canvas.style.height = `${img.clientHeight}px`;

    canvas.width = img.naturalWidth;
    canvas.height = img.naturalHeight;
  }






}
