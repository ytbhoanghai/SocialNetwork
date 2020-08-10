(function (jQuery) {

    "use strict";

    activeTabPage('ytb-page-notification');

    function activeTabPage(name) {
        $('.ytb-page-tab').removeClass('active');
        $(`#${name}`).addClass('active');
    }

    let page = 0;
    let area = $(`#area-show-notification-1`);

    let controller = new ScrollMagic.Controller();
    let scene = null;

    triggerEventScrollPost();

    function triggerEventScrollPost() {
        scene = new ScrollMagic.Scene({
            triggerElement: "#loading-notifications",
            triggerHook: "onEnter"
        })
            .addTo(controller)
            .on("enter", function () {
                $.getJSON(`/notification/${page}/20`, function (response) {
                    let {
                        notificationResponses
                    } = response;

                    if (notificationResponses.length < 20) {
                        $('#loading-notifications').removeClass('loading-active');
                    }
                    notificationResponses.forEach(response => {
                        let {
                                id,
                                objectName,
                                content,
                                link,
                                picture,
                                dateCreated
                            } = response,
                            _distanceTime = distanceTime(new Date(), new Date(dateCreated));

                        let html = $(` <div class="iq-card">
                                            <div class="iq-card-body">
                                                <ul class="notification-list m-0 p-0">
                                                    <li class="d-flex align-items-center">
                                                        <div class="user-img img-fluid"><img src="${picture}" alt="story-img" class="rounded-circle avatar-40"></div>
                                                        <div class="media-support-info ml-3">
                                                            <h6>${objectName + ' ' +    content}</h6>
                                                            <p class="mb-0">${_distanceTime} ago</p>
                                                        </div>
                                                        <div class="d-flex align-items-center">
                                                            <a href="/${link}" class="mr-3 iq-notify iq-bg-primary rounded"><img src="/images/icon/click.png"></a>
                                                        </div>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>`);

                        area.append(html);
                    });
                    scene.update();
                })
            });
    }

    function distanceTime(d1, d2) {
        let diff = d1 - d2,
            t = 'seconds';
        let diffValues = diff / 1000;
        if (diffValues > 60) {
            diffValues /= 60;
            t = 'minutes';
            if (diffValues > 60) {
                diffValues /= 60;
                t = 'hours';
                if (diffValues > 24) {
                    diffValues /= 24;
                    t = 'days';
                    if (diffValues > 7) {
                        diffValues /= 7;
                        t = 'weeks';
                    }
                }
            }
        }
        diffValues = Math.floor(diffValues);
        if (diffValues < 2) {
            t = t.substr(0, t.length - 1);
        }

        return diffValues + ' ' + t;
    }

}());