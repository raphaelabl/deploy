import {JobTemplate} from './job-template';
import {JobAttribute} from './job-attribute';

export interface Job {
  name?: string;
  refTemplate?: JobTemplate;
  attributeList?: JobAttribute[];
}
