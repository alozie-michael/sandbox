import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { Ng2Webstorage } from 'ngx-webstorage';

import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { ApicockpitSharedModule } from 'app/shared';
import { ApicockpitCoreModule } from 'app/core';
import { ApicockpitAppRoutingModule } from './app-routing.module';
import { ApicockpitHomeModule } from './home/home.module';
import { ApicockpitAccountModule } from './account/account.module';
import { ApicockpitEntityModule } from './entities/entity.module';
import * as moment from 'moment';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent, NavbarComponent, FooterComponent, PageRibbonComponent, ActiveMenuDirective, ErrorComponent } from './layouts';
import { SweetAlert2Module } from '@toverux/ngx-sweetalert2';
import { SidebarComponent } from './layouts/sidebar/sidebar.component';
import { DashboardComponent } from './layouts/dashboard/dashboard.component';
import { MenuComponent } from 'app/layouts/menu/menu.component';

@NgModule({
    imports: [
        BrowserModule,
        ApicockpitAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-' }),
        ApicockpitSharedModule,
        ApicockpitCoreModule,
        ApicockpitHomeModule,
        ApicockpitAccountModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
        ApicockpitEntityModule,
        SweetAlert2Module.forRoot()
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent,
        SidebarComponent,
        DashboardComponent,
        MenuComponent
    ],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true
        }
    ],
    bootstrap: [JhiMainComponent]
})
export class ApicockpitAppModule {
    constructor(private dpConfig: NgbDatepickerConfig) {
        this.dpConfig.minDate = { year: moment().year() - 100, month: 1, day: 1 };
    }
}
