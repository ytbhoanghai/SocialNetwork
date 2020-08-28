(function (jQuery) {

    'use strict';

    activeTabPage('ytb-page-post');

    function activeTabPage(name) {
        $('.ytb-page-tab').removeClass('active');
        $(`#${name}`).addClass('active');
    }

    let id = getUrlParameter('id'),
        c = getUrlParameter('c');

    $.getJSON(`post/${id}`, function (data) {
        drawCardPost(data, c).appendTo('#single-post');
    });

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
            html = $(`<div class="col-sm-12">
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
            if (ofMe) {
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

                    $('html, body').animate({
                        scrollTop: m.position().top
                    }, "slow");
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
                        $('html, body').animate({
                            scrollTop: $(`div[data-id-comment="${c}"]`).position().top
                        }, "slow");
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
                let temp = html.find(`#form-post-comment-inside-${id}`);
                temp.find('input').attr('data-patter-user-reply', '');
                temp.find('input').attr('data-idComment-reply', '');
                temp.find('input').val('');
                temp.addClass('d-flex').removeClass('d-none');
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
                content,
                dateCreated,
                iliked,
                ofMe,
                numberLiked,
                someUserLiked,
                infoUserOrigin
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
                                     <h6>${infoAuthor.lastName + ' ' + infoAuthor.firstName}</h6>
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
                    scrollTop: inputComment.offset().top - ( $(window).height() - $(this).outerHeight(true) ) / 2
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

    function showModalNotificationError(message) {
        let modalNotification = $('#modalNotification');
        modalNotification.find('.modal-body > p').text(message);
        modalNotification.modal('show');
    }

    function callModalShowImage(indexActive, photos) {
        let modal = $('#modal-show-image');
        new Promise(resolve => {
            let indicator = modal.find('#carouselExampleIndicators > ol.carousel-indicators'),
                inner = modal.find('#carouselExampleIndicators > div.carousel-inner');

            indicator.empty();
            inner.empty();

            for (let i = 0; i < photos.length; i++) {
                $(`<li data-target="#carouselExampleIndicators" data-slide-to="${i}"></li>`).appendTo(indicator);
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

    function getImagesOpacity(number, base64, styles) {
        return $(`<div style="position: relative; text-align: center; color: #FFFFFF"><img src="${base64}" class="img-fluid mb-4" alt="" style="opacity: 50%; ${styles}"><div style="position: absolute; top: 50%; left: 50%; font-size: 50px; transform: translate(-50%, -50%);">+${number}</div></div>`);
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
}(jQuery));