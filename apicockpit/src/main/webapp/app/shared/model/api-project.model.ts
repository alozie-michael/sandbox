import { Moment } from 'moment';
import { IApiProjectService } from 'app/shared/model//api-project-service.model';

export interface IApiProject {
    id?: number;
    name?: string;
    description?: string;
    active?: boolean;
    dateCreated?: Moment;
    apiKeyClientId?: string;
    apiKeyId?: number;
    apis?: any;
    ownerName?: string;
    ownerId?: number;
}

export class ApiProject implements IApiProject {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public active?: boolean,
        public dateCreated?: Moment,
        public apiKeyClientId?: string,
        public apiKeyId?: number,
        public apis?: any,
        public ownerName?: string,
        public ownerId?: number
    ) {
        this.active = this.active || true;
    }
}
