import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { ApicockpitSharedLibsModule, ApicockpitSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';
import { NgSelectModule } from '@ng-select/ng-select';
import { SidebarDropdownDirective } from 'app/shared/directives/sidebar-dropdown.directive';

@NgModule({
    imports: [ApicockpitSharedLibsModule, ApicockpitSharedCommonModule, NgSelectModule],
    declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective, SidebarDropdownDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [JhiLoginModalComponent],
    exports: [ApicockpitSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective, NgSelectModule, SidebarDropdownDirective],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApicockpitSharedModule {}
