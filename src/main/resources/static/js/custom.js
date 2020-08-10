/*----------------------------------------------
Index Of Script
------------------------------------------------

:: Tooltip
:: Sidebar Widget
:: Magnific Popup
:: Ripple Effect
:: Page faq
:: Page Loader
:: Owl Carousel
:: Select input
:: Search input
:: Scrollbar
:: Counter
:: slick
:: Progress Bar
:: Page Menu
:: Page Loader
:: Wow Animation
:: Mail Inbox
:: Chat
:: Todo
:: Form Validation
:: Sidebar Widget
:: Flatpicker

------------------------------------------------
Index Of Script
----------------------------------------------*/

(function (jQuery) {


    "use strict";

    jQuery(document).ready(function () {

        /*---------------------------------------------------------------------
        Tooltip
        -----------------------------------------------------------------------*/
        jQuery('[data-toggle="popover"]').popover();
        jQuery('[data-toggle="tooltip"]').tooltip();

        /*---------------------------------------------------------------------
        Magnific Popup
        -----------------------------------------------------------------------*/
        jQuery('.popup-gallery').magnificPopup({
            delegate: 'a.popup-img',
            type: 'image',
            tLoading: 'Loading image #%curr%...',
            mainClass: 'mfp-img-mobile',
            gallery: {
                enabled: true,
                navigateByImgClick: true,
                preload: [0, 1] // Will preload 0 - before current, and 1 after the current image
            },
            image: {
                tError: '<a href="%url%">The image #%curr%</a> could not be loaded.',
                titleSrc: function (item) {
                    return item.el.attr('title') + '<small>by Marsel Van Oosten</small>';
                }
            }
        });
        jQuery('.popup-youtube, .popup-vimeo, .popup-gmaps').magnificPopup({
            disableOn: 700,
            type: 'iframe',
            mainClass: 'mfp-fade',
            removalDelay: 160,
            preloader: false,
            fixedContentPos: false
        });


        /*---------------------------------------------------------------------
        Ripple Effect
        -----------------------------------------------------------------------*/
        jQuery(document).on('click', ".iq-waves-effect", function (e) {
            // Remove any old one
            jQuery('.ripple').remove();
            // Setup
            let posX = jQuery(this).offset().left,
                posY = jQuery(this).offset().top,
                buttonWidth = jQuery(this).width(),
                buttonHeight = jQuery(this).height();

            // Add the element
            jQuery(this).prepend("<span class='ripple'></span>");


            // Make it round!
            if (buttonWidth >= buttonHeight) {
                buttonHeight = buttonWidth;
            } else {
                buttonWidth = buttonHeight;
            }

            // Get the center of the element
            let x = e.pageX - posX - buttonWidth / 2;
            let y = e.pageY - posY - buttonHeight / 2;


            // Add the ripples CSS and start the animation
            jQuery(".ripple").css({
                width: buttonWidth,
                height: buttonHeight,
                top: y + 'px',
                left: x + 'px'
            }).addClass("rippleEffect");
        });

        /*---------------------------------------------------------------------
        Page faq
        -----------------------------------------------------------------------*/
        jQuery('.iq-accordion .iq-accordion-block .accordion-details').hide();
        jQuery('.iq-accordion .iq-accordion-block:first').addClass('accordion-active').children().slideDown('slow');
        jQuery(document).on("click", '.iq-accordion .iq-accordion-block', function () {
            if (jQuery(this).children('div.accordion-details ').is(':hidden')) {
                jQuery('.iq-accordion .iq-accordion-block').removeClass('accordion-active').children('div.accordion-details ').slideUp('slow');
                jQuery(this).toggleClass('accordion-active').children('div.accordion-details ').slideDown('slow');
            }
        });

        /*---------------------------------------------------------------------
        Page Loader
        -----------------------------------------------------------------------*/
        jQuery("#load").fadeOut();
        jQuery("#loading").delay().fadeOut("");


        /*---------------------------------------------------------------------
       Owl Carousel
       -----------------------------------------------------------------------*/
        jQuery('.owl-carousel').each(function () {
            let jQuerycarousel = jQuery(this);
            jQuerycarousel.owlCarousel({
                items: jQuerycarousel.data("items"),
                loop: jQuerycarousel.data("loop"),
                margin: jQuerycarousel.data("margin"),
                nav: jQuerycarousel.data("nav"),
                dots: jQuerycarousel.data("dots"),
                autoplay: jQuerycarousel.data("autoplay"),
                autoplayTimeout: jQuerycarousel.data("autoplay-timeout"),
                navText: ["<i class='fa fa-angle-left fa-2x'></i>", "<i class='fa fa-angle-right fa-2x'></i>"],
                responsiveClass: true,
                responsive: {
                    // breakpoint from 0 up
                    0: {
                        items: jQuerycarousel.data("items-mobile-sm"),
                        nav: false,
                        dots: true
                    },
                    // breakpoint from 480 up
                    480: {
                        items: jQuerycarousel.data("items-mobile"),
                        nav: false,
                        dots: true
                    },
                    // breakpoint from 786 up
                    786: {
                        items: jQuerycarousel.data("items-tab")
                    },
                    // breakpoint from 1023 up
                    1023: {
                        items: jQuerycarousel.data("items-laptop")
                    },
                    1199: {
                        items: jQuerycarousel.data("items")
                    }
                }
            });
        });

        /*---------------------------------------------------------------------
        Select input
        -----------------------------------------------------------------------*/
        jQuery('.select2jsMultiSelect').select2({
            tags: true
        });

        /*---------------------------------------------------------------------
        Search input
        -----------------------------------------------------------------------*/
        jQuery(document).on('click', function (e) {
            let myTargetElement = e.target;
            let selector, mainElement;
            if (jQuery(myTargetElement).hasClass('search-toggle') || jQuery(myTargetElement).parent().hasClass('search-toggle') || jQuery(myTargetElement).parent().parent().hasClass('search-toggle')) {
                if (jQuery(myTargetElement).hasClass('search-toggle')) {
                    selector = jQuery(myTargetElement).parent();
                    mainElement = jQuery(myTargetElement);
                } else if (jQuery(myTargetElement).parent().hasClass('search-toggle')) {
                    selector = jQuery(myTargetElement).parent().parent();
                    mainElement = jQuery(myTargetElement).parent();
                } else if (jQuery(myTargetElement).parent().parent().hasClass('search-toggle')) {
                    selector = jQuery(myTargetElement).parent().parent().parent();
                    mainElement = jQuery(myTargetElement).parent().parent();
                }
                if (!mainElement.hasClass('active') && jQuery(".navbar-list li").find('.active')) {
                    jQuery('.navbar-list li').removeClass('iq-show');
                    jQuery('.navbar-list li .search-toggle').removeClass('active');
                }

                selector.toggleClass('iq-show');
                mainElement.toggleClass('active');

                e.preventDefault();
            } else if (jQuery(myTargetElement).is('.search-input')) {} else {
                jQuery('.navbar-list li').removeClass('iq-show');
                jQuery('.navbar-list li .search-toggle').removeClass('active');
            }
        });

        /*---------------------------------------------------------------------
        Scrollbar
        -----------------------------------------------------------------------*/
        let Scrollbar = window.Scrollbar;
        if (jQuery('#sidebar-scrollbar').length) {
            Scrollbar.init(document.querySelector('#sidebar-scrollbar'), options);
        }
        let Scrollbar1 = window.Scrollbar;
        if (jQuery('#right-sidebar-scrollbar').length) {
            Scrollbar1.init(document.querySelector('#right-sidebar-scrollbar'), options);
        }


        /*---------------------------------------------------------------------
        Counter
        -----------------------------------------------------------------------*/
        jQuery('.counter').counterUp({
            delay: 10,
            time: 1000
        });

        /*---------------------------------------------------------------------
        slick
        -----------------------------------------------------------------------*/
        jQuery('.slick-slider').slick({
            centerMode: true,
            centerPadding: '60px',
            slidesToShow: 9,
            slidesToScroll: 1,
            focusOnSelect: true,
            responsive: [{
                breakpoint: 992,
                settings: {
                    arrows: false,
                    centerMode: true,
                    centerPadding: '30',
                    slidesToShow: 3
                }
            }, {
                breakpoint: 480,
                settings: {
                    arrows: false,
                    centerMode: true,
                    centerPadding: '15',
                    slidesToShow: 1
                }
            }],
            nextArrow: '<a href="#" class="ri-arrow-left-s-line left"></a>',
            prevArrow: '<a href="#" class="ri-arrow-right-s-line right"></a>',
        });

        jQuery('#new-music').slick({
            slidesToShow: 6,
            slidesToScroll: 1,
            focusOnSelect: true,
            arrows: false,
            responsive: [{
                breakpoint: 992,
                settings: {
                    arrows: false,
                    centerMode: true,
                    slidesToShow: 3
                }
            }, {
                breakpoint: 480,
                settings: {
                    arrows: false,
                    centerMode: true,
                    slidesToShow: 1
                }
            }],

        });

        jQuery('#recent-music').slick({
            slidesToShow: 6,
            slidesToScroll: 1,
            focusOnSelect: true,
            arrows: false,
            responsive: [{
                breakpoint: 992,
                settings: {
                    arrows: false,
                    centerMode: true,
                    slidesToShow: 3
                }
            }, {
                breakpoint: 480,
                settings: {
                    arrows: false,
                    centerMode: true,
                    slidesToShow: 1
                }
            }],

        });

        jQuery('#top-music').slick({
            slidesToShow: 6,
            slidesToScroll: 1,
            focusOnSelect: true,
            arrows: false,
            responsive: [{
                breakpoint: 992,
                settings: {
                    arrows: false,
                    centerMode: true,
                    slidesToShow: 3
                }
            }, {
                breakpoint: 480,
                settings: {
                    arrows: false,
                    centerMode: true,
                    slidesToShow: 1
                }
            }],

        });


        /*---------------------------------------------------------------------
        Progress Bar
        -----------------------------------------------------------------------*/
        jQuery('.iq-progress-bar > span').each(function () {
            let progressBar = jQuery(this);
            let width = jQuery(this).data('percent');
            progressBar.css({
                'transition': 'width 2s'
            });

            setTimeout(function () {
                progressBar.appear(function () {
                    progressBar.css('width', width + '%');
                });
            }, 100);
        });


        /*---------------------------------------------------------------------
        Page Menu
        -----------------------------------------------------------------------*/
        jQuery(document).on('click', '.wrapper-menu', function () {
            jQuery(this).toggleClass('open');
        });

        jQuery(document).on('click', ".wrapper-menu", function () {
            jQuery("body").toggleClass("sidebar-main");
        });


        /*---------------------------------------------------------------------
        Wow Animation
        -----------------------------------------------------------------------*/
        let wow = new WOW({
            boxClass: 'wow',
            animateClass: 'animated',
            offset: 0,
            mobile: false,
            live: true
        });
        wow.init();


        /*---------------------------------------------------------------------
        Mailbox
        -----------------------------------------------------------------------*/
        jQuery(document).on('click', 'ul.iq-email-sender-list li', function () {
            jQuery(this).next().addClass('show');
        });

        jQuery(document).on('click', '.email-app-details li h4', function () {
            jQuery('.email-app-details').removeClass('show');
        });


        /*---------------------------------------------------------------------
        chatuser
        -----------------------------------------------------------------------*/
        jQuery(document).on('click', '.chat-head .chat-user-profile', function () {
            jQuery(this).parent().next().toggleClass('show');
        });
        jQuery(document).on('click', '.user-profile .close-popup', function () {
            jQuery(this).parent().parent().removeClass('show');
        });

        /*---------------------------------------------------------------------
        chatuser main
        -----------------------------------------------------------------------*/
        jQuery(document).on('click', '.chat-search .chat-profile', function () {
            jQuery(this).parent().next().toggleClass('show');
        });
        jQuery(document).on('click', '.user-profile .close-popup', function () {
            jQuery(this).parent().parent().removeClass('show');
        });

        /*---------------------------------------------------------------------
        Chat start
        -----------------------------------------------------------------------*/
        jQuery(document).on('click', '#chat-start', function () {
            jQuery('.chat-data-left').toggleClass('show');
        });
        jQuery(document).on('click', '.close-btn-res', function () {
            jQuery('.chat-data-left').removeClass('show');
        });
        jQuery(document).on('click', '.iq-chat-ui li', function () {
            jQuery('.chat-data-left').removeClass('show');
        });
        jQuery(document).on('click', '.sidebar-toggle', function () {
            jQuery('.chat-data-left').addClass('show');
        });

        /*---------------------------------------------------------------------
        todo Page
        -----------------------------------------------------------------------*/
        jQuery(document).on('click', '.todo-task-list > li > a', function () {
            jQuery('.todo-task-list li').removeClass('active');
            jQuery('.todo-task-list .sub-task').removeClass('show');
            jQuery(this).parent().toggleClass('active');
            jQuery(this).next().toggleClass('show');
        });
        jQuery(document).on('click', '.todo-task-list > li li > a', function () {
            jQuery('.todo-task-list li li').removeClass('active');
            jQuery(this).parent().toggleClass('active');
        });
        /*---------------------------------------------------------------------
        Form Validation
        -----------------------------------------------------------------------*/

        // Example starter JavaScript for disabling form submissions if there are invalid fields
        window.addEventListener('load', function () {
            // Fetch all the forms we want to apply custom Bootstrap validation styles to
            var forms = document.getElementsByClassName('needs-validation');
            // Loop over them and prevent submission
            var validation = Array.prototype.filter.call(forms, function (form) {
                form.addEventListener('submit', function (event) {
                    if (form.checkValidity() === false) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
        }, false);

        /*---------------------------------------------------------------------
        Sidebar Widget
        -----------------------------------------------------------------------*/
        jQuery(document).ready(function () {
            jQuery().on('click', '.todo-task-lists li', function () {
                if (jQuery(this).find('input:checkbox[name=todo-check]').is(":checked")) {

                    jQuery(this).find('input:checkbox[name=todo-check]').attr("checked", false);
                    jQuery(this).removeClass('active-task');
                } else {
                    jQuery(this).find('input:checkbox[name=todo-check]').attr("checked", true);
                    jQuery(this).addClass('active-task');
                }

            });
        });


        /*------------------------------------------------------------------
        Flat picker
        * -----------------------------------------------------------------*/
        if (typeof flatpickr !== 'undefined' && jQuery.isFunction(flatpickr)) {
            jQuery(".flatpicker").flatpickr({
                inline: true
            });
        }

        /*------------------------------------------------------------------
        Click sign-out
        * -----------------------------------------------------------------*/

        $('a.bg-primary.iq-sign-btn[href="/logout"]').on('click', function () {
            window.localStorage.removeItem("user");
            window.location.href = "/logout";
        });

        let pathName = window.location.pathname;
        if (pathName !== '/login' && pathName !== '/sign-up' && pathName !== '/error' && pathName !== '/page/change-password') {
            $.getJSON('/me/basic-info', function (basicInfo) {
                let {
                    isValidEmail
                } = basicInfo;
                setAvatarAndName(basicInfo);
                if (pathName === '/') {
                    showNotificationValidEmail(basicInfo);
                }

                updateFriendRequestViewed();
                updateNotifications();
                getInfoFriendsOnline(basicInfo);
            });
            $.ajax({
                url: '/chat/chat-box/un-seen',
                type: 'GET',
                dataType: 'text'
            }).done(function (response) {
                triggerNotifyMessages(response);
            });

            // listen to the event
            let webSocket = new SockJS('/listen');
            let stompClient = Stomp.over(webSocket);
            stompClient.debug = function () {};
            stompClient.connect({}, function () {
                keepAlive(stompClient);

                stompClient.subscribe('/user/queue/notification.new-friend-request', function ({
                                                                                                   body
                                                                                               }) {
                    updateNotificationFriendRequest();
                });
                stompClient.subscribe('/user/queue/notification.delete-friend-request', function ({
                                                                                                      body
                                                                                                  }) {
                    removeFriendRequest(body);
                });
                stompClient.subscribe('/user/queue/notification.friend/online', function ({
                                                                                              body
                                                                                          }) {
                    let response = JSON.parse(body);
                    drawCardFriend(response);
                    $(`#dot-on-off-${response.id}`).addClass('dot-online').removeClass('dot-offline');
                    $(`i[data-status-online=${response.id}]`).removeClass('text-dark').addClass('text-success');
                });
                stompClient.subscribe('/user/queue/notification.friend/offline', function ({
                                                                                               body
                                                                                           }) {
                    let response = JSON.parse(body);
                    drawCardFriend(response);
                    $(`#dot-on-off-${response.id}`).addClass('dot-offline').removeClass('dot-online');
                    $(`i[data-status-online=${response.id}]`).removeClass('text-success').addClass('text-dark');
                });
                stompClient.subscribe('/user/queue/notification.post', function ({
                                                                                     body
                                                                                 }) {
                    $('#button-view-notifications span').show();
                });
                let sndNewMessage = new Audio('/audio/new-message.wav');

                stompClient.subscribe('/user/queue/notification/messages', function ({
                                                                                         body
                                                                                     }) {
                    let response = JSON.parse(body),
                        {
                            basicInfo,
                            ofMe,
                            id,
                            idChatBox,
                            content,
                            idAuthor,
                            dateCreated
                        } = response;

                    let e = $(`#default-block[data-id-chatBox="${idChatBox}"]`),
                        areaShowMessage = e.find('#area-show-messages');

                    if (e.length === 0) {
                        sndNewMessage.play().then(function () {
                            /*sndNewMessage.currentTime = 0;*/
                        });
                    }
                    if (pathName.match(/^\/chat.*$/g)) {
                        if (e.length !== 0) {
                            areaShowMessage.append(drawOneMessage(response));
                            areaShowMessage.parent().scrollTop(areaShowMessage.parent()[0].scrollHeight);
                            $.ajax({
                                url: `/chat/chat-box/${idChatBox}/seen?number=1`,
                                type: 'PUT'
                            }).done(function () {});
                        }

                        if ($(`li[data-id-chatBox=${idChatBox}]`).length !== 0) {
                            if (e.length === 0) {
                                let _numberUnSeen = $(`#num-messages-for-${idChatBox}`);
                                _numberUnSeen.text(+_numberUnSeen.text() + 1);
                            }
                        } else {
                            let idUser = basicInfo.id,
                                areaShowChatList = $('#area-show-chat-list');

                            getChatBox(idUser);

                            function getChatBox(idUser) {
                                $.getJSON(`/chat/chat-box/${idUser}`, function (response) {
                                    let {
                                            id
                                        } = response,
                                        e = areaShowChatList.find(`li[data-id-chatBox='${id}']`);
                                    if (e.length !== 0) {
                                        e.remove();
                                    }
                                    areaShowChatList.prepend(drawOverviewChatBox(response));
                                });
                            }

                            function drawOverviewChatBox(response) {
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
                                            <div id="num-messages-for-${id}" class="chat-msg-counter bg-primary text-white">${numberUnSeen}</div>
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

                            function openChatBox(_response) {
                                let {
                                    id,
                                    basicInfo,
                                    isBlocked
                                } = _response;
                                let defaultBlock = $('#default-block');
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
                                                   <a class="dropdown-item" href="#"><i class="fa fa-trash-o" aria-hidden="true"></i> Delete chat</a>
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
                                    })
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
                                            $(`#sub-messages-for-${idChatBox}`).text(content.length > 25 ? content.substring(0, 25) + '...' : content);

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
                            }
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
                    }
                    $(`#sub-messages-for-${idChatBox}`).text(content.length > 25 ? content.substring(0, 25) + '...' : content);
                    if (e.length === 0) {
                        triggerNotifyMessages(1);
                    }
                });

            });


        }

        // cache variable
        let viewMoreFriendRequest = $('#view-more-friend-request'),
            formSearchUser = $('.iq-search-bar > form'),
            buttonViewNotifications = $('#button-view-notifications'),
            viewMoreNotifications = $('#view-more-notifications');

        // event click
        $('#button-show-friend-request').on('click', function (event) {
            event.preventDefault();
            viewMoreFriendRequest.show();
            showFriendRequest1().then(updateFriendRequestViewed);
        });
        viewMoreFriendRequest.on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();

            let page = $(this).attr('data-page'),
                pageNext = (+page + 1);

            $.getJSON(`/me/friend-request/${pageNext}/4`, function (response) {
                response.forEach(e => showFriendRequest2(e));
                updateNotificationFriendRequest();
                updateFriendRequestViewed();
                if (response.length < 4) {
                    viewMoreFriendRequest.hide();
                }
            });

        });
        formSearchUser.find('a').on('click', function (event) {
            event.preventDefault();
            redirectProfileUser.call(formSearchUser);
        });
        buttonViewNotifications.on('click', function (event) {
            event.preventDefault();

            viewMoreNotifications.show();
            viewMoreNotifications.attr('data-page', 0);
            drawNotifications().then(function () {
                updateNotificationsViewed();
            })
        });
        viewMoreNotifications.on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();

            let nextPage = +$(this).attr('data-page') + 1;
            $.getJSON(`/notification/${nextPage}/4`, function (response) {
                drawNotifications(response).then(value => {
                    updateNotificationsViewed();
                    if (value < 4) {
                        viewMoreNotifications.hide();
                    } else if (value === 4) {
                        viewMoreNotifications.attr('data-page', nextPage);
                    }
                });
            })
        });

        // event submit
        formSearchUser.on('submit', function (event) {
            event.preventDefault();
            redirectProfileUser.call(this);
        });

        function getInfoFriendsOnline({
                                          id
                                      }) {
            $.getJSON(`/user/friends/${id}/0/1000?option=online`, function (response) {
                response.forEach(e => {
                    drawCardFriend(e);
                })
            })
        }

        function drawCardFriend(basicInfo) {
            let {
                    id,
                    firstName,
                    lastName,
                    urlAvatar,
                    isOnline,
                    lastAccess
                } = basicInfo,
                e = $(`#nav-card-friend-${id}`),
                firstTime = new Date(),
                secondTime = new Date(lastAccess);

            let diffValues = distanceTime(firstTime, secondTime);

            if (firstTime - secondTime < 1209600001) {
                let html = isOnline ? $(`<div class="media align-items-center mb-4" id="nav-card-friend-${id}"> <div class="iq-profile-avatar status-online"> <img class="rounded-circle avatar-50" src="${urlAvatar}" alt=""> </div> <div class="media-body ml-3"> <h6 class="mb-0"><a href="/user?id=${id}">${lastName + ' ' + firstName}</a></h6> </div> </div>`) :
                    $(`<div class="media align-items-center mb-4" id="nav-card-friend-${id}"> <div class="iq-profile-avatar"> <img class="rounded-circle avatar-50" src="${urlAvatar}" alt=""> </div> <div class="media-body ml-3"> <h6 class="mb-0"><a href="/user?id=${id}">${lastName + ' ' + firstName}</a></h6> <p class="mb-0">${diffValues + ' ago'}</p> </div> </div>`);

                if (e.length === 1) {
                    e.replaceWith(html);
                } else {
                    if (isOnline) {
                        $('#show-nav-card-friend').prepend(html);
                    } else {
                        $('#show-nav-card-friend').append(html);
                    }
                }
            }
        }
        async function drawNotifications(notificationResponseWrapper) {
            if (notificationResponseWrapper) {
                let {
                    notificationResponses
                } = notificationResponseWrapper;
                notificationResponses.forEach(e => drawCardNotification(e));
                return notificationResponses.length;
            } else {
                await $.getJSON('/notification/0/4', function (response) {
                    $('#show-notifications-default').empty();
                    let {
                        notificationResponses,
                        numberUnViewed
                    } = response;
                    notificationResponses.forEach(e => drawCardNotification(e));
                    updateNotifications(numberUnViewed);
                });
            }
        }

        function drawCardNotification(notificationResponse) {
            let {
                    id,
                    content,
                    link,
                    objectName,
                    objectId,
                    picture,
                    dateCreated,
                    viewed
                } = notificationResponse,
                time = distanceTime(new Date(), new Date(dateCreated)),
                html = $(`<a href="${link}" class="iq-sub-card pt-2 pb-2 d-flex" data-id-notification="${id}"><div class="media align-items-center row w-100"><div class="col-3"><img class="avatar-40 rounded d-flex mx-auto" src="${picture}" alt=""><small class="mt-2 d-flex font-size-12 justify-content-center">${time}</small></div><div class="media-body col-9 pr-0 pl-0"><div><h6 class="mb-0 d-inline">${objectName}</h6><p class="mb-0 d-inline">${' ' + content}</p></div></div></div></div></a>`);

            if (!viewed) {
                html.css('background', '#cae9ef')
            }
            $('#show-notifications-default').append(html);
        }

        function setAvatarAndName({
                                      urlAvatar,
                                      firstName,
                                      lastName
                                  }) {
            $('.my-avatar').attr("src", urlAvatar);
            $('.my-firstName').text(firstName);
            $('.my-fullName').text(lastName + ' ' + firstName);
        }

        function redirectProfileUser() {
            let contentSearch = $(this).find('input').val();
            window.location.href = '/user?id=' + encodeURI(contentSearch);
        }

        /*
        For Notifications */
        // Friend Request
        async function showFriendRequest1() {
            await $.getJSON('/me/friend-request/0/4', function (response) {
                $('#show-friend-request').next().empty();
                response.forEach(e => showFriendRequest2(e));
                updateNotificationFriendRequest();
            })
        }

        function showFriendRequest2(basicInfo, flag) {
            let {
                idUserRequest,
                picture,
                fullName,
                numberFriends,
                viewed
            } = basicInfo;
            let temp = $('#show-friend-request'),
                html = $(`<div class="iq-friend-request my-friend-request" id="friend-request-${idUserRequest}"><div class="iq-sub-card iq-sub-card-big d-flex align-items-center justify-content-between"><div class="d-flex align-items-center"><div class=""><a href="/user?id=${idUserRequest}"><img class="avatar-40 rounded" src="${picture}" alt=""></a></div><div class="media-body ml-3"><h6 class="mb-0 ">${fullName}</h6><p class="mb-0">${numberFriends} friends</p></div></div><div class="d-flex align-items-center"><a href="#" class="mr-3 btn btn-primary rounded">Confirm</a><a href="#" class="mr-3 btn btn-secondary rounded"><i class="ri-delete-bin-7-line mr-0"></i></a></div></div></div>`);
            if (!viewed) {
                html.css('background', '#cae9ef');
            }
            if (flag) {
                temp.next().prepend(html);
            } else {
                temp.next().append(html);
            }

            html.find('a:eq(1)').off('click').on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();

                $.ajax({
                    url: '/me/friend-request/accept',
                    type: 'POST',
                    dataType: 'text',
                    data: {
                        'id': idUserRequest
                    }
                });
            });

            html.find('a:eq(2)').off('click').on('click', function (event) {
                event.preventDefault();
                event.stopPropagation();

                $.ajax({
                    url: `/me/friend-request/${idUserRequest}`,
                    type: 'DELETE',
                    dataType: 'text'
                });
            })

        }

        function removeFriendRequest(id) {
            let temp = $('#show-friend-request');
            $(`#friend-request-${id}`).remove();

            let e = temp.next().find('.iq-friend-request');
            if (e.length < 4) {
                $.getJSON(`/me/friend-request/3/1`, function (data) {
                    data.forEach(_e => showFriendRequest2(_e));
                })
            }

            updateNotificationFriendRequest();
        }

        function updateNotificationFriendRequest() {
            let t = $('#notify-new-friend-request');
            $.getJSON('/me/friend-request/details', function (response) {
                let temp = $('#show-friend-request'),
                    {
                        sum,
                        unViewed
                    } = response;

                temp.find('small').text(sum);
                $('#view-more-friend-request').attr('data-page', Math.floor((temp.next().find('.iq-friend-request').length - 1) / 4));

                if (+unViewed !== 0) {
                    t.show();
                } else {
                    t.hide();
                }
            })
        }

        function updateFriendRequestViewed() {
            let t = [],
                es = $('.my-friend-request');

            es.each((ii, e) => {
                let element = $(e).attr('id'),
                    i = element.lastIndexOf('-'),
                    id = $(e).attr('id').substr(i + 1);
                t.push(id);
            });

            $.ajax({
                url: '/me/friend-request/viewed',
                type: 'POST',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(t)
            }).done(function (response) {
                let t = $('#notify-new-friend-request');

                if (+response !== 0) {
                    t.show();
                } else {
                    t.hide();
                }
            })
        }

        function updateNotifications(numberUnViewed) {
            let f = function (numberUnViewed) {
                if (+numberUnViewed === 0) {
                    buttonViewNotifications.find('span').hide();
                } else {
                    buttonViewNotifications.find('span').show();
                }
                buttonViewNotifications.next().find('div.bg-primary.p-3 small').text(numberUnViewed);
            };

            if (numberUnViewed) {
                f(numberUnViewed);
            } else {
                $.ajax({
                    url: '/notification/number-unViewed',
                    type: 'GET',
                    dataType: 'text'
                }).done(function (response) {
                    f(response);
                })
            }
        }

        function updateNotificationsViewed() {
            let ids = [];
            $('#show-notifications-default > a').each((i, v) => {
                ids.push($(v).attr('data-id-notification'));
            });
            $.ajax({
                url: '/notification/viewed',
                type: 'POST',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(ids)
            }).done(function (response) {
                if (+response === 0) {
                    $('#button-view-notifications span').hide();
                }
            })
        }

        // Notifications

        // Chats
        let buttonClickViewNotificationMessages = $('#button-view-notification-messages'),
            areaShowNotificationMessage = $('#area-show-notification-messages'),
            buttonViewMoreNotificationMessages = $('#view-more-notification-messages');

        buttonClickViewNotificationMessages.on('click', function (event) {
            event.preventDefault();

            buttonViewMoreNotificationMessages.show();

            $.getJSON(`/chat/chat-box/from?number=4`, function (response) {
                areaShowNotificationMessage.empty();

                let {
                    chatBoxResponses,
                    numberUnSeen
                } = response;
                triggerNotifyMessages(numberUnSeen);
                $('#number-messages-unseen').text(numberUnSeen);

                if (chatBoxResponses.length < 4) {
                    buttonViewMoreNotificationMessages.hide();
                }

                if (chatBoxResponses.length === 0) {
                    areaShowNotificationMessage.append($('<p class="text-center">Nothing</p>'));
                }
                chatBoxResponses.forEach(response =>
                    areaShowNotificationMessage.append(drawNotificationMessage(response)));
            });
        });
        buttonViewMoreNotificationMessages.on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();

            let idChatBox = areaShowNotificationMessage
                .find('a')
                .last()
                .attr('data-id-chat-box');


            $.getJSON(`/chat/chat-box/from?idChatBox=${idChatBox}&number=4`, function (responses) {
                let {
                    chatBoxResponses
                } = responses;
                if (chatBoxResponses.length < 4) {
                    buttonViewMoreNotificationMessages.hide();
                }
                chatBoxResponses.forEach(response => areaShowNotificationMessage.append(drawNotificationMessage(response)));
            })
        });


        function triggerNotifyMessages(response) {
            if (+response > 0) {
                buttonClickViewNotificationMessages.find('span').show();
            } else {
                buttonClickViewNotificationMessages.find('span').hide();
            }
        }

        function drawNotificationMessage(response) {
            let {
                basicInfo,
                latestContent
            } = response, {
                id,
                firstName,
                lastName,
                urlAvatar
            } = basicInfo;

            return $(` <a href="/chat?id=${id}" class="iq-sub-card" data-id-chat-box="${response.id}" >
                                        <div class="media align-items-center">
                                            <div class="">
                                                <img class="avatar-40 rounded" src="${urlAvatar}" alt="">
                                            </div>
                                            <div class="media-body ml-3">
                                                <h6 class="mb-0 ">${lastName + ' ' + firstName}</h6>
                                                <small class="float-left font-size-12">${latestContent.length > 40 ? latestContent.substring(0, 40) + '...' : latestContent}</small>
                                            </div>
                                        </div>
                                    </a>`);

        }

        // Other Function
        function showNotificationValidEmail({
                                                isValidEmail
                                            }) {
            if (!isValidEmail) {
                $('#notification').append(`<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>You have not done email verification, we will send you a verification email if you <a href="#" id="button-verify">agree</a> or <a href="#" id="button-close1">later</a>.</p></div>`);
                $('#button-close1').on('click', function (event) {
                    event.preventDefault();
                    $(this).closest('.alert').remove();
                });
                $('#button-verify').on('click', function (event) {
                    event.preventDefault();
                    $.ajax({
                        url: '/email/verify',
                        type: 'GET',
                        dataType: 'text'
                    }).done(response => {
                        $('#notification').html(`<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>${response} -> <a href="#" id="button-close2">OK</a></p></div>`);
                        $('#button-close2').on('click', function (event) {
                            event.preventDefault();
                            $(this).closest('.alert').remove();
                        });
                    }).fail(jqXHR => {
                        console.log(jqXHR);
                    })
                })
            }
        }

        function keepAlive(stompClient) {
            stompClient.send('/ws/me/online', {}, 'i\'m alive');
            setTimeout(function () {
                keepAlive(stompClient);
            }, 2 * 60 * 1000);
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

    });

})(jQuery);