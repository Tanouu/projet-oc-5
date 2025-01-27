describe('Sessions Tests', () => {
  beforeEach(() => {
    // Étape 1 : Simule la connexion via le formulaire de login
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
        { id: 1, name: 'Morning Yoga', date: '2023-01-01', description: 'Start your day right!' },
        { id: 2, name: 'Evening Yoga', date: '2023-01-02', description: 'Relax before bed.' },
      ],
    }).as('getSessions');

    // Visite la page de login
    cy.visit('/login');

    // Remplit les champs pour se connecter
    cy.get('input[formControlName=email]').type('user@test.com');
    cy.get('input[formControlName=password]').type('password123');

    // Clique sur le bouton de connexion
    cy.get('button[type=submit]').click();

    // Vérifie que l'utilisateur est redirigé vers la page des sessions
    cy.wait('@loginRequest');
    cy.url().should('include', '/sessions');
  });

  it('should display the list of sessions', () => {
    // Attend que l'API des sessions soit appelée
    cy.wait('@getSessions');

    // Vérifie que les sessions sont affichées
    cy.contains('Morning Yoga').should('be.visible');
    cy.contains('Evening Yoga').should('be.visible');
  });

  it('should display the "Create" button for admin users', () => {
    // Étape 2 : Simule une connexion admin via le formulaire de login
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 2,
        username: 'adminName',
        token: 'adminFakeToken123',
        admin: true,
      },
    }).as('adminLoginRequest');

    // Recharge la page de login pour l'admin
    cy.visit('/login');

    // Remplit les champs pour se connecter en tant qu'admin
    cy.get('input[formControlName=email]').type('admin@test.com');
    cy.get('input[formControlName=password]').type('adminPassword123');

    // Clique sur le bouton de connexion
    cy.get('button[type=submit]').click();

    // Vérifie que l'utilisateur est redirigé vers la page des sessions
    cy.wait('@adminLoginRequest');
    cy.url().should('include', '/sessions');

    // Attend que l'API des sessions soit appelée
    cy.wait('@getSessions');

    // Vérifie que le bouton "Create" est visible pour l'admin
    cy.get('button').contains('Create').should('be.visible');
  });

  it('should navigate to session detail when clicking "Detail"', () => {
    // Mock pour les détails d'une session
    cy.intercept('GET', '/api/session/1', {
      statusCode: 200,
      body: {
        id: 1,
        name: 'Morning Yoga',
        description: 'Start your day right!',
        date: '2023-01-01',
        teacher_id: 101,
        users: [1, 2],
        createdAt: '2023-01-01T08:00:00Z',
        updatedAt: '2023-01-02T08:00:00Z',
      },
    }).as('getSessionDetail');

    // Attend que l'API des sessions soit appelée
    cy.wait('@getSessions');

    // Clique sur le bouton "Detail" pour la première session
    cy.get('button').contains('Detail').click();

    // Vérifie que la redirection fonctionne
    cy.url().should('include', '/sessions/detail/1');
    cy.wait('@getSessionDetail');

    // Vérifie que les détails de la session sont affichés
    cy.contains('Morning Yoga').should('be.visible');
    cy.contains('Start your day right!').should('be.visible');
    cy.contains('Create at:').should('be.visible');
    cy.contains('Last update:').should('be.visible');
  });

  it('should display the "Edit" button for admin users', () => {
    // Étape 2 : Simule une connexion admin via le formulaire de login
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 2,
        username: 'adminName',
        token: 'adminFakeToken123',
        admin: true,
      },
    }).as('adminLoginRequest');

    // Recharge la page de login pour l'admin
    cy.visit('/login');

    // Remplit les champs pour se connecter en tant qu'admin
    cy.get('input[formControlName=email]').type('admin@test.com');
    cy.get('input[formControlName=password]').type('adminPassword123');

    // Clique sur le bouton de connexion
    cy.get('button[type=submit]').click();

    // Vérifie que l'utilisateur est redirigé vers la page des sessions
    cy.wait('@adminLoginRequest');
    cy.url().should('include', '/sessions');

    // Attend que l'API des sessions soit appelée
    cy.wait('@getSessions');

    // Vérifie que le bouton "Edit" est visible pour l'admin
    cy.get('button').contains('Edit').should('be.visible');
  });
});
