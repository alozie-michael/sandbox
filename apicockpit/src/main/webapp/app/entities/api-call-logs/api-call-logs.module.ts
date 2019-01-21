import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ApicockpitSharedModule } from 'app/shared';
import {
    ApiCallLogsComponent,
    ApiCallLogsDetailComponent,
    ApiCallLogsUpdateComponent,
    ApiCallLogsDeletePopupComponent,
    ApiCallLogsDeleteDialogComponent,
    apiCallLogsRoute,
    apiCallLogsPopupRoute
} from './';

const ENTITY_STATES = [...apiCallLogsRoute, ...apiCallLogsPopupRoute];

@NgModule({
    imports: [ApicockpitSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ApiCallLogsComponent,
        ApiCallLogsDetailComponent,
        ApiCallLogsUpdateComponent,
        ApiCallLogsDeleteDialogComponent,
        ApiCallLogsDeletePopupComponent
    ],
    entryComponents: [ApiCallLogsComponent, ApiCallLogsUpdateComponent, ApiCallLogsDeleteDialogComponent, ApiCallLogsDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApicockpitApiCallLogsModule {}
