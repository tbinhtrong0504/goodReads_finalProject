let firebaseConfig = {
    apiKey: "AIzaSyDHL076Sw_Aru3k1TwtODbumP1uvVNP9tg",
    projectId: "fir-e9a96",
    storageBucket: "fir-e9a96.appspot.com",
};
// Khởi tạo Firebase
firebase.initializeApp(firebaseConfig);
// Lấy tham chiếu đến Firebase Storage bucket
let storage = firebase.storage();
let storageRef = storage.ref();

// Update personal information
$(document).ready(function () {

    //open chosen image form
    $('#change-avatar').click(() => {
        $('#fileInput').click()
    })

    // show image
    let chosenFile = null;
    $('#fileInput').change(event => {
        const tempFiles = event.target.files;
        const maxSizeInBytes = 5242880;
        if (!tempFiles || tempFiles.length === 0) {
            return;
        }
        chosenFile = tempFiles[0];
        if (chosenFile && chosenFile.size > maxSizeInBytes) {
            alert("File size exceeded the allowed limit (5MB)!");
            this.value = '';
            return;
        }
        const imageBlob = new Blob([chosenFile], {type: chosenFile.type});
        const imageUrl = URL.createObjectURL(imageBlob);
        let showImageHtml = `<img src="${imageUrl}" alt="image" class="img-ratio rounded-4">`
        $('#show-avatar').html(showImageHtml)
    });

    $.validator.addMethod("pastDate", function (value, element) {
        let currentDate = new Date();
        let inputDate = new Date(value);
        return inputDate < currentDate;
    }, "date must be the past date");

    $.validator.addMethod("phoneWithZeroPrefix", function (value, element) {
        return this.optional(element) || /^0\d{9}$/.test(value);
    }, "Please enter a valid 10-digit phone number starting with 0.");

// Edit user
    $("#edit-profile-form").validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        errorPlacement: function (error, element) {
            error.addClass("error-message");
            error.insertAfter(element);
        },
        rules: {
            "fullName": {
                maxlength: 255
            },
            "about": {
                maxlength: 65535
            },
            "dob": {
                pastDate: true
            },
            "phone": {
                phoneWithZeroPrefix: true
            }
        },
        messages: {
            "fullName": {
                maxlength: "Cannot be longer than 255 characters"
            },
            "about": {
                maxlength: "Cannot be longer than 65535 characters"
            },
            "dob": {
                pastDate: "* Date of birth must be the past date"
            },
            "phone": {
                phoneWithZeroPrefix: "Phone number incorrect"
            }
        },
        invalidHandler: function (form, validator) {
            let errors = validator.numberOfInvalids();
            if (errors) {
                let firstErrorElement = $(validator.errorList[0].element);
                $('html, body').animate({
                    scrollTop: firstErrorElement.offset().top - 200
                }, 500);
                firstErrorElement.focus();
            }
        }
    });

    $('#edit-profile-form input').on('keyup', function (event) {
        if (event.key === 'Enter') {
            $('#submit-edit-personal-btn').click();
        }
    })

    $('#submit-edit-personal-btn').click(event => {
        let userId = userResponse.id;
        let isValidForm = $("#edit-profile-form").valid();
        if (!isValidForm) return;
        $('#submit-edit-personal-btn').prop('disabled', true);
        uploadImageAndUpdateUser(userId);
    })

    async function uploadImageAndUpdateUser(userId) {
        let fullName = $('#fullName').val();
        let phone = $('#phone').val();
        let dob = $('#dob').val();
        let about = $('#about').val();
        let gender = $('#gender').val();

        let editUser = {
            avatar: "",
            fullName: fullName,
            phone: phone,
            dob: dob,
            about: about,
            gender: gender,
            email: ''
        };

        if (chosenFile != null) {
            editUser.avatar = await uploadImage(chosenFile)
            updateUser(editUser);
        } else {
            updateUser(editUser);
        }
    }

    async function uploadImage(chosenFile) {
        try {
            let imageName = chosenFile.name;
            let imageRef = storageRef.child("images/" + imageName);
            let snapshort = await imageRef.put(chosenFile);
            return await snapshort.ref.getDownloadURL();
        } catch (error) {
            throw error;
        }
    }

    function updateUser(editUser) {
        $.ajax({
            url: '/api/v1/admin/profile',
            type: 'PUT',
            contentType: "application/json",
            data: JSON.stringify(editUser),
            success: function () {
                toastr.success("Update profile successfully!");
                window.scrollTo(0, 0);
                setTimeout(function () {
                    $('#submit-edit-personal-btn').prop('disabled', false);
                    saveNewInformation(editUser);
                    window.location.reload();
                }, 700)
            },
            error: function () {
                toastr.warning("Update profile not successfully!");
                $('#submit-edit-personal-btn').prop('disabled', false);
            }
        })
    }
})

