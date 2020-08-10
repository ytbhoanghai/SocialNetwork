(function (jQuery) {

    "use strict";

    let inputPassword = $('#input-password'),
        inputConfirmPassword = $('#input-confirm-password'),
        labelInvalidConfirmPassword = $('#invalid-password-confirmation');

    let buttonGetVerificationCode = $('#button-get-verification-code'),
        inputEmail = $('#input-email');

    let form = $('#form-change-password');

    $('input.pass').on('keyup', function (event) {
        event.preventDefault();
        let npass = inputPassword.val(),
            cpass = inputConfirmPassword.val();

        if (npass !== '' && cpass !== '') {
            if (npass !== cpass) {
                labelInvalidConfirmPassword.show();
                inputConfirmPassword.addClass('is-invalid');
                return;
            }
        }

        labelInvalidConfirmPassword.hide();
        inputConfirmPassword.removeClass('is-invalid');
    });

    Array.prototype.filter.call(form, function (form) {
        form.addEventListener('submit', function (event) {
            event.preventDefault();
            event.stopPropagation();

            if (form.checkValidity() === true) {
                let npass = inputPassword.val(),
                    cpass = inputConfirmPassword.val(),
                    code = $('#input-code').val();

                if (npass === cpass) {
                    $.ajax({
                        url: `/setting/password?email=${inputEmail.val()}`,
                        type: 'POST',
                        contentType: 'application/json',
                        dataType: 'json',
                        data: JSON.stringify({
                            newPassword: npass,
                            verificationCode: code
                        })
                    }).done(function ({
                                          message,
                                          urlLogoutAllDevices
                                      }) {
                        let customMessage = `${message}. Would you like to log out of all devices?`;
                        showModalChangePasswordSuccess(customMessage, urlLogoutAllDevices);
                    }).fail(jqXHR => {
                        let {
                            message
                        } = JSON.parse(jqXHR.responseText);
                        showModalNotificationError(message);
                    })
                }
            }
            $(form).addClass('was-validated');
        }, false)
    }, false);

    buttonGetVerificationCode.on('click', function (event) {
        event.preventDefault();
        if (inputEmail.val() === '') {
            showModalNotificationError('Please enter your email');
        } else if (!inputEmail.val().match(/^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$/)) {
            showModalNotificationError('The email address provided is not a valid');
        } else {
            $.ajax({
                url: `/email/change-password?email=${inputEmail.val()}`,
                type: 'GET',
                dataType: 'text'
            }).done(function (response) {
                showModalNotificationError(response);
            }).fail(jqXHR => {
                let {
                    message
                } = JSON.parse(jqXHR.responseText);
                showModalNotificationError(message);
            })
        }
    });


    function showModalChangePasswordSuccess(message, urlRedirect) {
        let modal = $('#modal-notification-change-password-success');
        modal.find('p').text(message);
        modal.find('a').last().attr('href', `/${urlRedirect}`);
        modal.modal('show');
    }

    function showModalNotificationError(message) {
        let modalNotification = $('#modal-notification');
        modalNotification.find('.modal-body > p').text(message);
        modalNotification.modal('show');
    }

}());