import {JobStatus} from './job-status';

export interface JobConfig {
    jobId: string;
    date: string;
    fileNameWithExtension: string;
    fileSize: number;
    status: JobStatus
    resultUrl?: string;
    imgPaths?: string[];
}