function saveNewInformation(newInfo) {
    let userInfomation = JSON.parse(localStorage.getItem("userInfomation"))
    let userData = {
        "email": newInfo.email === '' ? userInfomation.email : newInfo.email,
        "userId": userInfomation.userId,
        "avatar": newInfo.avatar === '' ? userInfomation.avatar : newInfo.avatar,
        "fullName": newInfo.fullName === '' ? userInfomation.fullName : newInfo.fullName,
        "role": userInfomation.role
    };
    localStorage.setItem('userInfomation', JSON.stringify(userData))
}

// Change password
$(document).ready(function () {

    $.validator.addMethod("notEqualTo", function (value, element, param) {
        return this.optional(element) || value !== $(param).val();
    }, "New password must be different from current password");

    // Change Password
    $('#change-password-form').validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        errorPlacement: function (error, element) {
            error.addClass("error-message");
            error.insertAfter(element);
        },
        rules: {
            "current-password": {
                required: true
            },
            "new-password": {
                required: true,
                minlength: 6,
                notEqualTo: "#current-password"
            },
            "re-password": {
                required: true,
                equalTo: "#new-password"
            }
        },
        messages: {
            "current-password": {
                required: "* Enter your current password"
            },
            "new-password": {
                required: "* Enter your new password",
                minlength: "* Please enter at least 6 characters",
                notEqualTo: "* New password must be different from current password"
            },
            "re-password": {
                required: "* Repeat your new password",
                equalTo: "* Re-password incorrect"
            }
        },
        invalidHandler: function (form, validator) {
            let errors = validator.numberOfInvalids();
            if (errors) {
                let firstErrorElement = $(validator.errorList[0].element);
                $('html, body').animate({
                    scrollTop: firstErrorElement.offset().top - 200
                }, 500);
                firstErrorElement.focus();
            }
        }
    })

    $('#change-password-form input').on('keyup', function (event) {
        if (event.key === 'Enter') {
            $('#change-password-btn').click();
        } else if (event.which === 27) {
            $('#cancel-change-password-btn').click();
        }
    })
    $('#change-password-btn').click(() => {
        let isValidForm = $('#change-password-form').valid();
        if (!isValidForm) {
            return;
        }
        $('#change-password-btn').prop('disabled', true);
        // change password
        let formData = {
            currentPassword: CryptoJS.MD5($('#current-password').val()).toString(),
            newPassword: CryptoJS.MD5($('#new-password').val()).toString(),
            rePassword: CryptoJS.MD5($('#re-password').val()).toString(),
        }
        changePassword(formData);
    })

    function changePassword(formData) {
        $.ajax({
            url: "/api/v1/authentication/password-change",
            type: 'PUT',
            contentType: "application/json",
            data: JSON.stringify(formData),
            success: function () {
                toastr.success("Changed password successfully!");
                setTimeout(function () {
                    $.ajax({
                        url: '/api/v1/authentication/logout',
                        type: 'POST',
                        success: function () {
                            localStorage.clear()
                            window.location.href = 'http://localhost:8080/login'
                        },
                        error: function () {
                            toastr.warning("Error network!")
                        }
                    });
                }, 700);
            },
            error: function () {
                toastr.warning("Changed password not successfully!");
                $('#change-password-btn').prop('disabled', false);
            }
        })
    }

    $('#cancel-change-password-btn').click(() => {
        $('#change-password-form').validate().resetForm();
        $('#current-password').val('');
        $('#new-password').val('');
        $('#re-password').val('');
    })

    $('#show-password').change(function () {
        if ($(this).is(':checked')) {
            $('#current-password').prop('type', 'text');
            $('#new-password').prop('type', 'text');
            $('#re-password').prop('type', 'text');
        } else {
            $('#current-password').prop('type', 'password');
            $('#new-password').prop('type', 'password');
            $('#re-password').prop('type', 'password');
        }
    })

    $('#current-password, #new-password, #re-password').on('keyup', function (event) {
        if (event.key !== 'Enter') {
            $('#change-password-form').validate().resetForm();
        }
    })
})

