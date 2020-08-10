(function ($) {
    let formSignIn          = $('#form-sign-in');
    let inputEmail          = $('#exampleInputEmail1');
    let inputPassword       = $('#exampleInputPassword1');

    let validation = Array.prototype.filter.call(formSignIn, function(formSignIn) {
        formSignIn.addEventListener('submit', function(event) {
            event.preventDefault();
            event.stopPropagation();

            $('.alert').remove();

            if (formSignIn.checkValidity() === true) {
                $.ajax({
                    url: '/login',
                    type: 'POST',
                    dataType: 'json',
                    data: {
                        "email": inputEmail.val(),
                        "password": inputPassword.val(),
                    }
                }).done(({idUser, redirect}) => {
                    window.localStorage.setItem("idUser", idUser);
                    window.location.href = redirect;
                }).fail(jqXHR => {
                    let response = JSON.parse(jqXHR.responseText);
                    $(formSignIn).before(`<div class="alert alert-danger" role="alert">${response.message}</div>`);
                    inputPassword.val('');
                })
            }

            formSignIn.classList.add('was-validated');
        }, false)
    }, false);

})(jQuery);