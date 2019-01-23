import { Component, OnInit, SimpleChanges, OnChanges } from '@angular/core';
import { DashboardService } from './dashboard.service';
import { ApiCallGraph } from './dahboard-models';

@Component({
    selector: 'jhi-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
    subscribedServices: any;
    totalApiCalls: any;
    totalApiErrorCalls: any;
    apiCallGraph: ApiCallGraph[] = [];
    userActivities = [];

    barChartType: string = 'bar';
    barChartLegend: boolean = true;
    barChartOptions: any = {
        scaleShowVerticalLines: false,
        responsive: true
    };
    public barChartLabels: string[] = [];
    barChartData: any[] = [{ data: [] }];

    constructor(private dashboardService: DashboardService) {}

    ngOnInit() {
        this.dashboardService.get().subscribe(
            response => {
                this.subscribedServices = response.body.subscribedServices;
                this.totalApiCalls = response.body.totalApiCalls;
                this.totalApiErrorCalls = response.body.totalApiErrorCalls;
                this.apiCallGraph = response.body.apiCallGraph;
                this.userActivities = response.body.userActivityDTOList.slice(0, 4);
            },
            null,
            () => {
                this.generateDataForBar();
            }
        );
    }

    generateDataForBar() {
        this.barChartData = [];
        this.barChartLabels = [];

        this.apiCallGraph.forEach(element => {
            this.barChartLabels.push(element.name);
            this.barChartData.push({ data: [element.count], label: element.name });
        });
    }
}
