$('#date-seach-order').daterangepicker({
    alwaysShowCalendars: true,
    autoUpdateInput: false,
    locale: {
        format: 'YYYY/MM/DD',
        cancelable: 'Clear'
    },
    opens:'center',
    showDropdowns: true,
    minYear: 1990,
    maxDate: new Date(),
    ranges: {
        'Today': [moment(), moment()],
        'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
        'Last 7 Days': [moment().subtract(6, 'days'), moment()],
        'Last 30 Days': [moment().subtract(29, 'days'), moment()]
    },
});
// $('input[id="date-seach-order"]').on('cancel.daterangepicker', function(ev, picker) {
//     $(this).val("MM/dd/yyyy"-"MM/dd/yyyy");
// });
$('input[id="date-seach-order"]').on('apply.daterangepicker', function(ev, picker) {
    $(this).val(picker.startDate.format('YYYY/MM/DD') + ' - ' + picker.endDate.format('YYYY/MM/DD'));
});

$('#date-seach-voucher').daterangepicker({
    alwaysShowCalendars: true,
    autoUpdateInput: false,
    locale: {
        format: 'YYYY/MM/DD',
        cancelable: 'Clear'
    },
    opens:'center',
    showDropdowns: true,
    minYear: 1990,
    ranges: {
        'Today': [moment(), moment()],
        'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
        'Last 7 Days': [moment().subtract(6, 'days'), moment()],
        'Last 30 Days': [moment().subtract(29, 'days'), moment()]
    },
});
$('input[id="date-seach-voucher"]').on('apply.daterangepicker', function(ev, picker) {
    $(this).val(picker.startDate.format('YYYY/MM/DD') + ' - ' + picker.endDate.format('YYYY/MM/DD'));
});
