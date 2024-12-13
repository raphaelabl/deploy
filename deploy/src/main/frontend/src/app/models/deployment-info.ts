import {UserConnector} from './user-connector';
import {RepositoryInfo} from './repository-info';
import {Workflow} from './workflow.model';

export interface DeploymentInfo {
  id?: number;
  deploymentName?: string;
  selectedDeploymentSteps?: string[];
  ghUser?: UserConnector;
  repository?: RepositoryInfo;
  workflow?: Workflow;
  state?: string;
  errors?: string[];
}
