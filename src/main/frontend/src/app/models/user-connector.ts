import {RepositoryInfo} from './repository-info';

export interface UserConnector {
  id?: number;
  username: string;
  token: string;
  repositories: RepositoryInfo[];
}
