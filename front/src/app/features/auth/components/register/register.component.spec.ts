import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceMock: any;
  let routerSpy: any;

  beforeEach(async () => {
    // Mock AuthService
    authServiceMock = {
      register: jest.fn()
    };

    // Mock Router
    routerSpy = { navigate: jest.fn() };

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerSpy },
        FormBuilder
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('Submit', () => {
    it('should call AuthService.register with correct data', () => {
      const mockRegisterRequest = {
        email: 'tanou@example.com',
        firstName: 'Tanou',
        lastName: 'Pacheco',
        password: 'toto123'
      };
      authServiceMock.register.mockReturnValue(of(void 0));

      component.form.setValue(mockRegisterRequest);
      component.submit();

      expect(authServiceMock.register).toHaveBeenCalledWith(mockRegisterRequest);
    });
  });

  describe('Error Display', () => {
    it('should display an error message if form is invalid', () => {
      component.form.controls['email'].setValue('');
      component.form.controls['firstName'].setValue('');
      component.form.controls['lastName'].setValue('');
      component.form.controls['password'].setValue('');
      component.form.markAllAsTouched();
      fixture.detectChanges();

      const compiled = fixture.nativeElement as HTMLElement;
      expect(compiled.querySelector('.error-message')).toBeTruthy();
    });
  });
});
