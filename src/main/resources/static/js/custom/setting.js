(function (jQuery) {

    activeTabPage('ytb-page-setting');

    function activeTabPage(name) {
        $('.ytb-page-tab').removeClass('active');
        $(`#${name}`).addClass('active');
    }

    let selectLanguages = $('#select-languages'),
        selectCountry = $('#select-country'),
        showLanguages = $('#show-languages'),
        inputBirthDay = $('#input-birth-day'),
        inputFirstName = $('#input-first-name'),
        inputLastName = $('#input-last-name'),
        radioGenderMale = $('#radio-gender-male'),
        radioGenderFemale = $('#radio-gender-female'),
        inputAddress = $('#input-address'),
        inputFavoriteQuotes = $('#input-favorite-quotes'),
        inputOtherName = $('#input-other-name'),
        inputAboutMe = $('#input-about-me'),
        formPersonalInformation = $('#form-personal-information'),
        buttonReset1 = $('#button-reset1');

    let inputNewPassword = $('#input-new-password'),
        inputVerifyPassword = $('#input-verify-password'),
        formChangePassword = $('#form-change-password'),
        inputVerificationCode = $('#input-verification-code'),
        buttonGetVerificationCode = $('#button-get-verification-code');

    let formUpdateInformationContact = $('#form-update-information-contact'),
        buttonResetInformationContact = $('#button-reset-info-contact'),
        tagsA = $('a[class="link-social"]');


    function preparingData() {
        return new Promise(resolve => {
            $.getJSON('data/language', function (response) {
                response.forEach(({
                                      id,
                                      name
                                  }) => {
                    $(`<option value="${id}">${name}</option>`).appendTo(selectLanguages);
                });
                selectLanguages.selectric().on('change', function () {
                    let id = $(this).val(),
                        name = $($(this).parents(':eq(1)').get(0)).find('.selectric > .label').text();
                    drawLanguage2({
                        id,
                        name
                    });
                });
            });
            $.getJSON('data/country', function (response) {
                response.forEach(({
                                      id,
                                      name
                                  }) => {
                    $(`<option value="${id}">${name}</option>`).appendTo(selectCountry);
                });
                selectCountry.selectric();
            });
            resolve();
        })
    }

    preparingData().then(function () {
        $.getJSON('me/info-user', function (infoUser) {
            showIdUser(infoUser);
            showPersonalInformation(infoUser);
            showManageContact(infoUser);
        });

        getHistory(0, 20).then(_ => {
            onEventScrollHistory();
        });
    });

    // draw
    function drawLanguage2({
                               id,
                               name
                           }) {
        let updateNumber = () => {
            let _currNumElement = showLanguages.children(),
                _maxElement = showLanguages.attr('data-max');
            showLanguages.prev().text(`Languages (${_currNumElement.length} / ${_maxElement})`);
        };

        // update counting
        let currNumElement = showLanguages.children(),
            maxElement = showLanguages.attr('data-max');
        if (currNumElement.length + 1 <= +maxElement && showLanguages.find(`span[data-idLanguage="${id}"]`).length === 0 && +id !== 0) {
            let html = $(`<span class="badge mr-2 my-auto" style="padding-left: 0; line-height: 1.5em;" data-idLanguage="${id}"><h4 style="padding: 2px; background: #f5fbff"><span>${name}</span><button aria-label="Remove" tabindex="-1" type="button" class="close ml-2" style="font-size: 1em; line-height: 0.9em; float: none;"><span aria-hidden="true">×</span></button> </h4></span>`);
            showLanguages.append(html);
            html.find('button').on('click', function (event) {
                event.preventDefault();
                html.remove();
                updateNumber();
            });
            // counting number
            updateNumber();
        }
    }

    // show info
    function showIdUser(infoUser) {
        let {
                id
            } = infoUser,
            shortenId = id.substr(0, 3) + '...' + id.substr(id.length - 5);
        $('#user-id').find('span').text('UserId: ' + shortenId);
        $('#button-copy-id').on('click', function (event) {
            event.preventDefault();
            navigator.clipboard.writeText(id).then(function () {
                console.log('Copying id to clipboard was successful!');
                showModalNotificationError('Copying id to clipboard was successful!')
            });
        })
    }

    function showPersonalInformation(infoUser) {
        let {
            firstName,
            lastName,
            birthDay,
            gender,
            country,
            address,
            languages,
            favoriteQuotes,
            otherName,
            aboutMe
        } = infoUser;
        // name
        inputFirstName.val(firstName);
        inputLastName.val(lastName);
        // birthday
        inputBirthDay.datepicker('setDate', new Date(birthDay));
        inputBirthDay.datepicker('option', 'maxDate', new Date());
        // gender
        gender ? radioGenderMale.attr('checked', 'checked') :
            radioGenderFemale.attr('checked', 'checked');
        // country
        if (country) {
            let {
                id,
                name
            } = country;
            let index = selectCountry.find(`option[value="${id}"]`).index();
            selectCountry.prop('selectedIndex', index).selectric('refresh');
        }
        // address
        inputAddress.val(address);
        // languages
        for (let key in languages) {
            if (!languages.hasOwnProperty(key)) {
                continue;
            }
            drawLanguage2(languages[key]);
        }
        // rest
        inputFavoriteQuotes.val(favoriteQuotes);
        inputOtherName.val(otherName);
        inputAboutMe.val(aboutMe);
    }

    function showManageContact(infoUser) {
        // clear
        tagsA.attr('href', '');

        let {
            socialLinks,
            mobile,
            email,
            website
        } = infoUser;
        for (let key in socialLinks) {
            if (!socialLinks.hasOwnProperty(key)) {
                continue;
            }
            $(`a[data-social="${key}"]`).attr('href', socialLinks[key]);
        }
        formUpdateInformationContact.find('#email').val(email);
        formUpdateInformationContact.find('#cno').val(mobile);
        formUpdateInformationContact.find('#url-website').val(website);
    }

    // event click
    buttonReset1.on('click', function (event) {
        event.preventDefault();
        $.getJSON('me/info-user', function (infoUser) {
            showPersonalInformation(infoUser);
        })
    });
    buttonGetVerificationCode.on('click', function (event) {
        event.preventDefault();
        $.ajax({
            url: 'email/change-password',
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
    });
    tagsA.each((i, v) => {
        $(v).on('click', function (event) {
            event.preventDefault();
            showModalInputSocialLink.call(v);
        })
    });
    buttonResetInformationContact.on('click', function (event) {
        event.preventDefault();
        $.getJSON('me/info-user', function (infoUser) {
            showManageContact(infoUser);
        })
    });

    // event submit
    formPersonalInformation.on('submit', function (event) {
        event.preventDefault();
        let obj = getDateFromPersonalInfoForm();
        showModalAcceptAction(function () {
            $.ajax({
                url: 'setting/personal-information',
                type: 'POST',
                contentType: 'application/json',
                dataType: 'text',
                data: JSON.stringify(obj)
            }).done(response => {
                $('.my-firstName').text(obj.firstName);
                showModalNotificationError(response);
            }).fail(jqXHR => {
                let {
                    message
                } = JSON.parse(jqXHR.responseText);
                showModalNotificationError(message);
            })
        })
    });
    formChangePassword.on('submit', function (event) {
        event.preventDefault();
        let newPassword = inputNewPassword.val(),
            confirmPassword = inputVerifyPassword.val(),
            verificationCode = inputVerificationCode.val();

        if (newPassword && verificationCode) {
            if (newPassword === confirmPassword) {
                $.ajax({
                    url: 'setting/password',
                    type: 'POST',
                    contentType: 'application/json',
                    dataType: 'json',
                    data: JSON.stringify({
                        newPassword,
                        verificationCode
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


    });
    formUpdateInformationContact.on('submit', function (event) {
        event.preventDefault();
        if (formUpdateInformationContact.get(0).checkValidity()) {
            let socialLinks = {},
                mobile = $('#cno').val(),
                website = $('#url-website').val();
            tagsA.each(function () {
                socialLinks[$(this).attr('data-social')] = $(this).attr('href');
            });

            $.ajax({
                url: 'setting/contact-information',
                type: 'POST',
                contentType: 'application/json',
                dataType: 'text',
                data: JSON.stringify({
                    socialLinks,
                    mobile,
                    website
                })
            }).done(function (response) {
                showModalNotificationError(response);
            }).fail(jqXHR => {
                let {
                    message
                } = JSON.parse(jqXHR.responseText);
                showModalNotificationError(message);
            });
        }
    });

    // event input
    inputAddress.on('input', function (event) {
        event.preventDefault();
        countingNumberCharacterInput(inputAddress, 'Address');
    });
    inputFavoriteQuotes.on('input', function (event) {
        event.preventDefault();
        countingNumberCharacterInput(inputFavoriteQuotes, 'Favorite Quotes');
    });
    inputOtherName.on('input', function (event) {
        event.preventDefault();
        countingNumberCharacterInput(inputOtherName, 'Other Name');
    });
    inputAboutMe.on('input', function (event) {
        event.preventDefault();
        countingNumberCharacterInput(inputAboutMe, 'About Me');
    });

    // event keyup
    $('.pass').on('keyup', function (event) {
        event.preventDefault();
        let nPass = inputNewPassword.val(),
            vPass = inputVerifyPassword.val(),
            invalidPasswordConfirmation = $('#invalid-password-confirmation');

        if (nPass !== '' && vPass !== '') {
            if (nPass !== vPass) {
                invalidPasswordConfirmation.show();
                inputVerifyPassword.addClass('is-invalid');
                return;
            }
        }

        invalidPasswordConfirmation.hide();
        inputVerifyPassword.removeClass('is-invalid');
    });

    // croppie avatar
    let croppieAvatar = $('#croppie-avatar'),
        fileUpload = $('.file-upload'),
        modalCropAvatar = $('#crop-avatar');

    let croppieAvt = croppieAvatar.croppie({
        enableOrientation: true,
        mouseWheelZoom: false,
        viewport: {
            width: 150,
            height: 150,
            type: 'circle'
        },
        boundary: {
            width: 300,
            height: 300
        }
    });
    fileUpload.on('input', function (event) {
        event.preventDefault();
        let reader = new FileReader();
        reader.onload = function (e) {
            croppieAvt.croppie('bind', {
                url: e.target.result,
            })
        };
        reader.readAsDataURL(this.files[0]);
        modalCropAvatar.modal('show');
    });
    modalCropAvatar.find('button:last').on('click', function (event) {
        event.preventDefault();
        croppieAvt.croppie('result', {
            type: 'base64',
            size: {
                width: 750,
                height: 750
            },
            circle: false,
            format: 'jpeg'
        }).then(function (image) {
            let createPost = $('#create-post-for-avatar').is(':checked');

            let formData = new FormData();
            formData.append('avatarBase64', image.split(',')[1]);
            formData.append('createPost', createPost);

            $.ajax({
                url: '/setting/avatar',
                type: 'POST',
                contentType: false,
                processData: false,
                cache: false,
                dataType: 'json',
                data: formData
            }).done(response => {
                let {
                    src,
                    message
                } = response;
                $('.my-avatar').attr('src', src);
                showModalNotificationError(message);
            }).fail(jqXHR => {
                let {
                    message
                } = JSON.parse(jqXHR.responseText);
                showModalNotificationError(message);
            })
        });
        modalCropAvatar.modal('hide');
    });

    // date picker
    inputBirthDay.datepicker();
    inputBirthDay.datepicker('option', 'maxDate', new Date());

    // other function
    function showModalNotificationError(message) {
        let modalNotification = $('#modal-notification');
        modalNotification.find('.modal-body > p').text(message);
        modalNotification.modal('show');
    }

    function showModalAcceptAction(callback) {
        let modalAccept = $('#modalAccept'),
            buttonOk = modalAccept.find('#buttonOK');
        buttonOk.off('click');

        buttonOk.on('click', function (event) {
            event.preventDefault();
            callback();
            modalAccept.modal('hide');
        });
        modalAccept.modal('show');
    }

    function showModalChangePasswordSuccess(message, urlRedirect) {
        let modal = $('#modal-notification-change-password-success');
        modal.find('p').text(message);
        modal.find('a').attr('href', urlRedirect);
        modal.modal('show');
    }

    function showModalInputSocialLink() {
        let e = $(this),
            type = e.attr('data-social'),
            modal = $('#modal-link-social'),
            buttonSaveOnModal = modal.find('button:last'),
            inputUrl = modal.find('#input-social-link');

        // clear
        modal.find('.invalid-feedback').hide();

        // logic here
        modal.find('.modal-title').text(type.substr(0, 1).toUpperCase() + type.substr(1));
        inputUrl.val(e.attr('href'));
        buttonSaveOnModal.off();
        buttonSaveOnModal.on('click', function () {
            let url = inputUrl.val();
            if (!url.match(e.attr('data-regex'))) {
                modal.find('.invalid-feedback').show();
            } else {
                modal.find('.invalid-feedback').hide();
                e.attr('href', url);
                modal.modal('hide');
            }
        });

        modal.modal('show');
    }

    function countingNumberCharacterInput(element, title) {
        let curNumber = element.val().length,
            max = element.attr('maxLength');
        element.prev().text(`${title} (${curNumber} / ${max})`);
    }

    function getDateFromPersonalInfoForm() {
        let firstName = inputFirstName.val(),
            lastName = inputLastName.val(),
            birthDay = inputBirthDay.datepicker('getDate'),
            gender = $('input[name="radio-gender"]:checked').val() === 'Male',
            idCountry = selectCountry.val(),
            address = inputAddress.val(),
            favoriteQuotes = inputFavoriteQuotes.val(),
            otherName = inputOtherName.val(),
            aboutMe = inputAboutMe.val();

        // languages
        let idLanguages = [];
        showLanguages.children('span').each(function () {
            idLanguages.push($(this).attr('data-idLanguage'));
        });

        return {
            firstName,
            lastName,
            birthDay,
            gender,
            idCountry,
            idLanguages,
            address,
            favoriteQuotes,
            otherName,
            aboutMe
        };
    }
    async function getHistory(page, number) {
        let e = $('#history ul.timeline');
        await $.getJSON(`me/history?page=${page}&number=${number}`, function (responses) {
            for (let i = 0; i < responses.length; i++) {
                drawHistoryCard(responses[i]).appendTo(e);
            }
        })
    }

    function drawHistoryCard({
                                 id,
                                 content,
                                 url,
                                 dateTime
                             }) {
        let e = $('#history ul.timeline');
        let lastLi = e.find('li').last();
        dateTime = new Date(dateTime);
        if (lastLi) {
            let time = new Date(+lastLi.attr('data-dateTime'));
            if (dateTime.toDateString() !== time.toDateString()) {
                e.append($(`<h4>${dateTime.toDateString()}</h4>`));
            }
        } else {
            e.append($(`<h4>${dateTime.toDateString()}</h4>`));
        }
        return $(`<li class="row" data-dateTime="${dateTime.getTime()}" data-id-history="${id}" id="history-${id}">
               <a href="${url}" class="col-10">${content}</a>
               <a href="#" class="col-2 text-right">${dateTime.toLocaleTimeString()}</a>
           </li>`);
    }

    let flag = true;

    function onEventScrollHistory() {
        let e = $('#history ul.timeline'),
            lastE = e.find('li').last(),
            id = lastE.attr('data-id-history');
        // for scroll history
        let controller = new ScrollMagic.Controller();
        let scene = getNewScene(lastE.attr('id'), id);

        function getNewScene(idElement, id) {
            return new ScrollMagic.Scene({
                triggerElement: `#${idElement}`,
                triggerHook: "onEnter"
            })
                .addTo(controller)
                .on("enter", function () {
                    if (flag) {
                        flag = false;
                    } else {
                        $.getJSON(`me/history/${id}?number=20`, function (responses) {
                            responses.forEach(response => {
                                drawHistoryCard(response).appendTo(e);
                            });
                            scene.destroy(true);
                            flag = true;

                            if (responses.length !== 0) {
                                let temp = e.find('li').last();
                                scene = getNewScene(temp.attr('id'), temp.attr('data-id-history'));
                            }
                        })
                    }
                });
        }
    }
}(jQuery));