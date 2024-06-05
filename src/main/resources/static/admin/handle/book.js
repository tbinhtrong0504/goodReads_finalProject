// Cấu hình thông tin Firebase của bạn
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

$(document).ready(function () {

    //open chosen image form
    $('.btn-upload-image').click(() => {
        $('#fileInput').click();
    });

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
        $('#image-book .btn-upload-image').empty();
        let showImageHtml = `<img alt='Avatar' style="width: auto; height: 100%; object-fit: cover" src='${imageUrl}'/>`;
        $('#image-book .btn-upload-image').append(showImageHtml)
    });


    async function uploadImageAndCreateBook() {
        let title = $("#title").val();
        let author = $("#author").val();
        let pages = $("#pages").val();
        let published = $("#published").val();
        let about = $("#about").val();
        let categories = $('#book-category').val();
        let categoryConvert = categories.map(c => String(c));

        let newBook = {
            image: "",
            buyBook: "",
            title: title,
            categoryId: categoryConvert,
            author: author,
            pages: pages,
            description: about,
            rating: 0,
            published: published,
        };
        console.log(newBook);
        // Upload the image first
        if (chosenFile != null) {
            newBook.image = await uploadImage(chosenFile);
            createBook(newBook);
        } else {
            createBook(newBook);
        }
    }

    function createBook(newBook) {
        $.ajax({
            type: "POST",
            url: "/api/v1/admin/book",
            contentType: "application/json",
            data: JSON.stringify(newBook),
            success: function (response) {
                toastr.success("Create book success!");
                setTimeout(function () {
                    $('#submitBtn').prop('disabled', false);
                    window.location.href = '/admin/books';
                }, 700)
            },
            error: function () {
                toastr.warning("Create book not success!");
                $('#submitBtn').prop('disabled', false);
            }
        });
    }

    $.validator.addMethod("pastDate", function (value, element) {
        // Lấy ngày hiện tại
        let currentDate = new Date();

        // Chuyển đổi giá trị ngày nhập vào sang đối tượng Date
        let inputDate = new Date(value);

        // So sánh ngày nhập vào với ngày hiện tại
        return inputDate < currentDate;
    }, "date must be the past date");

    $("#create-book-form").validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        errorPlacement: function (error, element) {
            if (element.attr("name") === "category") {
                error.addClass("error-message");
                error.appendTo("#category-error");
            } else {
                error.addClass("error-message");
                error.insertAfter(element);
            }
        },
        rules: {
            "title": {
                required: true,
                maxlength: 255
            },
            "author": {
                required: true,
                maxlength: 255
            },
            "pages": {
                required: true,
                max: 21450
            },
            "category": {
                required: true
            },
            "about": {
                maxlength: 65535
            },
            "published": {
                required: true,
                pastDate: true
            }
        },
        messages: {
            "title": {
                required: "* Enter title",
                maxlength: "Cannot be longer than 255 characters"
            },
            "author": {
                required: "* Enter author",
                maxlength: "Cannot be longer than 255 characters"
            },
            "pages": {
                required: "* Enter number of pages",
                max: "* Cannot exceed 21450 pages"
            },
            "category": {
                required: "* Select category",
            },
            "about": {
                maxlength: "Cannot be longer than 65535 characters"
            },
            "published": {
                required: "* Enter published date",
                pastDate: "* Published date must be the past date"
            }
        },
        invalidHandler: function (form, validator) {
            // Tìm trường đầu tiên có lỗi
            let errors = validator.numberOfInvalids();
            if (errors) {
                let firstErrorElement = $(validator.errorList[0].element);
                // Cuộn trình duyệt đến trường đầu tiên có lỗi
                $('html, body').animate({
                    scrollTop: firstErrorElement.offset().top - 200 // Điều chỉnh vị trí cuộn để hiển thị tooltip không bị che khuất
                }, 500);
                firstErrorElement.focus(); // Đưa con trỏ vào trường đầu tiên có lỗi
            }
        }
    });

    $('#submitBtn').click(event => {
        console.log("click");
        event.preventDefault();
        let isValidForm = $("#create-book-form").valid();
        if (!isValidForm) return;
        $('#submitBtn').prop('disabled', true);
        uploadImageAndCreateBook();
    })


// Edit book
    $("#edit-book-form").validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        errorPlacement: function (error, element) {
            if (element.attr("name") === "category") {
                error.addClass("error-message");
                error.appendTo("#category-error");
            } else {
                error.addClass("error-message");
                error.insertAfter(element);
            }
        },
        rules: {
            "title": {
                required: true,
                maxlength: 255
            },
            "author": {
                required: true,
                maxlength: 255
            },
            "pages": {
                required: true,
                max: 21450
            },
            "category": {
                required: true
            },
            "about": {
                maxlength: 65535
            },
            "published": {
                required: true,
                pastDate: true
            }
        },
        messages: {
            "title": {
                required: "* Enter title",
                maxlength: "Cannot be longer than 255 characters"
            },
            "author": {
                required: "* Enter author",
                maxlength: "Cannot be longer than 255 characters"
            },
            "pages": {
                required: "* Enter number of pages",
                max: "* Cannot exceed 21450 pages"
            },
            "category": {
                required: "* Select category",
            },
            "about": {
                maxlength: "Cannot be longer than 65535 characters"
            },
            "published": {
                required: "* Enter published date",
                pastDate: "* Published date must be the past date"
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

    $('#submit-edit-btn').click(event => {
        // let bookId = $(event.currentTarget).attr('book-id');
        let bookId = bookDetail.id;
        console.log(bookId)
        let isValidForm = $("#edit-book-form").valid();
        if (!isValidForm) return;
        $('#submit-edit-btn').prop('disabled', true);
        uploadImageAndUpdateBook(bookId);
    })

    async function uploadImageAndUpdateBook(bookId) {
        let title = $("#title").val();
        let author = $("#author").val();
        let pages = $("#pages").val();
        let published = $("#published").val();
        let description = $("#about").val();
        let buyBook = $("#buyBook").val();

        let categories = $('#book-category').val();
        let categoryConvert = categories.map(c => String(c));

        let editBook = {
            bookId: bookId,
            image: "",
            title: title,
            categoryId: categoryConvert,
            author: author,
            pages: pages,
            description: description,
            published: published,
            buyBook: buyBook
        };

        if (chosenFile != null) {
            editBook.image = await uploadImage(chosenFile)
            updateBook(editBook);

        } else {
            updateBook(editBook);
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

    function updateBook(editBook) {
        $.ajax({
            url: '/api/v1/admin/book',
            type: 'PUT',
            contentType: "application/json",
            data: JSON.stringify(editBook),
            success: function () {
                toastr.success("Update book success!");
                setTimeout(function () {
                    $('#submit-edit-btn').prop('disabled', false);
                    window.location.href = 'http://localhost:8080/admin/books';
                }, 700)
            },
            error: function () {
                toastr.warning("Update book not success!");
            }
        })
    }
})


