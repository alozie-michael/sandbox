import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IApiProject } from 'app/shared/model/api-project.model';
import { ApiProjectService } from './api-project.service';
import { IApiProjectAuthConfig } from 'app/shared/model/api-project-auth-config.model';
import { ApiProjectAuthConfigService } from 'app/entities/api-project-auth-config';
import { IApiProjectService } from 'app/shared/model/api-project-service.model';
import { ApiProjectServiceService } from 'app/entities/api-project-service';
import { IApiConsumerProfile } from 'app/shared/model/api-consumer-profile.model';
import { ApiConsumerProfileService } from 'app/entities/api-consumer-profile';

import { UnsavedChangesGuard } from 'app/shared/guard/unsavedChangesGuard';
import { count } from 'rxjs/operators';

@Component({
    selector: 'jhi-api-project-update',
    templateUrl: './api-project-update.component.html',
    styleUrls: ['./api-project-update.component.scss']
})
export class ApiProjectUpdateComponent implements OnInit, UnsavedChangesGuard {
    apiProject: IApiProject;
    isSaving: boolean;
    apikeys: IApiProjectAuthConfig[];
    apiprojectservices: IApiProjectService[];
    apiconsumerprofiles: IApiConsumerProfile[];
    dateCreated: string;

    @ViewChild('confirmUnsavedChanges')
    confirmChangesPrompt;

    constructor(
        private jhiAlertService: JhiAlertService,
        private apiProjectService: ApiProjectService,
        private apiProjectAuthConfigService: ApiProjectAuthConfigService,
        private apiProjectServiceService: ApiProjectServiceService,
        private apiConsumerProfileService: ApiConsumerProfileService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ apiProject }) => {
            this.apiProject = apiProject;
            this.dateCreated = this.apiProject.dateCreated != null ? this.apiProject.dateCreated.format(DATE_TIME_FORMAT) : null;
        });
        this.apiProjectAuthConfigService.query({ filter: 'project(name)-is-null' }).subscribe(
            (res: HttpResponse<IApiProjectAuthConfig[]>) => {
                if (!this.apiProject.apiKeyId) {
                    this.apikeys = res.body;
                } else {
                    this.apiProjectAuthConfigService.find(this.apiProject.apiKeyId).subscribe(
                        (subRes: HttpResponse<IApiProjectAuthConfig>) => {
                            this.apikeys = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.apiProjectServiceService.query().subscribe(
            (res: HttpResponse<IApiProjectService[]>) => {
                this.apiprojectservices = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.apiConsumerProfileService.query().subscribe(
            (res: HttpResponse<IApiConsumerProfile[]>) => {
                this.apiconsumerprofiles = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    resetApiKeys() {
        console.log('reseting api key');
    }

    toggleApiKeyState() {
        console.log('toggling api key state');
    }

    save() {
        this.isSaving = true;
        if (this.apiProject.id !== undefined) {
            this.subscribeToSaveResponse(this.apiProjectService.update(this.apiProject));
        } else {
            this.subscribeToSaveResponse(this.apiProjectService.create(this.apiProject));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IApiProject>>) {
        result.subscribe((res: HttpResponse<IApiProject>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    // trackApiProjectAuthConfigById(index: number, item: IApiProjectAuthConfig) {
    //     return item.id;
    // }

    trackApiProjectServiceById(index: number, item: IApiProjectService) {
        return item.id;
    }

    trackApiConsumerProfileById(index: number, item: IApiConsumerProfile) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        this.apiProject.apis = this.apiProject.apis ? this.apiProject.apis : [];
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        this.apiProject.apis.push(option);
        return option;
    }

    removeApi(apiOption) {
        const index = this.apiProject.apis.findIndex(api => apiOption.id === api.id);
        this.apiProject.apis.splice(index, 1);
    }

    canDeactivate() {
        if (!true) {
            return true;
        } else {
            this.confirmChangesPrompt.show();
        }
    }

    groupBy(item) {
        return item['serviceGroupName'];
    }

    groupByValueFn(_: string, children: any[]) {
        return { name: children[0].serviceGroupName, length: children.length };
    }
}
