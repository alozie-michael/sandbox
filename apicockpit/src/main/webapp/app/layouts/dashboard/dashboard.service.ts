import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class DashboardService {
    constructor(private http: HttpClient) {}

    get(): Observable<HttpResponse<any>> {
        return this.http.get(SERVER_API_URL + 'api/dashboard', { observe: 'response' });
    }
}
