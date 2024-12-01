import {RepositoryInfo} from './repository-info';

export interface UserConnector {
  username: string;
  token: string;
  repositories: RepositoryInfo[];
}
