import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';

import { DetailComponent } from './detail.component';
import {MatButtonModule} from "@angular/material/button";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
  };

  const mockSessionApiService = {
    detail: jest.fn().mockReturnValue(of({
      id: 1,
      name: 'Yoga Session',
      description: 'A relaxing yoga session.',
      date: new Date('2024-12-01'),
      teacher_id: 101,
      users: [1, 2, 3],
      createdAt: new Date('2024-11-01'),
      updatedAt: new Date('2024-11-15'),
    })),
    delete: jest.fn(),
    participate: jest.fn(),
    unParticipate: jest.fn(),
  };

  const mockTeacherService = {
    detail: jest.fn().mockReturnValue(of({
      id: 101,
      firstName: 'Tanou',
      lastName: 'Pacheco',
    })),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule,
        MatButtonModule,
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display session information correctly', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    const titleElement = compiled.querySelector('h1');
    expect(titleElement?.textContent).toContain('Yoga Session');


    const descriptionElement = compiled.querySelector('.description');
    expect(descriptionElement?.textContent).toContain('A relaxing yoga session.');

    const createdAtElement = compiled.querySelector('.created');
    const normalizedCreatedAt = createdAtElement?.textContent?.replace(/\s+/g, ' ');
    expect(normalizedCreatedAt).toContain('Create at: November 1, 2024');

    const updatedAtElement = compiled.querySelector('.updated');
    const normalizedUpdatedAt = updatedAtElement?.textContent?.replace(/\s+/g, ' ');
    expect(normalizedUpdatedAt).toContain('Last update: November 15, 2024');
  });

  it('should display Delete button if user is admin', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    const deleteButton = compiled.querySelector('button[color="warn"]');
    expect(deleteButton).toBeTruthy();
    expect(deleteButton?.textContent).toContain('Delete');
  });

  it('should call the delete method with the correct session ID', () => {
    const deleteSpy = jest.spyOn(mockSessionApiService, 'delete').mockReturnValue(of({}));

    component.delete();

    expect(deleteSpy).toHaveBeenCalledWith(component.sessionId);
  });

});
