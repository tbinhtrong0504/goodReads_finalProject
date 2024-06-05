//PAGE LOADER
$(window).on("load", function () {

    function checkLoggedIn() {
        const jwtToken = localStorage.getItem("jwtToken");

        if (!jwtToken) {
            $(".tg-wishlistandcart").empty();
            const userHtmlContent = "<div class='btn-group btn-group-regis'>\n" +
                "<a type=\"button\" href=\"/login\" class=\"btn btn-regis-signin\">SIGN IN</a>\n" +
                " <a type=\"button\" href=\"/signup\" class=\"btn btn-regis-signup\">SIGN UP</a>\n" +
                " </div>";
            $(".tg-wishlistandcart").append(userHtmlContent);

        } else {
            // avatar ADMIN
            const userInfomation = JSON.parse(localStorage.getItem('userInfomation'));
            const avatar = userInfomation.avatar;
            const fullName = userInfomation.fullName;

            let avatarAdminHtml;
            if (avatar === null) {
                avatarAdminHtml = ` <img src="https://firebasestorage.googleapis.com/v0/b/fir-e9a96.appspot.com/o/images%2Fu_60x60-267f0ca0ea48fd3acfd44b95afa64f01.png?alt=media&token=894f32ca-266a-40c1-81c0-eb7f8142f13a" alt="image" class="size-40 rounded-22 object-cover">`
            } else {
                avatarAdminHtml = ` <img src="${avatar}" alt="image" class="size-40 rounded-22 object-cover">`
            }
            $('#avatar-admin').append(avatarAdminHtml);

            // Avatar trang phía user
            let userLoginHtml;
            if (userInfomation.role[0] === 'ADMIN') {
                userLoginHtml = `
                <div class="dropdown tg-themedropdown tg-wishlistdropdown">
                                    <a href="javascript:void(0);" id="tg-wishlisst" class="tg-btnthemedropdown"
                                       data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
<!--                                        <span class="tg-themebadge">3</span>-->
                                        <i class="fa-regular fa-bell"></i>
                                    </a>
                                    <div class="dropdown-menu tg-themedropdownmenu" aria-labelledby="tg-wishlisst">
                                        <div class="tg-description">
                                            <p>No products were added to the wishlist!</p>
                                        </div>
                                    </div>
                                </div>
                <div class="dropdown tg-themedropdown tg-minicartdropdown">
                                    <a href="javascript:void(0);" id="tg-minicart" class="tg-btnthemedropdown"
                                       data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
<!--                                        <span class="tg-themebadge">3</span>-->
                                        <i class="fa-solid fa-people-group"></i>
                                    </a>
                                    <div class="dropdown-menu tg-themedropdownmenu" aria-labelledby="tg-minicart">
                                        <div class="tg-minicartbody">
                                            <div class="tg-minicarproduct">
                                                <figure>
                                                    <img src="images/products/img-01.jpg" alt="image description">

                                                </figure>
                                                <div class="tg-minicarproductdata">
                                                    <h5><a href="javascript:void(0);">Our State Fair Is A Great
                                                        Function</a></h5>
                                                    <h6><a href="javascript:void(0);">$ 12.15</a></h6>
                                                </div>
                                            </div>
                                            <div class="tg-minicarproduct">
                                                <figure>
                                                    <img src="images/products/img-02.jpg" alt="image description">

                                                </figure>
                                                <div class="tg-minicarproductdata">
                                                    <h5><a href="javascript:void(0);">Bring Me To Light</a></h5>
                                                    <h6><a href="javascript:void(0);">$ 12.15</a></h6>
                                                </div>
                                            </div>
                                            <div class="tg-minicarproduct">
                                                <figure>
                                                    <img src="images/products/img-03.jpg" alt="image description">

                                                </figure>
                                                <div class="tg-minicarproductdata">
                                                    <h5><a href="javascript:void(0);">Have Faith In Your Soul</a>
                                                    </h5>
                                                    <h6><a href="javascript:void(0);">$ 12.15</a></h6>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tg-minicartfoot">
                                            <a class="tg-btnemptycart" href="javascript:void(0);">
                                                <i class="fa fa-trash-o"></i>
                                                <span>Clear Your Cart</span>
                                            </a>
                                            <span class="tg-subtotal">Subtotal: <strong>35.78</strong></span>
                                            <div class="tg-btns">
                                                <a class="tg-btn tg-active" href="javascript:void(0);">View Cart</a>
                                                <a class="tg-btn" href="javascript:void(0);">Checkout</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                <div class="dropdown tg-themedropdown tg-currencydropdown">
                                    <a href="javascript:void(0);" id="tg-currenty" class="tg-btnthemedropdown"
                                       data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        <span> Hi, ${fullName}</span>
                                        <a href="javascript:void(0);" id="avatar-user">
                                            <img 
                                                 style="border-radius: 50%;height:45px;width:45px;margin-left: 10px; padding: 2px"
                                                 src="${avatar}"
                                                 alt="image description"></a>
                                    </a>

                                    <ul class="dropdown-menu tg-themedropdownmenu" aria-labelledby="tg-currenty">
                                        <li>
                                            <a href="/admin/profile">
                                                <i class="fa-regular fa-user"></i>
                                                <span>Profile</span>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="/users/challenge">
                                                <i class="fa-regular fa-bookmark"></i>
                                                <span>Reading Challenge</span>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="/admin/books">
                                                <i class="fa-regular fa-user"></i>
                                                <span>Admin page</span>
                                            </a>
                                        </li>
                                        
                                        <li>
                                            <a href="#" id="sign-out">
                                                <i class="fa-solid fa-arrow-right-from-bracket"></i>
                                                <span>Sign out</span>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                                `
            } else {
                userLoginHtml = `
                <div class="dropdown tg-themedropdown tg-wishlistdropdown">
                                    <a href="javascript:void(0);" id="tg-wishlisst" class="tg-btnthemedropdown"
                                       data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
<!--                                        <span class="tg-themebadge">3</span>-->
                                        <i class="fa-regular fa-bell"></i>
                                    </a>
                                    <div class="dropdown-menu tg-themedropdownmenu" aria-labelledby="tg-wishlisst">
                                        <div class="tg-description">
                                            <p>No products were added to the wishlist!</p>
                                        </div>
                                    </div>
                                </div>
                <div class="dropdown tg-themedropdown tg-minicartdropdown">
                                    <a href="javascript:void(0);" id="tg-minicart" class="tg-btnthemedropdown"
                                       data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
<!--                                        <span class="tg-themebadge">3</span>-->
                                        <i class="fa-solid fa-people-group"></i>
                                    </a>
                                    <div class="dropdown-menu tg-themedropdownmenu" aria-labelledby="tg-minicart">
                                        <div class="tg-minicartbody">
                                            <div class="tg-minicarproduct">
                                                <figure>
                                                    <img src="images/products/img-01.jpg" alt="image description">

                                                </figure>
                                                <div class="tg-minicarproductdata">
                                                    <h5><a href="javascript:void(0);">Our State Fair Is A Great
                                                        Function</a></h5>
                                                    <h6><a href="javascript:void(0);">$ 12.15</a></h6>
                                                </div>
                                            </div>
                                            <div class="tg-minicarproduct">
                                                <figure>
                                                    <img src="images/products/img-02.jpg" alt="image description">

                                                </figure>
                                                <div class="tg-minicarproductdata">
                                                    <h5><a href="javascript:void(0);">Bring Me To Light</a></h5>
                                                    <h6><a href="javascript:void(0);">$ 12.15</a></h6>
                                                </div>
                                            </div>
                                            <div class="tg-minicarproduct">
                                                <figure>
                                                    <img src="images/products/img-03.jpg" alt="image description">

                                                </figure>
                                                <div class="tg-minicarproductdata">
                                                    <h5><a href="javascript:void(0);">Have Faith In Your Soul</a>
                                                    </h5>
                                                    <h6><a href="javascript:void(0);">$ 12.15</a></h6>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tg-minicartfoot">
                                            <a class="tg-btnemptycart" href="javascript:void(0);">
                                                <i class="fa fa-trash-o"></i>
                                                <span>Clear Your Cart</span>
                                            </a>
                                            <span class="tg-subtotal">Subtotal: <strong>35.78</strong></span>
                                            <div class="tg-btns">
                                                <a class="tg-btn tg-active" href="javascript:void(0);">View Cart</a>
                                                <a class="tg-btn" href="javascript:void(0);">Checkout</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                <div class="dropdown tg-themedropdown tg-currencydropdown">
                                    <a href="javascript:void(0);" id="tg-currenty" class="tg-btnthemedropdown"
                                       data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        <span> Hi, ${fullName}</span>
                                        <a href="javascript:void(0);" id="avatar-user">
                                            <img 
                                                 style="border-radius: 50%;height:45px;width:45px;margin-left: 10px; padding: 2px"
                                                 src="${avatar}"
                                                 alt="image description"></a>
                                    </a>

                                    <ul class="dropdown-menu tg-themedropdownmenu" aria-labelledby="tg-currenty">
                                        <li>
                                            <a href="javascript:void(0);">
                                                <i class="fa-regular fa-user"></i>
                                                <span>Profile</span>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="/users/challenge">
                                                <i class="fa-regular fa-bookmark"></i>
                                                <span>Reading Challenge</span>
                                            </a>
                                        </li>
                                        
                                        <li>
                                            <a href="#" id="sign-out">
                                                <i class="fa-solid fa-arrow-right-from-bracket"></i>
                                                <span>Sign out</span>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
            `;
            }

            $(".tg-wishlistandcart").append(userLoginHtml);
        }
    }

    checkLoggedIn();

    // Sign out
    $("#sign-out").click(() => {
        let jwtToken = getJwtToken();
        if (jwtToken) {
            $.ajax({
                url: '/api/v1/authentication/logout',
                type: 'POST',
                success: function () {
                    localStorage.clear()
                    toastr.success("Log out success")
                    setTimeout(function () {
                        window.location.href = 'http://localhost:8080/login'
                    }, 700)
                },
                error: function () {
                    toastr.warning("Error network!")
                }
            });
        } else {
            toastr.warning("You are not login")
        }
    });
});

