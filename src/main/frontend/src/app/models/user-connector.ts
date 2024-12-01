import {Repository} from './repository';

export interface UserConnector {
  username: string;
  token: string;
  repositories: Repository[];
}
