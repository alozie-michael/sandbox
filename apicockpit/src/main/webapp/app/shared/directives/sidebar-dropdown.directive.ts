import { Directive, HostListener, ElementRef } from '@angular/core';

@Directive({
    selector: '[jhiSidebarDropdown]'
})
export class SidebarDropdownDirective {
    constructor(private element: ElementRef) {}

    @HostListener('click', ['$event.currentTarget.children'])
    toggleSubMenu(children) {
        this.closePreviousOpenedMenu(children[1]);
        children[1].classList.toggle('open');
    }

    closePreviousOpenedMenu(currentTarget) {
        if (!currentTarget.classList.contains('open')) {
            const openedMenu = document.querySelector('.open');
            if (openedMenu) {
                openedMenu.classList.remove('open');
            }
        }
    }
}
