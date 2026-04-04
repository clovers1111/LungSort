import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpEvent} from '@angular/common/http';
import { Observable } from 'rxjs';
import { FileUpload } from '../../api/file-upload/file-upload';
import { JobConfig } from '../../../models/upload-response';

export interface UploadState {
  uploadAttempt: boolean;
  uploadError: boolean;
  uploadComplete: boolean;
  uploadPercentage: number;
  responseMessage: string;
}

@Injectable({
  providedIn: 'root',
})
export class FileFacade {

  private jobConfigs: JobConfig[] = [];

  constructor(private fileUpload: FileUpload, private router: Router) {}

  uploadFile(file: File): Observable<HttpEvent<JobConfig>> {
    return this.fileUpload.uploadFile(file);
  }

  addJobConfig(body: JobConfig): void {
    this.jobConfigs.push(body);
  }

  getJobConfigs(): JobConfig[] {
    return this.jobConfigs;
  }

  navigateTo(path: string): void {
    this.router.navigate([path]);
  }

  clearJobConfigs(): void {
    this.jobConfigs = [];
  }
  
}
