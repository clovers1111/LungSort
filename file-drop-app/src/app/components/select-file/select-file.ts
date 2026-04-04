import { Component } from '@angular/core';
import { ProcessFacade } from '../../services/facade/process-facade/process-facade';
import { JobConfig } from '../../models/upload-response';
import { JobCard } from './job-card/job-card';
import { FileFacade } from '../../services/facade/upload-facade/upload-facade';

@Component({
  selector: 'app-select-file',
  imports: [JobCard],
  templateUrl: './select-file.html',
  styleUrl: './select-file.css',
})
export class SelectFile {

  protected jobs: JobConfig[]

  constructor(private processFacade: ProcessFacade, private fileFacade: FileFacade) {
    this.jobs = fileFacade.getJobConfigs();
  }

  onJobClicked(jobId: string) {
    this.processFacade.processFile(jobId).subscribe((event) => {
      // Handle the response from the backend here, such as updating the UI or showing a notification
      console.log('File processing response:', event);
    });
  }

  clearJobs() {
    this.fileFacade.clearJobConfigs();
    this.jobs = [];
  }

}
