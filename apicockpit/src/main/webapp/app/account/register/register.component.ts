import { Router } from '@angular/router';
import { Component, OnInit, AfterViewInit, Renderer, ElementRef, ViewChild } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiLanguageService } from 'ng-jhipster';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from 'app/shared';
import { LoginModalService } from 'app/core';
import { Register } from './register.service';

declare var grecaptcha: any;

@Component({
    selector: 'jhi-register',
    templateUrl: './register.component.html',
    styleUrls: ['register.component.scss']
})
export class RegisterComponent implements OnInit, AfterViewInit {
    confirmPassword: string;
    doNotMatch: string;
    error: string;
    errorEmailExists: string;
    errorUserExists: string;
    captchaError: boolean = false;
    registerAccount: any;
    company: string;
    phoneNumber: string;
    gCaptchaKey: string;
    captchaEnabled: boolean;
    success: boolean;
    modalRef: NgbModalRef;
    @ViewChild('successModal')
    successModal;

    constructor(
        private languageService: JhiLanguageService,
        private loginModalService: LoginModalService,
        private registerService: Register,
        private elementRef: ElementRef,
        private renderer: Renderer,
        private router: Router
    ) {}

    ngOnInit() {
        this.success = false;
        this.registerAccount = { typeOfProject: '' };
        this.captchaEnabled = true;
        this.gCaptchaKey = '6LeJdEsUAAAAAAdlmZTMzUd4ACF1rjPGelUDQafp';
    }

    ngAfterViewInit() {
        this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#email'), 'focus', []);
    }

    register() {
        const response = grecaptcha.getResponse();
        if (response.length === 0) {
            this.captchaError = true;
            return;
        }
        this.registerAccount.recaptchaResponse = response;
        this.registerAccount.login = this.registerAccount.email;
        this.doNotMatch = null;
        this.error = null;
        this.errorUserExists = null;
        this.errorEmailExists = null;
        this.languageService.getCurrent().then(key => {
            this.registerAccount.langKey = key;
            this.registerService.save(this.registerAccount).subscribe(
                () => {
                    this.success = true;
                    this.successModal.show();
                },
                response => this.processError(response)
            );
        });
    }

    openLogin() {
        this.modalRef = this.loginModalService.open();
    }

    redirectToHome() {
        this.router.navigateByUrl('/');
        this.registerService.attemptLogin();
    }

    private processError(response: HttpErrorResponse) {
        this.success = null;
        if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
            this.errorUserExists = 'ERROR';
        } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
            this.errorEmailExists = 'ERROR';
        } else {
            this.error = 'ERROR';
        }
    }
}
