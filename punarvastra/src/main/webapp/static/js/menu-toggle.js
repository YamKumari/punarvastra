(function () {
    var btn = document.getElementById("navToggle");
    var panel = document.getElementById("navPanel");
    var userBtn = document.getElementById("userMenuBtn");
    var userPanel = document.getElementById("userMenuPanel");
    if (btn && panel) {
        btn.addEventListener("click", function () {
            var open = panel.classList.toggle("is-open");
            btn.setAttribute("aria-expanded", open ? "true" : "false");
        });
    }
    if (userBtn && userPanel) {
        userBtn.addEventListener("click", function () {
            var hidden = userPanel.hasAttribute("hidden");
            if (hidden) {
                userPanel.removeAttribute("hidden");
                userBtn.setAttribute("aria-expanded", "true");
            } else {
                userPanel.setAttribute("hidden", "hidden");
                userBtn.setAttribute("aria-expanded", "false");
            }
        });
    }
})();
