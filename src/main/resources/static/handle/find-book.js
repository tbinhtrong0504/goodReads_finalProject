const pageSize = 8;
const urlParam = new URLSearchParams(window.location.search);
const showPageSize = urlParam.get('pageSize') || pageSize;
const lastSearchCriteria = urlParam.get('search') || '';
$('#search-input').val(lastSearchCriteria);
$('#show-pagesize').val(showPageSize);

$(document).ready(function () {
    $('#search-input').on('keyup', function (event) {
        if (event.key === 'Enter') {
            $('#search-btn').click();
        }
    })

    $('#search-btn').click(() => {
        changeUrl(1, showPageSize);
    })

    $('#pageInput').change(() => {
        let pageNumber = $('#pageInput').val();
        changeUrl(pageNumber, showPageSize);
    })

    $('.chose-page').click(event => {
        let pageIndex = $(event.currentTarget).attr('page-number');
        changeUrl(pageIndex, showPageSize);
    })

    $('.previous-link').click(() => {
        const pageIndex = urlParam.get('pageIndex')
        changeUrl(pageIndex - 1, showPageSize);
    })

    $('.next-link').click(() => {
        const pageIndex = parseInt(urlParam.get('pageIndex')) || 1
        changeUrl(pageIndex + 1, showPageSize);
    })

    $('#show-pagesize').change((event) => {
        let chosenPageSize = event.target.value;
        changeUrl(1, chosenPageSize)
    })

    function checkSearchInput() {
        let searchKeyword = $('#search-input').val();
        return searchKeyword.trim();
    }

    function changeUrl(pageNumber, pageSize) {
        let keyword = checkSearchInput();
        if (keyword !== '') {
            window.location.href = `/books?pageIndex=${pageNumber}&pageSize=${pageSize}&search=${keyword}`;
        } else {
            window.location.href = `/books?pageIndex=${pageNumber}&pageSize=${pageSize}`;
        }
    }

})