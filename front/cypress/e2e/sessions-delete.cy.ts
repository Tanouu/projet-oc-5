describe('Session Delete Tests', () => {
  beforeEach(() => {
    // Mock initial pour les sessions
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [
        { id: 1, name: 'Morning Yoga', date: '2025-01-24', description: 'Start your day right!' },
      ],
    }).as('getSessions');

    // Mock pour les détails de la session
    cy.intercept('GET', '/api/session/1', {
      statusCode: 200,
      body: {
        id: 1,
        name: 'Morning Yoga',
        description: 'Start your day right!',
        date: '2025-01-24',
        teacher_id: 101,
        users: [1, 2],
        createdAt: '2025-01-01T08:00:00Z',
        updatedAt: '2025-01-02T08:00:00Z',
      },
    }).as('getSessionDetail');

    // Mock pour la suppression de la session
    cy.intercept('DELETE', '/api/session/1', {
      statusCode: 200,
      body: {},
    }).as('deleteSession');

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

  it('should delete a session successfully', () => {
    // Navigue vers les détails de la session
    cy.get('button').contains('Detail').click();
    cy.wait('@getSessionDetail');

    // Vérifie que les détails de la session sont affichés
    cy.contains('Morning Yoga').should('be.visible');
    cy.contains('Start your day right!').should('be.visible');

    // Met à jour le mock des sessions après suppression
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [], // Plus aucune session disponible
    }).as('getSessionsAfterDelete');

    // Clique sur le bouton "Delete"
    cy.get('button').contains('Delete').click();

    // Vérifie que la suppression est appelée
    cy.wait('@deleteSession');

    // Vérifie que l'utilisateur est redirigé vers la page des sessions
    cy.url().should('include', '/sessions');

    // Vérifie que la notification "Session deleted !" apparaît
    cy.contains('Session deleted !').should('be.visible');

    // Vérifie que la session supprimée n'est plus affichée
    cy.wait('@getSessionsAfterDelete');
    cy.contains('Morning Yoga').should('not.exist');
  });
});
