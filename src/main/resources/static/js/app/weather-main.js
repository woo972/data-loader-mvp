var startDatePicker;
var endDatePicker;
var pageMeta = {
    displayLimit: 10,
    paginationStartPage: 0,
    currentPage: 0,
    lastPage: 0
};
var main = {
    init : function () {
        var _this = this;
        startDatePicker = new tui.DatePicker('#start-wrapper', {
            date: new Date(),
            language: 'ko',
            input: {
              element: '#datepicker-start',
              format: 'yyyy-MM-dd HH:00'
            },
            timePicker: {
                defaultHour: 00,
                defaultMinute: 00,
                inputType: 'selectbox',
                hourStep: 1,
                minuteStep: 60,
                showMeridiem: false
            }
        });
        endDatePicker = new tui.DatePicker('#end-wrapper', {
            date: new Date(),
            language: 'ko',
            input: {
              element: '#datepicker-end',
              format: 'yyyy-MM-dd HH:00'
            },
            timePicker: {
                defaultHour: 23,
                defaultMinute: 00,
                inputType: 'selectbox',
                hourStep: 1,
                minuteStep: 60,
                showMeridiem: false
            }
        });
        $('#btn-search').on('click', function () {
            _this.search();
        });
        $('.pagination-link').on('click', function () {
            _this.searchPage($(this).data('page-number'));
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
            var tableData='';
            if(response.content.length <= 0){
                tableData='데이터 없음';
            }else{
                for(var idx = 0; idx < response.content.length; idx++){
                    tableData+='<tr>'
                        +'<td>'+response.content[idx].date+'</td>'
                        +'<td>'+response.content[idx].condition+'</td>'
                        +'<td>'+response.content[idx].temperature+'</td>'
                        +'<td>'+response.content[idx].humidity+'</td>'
                        +'<td>'+response.content[idx].precipitation_type+'</td>'
                        +'<td>'+response.content[idx].precipitation+'</td>'
                        +'<td>'+response.content[idx].wind_speed+'</td>'
                        +'<td>'+response.content[idx].wind_direction+'</td>'
                        +'<td>'+response.content[idx].wind_component_ew+'</td>'
                        +'<td>'+response.content[idx].wind_component_sn+'</td>'
                        +'<td>'+response.content[idx].lightning+'</td>'
                        +'</tr>';
                }
            }
            $('#weather-table').html(tableData);

            // 페이지 정보를 설정한다
            pageMeta.currentPage = response.number;
            pageMeta.lastPage = response.totalPages;

            // 페이지네이션 ui를 초기화한다
            $('.pagination-link').each(function(index, item){
                $(this).removeAttr('is-current');
            });
            if(pageMeta.currentPage <= pageMeta.displayLimit){
                $('.pagination-previous').prop('disabled');
            }else{
                $('.pagination-previous').prop('disabled', false);
            }
            if(pageMeta.currentPage + pageMeta.displayLimit > pageMeta.lastPage){
                $('.pagination-next').prop('disabled');
            }else{
                $('.pagination-next').prop('disabled', false);
            }

            // 페이지네이션 ui를 그린다
            var paginationData='';
            var paginationLastIndex = Math.min(pageMeta.lastPage, pageMeta.displayLimit);
            for(var index = 0; index < paginationLastIndex; index++){
                var isCurrent = (pageMeta.paginationStartPage + index) === pageMeta.currentPage? 'is-current' : '';
                paginationData+='<li>'
                    +'<a class="pagination-link '
                    +isCurrent
                    +'" data-page-number="' + (pageMeta.paginationStartPage + index)
                    +'" onclick="main.searchPage('+(pageMeta.paginationStartPage + index)+')">'
                    + (pageMeta.paginationStartPage + index + 1) + '</a></li>';
            }
            $('.pagination-list').html(paginationData);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    searchPage : function (selectedPage) {
        if (selectedPage === pageMeta.currentPage) return;
        this.search(selectedPage);
    },
    searchPrevious : function () {
        if (pageMeta.currentPage < pageMeta.displayLimit) {
            alert('첫 페이지 묶음입니다.');
            return;
        }
        pageMeta.paginationStartPage = pageMeta.currentPage - pageMeta.displayLimit;
        this.search(pageMeta.currentPage - pageMeta.displayLimit);
    },
    searchNext : function () {
        if (pageMeta.currentPage + pageMeta.displayLimit > pageMeta.lastPage) {
            alert('마지막 페이지 묶음입니다.');
            return;
        }
        pageMeta.paginationStartPage = pageMeta.currentPage + pageMeta.displayLimit;
        this.search(pageMeta.currentPage + pageMeta.displayLimit);
    }
};

main.init();
