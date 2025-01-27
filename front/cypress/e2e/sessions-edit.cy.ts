describe('Session Edit Tests', () => {
  beforeEach(() => {
    // Mock pour les détails de la session à modifier
    cy.intercept('GET', '/api/session/1', {
      statusCode: 200,
      body: {
        id: 1,
        name: 'Old Session Name',
        date: '2025-01-24',
        description: 'Old Description',
        teacher_id: 101,
        users: [],
        createdAt: '2025-01-01T08:00:00Z',
        updatedAt: '2025-01-02T08:00:00Z',
      },
    }).as('getSessionDetails');

    // Mock pour la liste des enseignants
    cy.intercept('GET', '/api/teacher', {
      statusCode: 200,
      body: [
        {
          id: 101,
          firstName: 'Hélène',
          lastName: 'THIERCELIN',
          createdAt: '2023-01-01T08:00:00Z',
          updatedAt: '2023-01-02T08:00:00Z',
        },
        {
          id: 102,
          firstName: 'Jean',
          lastName: 'DUPONT',
          createdAt: '2023-01-03T08:00:00Z',
          updatedAt: '2023-01-04T08:00:00Z',
        },
      ],
    }).as('getTeachers');

    // Mock pour la mise à jour de la session
    cy.intercept('PUT', '/api/session/1', {
      statusCode: 200,
      body: {
        id: 1,
        name: 'Updated Session Name',
        date: '2025-01-25',
        description: 'Updated Description',
        teacher_id: 102,
        users: [],
        createdAt: '2025-01-01T08:00:00Z',
        updatedAt: '2025-01-02T08:00:00Z',
      },
    }).as('updateSession');

    // Mock pour la liste des sessions après mise à jour
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [
        {
          id: 1,
          name: 'Updated Session Name',
          date: '2025-01-25',
          description: 'Updated Description',
          teacher_id: 102,
          createdAt: '2025-01-01T08:00:00Z',
          updatedAt: '2025-01-02T08:00:00Z',
        },
      ],
    }).as('getSessionsAfterUpdate');

    // Mock pour la connexion admin
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'adminName',
        token: 'adminFakeToken123',
        admin: true,
      },
    }).as('adminLogin');

    // Connexion admin
    cy.visit('/login');
    cy.get('input[formControlName=email]').type('admin@test.com');
    cy.get('input[formControlName=password]').type('adminPassword123');
    cy.get('button[type=submit]').click();

    // Vérifie la redirection vers la page des sessions
    cy.wait('@adminLogin');
    cy.url().should('include', '/sessions');
  });

  it('should edit a session successfully', () => {
    // Clique sur le bouton "Edit"
    cy.get('button').contains('Edit').click();
    cy.url().should('include', '/sessions/update/1');
    cy.wait('@getSessionDetails');
    cy.wait('@getTeachers');

    // Vérifie que les informations existantes sont chargées
    cy.get('input[formControlName=name]').should('have.value', 'Old Session Name');
    cy.get('input[formControlName=date]').should('have.value', '2025-01-24');
    cy.get('textarea[formControlName=description]').should('have.value', 'Old Description');

    // Met à jour le formulaire
    cy.get('input[formControlName=name]').clear().type('Updated Session Name');
    cy.get('input[formControlName=date]').clear().type('2025-01-25');
    cy.get('mat-select[formControlName=teacher_id]').click();
    cy.get('mat-option').contains('Jean DUPONT').click();
    cy.get('textarea[formControlName=description]').clear().type('Updated Description');

    // Soumet le formulaire
    cy.get('button').contains('Save').click();
    cy.wait('@updateSession');

    // Attends que la liste des sessions mise à jour soit chargée
    cy.wait('@getSessionsAfterUpdate');

    // Vérifie que l'utilisateur est redirigé vers la page des sessions
    cy.url().should('include', '/sessions');

    // Vérifie que la session mise à jour est affichée dans la liste
    cy.contains('Updated Session Name').should('be.visible');
    cy.contains('Session on January 25, 2025').should('be.visible');
    cy.contains('Updated Description').should('be.visible');
  });
});
