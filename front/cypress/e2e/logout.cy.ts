describe('Logout Tests', () => {
  beforeEach(() => {
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

  it('should log out successfully', () => {
    // Clique sur le bouton "Logout"
    cy.get('span').contains('Logout').click();

    // Vérifie que l'utilisateur est redirigé vers la page d'accueil
    cy.url().should('include', '/');

    // Vérifie que les boutons "Login" et "Register" sont visibles
    cy.contains('Login').should('be.visible');
    cy.contains('Register').should('be.visible');

    // Vérifie que le bouton "Logout" n'est plus présent
    cy.contains('Logout').should('not.exist');
  });
});
