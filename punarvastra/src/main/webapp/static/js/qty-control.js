(function () {
    document.querySelectorAll(".qty-input").forEach(function (el) {
        el.addEventListener("change", function () {
            var v = parseInt(el.value, 10);
            var max = parseInt(el.getAttribute("max"), 10);
            var min = parseInt(el.getAttribute("min"), 10) || 1;
            if (isNaN(v) || v < min) el.value = min;
            if (!isNaN(max) && v > max) el.value = max;
        });
    });
})();
