import { Component, Input } from '@angular/core';
import { JobConfig } from '../../../models/upload-response';
import { EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-job-card',
  standalone: true,
  templateUrl: './job-card.html',
  styleUrl: './job-card.css',
})
export class JobCard {
  @Input({ required: true }) job!: JobConfig;

  @Output() jobClicked = new EventEmitter<string>();
  
}
