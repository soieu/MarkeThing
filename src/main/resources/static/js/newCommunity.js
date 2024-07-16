$(document).ready(function() {
    $("#btn-submit").on("click", function() {
        submitCommunityForm();
    });

    function submitCommunityForm() {
        let formData = new FormData($("#create-community-form")[0]);

        $.ajax({
            url: '/api/communities',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: 'multipart/form-data',
            success: function(data) {
                $("#response-message").text("Community post created successfully!");
                console.log('Success:', data);
                location.href="communities/list"
            },
            error: function(error) {
                $("#response-message").text("Error creating community post.");
                console.error('Error:', error);
            }
        });
    }
});
