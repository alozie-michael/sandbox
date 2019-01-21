import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ApicockpitPlatformUserModule } from './platform-user/platform-user.module';
import { ApicockpitApiPublisherProfileModule } from './api-publisher-profile/api-publisher-profile.module';
import { ApicockpitApiConsumerProfileModule } from './api-consumer-profile/api-consumer-profile.module';
import { ApicockpitApiProjectModule } from './api-project/api-project.module';
import { ApicockpitApiServiceGroupModule } from './api-service-group/api-service-group.module';
import { ApicockpitApiServiceConfigModule } from './api-service-config/api-service-config.module';
import { ApicockpitApiProjectServiceModule } from './api-project-service/api-project-service.module';
import { ApicockpitApiProjectAuthConfigModule } from './api-project-auth-config/api-project-auth-config.module';
import { ApicockpitApiSvcProcConfigModule } from './api-svc-proc-config/api-svc-proc-config.module';
import { ApicockpitDbProcConfigModule } from './db-proc-config/db-proc-config.module';
import { ApicockpitGrovyProcConfigModule } from './grovy-proc-config/grovy-proc-config.module';
import { ApicockpitApiCallLogsModule } from './api-call-logs/api-call-logs.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        ApicockpitPlatformUserModule,
        ApicockpitApiPublisherProfileModule,
        ApicockpitApiConsumerProfileModule,
        ApicockpitApiProjectModule,
        ApicockpitApiServiceGroupModule,
        ApicockpitApiServiceConfigModule,
        ApicockpitApiProjectServiceModule,
        ApicockpitApiProjectAuthConfigModule,
        ApicockpitApiSvcProcConfigModule,
        ApicockpitDbProcConfigModule,
        ApicockpitGrovyProcConfigModule,
        ApicockpitApiCallLogsModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApicockpitEntityModule {}
