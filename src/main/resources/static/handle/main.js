$(document).ready(function () {
    $('#reading-book-status .dropdown-item').on('click', function (event) {
            console.log("click")
            event.preventDefault();
            let bookId = $(this).attr('book-id')
            let readingStatus = $(this).data('value')
            if (readingStatus !== 'Remove') {
                let formData = {
                    "bookId": bookId,
                    "readingStatus": readingStatus
                }
                $.ajax({
                    url: '/api/v1/users/book-reading',
                    type: 'POST',
                    data: JSON.stringify(formData),
                    contentType: 'application/json',
                    success: function (data) {
                        autoDirectLoginPage(data)
                    },
                    error: function () {
                        toastr.warning('Not success!')
                    }
                })
            } else {
                $.ajax({
                    url: '/api/v1/users/book-reading/' + bookId,
                    type: 'DELETE',
                    success: function (data) {
                        autoDirectLoginPage(data)
                    },
                    error: function () {
                        toastr.warning('Not success!')
                    }
                })
            }
        }
    )
})

function autoDirectLoginPage(data) {
    if (isLoginPage(data)) {
        window.location.href = '/login';
    } else {
        toastr.success('Success');
        setTimeout(function () {
            window.location.reload();
        }, 500);
    }
}

function isLoginPage(data) {
    return $(data).find('.signin-form').length > 0;
}
