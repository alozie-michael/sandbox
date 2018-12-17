import { NgModule } from '@angular/core';
import { PrivacyComponent } from 'app/privacy/privacy.component';
import { ApicockpitSharedModule } from 'app/shared';
import { RouterModule, Routes } from '@angular/router';

export const routes: Routes = [{ path: '', component: PrivacyComponent }];

@NgModule({
    imports: [ApicockpitSharedModule, RouterModule.forChild(routes)],
    declarations: [PrivacyComponent]
})
export class PrivacyModule {}
