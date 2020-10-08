var datepicker;
var main = {
    init : function () {
        var _this = this;
        $('#btn-search').on('click', function () {
            _this.search();
        });

        datepicker = new tui.DatePicker('#wrapper', {
            date: new Date(),
            input: {
                element: '#datepicker-input',
                format: 'yyyy-MM-dd'
            }
        });
    },
    search : function () {
        var startDate = moment(datepicker.getDate()).format('YYYYMMDD');
        var requestDto = {
            startDate: startDate,
            endDate: startDate,
            startTime: $('#time').val(),
            endTime: $('#time').val(),
            locationCode: $('#location-code').val()
        };
        $.ajax({
            type: 'POST',
            url: '/api/v1/weather',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(requestDto)
        }).done(function(response) {
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

        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
};

main.init();
