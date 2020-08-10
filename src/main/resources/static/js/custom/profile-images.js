(function (jQuery) {

    "use strict";

    activeTabPage('ytb-page-profile-images');

    function activeTabPage(name) {
        $('.ytb-page-tab').removeClass('active');
        $(`#${name}`).addClass('active');
    }

    $.getJSON('/me/basic-info', function (infoUser) {
        drawNewPhotos2(infoUser.id);
    });

    let controller1 = new ScrollMagic.Controller();
    let scene1 = null;

    function drawNewPhotos2(id) {
        let loader = $('#loading-photos');
        scene1 = new ScrollMagic.Scene({
            triggerElement: "#loading-photos",
            triggerHook: "onEnter"
        })
            .addTo(controller1)
            .on("enter", function () {
                let page = loader.attr('data-page');
                $.getJSON(`/user/${id}/photos?page=${page}&number=12`, function (responses) {
                    if (responses.length < 12) {
                        loader.removeClass('loading-active');
                    } else {
                        loader.attr('data-page', page + 1);
                    }
                    let area = $('#area-show-photos-of-you');
                    responses.forEach(({
                                           url,
                                           postLink,
                                           numberReacted,
                                           numberComment,
                                           numberShared
                                       }) => {
                        let html = $(`<div class="col-md-6 col-lg-4 mb-3">
                                           <div class="user-images position-relative overflow-hidden">
                                              <a href="/${postLink}">
                                              <img src="${url+"+H350"}" class="rounded" alt="Responsive image" style="width: 100%; height: 149px; object-fit: cover">
                                              </a>
                                              <div class="image-hover-data">
                                                 <div class="product-elements-icon">
                                                    <ul class="d-flex align-items-center m-0 p-0 list-inline">
                                                       <li><a href="#" class="pr-3 text-white"> ${numberReacted} <i class="ri-thumb-up-line"></i> </a></li>
                                                       <li><a href="#" class="pr-3 text-white"> ${numberComment} <i class="ri-chat-3-line"></i> </a></li>
                                                       <li><a href="#" class="pr-3 text-white"> ${numberShared} <i class="ri-share-forward-line"></i> </a></li>
                                                    </ul>
                                                 </div>
                                              </div>
                                           </div>
                                        </div>`);
                        area.append(html);
                    });
                    scene1.update();
                })
            })
    }

}());