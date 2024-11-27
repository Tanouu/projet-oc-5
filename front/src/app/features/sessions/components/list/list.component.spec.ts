import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { of } from 'rxjs';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { ListComponent } from './list.component';
import { expect } from '@jest/globals';
import {RouterTestingModule} from "@angular/router/testing";

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  };

  const mockSessionApiService = {
    all: jest.fn().mockReturnValue(of([
      { id: 1, name: 'Session 1', date: new Date(), users: [] },
      { id: 2, name: 'Session 2', date: new Date(), users: [] }
    ]))
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule, RouterTestingModule],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService }
      ]
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
    // Sélectionner uniquement les sessions avec la classe .item
    const sessionElements = compiled.querySelectorAll('.item');
    // Assertions
    expect(sessionElements.length).toBe(2); // Nombre de sessions attendues
    expect(sessionElements[0].textContent).toContain('Session 1');
    expect(sessionElements[1].textContent).toContain('Session 2');
  });

});
