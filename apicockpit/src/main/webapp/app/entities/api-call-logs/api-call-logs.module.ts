import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ApicockpitSharedModule } from 'app/shared';
import { ApiCallLogsComponent, ApiCallLogsDetailComponent, apiCallLogsRoute } from './';

const ENTITY_STATES = [...apiCallLogsRoute];

@NgModule({
    imports: [ApicockpitSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [ApiCallLogsComponent, ApiCallLogsDetailComponent],
    entryComponents: [ApiCallLogsComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApicockpitApiCallLogsModule {}
