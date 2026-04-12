import { HttpClient, HttpEvent } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/enivornment';
import {ImageProcessDto} from '../../../models/image-process-dto';


@Injectable({
  providedIn: 'root',
})
export class FileProcessApi {

  private processUrl = environment.backendApiUrl + '/process';

  constructor(private http: HttpClient) {}

  processFile(jobId: string): Observable<HttpEvent<ImageProcessDto>> {
      const formData = new FormData();
      formData.append('jobId', jobId);
      console.log(`Processing job ${jobId}`)

      return this.http.post<ImageProcessDto>(this.processUrl, formData, {
        reportProgress: true,
        observe: 'events'
      });
    }
  }
