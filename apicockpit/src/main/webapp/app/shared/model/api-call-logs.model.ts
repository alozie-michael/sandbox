export const enum ApiCallLogsStatus {
    SUCESS = 'SUCESS',
    FAILED = 'FAILED',
    ERROR = 'ERROR',
    UNKNOWN = 'UNKNOWN'
}

export interface IApiCallLogs {
    id?: number;
    name?: string;
    code?: string;
    status?: ApiCallLogsStatus;
}

export class ApiCallLogs implements IApiCallLogs {
    constructor(public id?: number, public name?: string, public code?: string, public status?: ApiCallLogsStatus) {}
}
