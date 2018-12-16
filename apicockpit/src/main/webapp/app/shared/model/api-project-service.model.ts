import { Moment } from 'moment';

export interface IApiProjectService {
    id?: number;
    active?: boolean;
    name?: string;
    dateCreated?: Moment;
    serviceConfigName?: string;
    serviceConfigId?: number;
}

export class ApiProjectService implements IApiProjectService {
    constructor(
        public id?: number,
        public active?: boolean,
        public name?: string,
        public dateCreated?: Moment,
        public serviceConfigName?: string,
        public groupName?: string,
        public serviceConfigId?: number
    ) {
        this.active = this.active || false;
    }
}
