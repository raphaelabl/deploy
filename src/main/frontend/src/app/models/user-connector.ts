import {RepositoryInfo} from './repository-info';
import {DeploymentInfo} from './deployment-info';

export interface UserConnector {
  id?: number;
  username: string;
  token: string;
  repositories: RepositoryInfo[];
}
