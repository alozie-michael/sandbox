import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { ApicockpitSharedLibsModule, ApicockpitSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';
import { NgSelectModule } from '@ng-select/ng-select';
import { SweetAlert2Module } from '@toverux/ngx-sweetalert2';

import { SidebarDropdownDirective } from 'app/shared/directives/sidebar-dropdown.directive';
import { JhMaterialModule } from 'app/shared/jh-material.module';

@NgModule({
    imports: [ApicockpitSharedLibsModule, ApicockpitSharedCommonModule, NgSelectModule, SweetAlert2Module, JhMaterialModule],
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
        JhMaterialModule
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApicockpitSharedModule {}
