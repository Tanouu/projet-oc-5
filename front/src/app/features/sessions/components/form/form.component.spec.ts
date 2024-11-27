import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { FormComponent } from './form.component';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';


describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionApiService: SessionApiService;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  };

  const mockSessionApiService = {
    create: jest.fn().mockReturnValue(of({
      id: 1,
      name: 'Yoga Session',
      description: 'A relaxing yoga session.',
      date: new Date('2024-12-01'),
      teacher_id: 101,
      users: [],
      createdAt: new Date(),
      updatedAt: new Date()
    })),
    update: jest.fn().mockReturnValue(of({
      id: 1,
      name: 'Yoga Session',
      description: 'A relaxing yoga session.',
      date: new Date('2024-12-01'),
      teacher_id: 101,
      users: [],
      createdAt: new Date(),
      updatedAt: new Date()
    })),
    detail: jest.fn().mockReturnValue(of({
      id: 1,
      name: 'Yoga Session',
      description: 'A relaxing yoga session.',
      date: new Date('2024-12-01'),
      teacher_id: 101,
      users: [],
      createdAt: new Date(),
      updatedAt: new Date()
    }))
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        NoopAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService }
      ],
      declarations: [FormComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    fixture.detectChanges();
  });

  it('should create a session successfully', () => {
    component.sessionForm?.setValue({
      name: 'Yoga Session',
      date: '2024-12-01',
      teacher_id: 101,
      description: 'A relaxing yoga session.'
    });

    const spy = jest.spyOn(sessionApiService, 'create').mockReturnValue(of({
      id: 1,
      name: 'Yoga Session',
      description: 'A relaxing yoga session.',
      date: new Date('2024-12-01'),
      teacher_id: 101,
      users: [],
      createdAt: new Date(),
      updatedAt: new Date()
    }));
    component.submit();
    expect(spy).toHaveBeenCalled();
  });

  it('should disable the submit button when the form is invalid', () => {
    component.sessionForm?.setValue({
      name: '', // Champ vide
      date: '2024-12-01',
      teacher_id: 101,
      description: 'A relaxing yoga session.'
    });

    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const submitButton = compiled.querySelector('button[type="submit"]');
    expect(submitButton).toBeTruthy();
    expect(submitButton?.hasAttribute('disabled')).toBe(true);
  });

});
