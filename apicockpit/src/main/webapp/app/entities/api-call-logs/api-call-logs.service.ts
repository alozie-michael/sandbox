import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

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
        return this.http.post<IApiCallLogs>(this.resourceUrl, apiCallLogs, { observe: 'response' });
    }

    update(apiCallLogs: IApiCallLogs): Observable<EntityResponseType> {
        return this.http.put<IApiCallLogs>(this.resourceUrl, apiCallLogs, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IApiCallLogs>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IApiCallLogs[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
