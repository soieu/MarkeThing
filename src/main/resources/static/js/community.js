let index = {
    init: function() {
        const pathname = window.location.pathname;
        const communityId = pathname.replace('/communities/', '');
        this.loadDetail(communityId);

        $("#create-comment").on("click", () => {
            const pathName = window.location.pathname.split('/');
            const communityId = pathName[pathName.length - 1];
            console.log(pathName, communityId);
            $.ajax({
                url: "/api/communities/" + communityId + '/comments',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                data: {
                    content: $("#comment-content").val()
                }
            }).done(function(response) {
                location.reload();
            }).fail(function(error) {
                console.error(error);
            });
        });
    },
    loadDetail: function(communityId) {
        $.ajax({
            url: "/api/communities/" + communityId,
            type: 'GET',
            dataType: 'json',
            contentType: 'application/json',
        }).done(function(response) {
            let community = response;
            let html = '<table border="1">';
            html += '<tr><th colspan="2"><h2>' + community.title + '</h2></th></tr>';
            html += '<tr><td><strong>Area:</strong></td><td>' + community.area + '</td></tr>';
            html += '<tr><td><strong>Content:</strong></td><td>' + community.content + '</td></tr>';
            html += '<tr><td><strong>Created At:</strong></td><td>' + community.createdAt + '</td></tr>';
            html += '<tr><td><strong>Post Image:</strong></td><td><img src="' + community.postImg + '" alt="Post Image"></td></tr>';
            html += '</table>';

            html += '<table border="1">';
            community.comments.forEach(comment => {
                html += '<tr id="comment-' + comment.id + '">';
                html += '<td>' + comment.userId + '</td>';
                html += '<td>' + comment.content + '</td>';
                html += '<td>' + comment.createdAt + '</td>';
                html += '<td><button onclick="index.openReply(' + comment.id + ')">Reply</button></td>';
                html += '</tr>';

                if (comment.replyComments.length > 0) {
                    comment.replyComments.forEach(reply => {
                        html += '<tr>';
                        html += '<td colspan="4">ㄴ ' + reply.userId + ': ' + reply.content + ' (' + reply.createdAt + ')</td>';
                        html += '</tr>';
                    });
                }
            });
            html += '</table>';

            $('#community-detail').html(html);
        }).fail(function(error) {
            console.error(error);
        });
    },
    openReply: function(commentId) {
        if ($("#reply-input-" + commentId).length === 0) {
            let replyHtml = '<tr id="reply-input-' + commentId + '">';
            replyHtml += '<td colspan="4">';
            replyHtml += '<input type="text" id="reply-content-' + commentId + '" placeholder="댓글을 입력하세요.">';
            replyHtml += '<button onclick="index.createReply(' + commentId + ')">Submit</button>';
            replyHtml += '</td>';
            replyHtml += '</tr>';
            $("#comment-" + commentId).after(replyHtml);
        }
    },
    createReply: function(commentId) {
        const pathName = window.location.pathname.split('/');
        const communityId = pathName[pathName.length - 1];
        const replyContent = $("#reply-content-" + commentId).val();

        $.ajax({
            url: '/api/communities/comments/' + commentId + '/reply-comments',
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: {
                content: replyContent
            }
        }).done(function(response) {
            location.reload();
        }).fail(function(error) {
            console.error(error);
        });
    }
};

$(document).ready(function() {
    index.init();
});
