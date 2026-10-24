// Asks for confirmation before submitting destructive forms (e.g. delete).
// Kept in an external file because the Content-Security-Policy blocks inline scripts.
document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.js-confirm').forEach(function (button) {
        button.addEventListener('click', function (event) {
            if (!window.confirm(button.dataset.confirm || 'Are you sure?')) {
                event.preventDefault();
            }
        });
    });
});
