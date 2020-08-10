(function (jQuery) {

    "use strict";

    activeTabPage('ytb-page-birthday');

    function activeTabPage(name) {
        $('.ytb-page-tab').removeClass('active');
        $(`#${name}`).addClass('active');
    }

    $.getJSON('/me/basic-info', function (infoUser) {
        drawUpcomingBirthday(infoUser.id);
    });

    function drawUpcomingBirthday(idUser) {
        $.getJSON(`/user/${idUser}/friends/UpComingBirthday`, function (response) {
            f(response, 7);
        });

        function f(response, numberNextMonth) {
            let data = [],
                currentMonth = new Date().getMonth() + 1,
                area = $('#area-show-birthday-2');
            data.push(getDataFromUBOF(response, "today"));
            for (let i = 0; i < numberNextMonth; i++) {
                data.push(getDataFromUBOF(response, getMonth(currentMonth + i)));
            }

            data.forEach(({
                              key,
                              value
                          }) => {
                let flag = true;
                let wrapper = $(`<div class="birthday-block">
                        <div class="iq-card">
                            <div class="iq-card-header d-flex justify-content-between" style="border:none;">
                                <div class="iq-header-title">
                                    <h4 class="card-title">${key}</h4>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                        </div>
                    </div>`);
                if (value && value.length !== 0) {
                    let content = "today";
                    value.forEach(infoUser => {
                        if (key !== "today") {
                            content = new Date(infoUser.birthDay).toDateString();
                            let lastIndex = content.lastIndexOf(' ');
                            content = content.substring(0, lastIndex);
                        }
                        wrapper.find('div.row').append(draw(infoUser, content))
                    });
                    flag = false;
                }
                if (!flag) {
                    area.append(wrapper);
                }

            });


            function draw({
                              id,
                              firstName,
                              lastName,
                              urlAvatar
                          }, content) {
                let fullName = lastName + ' ' + firstName;
                if (fullName.length > 9) {
                    fullName = '... ' + firstName;
                }
                return $(`<div class="col-md-6 col-lg-6">
                                <div class="iq-card">
                                    <div class="iq-card-body">
                                        <div class="iq-birthday-block">
                                            <div class="d-flex align-items-center justify-content-between">
                                                <div class="d-flex align-items-center">
                                                    <a href="user?id=${id}">
                                                        <img src="${urlAvatar}" alt="profile-img" class="img-fluid" style="max-width: 150px">
                                                    </a>
                                                    <div class="friend-info ml-3">
                                                        <h5>${fullName}</h5>
                                                        <p class="mb-0">${content}</p>
                                                    </div>
                                                </div>
                                                <a class="btn btn-primary" href="/chat?id=${id}"><i class="ri-send-plane-2-fill mr-1"></i> Send <i class="ml-1 ri-message-fill"></i></a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>`);
            }

            function getMonth(month) {
                return month > 12 ? month - 12 : month;
            }

            function getDataFromUBOF(ubof, key) {
                if (key === "today") {
                    return {
                        key: key,
                        value: ubof["today"]
                    };
                }
                let {
                        inMonths
                    } = ubof,
                    strMonths = {
                        '1': 'January',
                        '2': 'February',
                        '3': 'March',
                        '4': 'April',
                        '5': 'May',
                        '6': 'June',
                        '7': 'July',
                        '8': 'August',
                        '9': 'September',
                        '10': 'October',
                        '11': 'November',
                        '12': 'December'
                    };
                return {
                    key: strMonths[key],
                    value: inMonths[key]
                }
            }
        }
    }
}());