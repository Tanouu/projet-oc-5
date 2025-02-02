import { defineConfig } from 'cypress'

export default defineConfig({
  e2e: {
    setupNodeEvents(on, config) {
      console.log('✅ Cypress Code Coverage Plugin Loaded');
      require('@cypress/code-coverage/task')(on, config);
      return config;
    },
  },
});
