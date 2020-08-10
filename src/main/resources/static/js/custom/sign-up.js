(function(jQuery) {

    "use strict";

    let formSignUp              = $('#form-sign-up');
    let inputFullName           = $('#exampleInputFullName1');
    let inputEmail              = $('#exampleInputEmail2');
    let inputPassword           = $('#exampleInputPassword1');
    let checkboxCustomCheck1    = $('#customCheck1');

    let validation = Array.prototype.filter.call(formSignUp, function(formSignUp) {
        formSignUp.addEventListener('submit', function(event) {
            event.preventDefault();
            event.stopPropagation();
            $(formSignUp).parent().find('.alert').remove();

            if (formSignUp.checkValidity() === true) {
                let fullName = inputFullName.val();
                let firstName = fullName.substring(fullName.lastIndexOf(' ') + 1);
                let lastName = fullName.substring(0, fullName.indexOf(' '));
                let email = inputEmail.val();
                let password = inputPassword.val();
                let acceptTermsAndConditions = checkboxCustomCheck1.val() === "on";

                signUp({firstName, lastName, email, password, acceptTermsAndConditions});
            }

            formSignUp.classList.add('was-validated');
        }, false);
    }, false);

    let signUp = signUpForm => {
        $.ajax({
            url: '/sign-up',
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(signUpForm),
            beforeSend: function() {
                $('#loader').show();
            },
            complete: function () {
                $('#loader').hide();
            }
        }).done(({id, redirect}) => {
            formSignUp.before(`<div class="alert alert-success" role="alert">sign-up success, click to <a href="${redirect}">\xa0 login page</a></div>`);
        }).fail((jqXHR) => {
            $('#loader').hide();
            let response = JSON.parse(jqXHR.responseText);
            console.log(response);
            formSignUp.before(`<div class="alert alert-danger" role="alert">${response.message}</div>`);
        })
    };

})(jQuery);
