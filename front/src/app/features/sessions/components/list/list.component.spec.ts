import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { of } from 'rxjs';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { ListComponent } from './list.component';
import { expect } from '@jest/globals';
import { RouterTestingModule } from '@angular/router/testing';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  // Mock SessionService
  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
  };

  // Mock SessionApiService avec deux sessions
  const mockSessionApiService = {
    all: jest.fn().mockReturnValue(
      of([
        { id: 1, name: 'Session 1', date: new Date(), users: [] },
        { id: 2, name: 'Session 2', date: new Date(), users: [] },
      ])
    ),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule, RouterTestingModule],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display a list of sessions', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    const sessionElements = compiled.querySelectorAll('.item');
    // Assertions
    expect(sessionElements.length).toBe(2); // Nombre de sessions attendues
    expect(sessionElements[0].textContent).toContain('Session 1');
    expect(sessionElements[1].textContent).toContain('Session 2');
  });

  it('should display the Create button if user is admin and the Detail button', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    // Check for Create button
    const createButton = compiled.querySelector('button[routerLink="create"]');
    console.log('Create Button:', createButton);
    expect(createButton).toBeTruthy();
    expect(createButton?.textContent).toContain('Create');

    // Check for Detail button
    const detailButtons = compiled.querySelectorAll('button[ng-reflect-router-link^="detail,"]');
    expect(detailButtons.length).toBe(2);
    expect(detailButtons[0].textContent).toContain('Detail');
    expect(detailButtons[1].textContent).toContain('Detail');
  });

});
