// scripts.js

// Get references to buttons and sections
const emailToggle = document.getElementById('emailToggle');
const phoneToggle = document.getElementById('phoneToggle');
const emailSection = document.getElementById('emailSection');
const phoneSection = document.getElementById('phoneSection');

// Add event listeners for toggling sections
emailToggle.addEventListener('click', () => {
  emailSection.classList.add('active');
  phoneSection.classList.remove('active');
  emailToggle.classList.add('active');
  phoneToggle.classList.remove('active');
});

phoneToggle.addEventListener('click', () => {
  phoneSection.classList.add('active');
  emailSection.classList.remove('active');
  phoneToggle.classList.add('active');
  emailToggle.classList.remove('active');
});
