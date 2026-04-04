import { HttpClient, HttpEvent } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/enivornment';


@Injectable({
  providedIn: 'root',
})
export class FileProcess{

  private processUrl = environment.backendApiUrl + '/process';

  constructor(private http: HttpClient) {}

  processFile(jobId: string): Observable<HttpEvent<string>> {
      const formData = new FormData();
      formData.append('jobId', jobId);
      console.log(`Processing job ${jobId}`)

      return this.http.post<string>(this.processUrl, formData, {
        reportProgress: true,
        observe: 'events'
      });
    }
  }
