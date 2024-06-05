$(document).ready(function () {
    $('#add-category').click(() => {
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

    function createCategory() {
        let newCategory = $('.input-category').val().trim();
        if (newCategory === '') {
            window.alert("Category not blank!")
            return;
        }
        $('#submit-category').prop('disabled', true);
        let formData = {
            name: newCategory
        };
        $.ajax({
            url: '/api/v1/admin/category',
            type: 'POST',
            data: JSON.stringify(formData),
            contentType: "application/json",
            success: function () {
                toastr.success("Created category successfully!");
                setTimeout(function () {
                    $('#submit-category').prop('disabled', false);
                    window.location.reload();
                }, 800);
            },
            error: function () {
                toastr.warning("Can not create new category");
            }
        })
    }

    $('#submit-category').click(() => {
        createCategory();
    })
    $('#myModal input').on('keyup', function (event) {
        if (event.key === 'Enter') {
            $('#submit-category').click();
        } else if (event.which === 27) {
            $('#myModal').css('display', 'none');
        }
    })

    // Edit category
    let categoryId = null;
    $('.edit-category').click((event) => {
        $('#edit-modal').css('display', 'block');
        $('#edit-modal input').focus();

        categoryId = $(event.currentTarget).attr('category-id')
        $.ajax({
            url: '/api/v1/admin/category/' + categoryId,
            type: 'GET',
            success: function (data) {
                console.log(data)
                $('.input-edit-category').val(data.name);
            }, error: function (data) {
                console.log("Can not get category")
                console.log(data)

            }
        })
    })
    $('.close').click(() => {
        $('#edit-modal').css('display', 'none');
    })

    function editCategory() {
        let category = $('.input-edit-category').val().trim();
        if (category === '') {
            window.alert("Category not blank!");
            return;
        }
        $('#submit-edit-category').prop('disabled', true);
        let formData = {
            name: category
        }
        $.ajax({
            url: '/api/v1/admin/category/' + categoryId,
            type: 'PUT',
            data: JSON.stringify(formData),
            contentType: "application/json",
            success: function () {
                toastr.success("Updated category successfully!");
                setTimeout(function () {
                    $('#submit-category').prop('disabled', false);
                    window.location.reload();
                }, 800);
            },
            error: function () {
                toastr.warning("Can not updated category");
            }
        })
    }

    $('#submit-edit-category').click(() => {
        editCategory();
    })

    $('#edit-modal input').on('keyup', function (event) {
        if (event.key === 'Enter') {
            $('#submit-edit-category').click();
        } else if (event.which === 27) {
            $('#edit-modal').css('display', 'none');
        }
    })
})

