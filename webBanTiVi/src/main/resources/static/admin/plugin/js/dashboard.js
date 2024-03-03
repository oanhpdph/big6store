
function oncl() {
    window.location.href = "http://localhost:8080/admin/bill/list_bill"
}
function onclProduct() {
    window.location.href = "http://localhost:8080/admin/product/list"
}
function onclWait() {
    window.location.href = "http://localhost:8080/admin/bill/list_bill?billStatus=doncho&size=10&page=1"
}
function onclDeliver() {
    window.location.href = "http://localhost:8080/admin/bill/list_bill?billStatus=danggiao&size=10&page=1"
}
function onclDiscount() {
    window.location.href = "http://localhost:8080/admin/coupon/list"
}
function onclReturn() {
    window.location.href = "http://localhost:8080/admin/bill/list_invoice_return"
}
function onclVoucher() {
    window.location.href = "http://localhost:8080/admin/voucher/list"
}

$(document).ready(function () {
    var defaultValue = $("#completed").attr("defaultValue");
    $("#date").change(function () {
        var date = $(this).val();
        $.ajax({
            url: "/admin/dashboard/list",
            type: "GET",
            dataType: "json",
            data: {
                date: date
            },
            success: function (myData) {
                if(myData==null){
                    $("#completed").val(defaultValue);
                }else {
                    $("#completed").text(myData.length);
                }
            }
        })
        $.ajax({
            url: "/admin/dashboard/listReturn",
            type: "GET",
            dataType: "json",
            data: {
                date: date
            },
            success: function (DataReturn) {
                $("#return").text(DataReturn.length);
            }
        });
        $.ajax({
            url: "/admin/dashboard/listProcessing",
            type: "GET",
            dataType: "json",
            data: {
                date: date
            },
            success: function (DataProcessing) {
                $("#processing").text(DataProcessing.length);
            }
        });
        $.ajax({
            url: "/admin/dashboard/productCount",
            type: "GET",
            dataType: "json",
            data: {
                date: date
            },
            success: function (DataProcessing) {
                $("#productCount").text(DataProcessing.length);
            }
        });
        $.ajax({
            url: "/admin/dashboard/productCount",
            type: "GET",
            dataType: "json",
            data: {
                date: date
            },
            success: function (DataProcessing) {
                $("#productCount").text(DataProcessing.length);
            }
        });
    });
});