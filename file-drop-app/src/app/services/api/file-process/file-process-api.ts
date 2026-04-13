import { HttpClient, HttpEvent, HttpEventType } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
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
      }).pipe(
        map(event => {
          if (event.type === HttpEventType.Response && event.body) {
            return event.clone({
              body: {
                ...event.body,
                imagePaths: event.body.imagePaths.map(p => environment.backendApiUrl + p)
              }
            });
          }
          return event;
        })
      );
    }
  }
