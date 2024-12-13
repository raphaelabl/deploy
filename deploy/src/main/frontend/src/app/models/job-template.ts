export interface JobTemplate {
  id?: number;
  name?:string;
  runsOn?:string;

  identificationFiles?: string[];

  workflowFileContent?:string;
  workflowVariables?: string[];

  dockerFileVariables?: string[];
  dockerFileContent?:string;

  dockerComposeVariables?: string[];
  dockerComposeContent?: string;
}
