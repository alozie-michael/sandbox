import { dashboardRoute } from './layouts/dashboard/dashboard.route';
import { sidebarRoute } from './layouts/sidebar/sidebar.route';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute, navbarRoute } from './layouts';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { menuRoute } from 'app/layouts/menu/menu.route';

const LAYOUT_ROUTES = [navbarRoute, sidebarRoute, menuRoute, dashboardRoute, ...errorRoute];

@NgModule({
    imports: [
        RouterModule.forRoot(
            [
                ...LAYOUT_ROUTES,
                {
                    path: 'admin',
                    loadChildren: './admin/admin.module#ApicockpitAdminModule'
                }
            ],
            { useHash: true, enableTracing: DEBUG_INFO_ENABLED }
        )
    ],
    exports: [RouterModule]
})
export class ApicockpitAppRoutingModule {}
