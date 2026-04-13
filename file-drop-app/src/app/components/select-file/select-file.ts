import {Component, signal} from '@angular/core';
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

  protected jobs = signal<JobConfig[]>([]);

  protected isImgSelect = signal(false);

  protected currentImgPaths = signal<string[]>([]);

  // Cache for processed image paths by jobId
  protected imagePathsByJobId: Record<string, string[]> = {};

  constructor(private processFacade: ProcessFacade, private fileFacade: FileFacade) {
    this.jobs.set(fileFacade.getJobConfigs());
  }

  onJobClicked(jobId: string) {
    if (this.getJobById(jobId)?.status === JobStatus.PROCESSING) {
      return; // Prevent re-processing if already in progress
    }
    this.setJobStatus(jobId, JobStatus.PROCESSING);
    this.processFacade.processFile(jobId).subscribe({
      // process files in next
      next: (event) =>  {
        if (event.type === HttpEventType.Response && event.body) {
          const imgDto: ImageProcessDto = event.body;
          this.setJobImgPaths(jobId, imgDto.imagePaths);
          this.setJobStatus(jobId, JobStatus.PROCESSED);
        }
        },
      error: () => this.setJobStatus(jobId, JobStatus.FAILED)
    });
  }

  onOpenImages(jobId: string) {
    this.currentImgPaths.set(this.imagePathsByJobId[jobId] || []);
    this.isImgSelect.set(true);
  }

  onCloseImages() {
    this.isImgSelect.set(false);
    this.currentImgPaths.set([]);
  }

  private setJobImgPaths(jobId: string, imgPaths: string[]) {
    this.imagePathsByJobId = {
      ...this.imagePathsByJobId,
      [jobId]: imgPaths
    };
  }

  private setJobStatus(jobId: string, status: JobStatus) {
    this.jobs.update(jobs => jobs.map(job =>
      job.jobId === jobId ? {...job, status} : job));
  }

  private getJobById(jobId: string): JobConfig | undefined {
    return this.jobs().find(job => job.jobId === jobId);
  }

  clearJobs() {
    this.fileFacade.clearJobConfigs();
    this.jobs.set([]);
  }

}
