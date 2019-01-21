import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IApiCallLogs } from 'app/shared/model/api-call-logs.model';
import { ApiCallLogsService } from './api-call-logs.service';

@Component({
    selector: 'jhi-api-call-logs-delete-dialog',
    templateUrl: './api-call-logs-delete-dialog.component.html'
})
export class ApiCallLogsDeleteDialogComponent {
    apiCallLogs: IApiCallLogs;

    constructor(
        private apiCallLogsService: ApiCallLogsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.apiCallLogsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'apiCallLogsListModification',
                content: 'Deleted an apiCallLogs'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-api-call-logs-delete-popup',
    template: ''
})
export class ApiCallLogsDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ apiCallLogs }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ApiCallLogsDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.apiCallLogs = apiCallLogs;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
