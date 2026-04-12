import {Component} from '@angular/core';
import {ProcessFacade} from '../../services/facade/process-facade/process-facade';
import {JobConfig} from '../../models/upload-response';
import {JobCard} from './job-card/job-card';
import {FileFacade} from '../../services/facade/upload-facade/upload-facade';
import {JobStatus} from '../../models/job-status';
import {HttpEventType} from '@angular/common/http';
import {ImageProcessDto} from '../../models/image-process-dto';
import {ImgSelect} from './img-select/img-select';

@Component({
  selector: 'app-select-file',
  imports: [JobCard, ImgSelect],
  templateUrl: './select-file.html',
  styleUrl: './select-file.css',
})
export class SelectFile {

  protected jobs: JobConfig[]

  protected isImgSelect: boolean = false;

  protected currentImgPaths: string[] = [];

  // Cache for processed image paths by jobId
  protected imagePathsByJobId: Record<string, string[]> = {};

  constructor(private processFacade: ProcessFacade, private fileFacade: FileFacade) {
    this.jobs = fileFacade.getJobConfigs();
  }

  onJobClicked(jobId: string) {
    this.processFacade.processFile(jobId).subscribe({
      // process files in next
      next: (event) =>  {
        if (event.type === HttpEventType.Response && event.body) {
          const imgDto: ImageProcessDto = event.body;
          this.setJobImgPaths(jobId, imgDto.imagePaths)
          this.setJobStatus(jobId, JobStatus.PROCESSED);
        }
        },
      error: () => this.setJobStatus(jobId, JobStatus.FAILED)
    });
  }

  onOpenImages(jobId: string) {
    this.currentImgPaths = this.imagePathsByJobId[jobId] || [];
    this.isImgSelect = true;
  }

  onCloseImages() {
    this.isImgSelect = false;
    this.currentImgPaths = [];
  }

  private setJobImgPaths(jobId: string, imgPaths: string[]) {
    this.imagePathsByJobId = {
      ...this.imagePathsByJobId,
      [jobId]: imgPaths
    };
    // Emit event containing the imgPaths to the correct jobId.

  }


  private setJobStatus(jobId: string, status: JobStatus) {
    this.jobs = this.jobs.map(job =>
      job.jobId === jobId ? {...job, status} : job);
  }

  clearJobs() {
    this.fileFacade.clearJobConfigs();
    this.jobs = [];
  }

}
