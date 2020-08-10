(function (jQuery) {

    "use strict";

    activeTabPage('ytb-page-chat');

    function activeTabPage(name) {
        $('.ytb-page-tab').removeClass('active');
        $(`#${name}`).addClass('active');
    }

    // run
    // auto open chat box
    let idUser = getUrlParameter('id');
    if (idUser) {
        new Promise(resolve => {
            getChatBox(idUser, true);
            resolve();
        }).then(function () {
            runBackground();
            setTimeout(function () {
                $.ajax({
                    url: '/chat/chat-box/un-seen',
                    type: 'GET',
                    dataType: 'text'
                }).done(function (response) {
                    if (+response === 0) {
                        $('#button-view-notification-messages span').hide();
                    }
                });
            }, 2 * 1000);
            let clean_uri = location.protocol + "//" + location.host + location.pathname;
            window.history.replaceState({}, document.title, clean_uri);
        })
    } else {
        runBackground();
    }

    function runBackground() {
        getMyInformation();
        setAutoCompleteInputFriend($('#chat-search'));
        getListChatBox();
    }

    // preparing
    let userProfile = $('#user-profile'),
        defaultBlock = $('#default-block'),
        areaShowChatList = $('#area-show-chat-list');

    $('#button-copy-id').on('click', function (event) {
        event.preventDefault();
        let id = userProfile.find('p:eq(0)').attr('data-id-user');
        navigator.clipboard.writeText(id).then(function () {
            console.log('Copying id to clipboard was successful!');
            showModalNotificationError('Copying id to clipboard was successful!')
        });
    });

    function getMyInformation() {
        $.getJSON('me/basic-info', function ({
                                                 id,
                                                 firstName,
                                                 lastName,
                                                 urlAvatar
                                             }) {
            let subId = id.substring(0, 8) + '...' + id.substring(id.length - 4),
                fullName = lastName + ' ' + firstName;

            userProfile.find('img:eq(0)').attr('src', urlAvatar);
            userProfile.find('h5:eq(0)').text(fullName);
            userProfile.find('p:eq(0)').text(subId);
            userProfile.find('p:eq(0)').attr('data-id-user', id);

        })
    }

    function getListChatBox() {
        $.getJSON('/chat/chat-box', function (responses) {
            responses.forEach(response => {
                let temp = areaShowChatList.find(`li[data-id-chatBox="${response.id}"]`);

                if (temp.length === 0) {
                    areaShowChatList.append(drawOverviewChatBox(response));
                } else {
                    temp.find(`#num-messages-for-${response.id}`).text('0');
                }
            });
        });
    }

    function drawOverviewChatBox(response, zero) {
        let {
            id,
            basicInfo,
            latestContent,
            numberUnSeen
        } = response;
        let {
            urlAvatar,
            firstName,
            lastName,
            isOnline
        } = basicInfo;

        let html = $(`<li data-id-chatBox="${id}">
                                <a data-toggle="pill" href="#">
                                    <div class="d-flex align-items-center">
                                        <div class="avatar mr-2">
                                            <img src="${urlAvatar}" alt="chatuserimage" class="avatar-50 ">
                                            <span class="avatar-status"><i data-status-online="${basicInfo.id}" class="ri-checkbox-blank-circle-fill ${isOnline ? 'text-success' : 'text-dark'}"></i></span>
                                        </div>
                                        <div class="chat-sidebar-name">
                                            <h6 class="mb-0">${lastName + ' ' + firstName}</h6>
                                            <span id="sub-messages-for-${id}">${latestContent.length > 25 ? latestContent.substring(0, 25) + '...' : latestContent}</span>
                                        </div>
                                        <div class="chat-meta float-right text-center mt-2 mr-1">
                                            <div id="num-messages-for-${id}" class="chat-msg-counter bg-primary text-white">${zero ? 0 : numberUnSeen}</div>
                                        </div>
                                    </div>
                                </a>
                            </li>`);

        html.find('a:eq(0)').on('click', function (event) {
            event.preventDefault();
            response.basicInfo.isOnline = html.find(`i[data-status-online="${basicInfo.id}"]`).hasClass('text-success');
            $(`#num-messages-for-${id}`).text(0);
            openChatBox(response);

            // update notify
            setTimeout(function () {
                $.ajax({
                    url: '/chat/chat-box/un-seen',
                    type: 'GET',
                    dataType: 'text'
                }).done(function (response) {
                    if (+response === 0) {
                        $('#button-view-notification-messages span').hide();
                    }
                });
            }, 2 * 1000);
        });

        return html;
    }

    function getChatBox(idUser, zero) {
        $.getJSON(`/chat/chat-box/${idUser}`, function (response) {
            let {
                    id
                } = response,
                e = areaShowChatList.find(`li[data-id-chatBox="${id}"]`);
            if (e.length !== 0) {
                e.remove();
            }
            areaShowChatList.prepend(drawOverviewChatBox(response, zero));
            openChatBox(response);
        });
    }

    function openChatBox(_response) {
        let {
            id,
            basicInfo,
            isBlocked
        } = _response;
        defaultBlock.empty();
        defaultBlock.attr('data-id-chatBox', id);
        drawHeader(basicInfo);
        drawContent();
        drawFooter();

        function drawHeader({
                                id,
                                urlAvatar,
                                firstName,
                                lastName,
                                isOnline,
                                favoriteQuotes,
                                mobile,
                                birthDay,
                                gender
                            }) {
            let html = $(`<div class="chat-head">
                                <header class="d-flex justify-content-between align-items-center bg-white pt-3 pr-3 pb-3">
                                    <div class="d-flex align-items-center">
                                        <div class="sidebar-toggle">
                                            <i class="ri-menu-3-line"></i>
                                        </div>
                                        <div class="avatar chat-user-profile m-0 mr-3">
                                            <img src="${urlAvatar}" alt="avatar" class="avatar-50">
                                            <span class="avatar-status"><i data-status-online="${id}" class="ri-checkbox-blank-circle-fill ${isOnline ? 'text-success' : 'text-dark'}"></i></span>
                                        </div>
                                        <a href="/user?id=${id}"><h5 class="mb-0">${lastName + ' ' + firstName}</h5></a>
                                    </div>
                                    <div class="chat-user-detail-popup scroller">
                                        <div class="user-profile text-center">
                                            <button type="submit" class="close-popup p-3"><i class="ri-close-fill"></i></button>
                                            <div class="user mb-4">
                                                <a class="avatar m-0">
                                                    <img src="${urlAvatar}" alt="avatar" style="max-width: 150px">
                                                </a>
                                                <div class="user-name mt-4">
                                                    <h4>${lastName + ' ' + firstName}</h4>
                                                </div>
                                                <div class="user-desc">
                                                    <p>${favoriteQuotes.length > 30 ? favoriteQuotes.substring(0, 30) + '...' : favoriteQuotes}</p>
                                                </div>
                                            </div>
                                            <hr>
                                            <div class="chatuser-detail text-left mt-4">
                                                <div class="row">
                                                    <div class="col-6 col-md-6 title">Full Name:</div>
                                                    <div class="col-6 col-md-6 text-right">${lastName + ' ' + firstName}</div>
                                                </div>
                                                <hr>
                                                <div class="row">
                                                    <div class="col-6 col-md-6 title">Tel:</div>
                                                    <div class="col-6 col-md-6 text-right">${mobile ? mobile : 'nothing'}</div>
                                                </div>
                                                <hr>
                                                <div class="row">
                                                    <div class="col-6 col-md-6 title">Date Of Birth:</div>
                                                    <div class="col-6 col-md-6 text-right">${new Date(birthDay).toDateString()}</div>
                                                </div>
                                                <hr>
                                                <div class="row">
                                                    <div class="col-6 col-md-6 title">Gender:</div>
                                                    <div class="col-6 col-md-6 text-right">${gender ? 'Male' : 'Female'}</div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="chat-header-icons d-flex">
                                        <span class="dropdown iq-bg-primary">
                               <i class="ri-more-2-line cursor-pointer dropdown-toggle nav-hide-arrow cursor-pointer pr-0" id="dropdownMenuButton02" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" role="menu"></i>
                               <span class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuButton02">
                               <a class="dropdown-item" href="#" id="button-delete-${id}" ><i class="fa fa-trash-o" aria-hidden="true"></i> Delete chat</a>
                               <a class="dropdown-item" href="#" id="button-block-${id}" data-action-block="${isBlocked}">${!isBlocked ? '<i class="fa fa-ban" aria-hidden="true"></i>  Block' : '<i class="fa fa-unlock" aria-hidden="true"></i> UnBlock'}</a>
                               </span>
                               </span>
                                    </div>
                                </header>
                            </div>`);
            defaultBlock.append(html);
            html.find(`#button-block-${id}`).on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();
                let action = $(this).attr('data-action-block') === 'true',
                    button = $(this);
                if (action) {
                    // un block
                    $.ajax({
                        url: `/me/un-block?id=${id}`,
                        type: 'PUT',
                    }).done(function () {
                        _response.isBlocked = false;
                        button.attr('data-action-block', 'false');
                        button.html('<i class="fa fa-ban" aria-hidden="true"></i>  Block');
                    })
                } else {
                    // block
                    $.ajax({
                        url: '/me/block',
                        type: 'POST',
                        data: {
                            'id': id
                        }
                    }).done(function () {
                        _response.isBlocked = true;
                        button.attr('data-action-block', 'true');
                        button.html('<i class="fa fa-unlock" aria-hidden="true"></i> UnBlock');
                    })
                }
            });
            html.find(`#button-delete-${id}`).on('click', function (event) {
                event.preventDefault();
                showModalAcceptAction(function () {
                    $.ajax({
                        url: `/chat/chat-box?idUser=${id}`,
                        type: 'DELETE',
                        dataType: 'text'
                    }).done(function (response) {
                        let clean_uri = location.protocol + "//" + location.host + location.pathname;
                        window.history.replaceState({}, document.title, clean_uri);
                        defaultBlock.html(`<div class="chat-start">
                                            <span class="iq-start-icon text-primary"><i class="ri-message-3-line"></i></span>
                                            <a id="chat-start" class="btn bg-white mt-3">Welcome To The Chat Room</a>
                                        </div>`);
                        $(`li[data-id-chatBox="${_response.id}"]`).remove();
                        showModalNotificationError(response);
                    })
                })
            });
        }

        function drawContent() {
            let html = $(`<div class="chat-content scroller">
                                <div class="col-sm-12 text-center loader-post loading-active" id="loading-messages">
                                      <img src="images/page-img/page-load-loader.gif" alt="loader" style="height: 100px;">
                                   </div>
                                   <div id="area-show-messages">
                                   
                                    </div>
                            </div>`);

            new Promise(resolve => {
                getMessage(undefined, html);
                defaultBlock.append(html);
                resolve();
            }).then(function () {
                html.scroll(function () {
                    if (html.scrollTop() === 0) {
                        let lastId = html.find('#loading-messages').next().find('div:eq(0)').attr('data-id-message');
                        getMessage(lastId, html);
                    }
                });
            })

        }

        function drawFooter() {
            let html = $(`<div class="chat-footer p-3 bg-white">
                                <form class="d-flex align-items-center" >
                                    <div class="chat-attagement d-flex">
                                        <a href="#"><i class="fa fa-smile-o pr-3" aria-hidden="true"></i></a>
                                    </div>
                                    <input type="text" class="form-control mr-3" placeholder="Type your message">
                                    <button type="submit" class="btn btn-primary d-flex align-items-center p-2"><i class="fa fa-paper-plane-o" aria-hidden="true"></i><span class="d-none d-lg-block ml-1">Send</span></button>
                                </form>
                            </div>`);
            html.find('a:eq(0)').on('click', function (event) {
                event.preventDefault();
                showModalChooseEmoji(function (emoji) {
                    let e = html.find('input:eq(0)');
                    e.val(e.val() + emoji);
                })
            });

            html.find('form:eq(0)').on('submit', function (event) {
                event.preventDefault();
                let t = html.find('input:eq(0)');
                $.ajax({
                    url: `/chat/chat-box/${id}/messages`,
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'text/plain',
                    data: t.val(),
                }).done(function (response) {
                    let e = $('#area-show-messages'),
                        wrapper = e.parent(),
                        content = t.val();
                    e.append(drawOneMessage(response));
                    wrapper.scrollTop(wrapper[0].scrollHeight);
                    t.val('');
                    $(`#sub-messages-for-${id}`).text(content.length > 25 ? content.substring(0, 25) + '...' : content);
                })
            });

            defaultBlock.append(html);

        }

        function getMessage(first, html) {
            let url = first ? `/chat/chat-box/${id}/messages?fromMessage=${first}&number=15` : `/chat/chat-box/${id}/messages?number=15`,
                areaShowMessage = html.find('#area-show-messages'),
                old_height = html[0].scrollHeight;

            $.getJSON(url, function (responses) {
                if (responses.length < 15) {
                    html.find('#loading-messages').removeClass('loading-active');
                    html.off('scroll');
                }

                responses.forEach(response => areaShowMessage.prepend(drawOneMessage(response)));

                if (first === undefined) {
                    html.scrollTop(html[0].scrollHeight);
                } else {
                    let scrollDif = html[0].scrollHeight - old_height;
                    html[0].scrollTop += scrollDif;
                }
            });
        }

        function drawOneMessage(response) {
            let {
                basicInfo,
                id,
                idChatBox,
                content,
                idAuthor,
                dateCreated,
                ofMe
            } = response, {
                urlAvatar,
                firstName,
                lastName
            } = basicInfo;
            return $(`<div data-id-message="${id}" class="chat ${ofMe ? '' : 'chat-left'}">
                                        <div class="chat-user">
                                            <a class="avatar m-0">
                                                <img src="${urlAvatar}" alt="avatar" class="avatar-35 ">
                                            </a>
                                            <span class="chat-time mt-1">${new Date(dateCreated).toLocaleTimeString()}</span>
                                        </div>
                                        <div class="chat-detail">
                                            <div class="chat-message">
                                                <p>${content}</p>
                                            </div>
                                        </div>
                                    </div>`);
        }
    }

    // other functions
    function showModalAcceptAction(callback) {
        let modalAccept = $('#modalAccept'),
            buttonOk = modalAccept.find('#buttonOK');

        buttonOk.off('click').on('click', function (event) {
            event.preventDefault();
            callback();
            modalAccept.modal('hide');
        });
        modalAccept.modal('show');
    }

    function showModalNotificationError(message) {
        let modalNotification = $('#modal-notification');
        modalNotification.find('.modal-body > p').text(message);
        modalNotification.modal('show');
    }

    function setAutoCompleteInputFriend(...elements) {
        let cache = {};
        elements.forEach(e => {
            e.autocomplete({
                minLength: 1,
                source: function (request, response) {
                    let term = request.term;
                    if (term in cache) {
                        response(cache[term]);
                        return;
                    }

                    $.getJSON('/me/friend/basic-info', request, function (data) {
                        cache[term] = data;
                        response(cache[term]);
                    })
                },
                select: function (event, ui) {
                    let {
                        id
                    } = ui.item;
                    e.val('');
                    getChatBox(id);

                    return false;
                }
            }).autocomplete("instance")._renderItem = function (ul, {
                urlAvatar,
                firstName,
                lastName
            }) {
                let fullName = lastName + ' ' + firstName;
                return $('<li>')
                    .append(`<div class="row w-100"><img src="${urlAvatar}" alt="avatar" class="avatar-30 rounded-circle rol-4 ml-2"><p class="col-8 ml-1">${fullName}</p></div>`)
                    .appendTo(ul);
            };
        })
    }

    function showModalChooseEmoji(callback) {
        let temp = $('#modal-show-emoji'),
            temp2 = temp.find('div.modal-body');
        temp2.find('a').off('click').on('click', function (event) {
            event.preventDefault();
            callback($(this).text());
            temp.modal('hide');
        });
        temp.modal('show');
    }

    function getUrlParameter(sParam) {
        let sPageURL = window.location.search.substring(1),
            sURLVariables = sPageURL.split('&'),
            sParameterName,
            i;

        for (i = 0; i < sURLVariables.length; i++) {
            sParameterName = sURLVariables[i].split('=');

            if (sParameterName[0] === sParam) {
                return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
            }
        }
    }
}());