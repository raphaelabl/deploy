import {JobTemplate} from './job-template';
import {JobAttribute} from './job-attribute';

export interface Job {
  id?: number;
  name?: string;
  refTemplate?: JobTemplate;
  workflowAttributes?: JobAttribute[];
  dockerfileAttributes?: JobAttribute[];
}
