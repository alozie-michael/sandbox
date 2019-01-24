import { DashboardComponent } from './dashboard.component';
import { Route } from '@angular/router';
import { UserRouteAccessService } from 'app/core';

export const dashboardRoute: Route = {
    path: 'dashboard',
    component: DashboardComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'global.menu.dashboard'
    },
    canActivate: [UserRouteAccessService]
};
