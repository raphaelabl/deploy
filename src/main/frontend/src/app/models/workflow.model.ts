import {Job} from './job';
import {UserConnector} from './user-connector';
import {RepositoryInfo} from './repository-info';

export interface Workflow {
  ghUser?: UserConnector;
  ghRepository?: RepositoryInfo;
  modules?: Job[];
  name?: string;
}
