import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IApiCallLogs } from 'app/shared/model/api-call-logs.model';

type EntityResponseType = HttpResponse<IApiCallLogs>;
type EntityArrayResponseType = HttpResponse<IApiCallLogs[]>;

@Injectable({ providedIn: 'root' })
export class ApiCallLogsService {
    public resourceUrl = SERVER_API_URL + 'api/api-call-logs';

    constructor(private http: HttpClient) {}

    create(apiCallLogs: IApiCallLogs): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(apiCallLogs);
        return this.http
            .post<IApiCallLogs>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(apiCallLogs: IApiCallLogs): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(apiCallLogs);
        return this.http
            .put<IApiCallLogs>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IApiCallLogs>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IApiCallLogs[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(apiCallLogs: IApiCallLogs): IApiCallLogs {
        const copy: IApiCallLogs = Object.assign({}, apiCallLogs, {
            requestDate: apiCallLogs.requestDate != null && apiCallLogs.requestDate.isValid() ? apiCallLogs.requestDate.toJSON() : null,
            responseDate: apiCallLogs.responseDate != null && apiCallLogs.responseDate.isValid() ? apiCallLogs.responseDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.requestDate = res.body.requestDate != null ? moment(res.body.requestDate) : null;
            res.body.responseDate = res.body.responseDate != null ? moment(res.body.responseDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((apiCallLogs: IApiCallLogs) => {
                apiCallLogs.requestDate = apiCallLogs.requestDate != null ? moment(apiCallLogs.requestDate) : null;
                apiCallLogs.responseDate = apiCallLogs.responseDate != null ? moment(apiCallLogs.responseDate) : null;
            });
        }
        return res;
    }
}
