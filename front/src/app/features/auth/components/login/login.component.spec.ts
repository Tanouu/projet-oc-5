import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { LoginComponent } from './login.component';
import {SessionService} from "../../../../services/session.service";
import {Router} from "@angular/router";
import { expect } from '@jest/globals';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HttpClientModule} from "@angular/common/http";
import {MatCardModule} from "@angular/material/card";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";


describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceMock: any;
  let sessionServiceMock: any;
  let routerSpy: any;

  beforeEach(async () => {
    // Mock AuthService
    authServiceMock = {
      login: jest.fn()
    };

    // Mock SessionService
    sessionServiceMock = {
      logIn: jest.fn()
    };

    // Mock Router
    routerSpy = { navigate: jest.fn() };

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
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
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('Form Validation', () => {
    it('should mark form as invalid if fields are empty', () => {
      component.form.controls['email'].setValue('');
      component.form.controls['password'].setValue('');
      expect(component.form.invalid).toBeTruthy();
    });
  });

  describe('Submit', () => {
    it('should call AuthService.login with correct credentials', () => {
      const mockLoginRequest = { email: 'test@example.com', password: 'password123' };
      authServiceMock.login.mockReturnValue(of({ token: 'fake-token' }));

      component.form.setValue(mockLoginRequest);
      component.submit();

      expect(authServiceMock.login).toHaveBeenCalledWith(mockLoginRequest);
    });

    it('should navigate to /sessions on successful login', () => {
      authServiceMock.login.mockReturnValue(of({ token: 'fake-token' }));

      component.form.setValue({ email: 'test@example.com', password: 'password123' });
      component.submit();

      expect(sessionServiceMock.logIn).toHaveBeenCalledWith({ token: 'fake-token' });
      expect(routerSpy.navigate).toHaveBeenCalledWith(['/sessions']);
    });

    it('should set onError to true on login failure', () => {
      authServiceMock.login.mockReturnValue(throwError(() => new Error('Invalid credentials')));

      component.form.setValue({ email: 'test@example.com', password: 'wrong-password' });
      component.submit();

      expect(component.onError).toBeTruthy();
    });
  });

  describe('Error Display', () => {
    it('should display an error message if form is invalid', () => {
      component.form.controls['email'].setValue('');
      component.form.controls['password'].setValue('');
      component.form.markAllAsTouched(); // Mark the form as touched to trigger validation
      fixture.detectChanges();

      const compiled = fixture.nativeElement as HTMLElement;
      expect(compiled.querySelector('.error-message')).toBeTruthy();
    });

    it('should display an error message on login failure', () => {
      component.onError = true;
      fixture.detectChanges();

      const compiled = fixture.nativeElement as HTMLElement;
      expect(compiled.querySelector('.login-error')).toBeTruthy();
    });
  });
});