function getJwtToken() {
    return localStorage.getItem('jwtToken');
}

function getRefreshToken() {
    return localStorage.getItem('refreshToken');
}

function refreshToken() {
    console.log("run refreshtoken");
    let jwtToken = getJwtToken();
    if (!jwtToken) {
        return;
    }
    let refreshToken = getRefreshToken();
    let request = {
        refreshToken: refreshToken
    };
    $.ajax({
        url: "/api/v1/authentication/refresh-token",
        type: "POST",
        data: JSON.stringify(request),
        contentType: "application/json; charset=utf-8",
        // headers: {
        //     'Authorization': 'Bearer' + " " + jwtToken
        // },
        success: function (response) {
            localStorage.setItem("jwtToken", response.jwt);
        },
        error: function () {
            console.log(error);
        }
    })
}

setInterval(refreshToken, 23.5 * 60 * 60 * 1000);

// login and signup
$(document).ready(function () {
    // Sign up
    toastr.options = {
        positionClass: 'toast-center',
        timeOut: 1500
    };

    $("#register-form").validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        errorPlacement: function (error, element) {
            error.addClass("error-message");
            error.insertAfter(element);
        },
        rules: {
            "email": {
                required: true,
                email: true
            },
            "password": {
                required: true,
                minlength: 6
            },
            "re-pass": {
                required: true,
                equalTo: "#password"
            }
        },
        messages: {
            "email": {
                required: "* Please enter your email",
                email: "* Incorrect email format"
            },
            "password": {
                required: "* Please enter your password",
                minlength: "* Please enter at least 6 characters"

            },
            "re-pass": {
                required: "* Repeat your password",
                equalTo: "* Re-password incorrect"
            }
        }
    });

    $('#register-form .form-group input').on('keyup', function (event) {
        if (event.key === 'Enter') {
            $("#signup").click();
        }
    })

    $("#signup").click((key, value) => {
        let isValidForm = $("#register-form").valid()
        if (!isValidForm) return
        $("#signup").prop('disabled', true)
        let signupName = $('#name').val()
        let signupEmail = $('#email').val()
        let signupPassword = $('#password').val()
        let request = {
            fullName: signupName,
            email: signupEmail,
            password: CryptoJS.MD5(signupPassword).toString()
        }
        $.ajax({
            url: '/api/v1/authentication/signup',
            type: 'POST',
            data: JSON.stringify(request),
            contentType: "application/json; charset=utf-8",
            success: function (data) {

                toastr.success("Signup Success");
                setTimeout(function () {
                    alert("Please check your email to active now!");
                    window.location.href = 'http://localhost:8080/login';
                }, 1000)
            },
            error: function (data) {
                toastr.warning("Email is existed!");
                setTimeout(function () {
                    window.location.reload();
                }, 1000);
            },
        });
    });

    // Login
    $("#login-form").validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        errorPlacement: function (error, element) {
            error.addClass("error-message");
            error.insertAfter(element);
        },
        rules: {
            "email": {
                required: true,
                email: true
            },
            "password": {
                required: true,
            }
        },
        messages: {
            "email": {
                required: "* Please enter your email",
                email: "* Incorrect email format"
            },
            "password": {
                required: "* Pleaser enter your password"
            }
        }
    });

    $("#signin").click((key, value) => {
        let isValidForm = $("#login-form").valid();
        if (!isValidForm) return;
        let loginEmail = $('#email').val();
        let loginPassword = $('#password').val();
        let request = {
            email: loginEmail,
            password: CryptoJS.MD5(loginPassword).toString()
        };
        $.ajax({
            url: '/api/v1/authentication/login',
            type: 'POST',
            data: JSON.stringify(request),
            contentType: "application/json; charset=utf-8",
            success: function (data) {

                if (data.locked === true) {
                    window.alert('Your account was locked!')
                    return;
                }

                localStorage.clear();
                localStorage.setItem('jwtToken', data.jwt);
                localStorage.setItem('refreshToken', data.refreshToken);
                let userInfomation = {
                    email: data.email,
                    userId: data.id,
                    avatar: data.avatar,
                    fullName: data.fullName,
                    role: data.roles
                };
                localStorage.setItem('userInfomation', JSON.stringify(userInfomation));

                toastr.success("Login Success");
                setTimeout(function () {
                    if (data.roles[0] === "USER") {
                        window.location.href = 'http://localhost:8080/'
                    } else if (data.roles[0] === "ADMIN") {
                        window.location.href = 'http://localhost:8080/admin/books'
                    }
                }, 700);
            },
            error: function (data) {
                toastr.warning("Email or Password not correct!");
            },
        });
    });


    $('#login-form input').on('keyup', function (event) {
        if (event.key === 'Enter') {
            $("#signin").click();
        }
    });

    $('.forgot-password-link').click(() => {
        $('#forgot-password-modal').modal('show');
    });

    $(".email-reset-form").validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        errorPlacement: function (error, element) {
            error.addClass("error-message");
            error.insertAfter(element);
        },
        rules: {
            "email": {
                required: true,
                email: true
            }
        },
        messages: {
            "email": {
                required: "* Please enter your email",
                email: "* Incorrect email format"
            }
        }
    });

    $("#submit-reset-modal").click(async event => {
        let isValidForm = $(".email-reset-form").valid();
        if (!isValidForm) {
            return;
        }
        let emailReset = $(".email-reset-form #email").val();
        let request = {
            email: emailReset
        };
        await $.ajax({
            url: "/api/v1/anonymous/otp-sending",
            type: 'POST',
            data: JSON.stringify(request),
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                $(".email-reset-form #email").val('')
                $('#forgot-password-modal').modal('hide');
                toastr.success("Please check your email!");
            },
            error: function () {
                toastr.warning("Email not exist or not activated!");
            }
        });
    });

    $('#forgot-password-modal input').on('keydown', function (event) {
        if (event.which === 13 || event.keyCode === 13) {
            event.preventDefault();
            $("#submit-reset-modal").click();
        }
    });

    $('#show-password').change(function () {
        if ($(this).is(':checked')) {
            $('#password').prop('type', 'text')
            $('#re-pass').prop('type', 'text')
        } else {
            $('#password').prop('type', 'password')
            $('#re-pass').prop('type', 'password')
        }
    })

})