// update address
$(document).ready(function () {
    // Kích hoạt select2
    $('#province').select2({
        placeholder: "- Province..."
    });
    $('#district').select2({
        placeholder: "- District..."
    });
    $('#ward').select2({
        placeholder: "- Ward..."
    });
    $('.select2-container').prop('style', 'width: 100%;')

    renderAddressCurrent();
    renderProvinceOptions();

    $('#province').change(function () {
        renderDistrictOptions();
    })

    $('#district').change(function () {
        renderWardOptions();
    })

    $('#update-address-form').validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        errorPlacement: function (error, element) {
            error.addClass("error-message");
            error.insertAfter(element);
        },
        rules: {
            "province": {
                required: true
            },
            "district": {
                required: true
            },
            "ward": {
                required: true
            }
        },
        messages: {
            "province": {
                required: "* Please chose your province"
            },
            "district": {
                required: "* Please chose your district"
            },
            "ward": {
                required: "* Please chose your ward"
            }
        }
    })

    $('#update-address-btn').on('click', function () {
        // let isValidForm = $('#update-address-form').valid();
        // if (!isValidForm) {
        //     return;
        // }
        let wardCode = $('#ward').val();
        if (wardCode === null || wardCode.trim() === '') {
            toastr.warning('Please chose your address before save')
            return;
        }
        $('#update-address-btn').prop("disabled", true);
        let formData = {
            wardCode: wardCode,
            street: $('#street').val()
        }
        updateAddress(formData);
    })

})

function renderAddressCurrent() {
    let streetCurrent = userResponse.street;
    let provinceCurrent = userResponse.provinceFullName;
    let districtCurrent = userResponse.districtFullName;
    let wardCurrent = userResponse.wardFullName;

    let addressParts = [streetCurrent, wardCurrent, districtCurrent].map(part => part ? part + ', ' : '').join('');
    let provincePart = provinceCurrent ? provinceCurrent : '';

    let addressHTML = `<label class="lh-1 text-16 text-light-1">Current Address: </label>
                            <input type="text" disabled
                                    value="${addressParts}${provincePart}">
                       `;

    $('#current-address').html(addressHTML)
}

function renderProvinceOptions() {
    let optionProvinceHTML = '<option></option>';
    for (let i = 0; i < provinceList.length; i++) {
        let option = `<option value="${provinceList[i].provinceCode}">
                                     ${provinceList[i].provinceName}
                      </option>`;
        optionProvinceHTML += option;
    }
    $('#province').html(optionProvinceHTML);
}

function renderDistrictOptions() {
    let provinceCode = $('#province').val();
    $.ajax({
        url: '/api/v1/users/districts/' + provinceCode,
        type: 'GET',
        success: function (data) {
            $('.district-container').show()
            $('#district').focus();

            let optionDistricHTML = `<option></option>`;
            for (let i = 0; i < data.length; i++) {
                let option = `<option value="${data[i].districtCode}">
                                             ${data[i].districtName}
                              </option>`;
                optionDistricHTML += option;
            }
            $('#district').html(optionDistricHTML);
        }, error: function () {
            toastr.warning('Not found data')
        }
    })
}

function renderWardOptions() {
    let districtCode = $('#district').val();
    $.ajax({
        url: '/api/v1/users/wards/' + districtCode,
        type: 'GET',
        success: function (data) {
            $('.ward-container').show()
            $('#ward').focus();

            let optionWardHTML = `<option></option>`;
            for (let i = 0; i < data.length; i++) {
                let option = `<option value="${data[i].wardCode}">
                                             ${data[i].wardName}
                              </option>`;
                optionWardHTML += option;
            }
            $('#ward').html(optionWardHTML);
        }, error: function () {
            toastr.warning('Not found data')
        }
    })
}

function updateAddress(formData) {
    $.ajax({
        url: '/api/v1/users/address',
        type: 'PUT',
        data: JSON.stringify(formData),
        contentType: "application/json",
        success: function () {
            toastr.success("Updated address successfully");
            setTimeout(function () {
                window.location.reload();
            }, 700);
        }, error: function () {
            toastr.warning("Updated address not successfully")
        }
    })
}