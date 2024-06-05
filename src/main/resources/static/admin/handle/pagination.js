$(document).ready(function () {

    $('#pageInput').select2({});
    $('#pageNumberInput').select2({});


    $('#add-book').click(() => {
        window.location.href = '/admin/add-book'
    })

    let searchFrom = getPreviousSearchCriteria();

    $('.search-admin, #search-by div').on('keyup', function (event) {
        if (event.key === 'Enter') {
            $('#btn-search-admin').click();
        }
    })

    $('#btn-search-admin').click(() => {
        changeUrl(1, pageSize);
    })

    $('#pageInput').change(() => {
        let pageNumber = $('#pageInput').val();
        changeUrl(pageNumber, pageSize);
    })

    $('.chose-page').click(event => {
        let pageIndex = $(event.currentTarget).attr('page-number');
        changeUrl(pageIndex, pageSize);
    })

    $('.previous-link').click(() => {
        const pageIndex = urlParam.get('pageIndex')
        changeUrl(pageIndex - 1, pageSize);
    })

    $('.next-link').click(() => {
        const pageIndex = parseInt(urlParam.get('pageIndex')) || 1
        changeUrl(pageIndex + 1, pageSize);
    })

    $('#pageNumberInput').change((event) => {
        let chosenPageSize = event.target.value;
        changeUrl(1, chosenPageSize)
    })

    function changeUrl(pageNumber, pageSize) {
        let keyword = checkSearchInput();
        if (keyword !== '') {
            let searchParam = getSearchParam(keyword);
            if (searchFrom === 'books') {
                window.location.href = `/admin/${searchFrom}?pageIndex=${pageNumber}&pageSize=${pageSize}&${searchParam}`;
            } else if (searchFrom === 'categories') {
                window.location.href = `/admin/${searchFrom}?pageIndex=${pageNumber}&pageSize=${pageSize}&name=${keyword}`;
            } else if (searchFrom === 'users') {
                window.location.href = `/admin/${searchFrom}?pageIndex=${pageNumber}&pageSize=${pageSize}&email=${keyword}`;
            }
        } else {
            window.location.href = `/admin/${searchFrom}?pageIndex=${pageNumber}&pageSize=${pageSize}`;
        }
    }

    function checkSearchInput() {
        let searchKeyword = $('.search-admin').val();
        return searchKeyword.trim();
    }

    function getSearchParam(keyword) {
        let checkedValues = [];
        let searchParam = '';
        $("#search-by input[type='checkbox']:checked").each(function () {
            checkedValues.push($(this).val());
        })
        if (checkedValues.length > 0) {
            checkedValues.forEach(function (value) {
                searchParam += `${value}=${keyword}&`;
            })
        } else {
            searchParam = `all=${keyword}&`;
        }
        return searchParam;
    }

    function getPreviousSearchCriteria() {
        const name = urlParam.get('name')
        const email = urlParam.get('email')

        const currentUrl = window.location.href;
        const adminIndex = currentUrl.indexOf("/admin/");
        const paramIndex = currentUrl.indexOf("?");

        let searchFrom;
        if (paramIndex !== -1) {
            searchFrom = currentUrl.substring(adminIndex + ("/admin/".length), paramIndex);
        } else {
            searchFrom = currentUrl.substring(adminIndex + ("/admin/".length), currentUrl.length);
        }

        if (searchFrom === 'books') {
            $('.search-admin').val(checkParamsAndCheckBoxesAndGetSearchBook());
        } else if (searchFrom === 'categories') {
            $('.search-admin').val(name);
        } else if (searchFrom === 'users') {
            $('.search-admin').val(email);
        }
        return searchFrom;
    }

    function checkParamsAndCheckBoxesAndGetSearchBook() {
        let searchBook;
        const paramToCheck = ['title', 'author', 'category', 'all'];
        paramToCheck.forEach(function (param) {
            if (urlParam.has(param)) {
                const checkboxId = `#checkbox${param.charAt(0).toUpperCase() + param.slice(1)}`;
                $(checkboxId).prop('checked', true);
            }
            if (urlParam.get(param) !== null) {
                searchBook = urlParam.get(param);
            }
        })
        return searchBook;
    }
})





