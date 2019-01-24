import { MenuComponent } from './menu.component';
import { Route } from '@angular/router';
import { UserRouteAccessService } from 'app/core';

export const menuRoute: Route = {
    path: 'menu',
    component: MenuComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'global.menu.home'
    },
    canActivate: [UserRouteAccessService]
};
