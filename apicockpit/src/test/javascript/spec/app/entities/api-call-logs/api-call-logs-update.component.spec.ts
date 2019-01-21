/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ApicockpitTestModule } from '../../../test.module';
import { ApiCallLogsUpdateComponent } from 'app/entities/api-call-logs/api-call-logs-update.component';
import { ApiCallLogsService } from 'app/entities/api-call-logs/api-call-logs.service';
import { ApiCallLogs } from 'app/shared/model/api-call-logs.model';

describe('Component Tests', () => {
    describe('ApiCallLogs Management Update Component', () => {
        let comp: ApiCallLogsUpdateComponent;
        let fixture: ComponentFixture<ApiCallLogsUpdateComponent>;
        let service: ApiCallLogsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ApicockpitTestModule],
                declarations: [ApiCallLogsUpdateComponent]
            })
                .overrideTemplate(ApiCallLogsUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ApiCallLogsUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ApiCallLogsService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new ApiCallLogs(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.apiCallLogs = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new ApiCallLogs();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.apiCallLogs = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
