export interface JobTemplate {
  id?: number;
  name?:string;
  runsOn?:string;

  workflowFileContent?:string;
  workflowVariables?: string[];

  dockerFileVariables?: string[];
  dockerFileContent?:string;
}
