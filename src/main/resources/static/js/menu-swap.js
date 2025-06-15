// Get buttons
const buttons = ["menu-user-stats","menu-global-stats","menu-configuration"];
// Get pages
const pages = ["main-section-user-stats","main-section-global-stats","main-section-configuration"];
// Swapper
function swapper(buttonId) {
    for (let i = 0; i < buttons.length; i++) {
        const label = document.querySelector(`label[for="${buttons[i]}"]`);
        const page = document.getElementById(pages[i]);
        if (buttons[i] === buttonId) {
            label.style.backgroundColor = "rgba(0,0,0,0.1)";
            page.style.display = "grid";
        } else {
            label.style.backgroundColor = "white";
            page.style.display = "none";
        }
    }
}