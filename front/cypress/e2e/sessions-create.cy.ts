describe('Session Create Tests', () => {
  beforeEach(() => {
    // Mock pour la liste initiale des sessions
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [],
    }).as('getSessions');

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

    // Mock pour la création de la session
    cy.intercept('POST', '/api/session', {
      statusCode: 200,
      body: {
        id: 1,
        name: 'Test Session',
        date: '2025-01-24',
        description: 'Description test',
        teacher_id: 101,
        users: [],
        createdAt: '2025-01-01T08:00:00Z',
        updatedAt: '2025-01-02T08:00:00Z',
      },
    }).as('createSession');

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
    cy.wait('@getSessions');
  });

  it('should create a session successfully', () => {
    // Mock mis à jour pour la liste des sessions après création
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [
        {
          id: 1,
          name: 'Test Session',
          date: '2025-01-24',
          description: 'Description test',
          teacher_id: 101,
          users: [],
          createdAt: '2025-01-01T08:00:00Z',
          updatedAt: '2025-01-02T08:00:00Z',
        },
      ],
    }).as('getSessionsUpdated');

    // Clique sur le bouton "Create"
    cy.get('button').contains('Create').click();
    cy.url().should('include', '/sessions/create');
    cy.wait('@getTeachers');

    // Remplit le formulaire de création de session
    cy.get('input[formControlName=name]').type('Test Session');
    cy.get('input[formControlName=date]').type('2025-01-24');
    cy.get('mat-select[formControlName=teacher_id]').click();
    cy.get('mat-option').contains('Hélène THIERCELIN').click();
    cy.get('textarea[formControlName=description]').type('Description test');

    // Soumet le formulaire
    cy.get('button').contains('Save').click();
    cy.wait('@createSession');

    // Vérifie que l'utilisateur est redirigé vers la page des sessions
    cy.url().should('include', '/sessions');
    cy.wait('@getSessionsUpdated');

    // Vérifie la notification de succès
    cy.contains('Session created !').should('be.visible');

    // Vérifie que la session créée est affichée dans la liste
    cy.contains('Test Session').should('be.visible');
  });
});
