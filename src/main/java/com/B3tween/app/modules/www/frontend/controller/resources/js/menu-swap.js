// Get buttons
const buttons = ["menu-user-stats","menu-global-stats","menu-configuration"];
// Get pages
const pages = ["main-section-user-stats", "main-section-global-stats", "main-section-configuration"];
// Swapper
function swapper(buttonId, pageToSwap) {
    // Get button pressed
    const pressedButton = document.getElementById(buttonId);
    // Get page selected
    const presentPage = document.getElementById(pageToSwap);
    // Loop 
    buttons.forEach(button => {
        pages.forEach(page => {
            // Get actual button
            const actualButton = document.getElementById(button);
            const actualPage = document.getElementById(page)
            if (actualButton === pressedButton && actualPage === presentPage) {
                presentPage.style.display="inline";
            } else {
                actualPage.style.display="none";
            }
        })
    });
}