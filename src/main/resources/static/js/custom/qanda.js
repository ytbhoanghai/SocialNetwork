(function (jQuery) {

    let area = $('#area-show-qanda');
    $.getJSON('/data/qanda', function (responses) {
        for (let i = 0; i < responses.length; i++) {
            let {question, answer} = responses[i];
            let html = $(`<div class="iq-accordion career-style faq-style  ">
                                <div class="iq-card iq-accordion-block p-0">
                                   <div class="active-faq clearfix iq-card-header d-flex justify-content-between">
                                      <div class="iq-header-title">
                                         <h4 class="card-title">${question}</h4>
                                      </div>
                                   </div>
                                   <div class="accordion-details iq-card-body" style="display: none;">
                                      <p class="mb-0 p-0">${answer}</p>
                                   </div>
                                </div>
                             </div>`);
            if (i === 0) {
                html.find('div:eq(0)').addClass('accordion-active');
                html.find('div:eq(0) > div:eq(1)').show();
            }
            html.appendTo(area);
        }
    });
}(jQuery));