import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { map } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class Register {
    private attemptLoginSubject: BehaviorSubject<boolean>;
    loginObservable: Observable<boolean>;

    constructor(private http: HttpClient) {
        this.attemptLoginSubject = new BehaviorSubject(false);
        this.loginObservable = this.attemptLoginSubject.asObservable();
    }

    save(account: any): Observable<any> {
        return this.http.post(SERVER_API_URL + 'api/register', account);
    }

    attemptLogin() {
        this.attemptLoginSubject.next(true);
    }
}
