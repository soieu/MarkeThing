let index = {
    array: [],
    init: function () {
        const queryString = window.location.search;
        const params = new URLSearchParams(queryString);

        const page = parseInt(params.get("page")) || 0;
        const size = parseInt(params.get("size")) || 5;
        const sort = params.get("sort") || "date";
        const self = this;
        $("#btn-next").on("click", () => {
            this.nextPage(page, size, sort);
        });
        $("#btn-prev").on("click", () => {
            this.prevPage(page, size, sort);
        });
        $("#new-board").on("click", () => {
            location.href = "/communities"
        });
        $('#allClick').click(function () {
            $('input:checkbox').prop('checked', true);
        });
        $('#noClick').click(function () {
            $('input:checkbox').prop('checked', false);
        });
        $('#apply').click(function () {
            let arr = [];
            $("#area").find('input:checked').each(function () {
                arr.push($(this).val());
            });
            self.array = arr;
            self.loadPage(page, size, sort);
        });
        this.loadPage(page, size, sort);
    },

    loadPage: function (page, size, sort) {
        const self = this;
        $.ajax({
            url: `/api/communities/list?page=${page}&size=${size}&sort=${sort}`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                filtersForCommunity: {
                    areas: self.array.length > 0 ? self.array : []
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
            let prevPage = page - 1;
            location.href = "/communities/list?page=" + prevPage + "&size=" + size + "&sort=" + sort;
        }
    },

    nextPage: function (page, size, sort) {
        let nextPage = page + 1;
        location.href = "/communities/list?page=" + nextPage + "&size=" + size + "&sort=" + sort;
    }
};

$(document).ready(function () {
    index.init();
});
