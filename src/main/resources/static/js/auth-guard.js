(function () {
    var originalFetch = window.fetch ? window.fetch.bind(window) : null;
    if (!originalFetch) {
        return;
    }

    var isAuthenticated = document.body && document.body.dataset
        ? document.body.dataset.authenticated === "true"
        : false;

    var toastVisible = false;
    var lastToastAt = 0;
    var hideTimer = null;
    var protectedActionSelector = [
        ".quick-link",
        ".save-button",
        ".delete-button",
        ".btn-delete",
        ".edit-button",
        ".create-button",
        "[data-requires-auth='true']"
    ].join(",");

    function getRequestUrl(input) {
        if (typeof input === "string") {
            return input;
        }

        if (input && typeof input.url === "string") {
            return input.url;
        }

        return "";
    }

    function buildToast() {
        var toast = document.createElement("div");
        toast.className = "auth-toast";

        var title = document.createElement("strong");
        title.className = "auth-toast-title";
        title.textContent = "Connexion requise";

        var text = document.createElement("p");
        text.className = "auth-toast-text";
        text.textContent = "Vous devez vous connecter pour utiliser cette action.";

        var actions = document.createElement("div");
        actions.className = "auth-toast-actions";

        var laterBtn = document.createElement("button");
        laterBtn.type = "button";
        laterBtn.className = "auth-toast-later";
        laterBtn.textContent = "Plus tard";

        var loginBtn = document.createElement("button");
        loginBtn.type = "button";
        loginBtn.className = "auth-toast-login";
        loginBtn.textContent = "Se connecter";

        actions.appendChild(laterBtn);
        actions.appendChild(loginBtn);

        toast.appendChild(title);
        toast.appendChild(text);
        toast.appendChild(actions);

        return {
            toast: toast,
            laterBtn: laterBtn,
            loginBtn: loginBtn
        };
    }

    function showAuthToast() {
        var now = Date.now();
        if (toastVisible || now - lastToastAt < 1200) {
            return;
        }

        toastVisible = true;
        lastToastAt = now;

        var toastParts = buildToast();
        var toast = toastParts.toast;

        function closeToast() {
            if (hideTimer) {
                clearTimeout(hideTimer);
                hideTimer = null;
            }
            if (toast.parentNode) {
                toast.parentNode.removeChild(toast);
            }
            toastVisible = false;
        }

        toastParts.laterBtn.addEventListener("click", closeToast);
        toastParts.loginBtn.addEventListener("click", function () {
            window.location.href = "/login";
        });

        document.body.appendChild(toast);

        hideTimer = setTimeout(closeToast, 5000);
    }

    function shouldBlockActionClick(target) {
        if (isAuthenticated) {
            return false;
        }

        if (!target || typeof target.closest !== "function") {
            return false;
        }

        return !!target.closest(protectedActionSelector);
    }

    document.addEventListener("click", function (event) {
        if (!shouldBlockActionClick(event.target)) {
            return;
        }

        event.preventDefault();
        event.stopPropagation();
        showAuthToast();
    }, true);

    window.fetch = function () {
        var args = arguments;
        return originalFetch.apply(null, args).then(function (response) {
            var url = getRequestUrl(args[0]);
            if (response.status === 401 && url.indexOf("/api/") !== -1) {
                showAuthToast();
            }
            return response;
        });
    };
})();
