it('Check if coverage is being collected', () => {
  cy.window().then((win) => {
    const coverage = (win as unknown as { __coverage__: any }).__coverage__;
    console.log('Coverage object:', coverage);
  });
});
