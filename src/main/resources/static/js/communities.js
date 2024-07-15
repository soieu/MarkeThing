let index = {
    init: function () {
        $("#btn-next").on("click", () => {
            this.nextPage(page, size, sort);
        });

        $("#btn-prev").on("click", () => {
            this.prevPage(page, size, sort);
        });

        const queryString = window.location.search
        const params = new URLSearchParams(queryString);

        const page = parseInt(params.get("page")) || 0;
        const size = parseInt(params.get("size")) || 5;
        const sort = params.get("sort") || "date";

        this.loadPage(page, size, sort);
    },

    loadPage: function (page, size, sort) {
        $.ajax({
            url: `/api/communities/list?page=${page}&size=${size}&sort=${sort}`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                filtersForCommunity: {
                    areas: []
                }
            }),
            dataType: 'json',
        }).done(function (response) {
            console.log(response);
            let content = response.content;
            let html = '<table border="1">';
            html += '<tr><th>Title</th><th>Nickname</th><th>Area</th></tr>';
            for (var i = 0; i < content.length; i++) {
                html += '<tr>';
                html += '<td><a href="/communities/' + content[i].id + '">' + content[i].title + '</a></td>';
                html += '<td>' + content[i].nickname + '</td>';
                html += '<td>' + content[i].area + '</td>';
                html += '</tr>';
            }
            html += '</table>';
            $('#community-list').html(html);
        }).fail(function (error) {
            console.error(error);
        });
    },

    prevPage: function (page, size, sort) {
        if (page > 0) {
            prevPage = --page;
            location.href = "/communities/list?page="+ prevPage;
        }
    },

    nextPage: function (page, size, sort) {
        nextPage = ++page;
        location.href = "/communities/list?page="+ nextPage;
    }
};

$(document).ready(function() {
    index.init();
});
