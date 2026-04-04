import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpEvent} from '@angular/common/http';
import { Observable } from 'rxjs';
import { FileProcess } from '../../api/file-process/file-process';

@Injectable({
  providedIn: 'root',
})
export class ProcessFacade {

  constructor(private fileProcess: FileProcess) {}

    processFile(jobId: string): Observable<HttpEvent<string>> {
        return this.fileProcess.processFile(jobId);
    }

}