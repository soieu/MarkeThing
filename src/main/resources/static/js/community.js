let index = {
    init: function() {
        const pathname = window.location.pathname
        const communityId = pathname.replace('/communities/', '')
        this.loadDetail(communityId);
    },
    loadDetail: function(communityId) {
        $.ajax({
            url: "/api/communities/" + communityId,
            type: 'GET',
            dataType: 'json',
        }).done(function(response) {
            let community = response;
            let html = '<table border="1">';
            html += '<tr><th colspan="2"><h2>' + community.title + '</h2></th></tr>';
            html += '<tr><td><strong>Area:</strong></td><td>' + community.area + '</td></tr>';
            html += '<tr><td><strong>Content:</strong></td><td>' + community.content + '</td></tr>';
            html += '<tr><td><strong>Created At:</strong></td><td>' + community.createdAt + '</td></tr>';
            html += '<tr><td><strong>Post Image:</strong></td><td><img src="' + community.postImg + '" alt="Post Image"></td></tr>';
            html += '</table>';
            $('#community-detail').html(html);
        }).fail(function(error) {
            console.error(error);
        });
    }
}

$(document).ready(function() {
    index.init();
});