(function (jQuery) {

    /*------------------------------------------------------------------
    Login With Facebook
    * -----------------------------------------------------------------*/

    window.fbAsyncInit = function () {
        FB.init({
            appId: '547966652744310',
            cookie: true,
            xfbml: true,
            version: 'v6.0'
        });

        function loginFB() {
            FB.login(function (response) {
                if (response.status === 'unknown') return;
                let {
                    authResponse
                } = response, {
                    accessToken
                } = authResponse;
                $.ajax({
                    url: '/login/facebook',
                    type: 'POST',
                    dataType: 'json',
                    data: {
                        'accessToken': accessToken
                    },
                    beforeSend: function () {
                        $('#loader').show();
                    },
                }).done(({
                             idUser,
                             redirect
                         }) => {
                    window.localStorage.setItem("idUser", idUser);
                    window.location.href = redirect;
                }).fail(jqXHR => {
                    let e = window.location.pathname === "/login" ? $('#form-sign-in') : $('#form-sign-up');
                    $('.alert').remove();
                    $('#loader').hide();
                    let response = JSON.parse(jqXHR.responseText);
                    e.before(`<div class="alert alert-danger" role="alert">${response.message}</div>`);
                })
            }, {
                scope: 'public_profile,email'
            });
        }

        jQuery(document).on('click', '#button-login-facebook', loginFB);
    };

    (function (d, s, id) {
        let js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id)) {
            return;
        }
        js = d.createElement(s);
        js.id = id;
        js.src = "https://connect.facebook.net/en_US/sdk.js";
        fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));


    /*------------------------------------------------------------------
    Login With Google
    * -----------------------------------------------------------------*/

    let auth2;

    function startInitGoogleAPI() {
        gapi.load('auth2', function () {
            // Retrieve the singleton for the GoogleAuth library and set up the client.
            auth2 = gapi.auth2.init({
                client_id: '661810666435-1iqtsob5etcmben2ieu6uqaspob17fpa.apps.googleusercontent.com',
                cookiepolicy: 'single_host_origin',
                scope: 'profile email'
            });
            attachSignin(document.getElementById('button-login-google'));
        });
    }

    function attachSignin(element) {
        auth2.attachClickHandler(element, {},
            function (googleUser) {
                let idToken = googleUser.getAuthResponse().id_token;
                $.ajax({
                    url: '/login/google',
                    type: 'POST',
                    dataType: 'json',
                    data: {
                        "idToken": idToken
                    },
                    beforeSend: function () {
                        $('#loader').show();
                    },
                }).done(({
                             redirect
                         }) => {
                    window.location.href = redirect;
                }).fail(jqXHR => {
                    let e = window.location.pathname === "/login" ? $('#form-sign-in') : $('#form-sign-up');
                    $('.alert').remove();
                    $('#loader').hide();
                    let response = JSON.parse(jqXHR.responseText);
                    e.before(`<div class="alert alert-danger" role="alert">${response.message}</div>`);
                })
            });
    }

    startInitGoogleAPI();

})(jQuery);