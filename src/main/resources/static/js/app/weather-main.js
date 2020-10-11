var startDatePicker;
var endDatePicker;
var pageMeta = {
    currentPage: 1,
    lastPage: 1
};
var main = {
    init : function () {
        var _this = this;
        var startDatePicker = new tui.DatePicker('#wrapper', {
            date: new Date(),
            input: {
              element: '#datepicker-start',
              format: 'yyyy-MM-dd HH'
            },
            timePicker: true
        });
        var endDatePicker = new tui.DatePicker('#wrapper', {
            date: new Date(),
            input: {
              element: '#datepicker-end',
              format: 'yyyy-MM-dd HH'
            },
            timePicker: true
        });
        $('#btn-search').on('click', function () {
            _this.search();
        });
        $('.pagination-link').on('click', function () {
            _this.searchPage();
        });
        $('.pagination-previous').on('click', function () {
            _this.searchPrevious();
        });
        $('.pagination-next').on('click', function () {
            _this.searchNext();
        });
    },
    search : function (selectedPage) {
        var selectedPage = selectedPage === null? 1 : selectedPage;
        var startDate = moment(startDatePicker.getDate()).format('YYYYMMDDHHmm');
        var endDate = moment(endDatePicker.getDate()).format('YYYYMMDDHHmm');
        var requestDto = {
            startDate: startDate,
            endDate: endDate,
            locationCode: $('#location-code').val()
        };
        $.ajax({
            type: 'POST',
            url: '/api/v1/weather?page='+selectedPage,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(requestDto)
        }).done(function(response) {
            // 테이블 데이터를 그린다
            var resultSet='<tr>'
            +'<td>'+response.date+'</td>'
            +'<td>'+response.condition+'</td>'
            +'<td>'+response.temperature+'</td>'
            +'<td>'+response.humidity+'</td>'
            +'<td>'+response.precipitation_type+'</td>'
            +'<td>'+response.precipitation+'</td>'
            +'<td>'+response.wind_speed+'</td>'
            +'<td>'+response.wind_direction+'</td>'
            +'<td>'+response.wind_component_ew+'</td>'
            +'<td>'+response.wind_component_sn+'</td>'
            +'<td>'+response.lightning+'</td>'
            +'</tr>';
            $('#weather-table').html(resultSet);

            // 페이지 정보를 설정한다
            pageMeta.currentPage = response.page_number;

            // 페이지네이션 처리를 한다
            $('.pagination-previous').removeProp('disabled');
            $('.pagination-next').removeProp('disabled');
            $('.pagination-link').each(index, item){
                item.removeAttr('is-current');
            }
            if(pageMeta.currentPage === 1){
                $('.pagination-previous').prop('disabled');
            }
            if(pageMeta.currentPage === pageMeta.lastPage){
                $('.pagination-next').prop('disabled');
            }
            $('.pagination-link').each(index, item){
                if(item.)
            }
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    searchPage : function () {
        var selectedPage = $(this).data('page-number');
        if (selectedPage === pageMeta.currentPage) return;
        _this.search(selectedPage);
    },
    searchPrevious : function () {
        if (pageMeta.currentPage <= 1) {
            alert('첫 페이지입니다.');
            return;
        }
        _this.search(pageMeta.currentPage - 1);
    },
    searchNext : function () {
        if (pageMeta.currentPage >= pageMeta.lastPage) {
            alert('마지막 페이지입니다.');
            return;
        }
        _this.search(pageMeta.currentPage + 1);
    }
};

main.init();
