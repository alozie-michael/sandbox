import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IApiCallLogs } from 'app/shared/model/api-call-logs.model';

@Component({
    selector: 'jhi-api-call-logs-detail',
    templateUrl: './api-call-logs-detail.component.html'
})
export class ApiCallLogsDetailComponent implements OnInit {
    apiCallLogs: IApiCallLogs;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ apiCallLogs }) => {
            this.apiCallLogs = apiCallLogs;
        });
    }

    previousState() {
        window.history.back();
    }
}
