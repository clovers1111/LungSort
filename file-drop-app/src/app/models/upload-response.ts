export interface JobConfig {
    jobId: string;
    date: string;
    fileNameWithExtension: string;
    fileSize: number;
    status: 'pending' | 'processing' | 'completed' | 'failed';
    resultUrl?: string;
}