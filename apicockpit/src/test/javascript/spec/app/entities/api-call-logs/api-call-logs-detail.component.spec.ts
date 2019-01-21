/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApicockpitTestModule } from '../../../test.module';
import { ApiCallLogsDetailComponent } from 'app/entities/api-call-logs/api-call-logs-detail.component';
import { ApiCallLogs } from 'app/shared/model/api-call-logs.model';

describe('Component Tests', () => {
    describe('ApiCallLogs Management Detail Component', () => {
        let comp: ApiCallLogsDetailComponent;
        let fixture: ComponentFixture<ApiCallLogsDetailComponent>;
        const route = ({ data: of({ apiCallLogs: new ApiCallLogs(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ApicockpitTestModule],
                declarations: [ApiCallLogsDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ApiCallLogsDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ApiCallLogsDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.apiCallLogs).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
