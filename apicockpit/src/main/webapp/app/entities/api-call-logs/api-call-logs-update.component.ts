import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IApiCallLogs } from 'app/shared/model/api-call-logs.model';
import { ApiCallLogsService } from './api-call-logs.service';

@Component({
    selector: 'jhi-api-call-logs-update',
    templateUrl: './api-call-logs-update.component.html'
})
export class ApiCallLogsUpdateComponent implements OnInit {
    apiCallLogs: IApiCallLogs;
    isSaving: boolean;
    requestDate: string;
    responseDate: string;

    constructor(private apiCallLogsService: ApiCallLogsService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ apiCallLogs }) => {
            this.apiCallLogs = apiCallLogs;
            this.requestDate = this.apiCallLogs.requestDate != null ? this.apiCallLogs.requestDate.format(DATE_TIME_FORMAT) : null;
            this.responseDate = this.apiCallLogs.responseDate != null ? this.apiCallLogs.responseDate.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.apiCallLogs.requestDate = this.requestDate != null ? moment(this.requestDate, DATE_TIME_FORMAT) : null;
        this.apiCallLogs.responseDate = this.responseDate != null ? moment(this.responseDate, DATE_TIME_FORMAT) : null;
        if (this.apiCallLogs.id !== undefined) {
            this.subscribeToSaveResponse(this.apiCallLogsService.update(this.apiCallLogs));
        } else {
            this.subscribeToSaveResponse(this.apiCallLogsService.create(this.apiCallLogs));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IApiCallLogs>>) {
        result.subscribe((res: HttpResponse<IApiCallLogs>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
