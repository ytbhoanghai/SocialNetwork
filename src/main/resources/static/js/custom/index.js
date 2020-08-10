(function (jQuery) {

    "use strict";

    activeTabPage('ytb-page-index');

    function activeTabPage(name) {
        $('.ytb-page-tab').removeClass('active');
        $(`#${name}`).addClass('active');
    }

    // cache variable
    let buttonAddPhotoToPost = $('#post-modal ul.d-flex.flex-wrap > li:eq(0) > div.iq-bg-primary > a'),
        inputAddPhotoToPost = $('#post-modal ul.d-flex.flex-wrap > li:eq(0) > input'),
        buttonRemovePhotoPost = $('#button-remove-photo'),
        postModal = $('#post-modal');

    let areaShowTagFriendsPost = $('#area-tag-friends-post'),
        inputTagUser = $('#input-tag-user'),
        buttonShowAreaTagFriends = $('#post-modal ul.d-flex.flex-wrap > li:eq(1) a');

    let modalFeeling = $('#modal-feeling'),
        buttonShowModalFelling = $('#post-modal ul.d-flex.flex-wrap > li:eq(2) a'),
        areaShowFeeling = $('#area-show-feeling'),
        buttonCloseAreaShowFeeling = areaShowFeeling.find('#show-feeling a').last();

    let buttonCheckin = $('#post-modal ul.d-flex.flex-wrap > li:eq(3) a');

    let buttonCreatePost = $('#button-create-post');

    $(`#post-modal-data input:eq(0)`).on('click', function (event) {
        event.preventDefault();
        $('#post-modalLabel').text('Create Post');
    });
    buttonCreatePost.on('click', function (event) {
        event.preventDefault();
        if (postModal.find('form > textarea').val().trim()) {
            let form = getPostForm();
            $.ajax({
                url: 'post',
                type: 'POST',
                contentType: 'application/json',
                dataType: 'text',
                data: form
            }).done(function (response) {
                showModalNotificationError(response);
                postModal.modal('hide');
            }).fail(jqXHR => {
                console.log(jqXHR);
            }).always(function () {

            })
        }
    });

    // preparing data
    setAutoCompleteInputFriend(function (id, fullName) {
        let html = $(`<div class="dropdown-item"><a href="#" title="Click To Delete" data-id-user="${id}">${fullName}</a></div>`),
            e = areaShowTagFriendsPost.find('div:eq(1) .dropdown-menu'),
            count = +e.attr('data-count');

        if (e.find(`a[data-id-user="${id}"]`).length === 0) {
            e.append(html);
            e.attr('data-count', count + 1);

            html.on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();

                let count = +e.attr('data-count');
                e.attr('data-count', count - 1);

                html.remove();
                $('#post-modal #dropdown-show-tag').text(`Show Tags (${count - 1})`);

                if (count - 1 === 0) {
                    clearDropdownMenuTagFriends()
                }
            });

            $('#post-modal #dropdown-show-tag').text(`Show Tags (${e.attr('data-count')})`);
        }
    }, inputTagUser);
    $.getJSON('data/feeling', function (response) {
        for (let i = 0; i < response.length; i++) {
            let {
                    id,
                    url,
                    name
                } = response[i],
                html = $(`<a href="#" class="card-feeling"><img src="${url}" alt="..." class="mr-3"/>${name}</a>`);

            html.on('click', function (event) {
                event.preventDefault();
                let e = areaShowFeeling.find('#show-feeling');
                e.find('span:eq(0)').text(`feeling ${name}`);
                e.find('span:eq(0)').attr('data-id-feeling', id);
                e.find('.show-image-feeling').empty();
                e.find('.show-image-feeling').append($(`<img src="${url}" alt="..." class="ml-3"/>`));
                modalFeeling.modal('hide');

                e.removeClass('d-none').addClass('d-flex');
                areaShowFeeling.removeClass('d-none').addClass('d-flex');
            });

            switch (i % 3) {
                case 0:
                    modalFeeling.find('.row div:eq(0)').append(html);
                    break;
                case 1:
                    modalFeeling.find('.row div:eq(1)').append(html);
                    break;
                case 2:
                    modalFeeling.find('.row div:eq(2)').append(html);
                    break
            }
        }
    });
    $.getJSON('/me/basic-info', function (infoUser) {
        triggerEventScrollPost(infoUser.id);
        triggerEventBackToTop(infoUser.id);
        drawUpcomingBirthday(infoUser.id);
        drawUserMayBeKnow();
    });

    // event click
    buttonAddPhotoToPost.on('click', function (event) {
        event.preventDefault();
        inputAddPhotoToPost.trigger('click');
    });
    buttonRemovePhotoPost.on('click', function (event) {
        event.preventDefault();
        showModalAcceptAction(function () {
            let e = $('#modal-show-image'),
                i = e.find('ol.carousel-indicators li[class="active"]').index(),
                t1 = e.find(`ol.carousel-indicators li:eq(${i})`),
                t2 = e.find(`div.carousel-inner div.carousel-item:eq(${i})`),
                t1Next = t1.next(),
                t2Next = t2.next();

            t1.remove();
            t2.remove();

            e.find(`ol.carousel-indicators li`).each((i, v) => {
                $(v).attr('data-slide-to', i);
            });

            if (t1Next.length !== 0) {
                t1Next.addClass('active');
                t2Next.addClass('active');
            } else {
                e.find(`ol.carousel-indicators li:eq(0)`).addClass('active');
                e.find(`div.carousel-inner div.carousel-item:eq(0)`).addClass('active');
            }

            let c = e.find(`div.carousel-inner img`);
            c.each((index, e) => {
                if (index < 4) {
                    let base64 = $(e).attr('src');
                    switch (index) {
                        case 0:
                            drawImage1(base64, true);
                            break;
                        case 1:
                            drawImage2(base64, true);
                            break;
                        case 2:
                            drawImage3(base64, true);
                            break;
                        case 3:
                            let l = c.length > 4 ? c.length - 4 : null;
                            drawImage4(base64, true, l);
                            break;
                    }
                }
            });

            if (c.length < 4) {
                let h = $('#area-show-photo-post');
                for (let run = c.length; run < 4; run++) {
                    switch (run) {
                        case 0:
                            h.find('div:eq(0) img:eq(0)').remove();
                            e.modal('hide');
                            break;
                        case 1:
                            h.find('div:eq(1) img:eq(0)').remove();
                            break;
                        case 2:
                            h.find('div:eq(0) img:eq(1)').remove();
                            break;
                        case 3:
                            h.find('div:eq(1)').children().eq(1).remove();
                            break;
                    }
                }
            }
        })
    });
    buttonShowAreaTagFriends.on('click', function (event) {
        event.preventDefault();
        areaShowTagFriendsPost.show();
    });
    buttonShowModalFelling.on('click', function (event) {
        event.preventDefault();
        modalFeeling.modal('show');
    });
    buttonCloseAreaShowFeeling.on('click', function (event) {
        event.preventDefault();
        let e = areaShowFeeling.find('#show-feeling');
        e.find('span:eq(0)').text('');
        e.find('span:eq(0)').attr('data-id-feeling', '');
        e.find('.show-image-feeling').empty();
        e.removeClass('d-flex').addClass('d-none');

        if (areaShowFeeling.find('#show-checkin a:eq(0)').attr('data-id-checkin') === '') {
            areaShowFeeling.removeClass('d-flex').addClass('d-none');
        }
    });
    buttonCheckin.on('click', function (event) {
        event.preventDefault();
        showInputCheckin.call(this);
    });

    // event hidden modal
    postModal.on('hidden.bs.modal', function () {
        clearContentTextAreaPost();
        clearPhotoOfPost();
        clearModalShowImagePost();
        clearDropdownMenuTagFriends();
        clearAreaShowFeeling();
    });

    // event change
    inputAddPhotoToPost.on('input', function (event) {
        event.preventDefault();

        let length = this.files.length,
            t = $('#area-show-photo-post');
        t.find('div:eq(0)').empty();
        t.find('div:eq(1)').empty();
        clearModalShowImagePost();

        let i = 0,
            files = this.files;
        let reader = new FileReader();
        if (files[0]) {
            reader.readAsDataURL(files[i]);
        }
        reader.onload = async function (e) {
            if (i === 0) {
                await drawImage1(e.target.result).then(_ => {
                    addImageToModalShowImagePost(e.target.result, i);
                });
            } else if (i === 1) {
                await drawImage2(e.target.result).then(_ => {
                    addImageToModalShowImagePost(e.target.result, i);
                });
            } else if (i === 2) {
                await drawImage3(e.target.result).then(_ => {
                    addImageToModalShowImagePost(e.target.result, i);
                });
            } else if (i === 3) {
                let o = length > 4 ? length - 4 : null;
                await drawImage4(e.target.result, false, o).then(_ => {
                    addImageToModalShowImagePost(e.target.result, i);
                });
            } else {
                await addImageToModalShowImagePost(e.target.result, i);
            }
            i += 1;
            if (files[i]) {
                reader.readAsDataURL(files[i]);
            }
        };

    });

    // draw functions
    function drawImage1(_base64, replace) {
        return base64ImageSmartCrop(_base64, 483, 321).then(function (base64) {
            let html = $(`<img src="${base64}" class="img-fluid mb-4 rounded" alt="">`),
                t = $('#area-show-photo-post');

            html.on('click', function (event) {
                event.preventDefault();
                showModalImagePost(0);
            });
            if (replace) {
                t.find('div:eq(0) > img:eq(0)').replaceWith(html);
            } else {
                t.find('div:eq(0)').append(html);
            }
        });
    }

    function drawImage2(_base64, replace) {
        return base64ImageSmartCrop(_base64, 483, 725).then(function (base64) {
            let html = $(`<img src="${base64}" class="img-fluid mb-4 rounded" alt="">`),
                t = $('#area-show-photo-post');

            html.on('click', function (event) {
                event.preventDefault();
                showModalImagePost(1);
            });

            if (replace) {
                t.find('div:eq(1) > img:eq(0)').replaceWith(html);
            } else {
                t.find('div:eq(1)').append(html);
            }
        });
    }

    function drawImage3(_base64, replace) {
        return base64ImageSmartCrop(_base64, 483, 725).then(function (base64) {
            let html = $(`<img src="${base64}" class="img-fluid mb-4 rounded" alt="">`),
                t = $('#area-show-photo-post' +
                    '');

            html.on('click', function (event) {
                event.preventDefault();
                showModalImagePost(2);
            });

            if (replace) {
                t.find('div:eq(0) > img:eq(1)').replaceWith(html);
            } else {
                t.find('div:eq(0)').append(html);
            }
        });
    }

    function drawImage4(_base64, replace, opacity) {
        return base64ImageSmartCrop(_base64, 483, 321).then(function (base64) {
            let html = $(`<img src="${base64}" class="img-fluid mb-4 rounded" alt="">`),
                t = $('#area-show-photo-post');

            if (opacity) {
                html = getImagesOpacity(opacity, base64);
            }

            html.on('click', function (event) {
                event.preventDefault();
                showModalImagePost(3);
            });

            if (replace) {
                t.find('div:eq(1)').children().eq(1).replaceWith(html);
            } else {
                t.find('div:eq(1)').append(html);
            }
        });
    }

    // other function
    function drawUpcomingBirthday(idUser) {
        $.getJSON(`user/${idUser}/friends/UpComingBirthday`, function (response) {
            f(response, 1);
        });

        function f(response, numberNextMonth) {
            let data = [],
                currentMonth = new Date().getMonth() + 1,
                area = $('#area-show-upcoming-birthday');
            data.push(getDataFromUBOF(response, "today"));
            for (let i = 0; i < numberNextMonth; i++) {
                data.push(getDataFromUBOF(response, getMonth(currentMonth + i)));
            }
            let flag = true,
                count = 0;

            data.forEach(({
                              key,
                              value
                          }) => {
                if (value && value.length !== 0) {
                    value.forEach(infoUser => {
                        if (count < 6) {
                            area.append(draw(infoUser, key));
                            count += 1;
                        }
                    });
                    flag = false;
                }
            });
            if (flag) {
                area.append($('<p>Nothing</p>'));
            }

            function draw({
                              id,
                              firstName,
                              lastName,
                              urlAvatar
                          }, content) {
                return $(`<li class="d-flex mb-4 align-items-center">
                               <a href="user?id=${id}"><img src="${urlAvatar}" style="max-width: 60px" alt="story-img" class="rounded-circle img-fluid"></a>
                               <div class="stories-data ml-3">
                                  <h5>${lastName + ' ' + firstName}</h5>
                                  <p class="mb-0">${content}</p>
                               </div>
                            </li>`);
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

    function drawUserMayBeKnow() {
        let area = $('#area-show-user-may-know');
        $.getJSON('/me/may-know?max-size=4', function (responses) {
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
                    let html = $(`<li class="d-flex mb-4 align-items-center">
                                       <a href="/user?id=${id}"><img src="${urlAvatar}" alt="story-img" class="rounded-circle img-fluid" style="max-width: 60px"></a>
                                       <div class="stories-data ml-3">
                                          <h5>${lastName + ' ' + firstName}</h5>
                                          <p class="mb-0">${temp}</p>
                                       </div>
                                    </li>`);

                    area.append(html);
                })
            }
        });
    }

    function base64ImageSmartCrop(base64, optionWidth, optionHeight) {
        return new Promise(resolve => {
            let i = new Image();
            i.src = base64;
            i.onload = function () {
                smartcrop.crop(i, {
                    width: optionWidth,
                    height: optionHeight
                }, function (e) {
                    let crop = e.topCrop,
                        canvas = $('<canvas>')[0],
                        ctx = canvas.getContext('2d');
                    canvas.width = optionWidth;
                    canvas.height = optionHeight;
                    ctx.drawImage(
                        i, crop.x, crop.y, crop.width, crop.height, 0, 0, canvas.width, canvas.height);

                    resolve(canvas.toDataURL());
                })
            }
        })
    }

    function getImagesOpacity(number, base64, styles) {
        return $(`<div style="position: relative; text-align: center; color: #FFFFFF"><img src="${base64}" class="img-fluid mb-4" alt="" style="opacity: 50%; ${styles}"><div style="position: absolute; top: 50%; left: 50%; font-size: 50px; transform: translate(-50%, -50%);">+${number}</div></div>`);
    }

    function addImageToModalShowImagePost(base64, i) {
        let r = $('#modal-show-image');

        let indicator = r.find('#carouselExampleIndicators > ol.carousel-indicators');

        $(`<li data-target="#carouselExampleIndicators" data-slide-to="${i}"></li>`).appendTo(indicator);

        let inner = r.find('#carouselExampleIndicators > div.carousel-inner');
        $(`<div class="carousel-item" data-interval="10000"><img src="${base64}" class="d-block w-100" alt="..."></div>`).appendTo(inner);
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

    function getPostForm() {
        let content = postModal.find('form > textarea').val().trim(),
            idFeeling = areaShowFeeling.find('#show-feeling span:eq(0)').attr('data-id-feeling'),
            idCheckin = areaShowFeeling.find('#show-checkin > a:eq(0)').attr('data-id-checkin'),
            tagFriends = [],
            photos = [];

        areaShowTagFriendsPost.find('.dropdown-menu .dropdown-item')
            .each((i, v) => {
                let idUser = $(v).find('a').attr('data-id-user');
                tagFriends.push(idUser);
            });
        $('#modal-show-image #carouselExampleIndicators .carousel-inner img').each((i, v) => {
            let img = $(v).attr('src').split(',')[1];
            photos.push(img);
        });

        return JSON.stringify({
            content,
            idFeeling,
            idCheckin,
            tagFriends,
            photos
        });
    }

    // clear and show data
    function showModalNotificationError(message) {
        let modalNotification = $('#modalNotification');
        modalNotification.find('.modal-body > p').text(message);
        modalNotification.modal('show');
    }

    function clearPhotoOfPost() {
        let t = $('#area-show-photo-post');
        t.find('div:eq(0)').empty();
        t.find('div:eq(1)').empty();
        inputAddPhotoToPost.val('');
    }

    function showModalImagePost(indexImage) {
        let r = $('#modal-show-image'),
            e = r.find('#carouselExampleIndicators');

        e.find(`ol.carousel-indicators > li`).removeClass('active');
        e.find(`div.carousel-inner > div`).removeClass('active');

        e.find(`ol.carousel-indicators > li:eq(${indexImage})`).addClass('active');
        e.find(`div.carousel-inner > div:eq(${indexImage})`).addClass('active');

        r.modal('show');
    }

    function clearModalShowImagePost() {
        let r = $('#modal-show-image'),
            e = r.find('#carouselExampleIndicators');

        e.find(`ol.carousel-indicators`).empty();
        e.find(`div.carousel-inner`).empty();
    }

    function clearDropdownMenuTagFriends() {
        areaShowTagFriendsPost.find('.dropdown-menu').empty();
        areaShowTagFriendsPost.find('div:eq(1) a.dropdown-toggle').text('Show Tags');
        areaShowTagFriendsPost.find('div:eq(1) a.dropdown-toggle').next().attr('data-count', 0);
        areaShowTagFriendsPost.find('div:eq(0) input').val('');
        areaShowTagFriendsPost.hide();
    }

    function clearAreaShowFeeling() {
        let e = areaShowFeeling.find('#show-feeling');
        e.find('span:eq(0)').text('');
        e.find('span:eq(0)').attr('data-id-feeling', '');
        e.find('.show-image-feeling').empty();
        e.removeClass('d-flex').addClass('d-none');

        let t = areaShowFeeling.find('#show-checkin');
        t.find('a:eq(0)').attr('data-id-checkin', '');
        t.find('a:eq(0)').text('');
        t.removeClass('d-flex').addClass('d-none');

        areaShowFeeling.removeClass('d-flex').addClass('d-none');
    }

    function clearContentTextAreaPost() {
        postModal.find('textarea').val('');
    }

    function showInputCheckin() {
        let html = $(`<input id="input-checkin" type="text" class="rounded w-100" style="border: none; background: #f5fbff" placeholder="Where are you?">`);
        setAutoCompleteInputPlaceLived(function (id, name) {
            let t = areaShowFeeling.find('#show-checkin');
            t.find('a:eq(0)').attr('data-id-checkin', id);
            t.find('a:eq(0)').text(name);
            t.find('a:eq(1)').on('click', function (event) {
                event.preventDefault();
                t.find('a:eq(0)').attr('data-id-checkin', '');
                t.find('a:eq(0)').text('');

                t.removeClass('d-flex').addClass('d-none');

                if (areaShowFeeling.find('#show-feeling span:eq(0)').attr('data-id-feeling') === '') {
                    areaShowFeeling.removeClass('d-flex').addClass('d-none');
                }

            });
            t.removeClass('d-none').addClass('d-flex');
            areaShowFeeling.removeClass('d-none').addClass('d-flex');
        }, html);

        html.on('blur', function (event) {
            event.preventDefault();
            let html1 = $(`<a href="#"><img src="images/small/10.png" alt="icon" class="img-fluid"> Check in</a>`);
            html1.on('click', function (event) {
                event.preventDefault();
                showInputCheckin.call(html1);
            });
            html.replaceWith(html1);
        });
        $(this).replaceWith(html);
        html.focus();
    }

    // set auto complete
    function setAutoCompleteInputFriend(callbackSelect, ...elements) {
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
                            id,
                            firstName,
                            lastName
                        } = ui.item,
                        fullName = lastName + ' ' + firstName;
                    e.val(fullName);
                    callbackSelect(id, fullName);
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

    function setAutoCompleteInputPlaceLived(callbackSelect, ...elements) {
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

                    $.getJSON('data/place-lived', request, function (data) {
                        cache[term] = data;
                        response(cache[term]);
                    })
                },
                select: function (event, ui) {
                    let {
                        id,
                        name
                    } = ui.item;
                    e.val(name);
                    callbackSelect(id, name);
                    return false;
                }
            }).autocomplete("instance")._renderItem = function (ul, {
                urlAvatar,
                name
            }) {
                return $('<li>')
                    .append(`<div class="row w-100"><img src="${urlAvatar}" alt="avatar" class="avatar-30 rounded-circle rol-4 ml-2"><p class="col-8 ml-1">${name}</p></div>`)
                    .appendTo(ul);
            };
        })
    }

    /* for post */
    let controller = new ScrollMagic.Controller();
    let scene = null;

    async function getPost(idUser, idPost) {
        let url1 = `user/${idUser}/timeline?number=8`;
        if (idPost) {
            url1 += `&idPostStart=${idPost}`;
        }

        load(url1, 100).then(_ => {
            scene.update();
        });


        async function load(url, timeout) {
            $.getJSON(url, function (responses) {
                let area = $('#area-show-post'),
                    htmls = [];
                responses.forEach(response => {
                    let html = drawCardPost(response);
                    area.append(html);
                    htmls.push(html);
                });
                if (htmls.length < 8) {
                    $('#loading-post').removeClass('loading-active');
                }
                setTimeout(function () {
                    htmls.forEach(html => html.show());
                }, timeout);
            });
        }
    }

    function triggerEventScrollPost(idUser) {
        scene = new ScrollMagic.Scene({
            triggerElement: "#loading-post",
            triggerHook: "onEnter"
        })
            .addTo(controller)
            .on("enter", function () {
                let idPost = $('#area-show-post > div').last().attr('data-post-id');
                getPost(idUser, idPost);
            });
    }

    function drawCardPost(postResponse, c) {
        let {
                id,
                infoAuthor,
                content,
                type,
                dateCreated,
                photos,
                tagFriends,
                feeling,
                checkin,
                myAction,
                ofMe,
                othersInfo
            } = postResponse,
            time = distanceTime(new Date(), new Date(dateCreated)),
            html = $(`<div class="col-sm-12" style="display: none" data-post-id="${id}">
                        <div class="iq-card iq-card-block iq-card-stretch iq-card-height">
                           <div class="iq-card-body">
                              <div class="user-post-data">
                                 <div class="d-flex flex-wrap">
                                    <div class="media-support-user-img mr-3">
                                       <img class="rounded-circle img-fluid" src="${infoAuthor.urlAvatar}" alt="">
                                    </div>
                                    <div class="media-support-info mt-2">
                                       <h5 class="mb-0 d-inline-block"><a href="user?id=${infoAuthor.id}" class="">${infoAuthor.lastName + ' ' + infoAuthor.firstName}</a></h5>
                                       <div class="mb-0 d-inline-block title-header-post"></div>
                                       <p class="mb-0 text-primary">${time} ago</p>
                                    </div>
                                 </div>
                              </div>
                              <div class="mt-3">
                                 <p style="overflow: hidden">${content ? resolverContent(content) : ''}</p>
                              </div>
                              <div class="user-post">
                                 
                              </div>
                              <div class="comment-area mt-3">
                                 <div class="d-flex justify-content-between align-items-center">
                                    <div class="like-block position-relative d-flex align-items-center">
                                       <div class="d-flex align-items-center">
                                          <div class="like-data">
                                             <div class="dropdown">
                                                <span class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" role="button">
                                                <img src="images/icon/14.png" class="img-fluid" alt="">
                                                </span>
                                                <div class="dropdown-menu">
                                                   <a data-id-reacted="LIKE" class="ml-2 mr-2" href="#" data-toggle="tooltip" data-placement="top" title="Like" data-original-title="Like"><img src="images/icon/01.png" class="img-fluid" alt=""></a>
                                                   <a data-id-reacted="LOVE" class="mr-2" href="#" data-toggle="tooltip" data-placement="top" title="Love" data-original-title="Love"><img src="images/icon/02.png" class="img-fluid" alt=""></a>
                                                   <a data-id-reacted="HAPPY" class="mr-2" href="#" data-toggle="tooltip" data-placement="top" title="Happy" data-original-title="Happy"><img src="images/icon/03.png" class="img-fluid" alt=""></a>
                                                   <a data-id-reacted="HAHA" class="mr-2" href="#" data-toggle="tooltip" data-placement="top" title="Haha" data-original-title="HaHa"><img src="images/icon/04.png" class="img-fluid" alt=""></a>
                                                   <a data-id-reacted="THINK" class="mr-2" href="#" data-toggle="tooltip" data-placement="top" title="Think" data-original-title="Think"><img src="images/icon/05.png" class="img-fluid" alt=""></a>
                                                   <a data-id-reacted="SADE" class="mr-2" href="#" data-toggle="tooltip" data-placement="top" title="Sade" data-original-title="Sade"><img src="images/icon/06.png" class="img-fluid" alt=""></a>
                                                   <a data-id-reacted="LOVELY" class="mr-2" href="#" data-toggle="tooltip" data-placement="top" title="Lovely" data-original-title="Lovely"><img src="images/icon/07.png" class="img-fluid" alt=""></a>
                                                </div>
                                             </div>
                                          </div>
                                          <div class="total-like-block ml-2 mr-3">
                                             <div class="dropdown">
                                                <span class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" role="button">
                                                </span>
                                                <div class="dropdown-menu">
                                                </div>
                                             </div>
                                          </div>
                                       </div>
                                       <div class="total-comment-block">
                                          <div class="dropdown">
                                             <span class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" role="button">
                                             </span>
                                             <div class="dropdown-menu">
                                             </div>
                                          </div>
                                       </div>
                                    </div>
                                    <div class="share-block d-flex align-items-center feather-icon mr-3">
                                       <a href="#"><i class="ri-share-line"></i>
                                       <span class="ml-1"></span></a>
                                    </div>
                                 </div>
                                 <hr>
                                 <ul class="post-comments p-0 m-0">
                                    <li class="mb-2" id="area-show-comment-${id}">
                                    </li>
                                    <a href="#" id="show-view-more-comment-post-${id}" style="display: block; text-align: center">View More</a>
                                 </ul>
                                 <form class="comment-text d-flex align-items-center mt-3" id="comment-post-${id}">
                                    <input type="text" class="form-control rounded">
                                    <div class="comment-attagement d-flex">
                                       <a href="#"><i class="ri-user-smile-line"></i></a>
                                       <button type="submit" class="btn btn-link"><i class="ri-send-plane-fill"></i></button>
                                    </div>
                                 </form>
                              </div>
                           </div>
                        </div>
                     </div>`),
            titleHeader = html.find('div.mb-0.d-inline-block.title-header-post'),
            areaShowPhotos = html.find('div.iq-card-body > div.user-post'),
            areaShowComment = html.find('div.comment-area');

        drawHeader();
        drawPhoto();
        drawAreaComment(c);
        return html;

        function drawHeader() {
            if (type === 'SHARED_POST') {
                let {
                    arg1,
                    arg2
                } = othersInfo;
                titleHeader.append(`Share <a href="${arg2}">${arg1}</a> Post`);
            } else if (type === 'CHANGE_AVATAR') {
                titleHeader.append('Change Profile Picture');
            } else {
                let text = 'is';
                if (feeling) {
                    text += ` feeling ${feeling.name}`;
                }
                if (checkin) {
                    text += ` at ${checkin.name}`;
                }
                if (tagFriends && tagFriends.length !== 0) {
                    let temp = $(`<div class="total-comment-block d-inline-block"><div class="dropdown"><h6 class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" role="button">${tagFriends.length + ' others'}</h6><div class="dropdown-menu"></div></div></div>`);
                    tagFriends.forEach(s => {
                        let {
                            firstName,
                            lastName
                        } = s;
                        temp.find('div.dropdown-menu')
                            .append(`<a class="dropdown-item" href="#">${lastName + ' ' + firstName}</a>`);
                    });
                    text += ` with ${$('<div>').append(temp).html()}`;
                }

                if (text !== 'is') {
                    titleHeader.append(text);
                }
            }
            if (ofMe && type !== 'SHARED_POST') {
                let temp = $(`<div class="iq-card-post-toolbar ml-2">
                                  <div class="dropdown">
                                     <span class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" role="button">
                                     <i class="ri-more-fill"></i>
                                     </span>
                                     <div class="dropdown-menu m-0 p-0" style="">
                                        <a class="dropdown-item p-3" href="#">
                                           <div class="d-flex align-items-top">
                                              <div class="icon font-size-20"><i class="ri-delete-bin-7-line"></i></div>
                                              <div class="data ml-2">
                                                 <h6>Delete</h6>
                                                 <p class="mb-0">delete this post</p>
                                              </div>
                                           </div>
                                        </a>
                                     </div>
                                  </div>
                               </div>`);

                html.find('div.user-post-data > div.d-flex.flex-wrap').append(temp);
                temp.find('a:eq(0)').on('click', function (event) {
                    event.preventDefault();
                    showModalAcceptAction(function () {
                        $.ajax({
                            url: `post/${id}`,
                            type: 'DELETE',
                            dataType: 'text'
                        }).done(function (message) {
                            showModalNotificationError(message);
                            html.remove();
                        })
                    })
                })

            }
        }

        function drawPhoto() {
            if (type === 'CHANGE_AVATAR') {
                areaShowPhotos.addClass('text-center');
                let html = $(`<a href="#"><img src="${photos[0]}" alt="post-image" class="img-fluid profile-img"></a>`);
                html.appendTo(areaShowPhotos);
                return;
            }
            if (photos.length > 0) {
                if (photos.length === 1) {
                    let img = $(`<img src="${photos[0]}" alt="post-image" class="img-fluid rounded w-100">`),
                        wrapper = $(`<a href="#"></a>`);
                    wrapper.append(img);
                    wrapper.appendTo(areaShowPhotos);

                    img.on('click', function (event) {
                        event.preventDefault();
                        callModalShowImage(0, photos);
                    })

                } else if (photos.length === 2) {
                    let temp = $(`<div class="d-flex"><div class="col-md-6"><a href="#"></a></div><div class="col-md-6"><a href="#"></a></div></div>`);
                    areaShowPhotos.append(temp);

                    drawImage(temp, 277, 373, 0, photos);
                    drawImage(temp, 277, 373, 1, photos);
                } else if (photos.length > 2) {
                    let temp = $(`<div class="d-flex"><div class="col-md-6"><a href="#"></a></div><div class="col-md-6 row m-0 p-0"><div class="col-sm-12"><a href="#"></a></div><div class="col-sm-12 mt-3"><a href="#"></a></div></div></div>`);
                    areaShowPhotos.append(temp);
                    drawImage(temp, 277, 373, 0, photos);
                    drawImage(temp, 277, 179, 1, photos);
                    drawImage(temp, 277, 179, 2, photos, true);
                }
            } else {
                let regex = [...content.matchAll(/((?:https?:)?\/\/)?((?:www|m)\.)?((?:youtube\.com|youtu.be))(\/(?:[\w\-]+\?v=|embed\/|v\/)?)([\w\-]+)(\S+)?/g)];
                let linkYoutube = regex[0];
                if (linkYoutube) {
                    let id = linkYoutube['5'],
                        link = `https://www.youtube.com/embed/${id}`;
                    $(` <div class="embed-responsive embed-responsive-16by9">
                            <iframe class="embed-responsive-item" src="${link}" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
                        </div>`)
                        .appendTo(html.find('.user-post'));
                }
            }
        }

        function drawImage(temp, width, height, i, photos, opacity) {
            let e = $(`<img src="${photos[i]}" alt="post-image" class="rounded w-100" style="height: ${height}px; width: ${width}px; object-fit: cover">`);
            if (opacity) {
                e = photos.length === 3 ? e : getImagesOpacity(photos.length - 3, photos[i], 'height: 179px; width: 277px; object-fit: cover;');
            }
            temp.find(`a:eq(${i})`).append(e);
            e.on('click', function (event) {
                event.preventDefault();
                callModalShowImage(i, photos);
            })
        }

        function drawAreaComment(c) {
            let map = {
                    "NONE": "images/icon/14.png",
                    "LIKE": "images/icon/01.png",
                    "LOVE": "images/icon/02.png",
                    "HAPPY": "images/icon/03.png",
                    "HAHA": "images/icon/04.png",
                    "THINK": "images/icon/05.png",
                    "SADE": "images/icon/06.png",
                    "LOVELY": "images/icon/07.png"
                },
                {
                    numberFeelings,
                    numberComments,
                    numberShares
                } = othersInfo,
                t = areaShowComment.find('div.total-like-block div.dropdown-menu'),
                t2 = areaShowComment.find('div.like-block div.total-comment-block div.dropdown-menu');

            areaShowComment.find('div.like-data div.dropdown span.dropdown-toggle img')
                .attr('src', map[myAction])
                .attr('data-id-my-reacted', myAction)
                .on('click', function (event) {
                    event.preventDefault();

                    let action = $(this).attr('data-id-my-reacted'),
                        temp = areaShowComment.find('div.total-like-block div.dropdown > span');

                    $.ajax({
                        url: `post/${id}/reacted?action=${action}`,
                        type: 'POST',
                        dataType: 'json'
                    }).done(function ({
                                          numberReacted,
                                          userReacted
                                      }) {
                        temp.text(`${numberReacted} reacted`);

                        areaShowComment.find('div.like-data div.dropdown span.dropdown-toggle img')
                            .attr('src', map["NONE"])
                            .attr('data-id-my-reacted', "NONE");

                        t.empty();
                        let _temp = JSON.parse(userReacted);
                        if (_temp.length !== 0) {
                            _temp.forEach(({
                                               id,
                                               firstName,
                                               lastName
                                           }) => {
                                $(`<a class="dropdown-item" href="user?id=${id}">${lastName + ' ' + firstName}</a>`)
                                    .appendTo(t);
                            });
                            if (_temp.length > 15) {
                                $(`<a class="dropdown-item" href="#">Other</a>`)
                                    .appendTo(t);
                            }
                            t.show();
                        } else {
                            t.hide();
                        }
                    })
                });

            areaShowComment.find('div.total-like-block div.dropdown > span').text(`${numberFeelings} reacted`)
                .attr('data-number-reacted', numberFeelings);
            $.getJSON(`post/${id}/reacted?page=0&number=15`, function (response) {
                if (response.length !== 0) {
                    response.forEach(({
                                          id,
                                          firstName,
                                          lastName
                                      }) => {
                        $(`<a class="dropdown-item" href="user?id=${id}">${lastName + ' ' + firstName}</a>`)
                            .appendTo(t);
                    });
                    if (response.length > 15) {
                        $(`<a class="dropdown-item" href="#">Other</a>`)
                            .appendTo(t);
                    }
                    t.show();
                } else {
                    t.hide();
                }
            });

            areaShowComment.find('div.like-block div.total-comment-block div.dropdown > span').text(`${numberComments} comments`)
                .attr('data-number-comment', numberComments);
            $.getJSON(`post/${id}/reacted?page=0&number=15&action=COMMENT`, function (response) {
                if (response.length !== 0) {
                    response.forEach(({
                                          id,
                                          firstName,
                                          lastName
                                      }) => {
                        $(`<a class="dropdown-item" href="user?id=${id}">${lastName + ' ' + firstName}</a>`)
                            .appendTo(t2);
                    });
                    if (response.length === 15) {
                        $(`<a class="dropdown-item" href="#">Other</a>`)
                            .appendTo(t2);
                    }
                } else {
                    t2.remove();
                }
            });

            if (type === 'NEW_POST' || type === 'SHARED_POST') {
                if (type === 'NEW_POST') {
                    areaShowComment.find('div.share-block span').text(`${numberShares} Share`)
                        .attr('data-number-share', numberShares);
                }
                areaShowComment.find('div.share-block a').on('click', function (event) {
                    event.preventDefault();
                    showModalAcceptAction(function () {
                        $.ajax({
                            url: `post/${id}/share`,
                            type: 'POST',
                            dataType: 'text'
                        }).done(function (message) {
                            showModalNotificationError(message);
                        })
                    })
                });
            } else if (type === 'CHANGE_AVATAR') {
                areaShowComment.find('div.share-block').addClass('d-none').removeClass('d-flex');
            }

            // event click button
            html.find('div.comment-area div.like-data div.dropdown-menu a').on('click', function (event) {
                event.preventDefault();
                let action = $(this).attr('data-id-reacted'),
                    temp = areaShowComment.find('div.total-like-block div.dropdown > span');
                $.ajax({
                    url: `post/${id}/reacted?action=${action}`,
                    type: 'POST',
                    dataType: 'json'
                }).done(function ({
                                      numberReacted,
                                      result,
                                      userReacted
                                  }) {
                    let urlAction = result === 'true' ? map[action] : map["NONE"];

                    temp.text(`${numberReacted} reacted`);
                    areaShowComment.find('div.like-data div.dropdown span.dropdown-toggle img')
                        .attr('src', urlAction)
                        .attr('data-id-my-reacted', action);

                    t.empty();
                    let _temp = JSON.parse(userReacted);
                    if (_temp.length !== 0) {
                        _temp.forEach(({
                                           id,
                                           firstName,
                                           lastName
                                       }) => {
                            $(`<a class="dropdown-item" href="user?id=${id}">${lastName + ' ' + firstName}</a>`)
                                .appendTo(t);
                        });
                        if (_temp.length > 15) {
                            $(`<a class="dropdown-item" href="#">Other</a>`)
                                .appendTo(t);
                        }
                        t.show();
                    } else {
                        t.hide();
                    }
                })
            });
            html.find(`#show-view-more-comment-post-${id}`).on('click', function (event) {
                event.preventDefault();
                let fromComment = $(`#area-show-comment-${id} > div`).last().attr('data-id-comment');
                $.getJSON(`post/${id}/comment/${fromComment}?number=4`, function (response) {
                    if (response.length < 4) {
                        html.find(`#show-view-more-comment-post-${id}`).hide();
                    }
                    response.forEach(e => html.find(`#area-show-comment-${id}`).append(drawCardComment(e, id)));
                })
            });
            areaShowComment.find(`#comment-post-${id}`).on('submit', function (event) {
                event.preventDefault();
                let content = $(this).find('input:eq(0)').val();
                $.ajax({
                    url: `post/${id}/comment`,
                    type: 'POST',
                    contentType: 'text/plain',
                    dataType: 'json',
                    data: content
                }).done(function (response) {
                    let m = drawCardComment(response, id);
                    html.find(`#area-show-comment-${id}`).prepend(m);
                    let temp = areaShowComment.find('div.like-block div.total-comment-block div.dropdown > span');
                    temp.attr('data-number-comment', +temp.attr('data-number-comment') + 1);
                    temp.text(`${temp.attr('data-number-comment')} comments`);
                });
                $(this).find('input:eq(0)').val('');
            });

            // show emoji
            areaShowComment.find(`#comment-post-${id} a:eq(0)`).on('click', function (event) {
                event.preventDefault();
                showModalChooseEmoji(function (emoji) {
                    let temp = areaShowComment.find(`#comment-post-${id} input`).val();
                    areaShowComment.find(`#comment-post-${id} input`).val(temp + emoji);
                })
            });

            // first load comment
            if (c) {
                $.getJSON(`post/comment/${c}`, function (response) {
                    let {
                            beforeLevel0,
                            level0,
                            level1
                        } = response,
                        objBeforeLevel0 = JSON.parse(beforeLevel0),
                        objLevel0 = JSON.parse(level0),
                        area = html.find(`#area-show-comment-${id}`);

                    for (let i = objBeforeLevel0.length - 1; i >= 0; i--) {
                        area.append(drawCardComment(objBeforeLevel0[i]));
                    }

                    area.append(drawCardComment(objLevel0));

                    if (level1) {
                        let objLevel1 = JSON.parse(level1),
                            {
                                numberLeft
                            } = response;
                        for (let i = objLevel1.length - 1; i >= 0; i--) {
                            drawOneCommentInside(html.find(`#area-show-comment-inside-${objLevel0.id}`), objLevel1[i]);
                        }
                        if (+numberLeft > 0) {
                            let t = $(`#show-view-more-comment-inside-1-${objLevel0.id}`);
                            t.attr('data-count', numberLeft);
                            t.html(`<i class="ri-more-fill mr-1"></i> See ${numberLeft} answers`);
                        } else {
                            $(`#show-view-more-comment-inside-1-${objLevel0.id}`).removeClass('d-flex').addClass('d-none');
                        }
                        $(`#form-post-comment-inside-${objLevel0.id}`).addClass('d-flex').removeClass('d-none');
                        $('html, body').animate({
                            scrollTop: $(`li[data-id-comment="${c}"]`).position().top
                        }, "slow");
                        $(`li[data-id-comment="${c}"`).attr('style', 'background-color:rgb(251, 251, 251); padding:5px; border-radius:10px');
                    } else {
                        $(`div[data-id-comment="${objLevel0.id}"]`).attr('style', 'background-color:rgb(251, 251, 251); padding:5px; border-radius:10px');
                    }
                })
            } else {
                $.getJSON(`post/${id}/comment?page=0&number=4`, function (response) {
                    response.forEach(e => html.find(`#area-show-comment-${id}`).append(drawCardComment(e, id)));
                    if (response.length < 4) {
                        html.find(`#show-view-more-comment-post-${id}`).hide();
                    }
                });
            }
        }

        function drawCardComment(commentResponse) {
            let {
                    id,
                    infoAuthor,
                    content,
                    dateCreated,
                    iliked,
                    ofMe,
                    numberCommentsInside,
                    numberLiked,
                    someUserLiked
                } = commentResponse,
                timeAgo = distanceTime(new Date(), new Date(dateCreated)), {
                    firstName,
                    lastName,
                    urlAvatar
                } = infoAuthor,
                html = $(`<div class="d-flex flex-wrap row mb-3" data-id-comment="${id}">
                                          <div class="user-img" style="width: 10%">
                                             <img src="${urlAvatar}" alt="userimg" class="avatar-35 rounded-circle img-fluid d-flex mx-auto">
                                          </div>
                                          <div class="comment-data-block" style="width: 90%">
                                             <h6>
                                                <a href="user?id=${infoAuthor.id}">${lastName + ' ' + firstName}</a>

                                             </h6>
                                             <p class="mb-0" style="overflow: hidden">${content}</p>
                                             <div class="d-flex flex-wrap align-items-center comment-activity mb-3">
                                                <div class="total-comment-block mr-2" id="total-comment-block-${id}">
                                                    <div class="dropdown">
                                                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" role="button">${numberLiked}</a>
                                                        <div class="dropdown-menu">
                                                        </div>
                                                    </div>
                                                </div>
                                                <a href="#" id="like-comment-${id}">Like</a>
                                                <a href="#" id="reply-comment-${id}">reply</a>
                                                <span> ${timeAgo} ago</span>
                                             </div>
                                             <ul class="post-comments p-0 m-0 mt-3" id="area-show-comment-inside-${id}">
                                             </ul>
                                             <a href="#" class="d-none mt-2 ml-2" id="show-view-more-comment-inside-1-${id}" data-count="${numberCommentsInside}"><i class="ri-more-fill mr-1"></i> See ${numberCommentsInside} answers</a>
                                             <form class="comment-text d-none align-items-center mt-3" id="form-post-comment-inside-${id}">
                                                <input type="text" class="form-control rounded">
                                                <div class="comment-attagement d-flex">
                                                   <a href="#"><i class="ri-user-smile-line"></i></a>
                                                   <button type="submit" class="btn btn-link"><i class="ri-send-plane-fill"></i></button>
                                                </div>
                                             </form>
                                          </div>
                                       </div>`);

            drawTotalUserLikeComment(numberLiked, someUserLiked);

            function drawTotalUserLikeComment(numberLiked, someUserLiked) {
                let temp = html.find(`#total-comment-block-${id} > .dropdown > .dropdown-menu`);
                if (numberLiked > 0) {
                    temp.empty();
                    temp.prev().text(numberLiked);

                    someUserLiked.forEach(({
                                               id,
                                               firstName,
                                               lastName
                                           }) => {
                        temp.append($(`<a class="dropdown-item" href="user?id=${id}">${lastName + ' ' + firstName}</a>`));
                    });
                    html.find(`#total-comment-block-${id}`).show();
                } else {
                    temp.empty();
                    temp.prev().text(numberLiked);
                    html.find(`#total-comment-block-${id}`).hide();
                }
            }

            function createButtonLikeForComment(flag) {
                let e = 'Like'; // liked
                if (flag) {
                    e = $('<b>Like</b>');
                }
                html.find(`#like-comment-${id}`)
                    .empty().append(e);
            }

            createButtonLikeForComment(iliked);
            if (numberCommentsInside > 0) {
                html.find(`#show-view-more-comment-inside-1-${id}`)
                    .addClass('d-flex').removeClass('d-none');
            }
            if (ofMe) {
                let e = $(`<a href="#" style="float: right; margin-right: 10px" title="Delete Comment"><i class="ri-delete-bin-line"></i></a>`);
                html.find('div:eq(1) > h6:eq(0)').append(e);
                e.on('click', function (event) {
                    event.preventDefault();
                    showModalAcceptAction(function () {
                        $.ajax({
                            url: `post/comment/${id}`,
                            type: 'DELETE',
                            dataType: 'text'
                        }).done(function (response) {
                            html.remove();
                        });
                    });
                });
            }
            html.find(`#show-view-more-comment-inside-1-${id}`).on('click', function (event) {
                event.preventDefault();
                drawCommentInside(id, $(this), html.find(`#area-show-comment-inside-${id}`));
                html.find(`#form-post-comment-inside-${id}`).addClass('d-flex').removeClass('d-none');
            });
            html.find(`#like-comment-${id}`).on('click', function (event) {
                event.preventDefault();
                $.ajax({
                    url: `post/comment/${id}/reacted`,
                    type: 'POST',
                    dataType: 'json'
                }).done(function ({
                                      status,
                                      numberLiked,
                                      someUserLiked
                                  }) {
                    someUserLiked = JSON.parse(someUserLiked);
                    if (status === 'true') {
                        createButtonLikeForComment(true);
                    } else {
                        createButtonLikeForComment(false);
                    }
                    drawTotalUserLikeComment(numberLiked, someUserLiked);
                })
            });
            html.find(`#reply-comment-${id}`).on('click', function (event) {
                event.preventDefault();
                html.find(`#form-post-comment-inside-${id}`).addClass('d-flex').removeClass('d-none');
            });

            html.find(`#form-post-comment-inside-${id} a:eq(0)`).on('click', function (event) {
                event.preventDefault();
                let tagInput = $(this).parent().prev();
                showModalChooseEmoji(function (emoji) {
                    tagInput.val(tagInput.val() + emoji);
                })
            });
            html.find(`#form-post-comment-inside-${id}`).on('submit', function (event) {
                event.preventDefault();
                let content = $(this).find('input').val(),
                    _toIdComment = id,
                    patterCheck = $(this).find('input').attr('data-patter-user-reply');

                if (content.match(`^${patterCheck}`)) {
                    let e1 = $(this).find('input').attr('data-idComment-reply');
                    _toIdComment = e1 ? e1 : id;
                }

                $.ajax({
                    url: `post/comment/${_toIdComment}/comment`,
                    type: 'POST',
                    contentType: 'text/plain',
                    dataType: 'json',
                    data: content
                }).done(function (response) {
                    drawOneCommentInside(html.find(`#area-show-comment-inside-${id}`), response, true);
                });
                $(this).find('input').val('');
            });

            return html;
        }

        function drawCommentInside(id, button, area) {
            let fromComment = $(`#area-show-comment-inside-${id} > li`).last().attr('data-id-comment'),
                url = `post/comment/${id}/comment?page=0&number=4`;
            if (fromComment) {
                url = `post/comment/${id}/comment/${fromComment}?number=${4}`;
            }
            $.getJSON(url, function (response) {
                for (let run = response.length - 1; run >= 0; run--) {
                    drawOneCommentInside(area, response[run]);
                }
                if (response.length < 4) {
                    button.addClass('d-none').removeClass('d-flex');
                } else {
                    button.attr('data-count', +button.attr('data-count') - response.length);
                    button.html(`<i class="ri-more-fill mr-1"></i> See ${button.attr('data-count')} answers`);
                    if (+button.attr('data-count') === 0) {
                        button.addClass('d-none').removeClass('d-flex');
                    }
                }
            })

        }

        function drawOneCommentInside(area, commentResponse, firstPlace) {
            let {
                id,
                infoAuthor,
                infoUserOrigin,
                content,
                dateCreated,
                iliked,
                ofMe,
                numberLiked,
                someUserLiked
            } = commentResponse;

            if (infoUserOrigin) {
                if (content.match(/^#.+?#/)) {
                    content = content.replace(/^#.+?#/, '');
                }
                content = `<a href="/user?id=${infoUserOrigin.id}">${infoUserOrigin.lastName + ' ' + infoUserOrigin.firstName}</a> ${content}`;
            }

            let html = $(`<li class="mb-3" data-id-comment="${id}">
                               <div class="rounded-10 d-flex flex-wrap row">
                                  <div class="user-img" style="width: 10%">
                                     <img src="${infoAuthor.urlAvatar}" alt="userimg" class="avatar-35 rounded-circle img-fluid d-flex mx-auto">
                                  </div>
                                  <div class="comment-data-block" style="width: 90%">
                                     <a href="/user?id=${infoAuthor.id}"><h6>${infoAuthor.lastName + ' ' + infoAuthor.firstName}</h6></a>
                                     <p class="mb-0" style="overflow: hidden">${content}</p>
                                     <div class="d-flex flex-wrap align-items-center comment-activity">
                                         <div class="total-comment-block mr-2" id="total-comment-block-${id}" style="display: none">
                                            <div class="dropdown">
                                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" role="button">${numberLiked}</a>
                                                <div class="dropdown-menu">
                                                </div>
                                            </div>
                                        </div>
                                        <a href="#" id="like-comment-${id}">Like</a>   
                                        <a href="#" id="reply-comment-${id}">Reply</a>
                                        <span> ${distanceTime(new Date(), new Date(dateCreated))} </span>
                                     </div>
                                  </div>
                               </div>
                            </li>`);

            let e = html.find(`#total-comment-block-${id}`);
            html.find(`#like-comment-${id}`).on('click', function (event) {
                event.preventDefault();
                $.ajax({
                    url: `post/comment/${id}/reacted`,
                    type: 'POST',
                    dataType: 'json'
                }).done(function ({
                                      status,
                                      numberLiked,
                                      someUserLiked
                                  }) {
                    e.find('a:eq(0)').text(numberLiked);

                    e.find('div.dropdown-menu').empty();
                    JSON.parse(someUserLiked).forEach(({
                                                           id,
                                                           firstName,
                                                           lastName
                                                       }) => {
                        e.find('div.dropdown-menu').append($(`<a class="dropdown-item" href="user?id=${id}">${lastName + ' ' + firstName}</a>`));
                    });

                    if (status === 'true') {
                        html.find(`#like-comment-${id}`).html($(`<b>Like</b>`));
                    } else {
                        html.find(`#like-comment-${id}`).text('Like');
                    }

                    if (numberLiked > 0) {
                        e.show();
                    } else {
                        e.hide();
                    }
                })
            });
            if (iliked === true) {
                html.find(`#like-comment-${id}`).html($(`<b>Like</b>`));
            } else {
                html.find(`#like-comment-${id}`).text('Like');
            }
            if (ofMe) {
                let e = $(`<a href="#" style="float: right; margin-right: 10px" title="Delete Comment"><i class="ri-delete-bin-line"></i></a>`);
                html.find('h6:eq(0)').append(e);
                e.on('click', function (event) {
                    event.preventDefault();
                    showModalAcceptAction(function () {
                        $.ajax({
                            url: `post/comment/${id}`,
                            type: 'DELETE',
                            dataType: 'text'
                        }).done(function (response) {
                            html.remove();
                        });
                    })
                })
            }
            if (+numberLiked !== 0) {
                e.show();
                someUserLiked.forEach(({
                                           id,
                                           firstName,
                                           lastName
                                       }) => {
                    e.find('div.dropdown-menu').append($(`<a class="dropdown-item" href="user?id=${id}">${lastName + ' ' + firstName}</a>`));
                });
            }

            html.find(`#reply-comment-${id}`).on('click', function (event) {
                event.preventDefault();
                let idParentComment = area.attr('id').replace('area-show-comment-inside-', ''),
                    inputComment = $(`#form-post-comment-inside-${idParentComment} input:eq(0)`),
                    fullName = infoAuthor.lastName + ' ' + infoAuthor.firstName;

                inputComment.val(`#${fullName}# `);
                inputComment.attr('data-patter-user-reply', `#${fullName}#`);
                inputComment.attr('data-idComment-reply', id);

                $('html, body').animate({
                    scrollTop: inputComment.offset().top + $(window).height() / 2
                }, 1000);
            });
            firstPlace ? area.prepend(html) : area.append(html);
        }

        function resolverContent(content) {
            // convert url to tag a
            let temp = content.replace(/(http)?(s)?(:\/\/)?(([a-zA-Z])([-\w]+\.)+([^\s\\.]+[^\s]*)+[^,.\s])/g, function (url) {
                return `<a href="${url}" target="_blank">${url}</a>`;
            });
            // convert inline to tag br
            temp = temp.replace(/\n/g, function (c) {
                return `<br>`;
            });
            return temp;
        }

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

    function callModalShowImage(indexActive, photos) {
        let modal = $('#modal-show-image2');
        new Promise(resolve => {
            let indicator = modal.find('#carouselExampleIndicators2 > ol.carousel-indicators'),
                inner = modal.find('#carouselExampleIndicators2 > div.carousel-inner');

            indicator.empty();
            inner.empty();

            for (let i = 0; i < photos.length; i++) {
                $(`<li data-target="#carouselExampleIndicators2" data-slide-to="${i}"></li>`).appendTo(indicator);
                $(`<div class="carousel-item" data-interval="10000"><img src="${photos[i]}" class="d-block rounded w-100" alt="..."></div>`).appendTo(inner);
            }
            resolve({
                indicator,
                inner
            });
        }).then(function ({
                              indicator,
                              inner
                          }) {
            indicator.find(`li:eq(${indexActive})`).addClass('active');
            inner.find(`div:eq(${indexActive})`).addClass('active');

            modal.modal('show');
        })
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

    // scroll to top
    function triggerEventBackToTop(idUser) {
        let btn = $('#back-top-top');

        $(window).scroll(function () {
            if ($(window).scrollTop() > 300) {
                btn.addClass('show');
            } else {
                btn.removeClass('show');
            }
        });

        btn.on('click', function (e) {
            e.preventDefault();
            $('html, body').animate({
                scrollTop: 0
            }, '500');
            setTimeout(function () {
                $('#loading-post').addClass('loading-active');
                $('#area-show-post').empty();
                scene.remove();
                triggerEventScrollPost(idUser);
            }, 500);
        });
    }


})(jQuery);