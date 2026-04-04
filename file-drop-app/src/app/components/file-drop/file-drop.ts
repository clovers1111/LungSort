import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FileFacade } from '../../services/facade/upload-facade/upload-facade';
import { HttpEvent, HttpEventType } from '@angular/common/http';
import { JobConfig } from '../../models/upload-response';

@Component({
  selector: 'app-file-drop',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './file-drop.html',
  styleUrls: ['./file-drop.css']
})
export class FileDrop {
  files: File[] = [];
  isHovering = false;

  uploadAttempt = false;
  uploadError = false;
  uploadComplete = false;
  uploadPercentage = 0;
  responseMessage = '';

  constructor(private fileFacade: FileFacade) {}

  resetUploadProperties() {
    this.uploadAttempt = false;
    this.uploadPercentage = 0;
    this.uploadComplete = false;
    this.uploadError = false;
    this.responseMessage = '';
  }

  onDragOver(event: DragEvent) {
    this.resetUploadProperties();
    event.preventDefault();
    this.isHovering = true;
  }

  onDragLeave(event: DragEvent) {
    event.preventDefault();
    this.isHovering = false;
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    this.isHovering = false;
    this.uploadAttempt = false;

    if (event.dataTransfer?.files) {
      this.files = Array.from(event.dataTransfer.files);
    }
  }

  onUpload() {
    this.uploadAttempt = true;
    // At the moment, this is hardcoded to only send the first file; this is the prototype implementation
    this.fileFacade.uploadFile(this.files[0]).subscribe((event: HttpEvent<JobConfig>) => {
    switch (event.type) {
    case HttpEventType.UploadProgress:
      // Calculate and log upload progress
      this.uploadPercentage = Math.round(100 * event.loaded / event.total!);
      console.log(`File is ${this.uploadPercentage}% uploaded.`);
      // Update a progress bar in the UI
      break;
    case HttpEventType.Response:
      // Upload complete, final response received
      if (event.status === 200) {
        this.responseMessage = `Server responded with 200 - file uploaded.`;
        console.log('Upload complete');
        this.uploadComplete = true;
        if (event.body) {
          this.fileFacade.addJobConfig(event.body);
        }
        this.fileFacade.navigateTo('/select-file');
      } else {
        this.responseMessage = `Server responded with ${event.status} - upload failed.`;
        this.uploadError = true;
      }
      break;

    // Other events (Sent, ResponseHeader, DownloadProgress, User) can also be handled
      }
    });

  }
}