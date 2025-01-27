describe('User Information Tests', () => {
  beforeEach(() => {
    // Mock pour les informations de session
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        firstName: 'Admin',
        lastName: 'ADMIN',
        email: 'yoga@studio.com',
        admin: true,
        createdAt: '2025-01-24T08:00:00Z',
        updatedAt: '2025-01-24T08:00:00Z',
      },
    }).as('adminLogin');

    // Mock pour l'utilisateur connecté
    cy.intercept('GET', '/api/user/1', {
      statusCode: 200,
      body: {
        id: 1,
        firstName: 'Admin',
        lastName: 'ADMIN',
        email: 'yoga@studio.com',
        admin: true,
        createdAt: '2025-01-24T08:00:00Z',
        updatedAt: '2025-01-24T08:00:00Z',
      },
    }).as('getUserInfo');

    // Connexion
    cy.visit('/login');
    cy.get('input[formControlName=email]').type('admin@test.com');
    cy.get('input[formControlName=password]').type('adminPassword123');
    cy.get('button[type=submit]').click();

    // Attendez la redirection vers la page des sessions
    cy.wait('@adminLogin');
    cy.url().should('include', '/sessions');
  });

  it('should display user information correctly', () => {
    // Accéder à la page "Account"
    cy.get('span').contains('Account').click();
    cy.url().should('include', '/me');
    cy.wait('@getUserInfo');

    // Vérifier que les informations de l'utilisateur sont affichées
    cy.contains('Name: Admin ADMIN').should('be.visible');
    cy.contains('Email: yoga@studio.com').should('be.visible');
    cy.contains('You are admin').should('be.visible');
    cy.contains('Create at: January 24, 2025').should('be.visible');
    cy.contains('Last update: January 24, 2025').should('be.visible');
  });
});
