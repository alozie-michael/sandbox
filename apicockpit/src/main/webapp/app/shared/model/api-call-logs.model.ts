import { Moment } from 'moment';

export const enum ApiCallLogsStatus {
    SUCESS = 'SUCESS',
    FAILED = 'FAILED',
    ERROR = 'ERROR',
    UNKNOWN = 'UNKNOWN'
}

export interface IApiCallLogs {
    id?: number;
    status?: ApiCallLogsStatus;
    projectName?: string;
    apiName?: string;
    profile?: string;
    requestDate?: Moment;
    responseDate?: Moment;
}

export class ApiCallLogs implements IApiCallLogs {
    constructor(
        public id?: number,
        public status?: ApiCallLogsStatus,
        public projectName?: string,
        public apiName?: string,
        public profile?: string,
        public requestDate?: Moment,
        public responseDate?: Moment
    ) {}
}
