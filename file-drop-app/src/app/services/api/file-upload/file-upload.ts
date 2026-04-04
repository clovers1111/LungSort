import { HttpClient, HttpEvent } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/enivornment';
import { JobConfig } from '../../../models/upload-response';

@Injectable({
  providedIn: 'root',
})
export class FileUpload {

  private uploadUrl = environment.backendApiUrl + '/upload';

  constructor(private http: HttpClient) {}

  uploadFile(file: File): Observable<HttpEvent<JobConfig>> {
      const formData = new FormData();
      formData.append('file', file);
      console.log(`Uploading ${file.name}`)

      return this.http.post<JobConfig>(this.uploadUrl, formData, {
        reportProgress: true,
        observe: 'events'
      });
    }
  }
