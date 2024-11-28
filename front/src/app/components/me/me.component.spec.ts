import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { of } from 'rxjs';
import { SessionService } from 'src/app/services/session.service';
import { UserService } from 'src/app/services/user.service';
import { MeComponent } from './me.component';
import { User } from '../../interfaces/user.interface';
import { expect } from '@jest/globals';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };

  const mockUser: User = {
    id: 1,
    firstName: 'Tanou',
    lastName: 'Pacheco',
    email: 'tanou@example.com',
    admin: false,
    password: 'toto',
    createdAt: new Date('2023-01-01T00:00:00Z'),
    updatedAt: new Date('2023-06-01T00:00:00Z')
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        {
          provide: UserService,
          useValue: {
            getById: jest.fn().mockReturnValue(of(mockUser))
          }
        }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    userService = TestBed.inject(UserService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display user information', () => {
    fixture.detectChanges();

    const compiled = fixture.nativeElement;
    const userInfoElements = compiled.querySelectorAll('p');

    const userInfoText = Array.from(userInfoElements)
      .map(el => (el as HTMLElement).textContent?.replace(/\s+/g, ' ').trim())
      .join(' ');

    expect(userInfoText).toContain('Name: Tanou PACHECO');
    expect(userInfoText).toContain('Email: tanou@example.com');

    if (mockUser.admin) {
      expect(userInfoText).toContain('You are admin');
    } else {
      expect(userInfoText).toContain('Delete my account:');
    }

    // Formater les dates attendues
    const createdAtFormatted = mockUser.createdAt.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    }).trim();
    const updatedAtFormatted = mockUser.updatedAt?.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    }).trim();

    expect(userInfoText).toContain(`Create at: ${createdAtFormatted}`);
    expect(userInfoText).toContain(`Last update: ${updatedAtFormatted}`);
  });

});
