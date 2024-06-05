$(document).ready(function () {
    $('#add-role').click(() => {
        $('#myModal').css('display', 'block');
        $('#myModal input').focus();
    })
    $('.close').click(() => {
        $('#myModal').css('display', 'none');
    })
    window.onclick = function (event) {
        if ($(event.target).is('#myModal')) {
            $('#myModal').css('display', 'none');
        } else if ($(event.target).is('#edit-modal')) {
            $('#edit-modal').css('display', 'none');
        }
    }

    function createRole() {
        let newRole = $('.input-category').val().trim();
        if (newRole === '') {
            window.alert("Role name can not blank!")
            return;
        }
        $('#submit-category').prop('disabled', true);
        let formData = {
            name: newRole
        };
        $.ajax({
            url: '/api/v1/admin/roles',
            type: 'POST',
            data: JSON.stringify(formData),
            contentType: "application/json",
            success: function () {
                toastr.success("Created role successfully!");
                setTimeout(function () {
                    $('#submit-category').prop('disabled', false);
                    window.location.reload();
                }, 800);
            },
            error: function () {
                toastr.warning("Can not create new role. Please contact to your developer!");
                setTimeout(function () {
                    $('#submit-category').prop('disabled', false);
                    $('#myModal').css('display', 'none');
                }, 1000);
            }
        })
    }

    $('#submit-category').click(() => {
        createRole();
    })
    $('#myModal input').on('keyup', function (event) {
        if (event.key === 'Enter') {
            $('#submit-category').click();
        } else if (event.which === 27) {
            $('#myModal').css('display', 'none');
        }
    })

})

