(function (jQuery) {

    "use strict";

    activeTabPage('ytb-page-friend-request');

    function activeTabPage(name) {
        $('.ytb-page-tab').removeClass('active');
        $(`#${name}`).addClass('active');
    }
    let areaShowFriendRequest = $('#area-show-friend-request-1'),
        pageFriendRequest = 0;

    drawFriendRequest(true);

    function drawFriendRequest(first, lastId, number) {
        let url = `/me/friend-request/${pageFriendRequest}/8`;
        if (lastId) {
            url = `/me/friend-request/next/${lastId}?number=${number ? number : 8}`;
        }

        $.getJSON(url, function (responses) {
            if (responses.length < 8) {
                areaShowFriendRequest.find('li').last().addClass('d-none').removeClass('d-block');
                if (first && responses.length === 0) {
                    areaShowFriendRequest.append($('<p>Nothing</p>'));
                }
            } else {
                pageFriendRequest += 1;
            }
            responses.forEach(response => {
                let {
                        fullName,
                        idUserRequest,
                        numberFriends,
                        picture
                    } = response,
                    html = $(` <li class="d-flex align-items-center" data-id-friend-request="${idUserRequest}">
                                    <div class="user-img img-fluid"><a href="/user?id=${idUserRequest}"><img src="${picture}" alt="story-img" class="rounded-circle avatar-40"></a></div>
                                    <div class="media-support-info ml-3"    >
                                        <h6>${fullName}</h6>
                                        <p class="mb-0">${numberFriends} friends</p>
                                    </div>
                                    <div class="d-flex align-items-center">
                                        <a href="#" class="mr-3 btn btn-primary rounded">Confirm</a>
                                        <a href="#" class="mr-3 btn btn-secondary rounded">Delete Request</a>
                                    </div>
                                </li>`);

                areaShowFriendRequest.find('li').last().before(html);

                html.find('a:eq(1)').on('click', function (event) {
                    event.preventDefault();
                    $.ajax({
                        url: '/me/friend-request/accept',
                        type: 'POST',
                        dataType: 'text',
                        data: {
                            'id': idUserRequest,
                            'notify': false
                        }
                    }).done(function () {
                        html.remove();
                        let id = areaShowFriendRequest
                            .find('li')
                            .last()
                            .prev()
                            .attr('data-id-friend-request');
                        drawFriendRequest(false, id, 1);
                    })
                });

                html.find('a:eq(2)').on('click', function (event) {
                    event.preventDefault();
                    $.ajax({
                        url: `/me/friend-request/${idUserRequest}?notify=false`,
                        type: 'DELETE',
                        dataType: 'text'
                    }).done(function () {
                        html.remove();
                        let id = areaShowFriendRequest
                            .find('li')
                            .last()
                            .prev()
                            .attr('data-id-friend-request');
                        drawFriendRequest(false, id, 1);
                    })
                })
            });
        });
    }
    areaShowFriendRequest.find('li').last().find('a').on('click', function (event) {
        event.preventDefault();
        let id = areaShowFriendRequest
            .find('li')
            .last()
            .prev()
            .attr('data-id-friend-request');

        drawFriendRequest(false, id);
    });


    // phía trên là lười viết lắm rồi nên mới trình bày xấu như vậy đó :(

    drawUserMayBeKnow();

    function drawUserMayBeKnow() {
        let area = $('#area-show-user-may-know');
        $.getJSON('/me/may-know?max-size=30', function (responses) {
            if (responses.length === 0) {
                area.append($('<p>Nothing</p>'));
            } else {
                responses.forEach(({
                                       id,
                                       urlAvatar,
                                       firstName,
                                       lastName,
                                       numberFriends
                                   }) => {
                    let temp = numberFriends === 0 ? '' : numberFriends === 1 ? numberFriends + ' Friend' : numberFriends + ' Friends';
                    let html = $(`<li class="d-flex align-items-center">
                                    <div class="user-img img-fluid"><a href="/user?id=${id}"><img src="${urlAvatar}" alt="story-img" class="rounded-circle avatar-40"></a></div>
                                    <div class="media-support-info ml-3">
                                        <h6>${lastName + ' ' + firstName}</h6>
                                        <p class="mb-0">${temp}</p>
                                    </div>
                                    <div class="d-flex align-items-center">
                                        <a href="#" class="mr-3 btn btn-primary rounded" data-action="add"><i class="ri-user-add-line"></i>Add Friend</a>
                                        <a href="#" class="mr-3 btn btn-secondary rounded">Remove</a>
                                    </div>
                                </li>`);

                    area.append(html);

                    html.find('a:eq(1)').on('click', function (event) {
                        event.preventDefault();
                        let action = $(this).attr('data-action'),
                            button = $(this);
                        if (action === "add") {
                            $.ajax({
                                url: '/me/friend-request',
                                type: 'POST',
                                data: {
                                    'id': id
                                }
                            }).done(function () {
                                button.attr('data-action', 'cancel');
                                button.html('Cancel Request');
                            }).fail(function () {
                                showModalNotificationError("There was an unexpected error, please try again later")
                            })
                        } else {
                            $.ajax({
                                url: `/me/friend-request/${id}`,
                                type: 'DELETE',
                            }).done(function () {
                                button.attr('data-action', 'add');
                                button.html('<i class="ri-user-add-line"></i>Add Friend');
                            }).fail(function () {
                                showModalNotificationError("There was an unexpected error, please try again later")
                            })
                        }
                    });
                    html.find('a').last().on('click', function (event) {
                        event.preventDefault();
                        html.remove();
                    })
                })
            }
        });
    }

    function showModalNotificationError(message) {
        let modalNotification = $('#modalNotification');
        modalNotification.find('.modal-body > p').text(message);
        modalNotification.modal('show');
    }

}());