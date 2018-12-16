import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { ApicockpitSharedLibsModule, ApicockpitSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';
import { NgSelectModule } from '@ng-select/ng-select';
import { SweetAlert2Module } from '@toverux/ngx-sweetalert2';

import { SidebarDropdownDirective } from 'app/shared/directives/sidebar-dropdown.directive';
import { ShowHidePasswordModule } from 'ngx-show-hide-password';

@NgModule({
    imports: [
        ApicockpitSharedLibsModule,
        ApicockpitSharedCommonModule,
        NgSelectModule,
        SweetAlert2Module,
        ShowHidePasswordModule.forRoot()
    ],
    declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective, SidebarDropdownDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [JhiLoginModalComponent],
    exports: [
        ApicockpitSharedCommonModule,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        NgSelectModule,
        SidebarDropdownDirective,
        SweetAlert2Module,
        ShowHidePasswordModule
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApicockpitSharedModule {}
