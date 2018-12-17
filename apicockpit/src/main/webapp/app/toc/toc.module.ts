import { NgModule } from '@angular/core';
import { TocComponent } from 'app/toc/toc.component';
import { ApicockpitSharedModule } from 'app/shared';
import { RouterModule, Routes } from '@angular/router';

export const routes: Routes = [{ path: '', component: TocComponent }];

@NgModule({
    imports: [ApicockpitSharedModule, RouterModule.forChild(routes)],
    declarations: [TocComponent]
})
export class TocModule {}
