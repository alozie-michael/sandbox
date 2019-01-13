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

    pieChartLabels: string[] = ['Error Count', 'API Calls'];
    pieChartData: number[] = [10, 60];
    pieChartType: string = 'pie';

    constructor(private dashboardService: DashboardService) {}

    ngOnInit() {
        this.dashboardService.get().subscribe(
            response => {
                this.subscribedServices = response.body.subscribedServices;
                this.totalApiCalls = response.body.totalApiCalls;
                this.totalApiErrorCalls = response.body.totalApiErrorCalls;
                this.apiCallGraph = response.body.apiCallGraph;
                this.userActivities = response.body.userActivityDTOList.slice(0, 10);
            },
            null,
            () => {
                this.generateDataForPie();
                this.generateDataForBar();
            }
        );
    }
    public barChartLabels: string[] = [];
    barChartData: any[] = [{ data: [], label: '' }];

    generateDataForPie() {
        let dataForPie = [this.totalApiErrorCalls.count, this.totalApiCalls.count];
        this.pieChartData = dataForPie;
    }
    generateDataForBar() {
        let barChartDataClone = [];
        let barlabelClone = [];

        this.apiCallGraph.forEach(element => {
            barChartDataClone.push({ data: [element.count], label: element.name });
            barlabelClone.push(element.name);
        });
        this.barChartLabels = barlabelClone;
        setTimeout(() => {
            this.barChartData = barChartDataClone;
        }, 50);
    }
}
