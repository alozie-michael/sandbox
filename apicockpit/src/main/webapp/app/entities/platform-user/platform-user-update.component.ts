import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IPlatformUser } from 'app/shared/model/platform-user.model';
import { PlatformUserService } from './platform-user.service';
import { IUser, UserService } from 'app/core';
import { IApiPublisherProfile } from 'app/shared/model/api-publisher-profile.model';
import { ApiPublisherProfileService } from 'app/entities/api-publisher-profile';
import { IApiConsumerProfile } from 'app/shared/model/api-consumer-profile.model';
import { ApiConsumerProfileService } from 'app/entities/api-consumer-profile';

@Component({
    selector: 'jhi-platform-user-update',
    templateUrl: './platform-user-update.component.html',
    styleUrls: ['./platform-user-update.component.scss']
})
export class PlatformUserUpdateComponent implements OnInit {
    platformUser: IPlatformUser;
    isSaving: boolean;

    users: IUser[];

    apipublisherprofiles: IApiPublisherProfile[];

    apiconsumerprofiles: IApiConsumerProfile[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private platformUserService: PlatformUserService,
        private userService: UserService,
        private apiPublisherProfileService: ApiPublisherProfileService,
        private apiConsumerProfileService: ApiConsumerProfileService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ platformUser }) => {
            this.platformUser = platformUser;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.apiPublisherProfileService.query({ filter: 'platformuser-is-null' }).subscribe(
            (res: HttpResponse<IApiPublisherProfile[]>) => {
                if (!this.platformUser.apiPublisherProfileId) {
                    this.apipublisherprofiles = res.body;
                } else {
                    this.apiPublisherProfileService.find(this.platformUser.apiPublisherProfileId).subscribe(
                        (subRes: HttpResponse<IApiPublisherProfile>) => {
                            this.apipublisherprofiles = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.apiConsumerProfileService.query({ filter: 'platformuser-is-null' }).subscribe(
            (res: HttpResponse<IApiConsumerProfile[]>) => {
                if (!this.platformUser.apiConsumerProfileId) {
                    this.apiconsumerprofiles = res.body;
                } else {
                    this.apiConsumerProfileService.find(this.platformUser.apiConsumerProfileId).subscribe(
                        (subRes: HttpResponse<IApiConsumerProfile>) => {
                            this.apiconsumerprofiles = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.platformUser.id !== undefined) {
            this.subscribeToSaveResponse(this.platformUserService.update(this.platformUser));
        } else {
            this.subscribeToSaveResponse(this.platformUserService.create(this.platformUser));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPlatformUser>>) {
        result.subscribe((res: HttpResponse<IPlatformUser>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackApiPublisherProfileById(index: number, item: IApiPublisherProfile) {
        return item.id;
    }

    trackApiConsumerProfileById(index: number, item: IApiConsumerProfile) {
        return item.id;
    }
}
