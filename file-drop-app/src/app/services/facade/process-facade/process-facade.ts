import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpEvent} from '@angular/common/http';
import { Observable } from 'rxjs';
import { FileProcessApi } from '../../api/file-process/file-process-api';
import {ImageProcessDto} from '../../../models/image-process-dto';

@Injectable({
  providedIn: 'root',
})
export class ProcessFacade {

  constructor(private fileProcess: FileProcessApi) {}

    processFile(jobId: string): Observable<HttpEvent<ImageProcessDto>> {
        return this.fileProcess.processFile(jobId);
    }

}
