describe('Login Tests', () => {
  // Test 1 : Connexion réussie
  describe('Login - Success', () => {
    beforeEach(() => {
      cy.intercept('POST', '/api/auth/login', {
        statusCode: 200,
        body: {
          id: 1,
          username: 'userName',
          token: 'fakeToken123',
        },
      }).as('loginRequest');

      cy.intercept('GET', '/api/session', {
        statusCode: 200,
        body: [
          { id: 1, name: 'Morning Yoga', date: '2023-01-01' },
        ],
      }).as('sessionList');

      cy.visit('/login');
    });

    it('should login successfully with valid credentials', () => {
      // Remplit les champs
      cy.get('input[formControlName=email]').type('user@test.com');
      cy.get('input[formControlName=password]').type('password123');

      // Soumet le formulaire
      cy.get('button[type=submit]').click();

      // Vérifie que l'appel POST est intercepté
      cy.wait('@loginRequest');

      // Vérifie la redirection
      cy.url().should('include', '/sessions');

      // Vérifie que les données de session sont visibles
      cy.wait('@sessionList');
      cy.contains('Morning Yoga').should('be.visible');
    });
  });

  // Test 2 : Identifiants invalides
  describe('Login - Invalid Credentials', () => {
    beforeEach(() => {
      cy.intercept('POST', '/api/auth/login', {
        statusCode: 401,
        body: { message: 'Invalid login or password' },
      }).as('loginRequest');

      cy.visit('/login');
    });

    it('should show an error message for invalid credentials', () => {
      // Remplit les champs avec des identifiants invalides
      cy.get('input[formControlName=email]').type('wrong@test.com');
      cy.get('input[formControlName=password]').type('wrongpassword');

      // Soumet le formulaire
      cy.get('button[type=submit]').click();

      // Vérifie que l'appel POST est intercepté
      cy.wait('@loginRequest');

      // Vérifie l'affichage du message d'erreur
      cy.contains('Invalid login or password').should('be.visible');
    });
  });

  // Test 3 : Champs manquants
  describe('Login - Missing Fields', () => {
    beforeEach(() => {
      cy.visit('/login');
    });

    it('should show an error message for missing fields', () => {
      // Force le clic sur le bouton pour marquer le formulaire comme "touché"
      cy.get('button[type=submit]').click({ force: true });

      // Simule l'interaction avec les champs pour les marquer comme "touchés"
      cy.get('input[formControlName=email]').focus().blur();
      cy.get('input[formControlName=password]').focus().blur();

      // Vérifie que le message d'erreur apparaît
      cy.contains('All fields are required').should('be.visible');
    });
  });

  // Test 4 : Bouton désactivé
  describe('Login - Disabled Submit Button', () => {
    beforeEach(() => {
      cy.visit('/login');
    });

    it('should disable the submit button when the form is invalid', () => {
      // Vérifie que le bouton est désactivé au chargement de la page
      cy.get('button[type=submit]').should('be.disabled');

      // Remplit seulement le champ email
      cy.get('input[formControlName=email]').type('user@test.com');
      cy.get('button[type=submit]').should('be.disabled');

      // Remplit tous les champs correctement
      cy.get('input[formControlName=password]').type('password123');
      cy.get('button[type=submit]').should('not.be.disabled');
    });
  });
});
