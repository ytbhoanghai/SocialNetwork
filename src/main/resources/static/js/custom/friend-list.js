(function (jQuery) {

    "use strict";

    activeTabPage('ytb-page-friend-list');

    function activeTabPage(name) {
        $('.ytb-page-tab').removeClass('active');
        $(`#${name}`).addClass('active');
    }
    let area = $('#area-show-card-friend-1'),
        page = 0;

    $.getJSON('/me/basic-info', function (infoUser) {
        triggerEventLoadInfoFriendWhenScroll(infoUser.id);
    });

    let controller = new ScrollMagic.Controller(),
        scene = null;

    function triggerEventLoadInfoFriendWhenScroll(idUser) {
        scene = new ScrollMagic.Scene({
            triggerElement: "#loading-friend-card",
            triggerHook: "onEnter"
        })
            .addTo(controller)
            .on("enter", function () {
                $.getJSON(`/user/friends/${idUser}/${page}/8`, function (responses) {
                    if (responses.length < 8) {
                        $('#loading-friend-card').removeClass('loading-active');
                    }
                    responses.forEach(response => {
                        let {
                            urlBackground,
                            urlAvatar,
                            id,
                            firstName,
                            lastName,
                            isFollowing,
                            isOnline
                        } = response;
                        let fullName = lastName + ' ' + firstName;
                        if (fullName.length > 9) {
                            fullName = '... ' + firstName;
                        }
                        let html = $(` <div class="col-md-6">
                                            <div class="iq-card">
                                                <div class="iq-card-body profile-page p-0">
                                                    <div class="profile-header-image">
                                                        <div class="cover-container">
                                                            <img src="${urlBackground}" alt="profile-bg" class="rounded img-fluid w-100">
                                                        </div>
                                                        <div class="profile-info p-4">
                                                            <div class="user-detail">
                                                                <div class="d-flex flex-wrap justify-content-between align-items-start">
                                                                    <div class="profile-detail d-flex">
                                                                        <div class="profile-img pr-4">
                                                                            <a href="/user?id=${id}"><img src="${urlAvatar}" alt="profile-img" class="avatar-130 img-fluid" /></a>
                                                                        </div>
                                                                        <div class="user-data-block">
                                                                            <h4 class="">${fullName}</h4>
                                                                            <h6><span class="${isOnline ? 'dot-online' : 'dot-offline'}" id="dot-on-off-${id}"></span>${id.substring(0, 5) + '...' + id.substring(id.length - 3)}</h6>
                                                                        </div>
                                                                    </div>
                                                                    <button class="btn btn-primary">${isFollowing ? 'Following' : 'Follow'}</button>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>`);

                        area.append(html);

                        html.find('button').last().on('click', function (event) {
                            event.preventDefault();
                            let e = $(this);
                            let type = e.text() === 'Following' ? 'DELETE' : 'POST';
                            $.ajax({
                                url: `/me/following?id=${id}`,
                                type: type
                            }).done(function () {
                                if (type === 'DELETE') {
                                    e.text('Follow');
                                } else {
                                    e.text('Following');
                                }
                            })
                        });

                        scene.update();
                    });
                    page += 1;
                })
            });
    }

}());