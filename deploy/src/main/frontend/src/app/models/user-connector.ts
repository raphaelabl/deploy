import {RepositoryInfo} from './repository-info';
import {DeploymentInfo} from './deployment-info';
import {UserData} from './user-data';

export interface UserConnector {
  id?: number;
  username: string;
  token: string;
  repositories: RepositoryInfo[];
  userData?: UserData;
}
