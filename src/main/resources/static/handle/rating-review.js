// RATING
$(document).ready(function () {
    let hasContent = false;

    if (bookDetail.ratingDetail === 0) {
        $('#review-book').html('<p>Rate this book</p>')
    } else {
        if (bookDetail.content != null && bookDetail.content.trim() !== '') {
            $('#review-book').html('<a type="button" class="btn edit-review"> <i class="fa-solid fa-pen"></i> Edit review</a>')
            hasContent = true;
        } else {
            $('#review-book').html('<a type="button" class="btn add-review"><i class="fa-solid fa-pen"></i> Review this book</a>')
        }
    }

    $('.edit-review, .add-review').on('click', function () {
        window.location.href = '/users/review/' + bookId;
    })

    let initialWidth = (100 - (bookDetail.ratingDetail * 20)) + "%";
    $("#display-rating-personal .stars").mousemove(function (event) {
        $('#display-rating-personal .overlay').css('width', '0%');
        highlightStars(event);
    });

    $("#display-rating-personal .stars").mouseleave(function () {
        $('#display-rating-personal .overlay').css('width', initialWidth)
        resetStars();
    });

    let rating = 0;

    function highlightStars(event) {
        const mouseX = event.clientX;

        $('#display-rating-personal .stars .star').each(function () {
            const starX = $(this).offset().left;
            if (mouseX >= starX) {
                $(this).css("fill", "#FCD01E");
                rating = parseInt($(this).data('value'));

            } else {
                $(this).css("fill", "#CACACA");
            }
        })
    }

    function resetStars() {
        $('#display-rating-personal .stars .star').css("fill", "");
    }

    $('#display-rating-personal .stars .star').on('click', function (event) {

        if (rating === bookDetail.ratingDetail && rating !== 0) {
            $.ajax({
                url: '/api/v1/users/rating/' + bookId,
                type: 'DELETE',
                success: function () {
                    toastr.success('Rating removed');
                    setTimeout(function () {
                        window.location.reload();
                    }, 700);
                }, error: function () {
                    toastr.warning('Rating removed not successfully');
                }
            })
        } else {
            let formData = {
                bookId: bookId,
                rating: rating,
                content: bookDetail.content,
                readingStatus: bookDetail.readingStatus
            }
            $.ajax({
                url: '/api/v1/users/rating',
                type: 'POST',
                data: JSON.stringify(formData),
                contentType: 'application/json',
                success: function (data) {
                    autoDirectLoginPage(data);
                }, error: function () {
                    toastr.warning('Rating not successfully');
                }
            })
        }
    })
})

// REVIEW
$(document).ready(function () {
    let typeApi = "PUT";

    if (bookDetail.content != null && bookDetail.content.trim() !== '') {
        $('#review-content').html(bookDetail.content)
        $('#save-review').html('Save')
    } else {
        $('#save-review').html('Post')
        typeApi = "POST"
    }

    console.log('status' + bookDetail.readingStatus)

    $('#save-review').on('click', function (event) {
        let startDate = $('#start-date').val()
        let finishDate = $('#finish-date').val()
        event.preventDefault()
        let isValidForm = $('#review-form').valid();
        if (!isValidForm) {
            return;
        }
        let reviewContent = $('#review-content').val().trim();
        if (reviewContent === '') {
            window.confirm("Your review content is empty, do you want to continue?");
        }
        let readingStatus = bookDetail.readingStatus;
        let formData = {
            bookId: bookId,
            content: reviewContent,
            readingStatus: readingStatus,
            startedDate: startDate,
            finishedDate: finishDate
        }
        $.ajax({
            url: '/api/v1/users/review',
            type: typeApi,
            data: JSON.stringify(formData),
            contentType: 'application/json',
            success: function (data) {
                reviewAutoDirectLoginPage(data);
            }, error: function () {
                toastr.warning('Saved review not success')
            }

        })

    })

    $('#review-form').validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        errorPlacement: function (error, element) {
            error.addClass("error-message");
            error.insertAfter(element);
        },
        rules: {
            "review-content": {
                maxlength: 65535
            }
        },
        messages: {
            "review-content": {
                maxlength: "Cannot be longer than 65535 characters"
            }
        }
    })

    function reviewAutoDirectLoginPage(data) {
        if (isLoginPage(data)) {
            window.location.href = '/login';
        } else {
            toastr.success('Success');
            setTimeout(function () {
                window.location.href = '/books/' + bookId
            }, 500);
        }
    }

    function isLoginPage(data) {
        return $(data).find('.signin-form').length > 0;
    }

})