import {Job} from './job';
import {UserConnector} from './user-connector';

export interface Workflow {
  ghUser?: UserConnector;
  ghRepository?: string;
  modules?: Job[];

  name?: string;
  version?: string;
}
