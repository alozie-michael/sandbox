import { Component, OnInit } from '@angular/core';
import { DashboardService } from './dashboard.service';

@Component({
    selector: 'jhi-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
    subscribedServices: any;
    totalApiCalls: any;
    totalApiErrorCalls: any;

    constructor(private dashboardService: DashboardService) {}

    ngOnInit() {
        this.dashboardService.get().subscribe(response => {
            this.subscribedServices = response.body.subscribedServices;
            this.totalApiCalls = response.body.totalApiCalls;
            this.totalApiErrorCalls = response.body.totalApiErrorCalls;
        });
    }
}
