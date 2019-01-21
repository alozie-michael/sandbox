import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ApiCallLogs } from 'app/shared/model/api-call-logs.model';
import { ApiCallLogsService } from './api-call-logs.service';
import { ApiCallLogsComponent } from './api-call-logs.component';
import { ApiCallLogsDetailComponent } from './api-call-logs-detail.component';
import { ApiCallLogsUpdateComponent } from './api-call-logs-update.component';
import { ApiCallLogsDeletePopupComponent } from './api-call-logs-delete-dialog.component';
import { IApiCallLogs } from 'app/shared/model/api-call-logs.model';

@Injectable({ providedIn: 'root' })
export class ApiCallLogsResolve implements Resolve<IApiCallLogs> {
    constructor(private service: ApiCallLogsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ApiCallLogs> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<ApiCallLogs>) => response.ok),
                map((apiCallLogs: HttpResponse<ApiCallLogs>) => apiCallLogs.body)
            );
        }
        return of(new ApiCallLogs());
    }
}

export const apiCallLogsRoute: Routes = [
    {
        path: 'api-call-logs',
        component: ApiCallLogsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'apicockpitApp.apiCallLogs.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'api-call-logs/:id/view',
        component: ApiCallLogsDetailComponent,
        resolve: {
            apiCallLogs: ApiCallLogsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apicockpitApp.apiCallLogs.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'api-call-logs/new',
        component: ApiCallLogsUpdateComponent,
        resolve: {
            apiCallLogs: ApiCallLogsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apicockpitApp.apiCallLogs.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'api-call-logs/:id/edit',
        component: ApiCallLogsUpdateComponent,
        resolve: {
            apiCallLogs: ApiCallLogsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apicockpitApp.apiCallLogs.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const apiCallLogsPopupRoute: Routes = [
    {
        path: 'api-call-logs/:id/delete',
        component: ApiCallLogsDeletePopupComponent,
        resolve: {
            apiCallLogs: ApiCallLogsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'apicockpitApp.apiCallLogs.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
