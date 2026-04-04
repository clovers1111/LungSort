import { Component } from '@angular/core';
import { FileDrop } from './components/file-drop/file-drop';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FileDrop, RouterModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {}