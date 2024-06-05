$(document).ready(function () {
    //open popup
    let lockedUser;
    let userId;
    $('.cd-popup-trigger').on('click', function (event) {
        userId = $(event.currentTarget).attr('user-id');
        let statusOfAccount = $(event.currentTarget).attr('account-locked');
        let lockedHTML = `<div id="lockModalTemplate" class="cd-popup-container">
                                        <p>LOCK this account - Are you sure?</p>
                                        <ul class="cd-buttons">
                                            <li><a type="button" class="locked-user-btn"> <i
                                                    class="fa-solid fa-lock mr-10 "></i>
                                                LOCK</a></li>
                                            <li></li>
                                        </ul>
                                        <a href="#" class="cd-popup-close img-replace">Close</a>
                                    </div>`;
        let unlockedHTML = ` <div id="unlockModalTemplate" class="cd-popup-container">
                                    <p>UNLOCK this account!</p>
                                    <ul class="cd-buttons">
                                        <li>
                                            <a style="background-color: #77B748" type="button"
                                               class="locked-user-btn">
                                               <i class="fa-solid fa-lock-open mr-10"></i> UNLOCK
                                            </a>
                                        </li>
                                        <li></li>
                                    </ul>
                                    <a href="#" class="cd-popup-close img-replace">Close</a>
                                </div>`;
        if (statusOfAccount === 'true') {
            $('#unlockModal').empty();
            $('#unlockModal').append(unlockedHTML);
            lockedUser = false;
        } else {
            $('#unlockModal').empty();
            $('#unlockModal').append(lockedHTML);
            lockedUser = true;
        }
        event.preventDefault();
        $('.cd-popup').addClass('is-visible');
        $('.select2-container').css('position', 'unset');

    });

    //close popup
    $('.cd-popup').on('click', function (event) {
        if ($(event.target).is('.cd-popup-close') || $(event.target).is('.cd-popup')) {
            event.preventDefault();
            $(this).removeClass('is-visible');
            setTimeout(function () {
                $('.select2-container').css('position', 'relative');
            }, 200);
        }
    });

    //close popup when clicking the esc keyboard button
    $(document).keyup(function (event) {
        if (event.which == '27') {
            $('.cd-popup').removeClass('is-visible');
            setTimeout(function () {
                $('.select2-container').css('position', 'relative');
            }, 200);
        } else if (event.key === 'Enter') {
            $('#unlockModal .locked-user-btn').click();
        }
    });

    // $('#unlockModal').on("keyup", function (event) {
    //     if (event.key === 'Enter') {
    //         $('#unlockModal .locked-user-btn').click();
    //     } else if (event.which === '27') {
    //         $('.cd-popup').removeClass('is-visible');
    //         setTimeout(function () {
    //             $('.select2-container').css('position', 'relative');
    //         }, 200);
    //     }
    // })
    $('#unlockModal').on('click', '.locked-user-btn', function () {
        $('.locked-user-btn').prop('disabled', true)
        lockUser(userId);
    })

    //locked user
    function lockUser(userId) {
        $.ajax({
            url: '/api/v1/admin/users/' + userId,
            type: 'PUT',
            contentType: "application/json; charset=utf-8",
            success: function () {
                toastr.success("Success!");
                setTimeout(function () {
                    window.location.reload();
                }, 800)
            },
            error: function () {
                toastr.warning("Not success!");
            }
        })
    }
})