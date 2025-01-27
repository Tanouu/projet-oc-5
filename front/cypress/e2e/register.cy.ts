describe('Register Tests', () => {
  beforeEach(() => {
    cy.visit('/register');
  });

  // Test 1 : Inscription réussie
  it('should register successfully with valid data', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 201,
    }).as('registerRequest');

    // Remplit les champs
    cy.get('input[formControlName=firstName]').type('John');
    cy.get('input[formControlName=lastName]').type('Doe');
    cy.get('input[formControlName=email]').type('johndoe@test.com');
    cy.get('input[formControlName=password]').type('password123');

    // Soumet le formulaire
    cy.get('button[type=submit]').click();

    // Vérifie que l'appel POST est intercepté
    cy.wait('@registerRequest');

    // Vérifie la redirection
    cy.url().should('include', '/login');
  });

  // Test 2 : Email déjà utilisé
  it('should show an error message when the email is already taken', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 409,
      body: { message: 'Email already taken' },
    }).as('registerRequest');

    // Remplit les champs
    cy.get('input[formControlName=firstName]').type('Jane');
    cy.get('input[formControlName=lastName]').type('Smith');
    cy.get('input[formControlName=email]').type('existing@test.com');
    cy.get('input[formControlName=password]').type('password123');

    // Soumet le formulaire
    cy.get('button[type=submit]').click();

    // Vérifie que l'appel POST est intercepté
    cy.wait('@registerRequest');

    // Vérifie l'affichage du message d'erreur
    cy.contains('Registration failed. Please try again.').should('be.visible');
  });

  // Test 3 : Champs manquants ou invalides
  it('should show an error message for missing or invalid fields', () => {
    // Soumet le formulaire sans remplir les champs
    cy.get('button[type=submit]').click({ force: true });

    // Simule l'interaction avec les champs pour les marquer comme "touchés"
    cy.get('input[formControlName=firstName]').focus().blur();
    cy.get('input[formControlName=lastName]').focus().blur();
    cy.get('input[formControlName=email]').focus().blur();
    cy.get('input[formControlName=password]').focus().blur();

    // Vérifie que le message d'erreur apparaît
    cy.contains('All fields are required.').should('be.visible');
  });

  // Test 4 : Bouton désactivé
  it('should disable the submit button when the form is invalid', () => {
    // Vérifie que le bouton est désactivé au chargement de la page
    cy.get('button[type=submit]').should('be.disabled');

    // Remplit seulement le champ "First Name"
    cy.get('input[formControlName=firstName]').type('John');
    cy.get('button[type=submit]').should('be.disabled');

    // Remplit tous les champs correctement
    cy.get('input[formControlName=lastName]').type('Doe');
    cy.get('input[formControlName=email]').type('johndoe@test.com');
    cy.get('input[formControlName=password]').type('password123');
    cy.get('button[type=submit]').should('not.be.disabled');
  });
});
