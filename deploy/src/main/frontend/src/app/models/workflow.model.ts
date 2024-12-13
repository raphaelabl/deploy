import {Job} from './job';
import {UserConnector} from './user-connector';
import {RepositoryInfo} from './repository-info';

export interface Workflow {
  id?: number;
  modules?: Job[];
  name?: string;
}
