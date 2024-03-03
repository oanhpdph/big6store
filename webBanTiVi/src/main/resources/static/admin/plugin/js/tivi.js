$(document).ready(
    loadProduct()
)
let timeout;
let filter;

function loadProduct() {
    var data = {
        point: $("#filterRate").val(),
        sort: $("#sort").val(),
        key: $("#key").val(),
        size: $("#loadMore").val(),
    }
    data.listBrand = []

    $.each($(".filter-brand"), function (index, item) {
        if (item.checked) {
            data.listBrand.push(item.value)
        }
    })

    $.ajax({
        url: "/tivi/get",
        method: "get",
        data: data,
        success: function (data) {
            $("#list-product-tivi").empty()
            // data.reverse()
            var filterPrice = [];

            $.each($(".filter-price"), function (index, item) {
                if ($(item).prop("checked")) {
                    filterPrice.push($(item).val())
                }
            })
            if ($("#price-begin").val() || $("#price-end").val()) {
                filterPrice.push(5)
            }
            $.each(data, function (index, item) {
                var check = true;
                $.each(filterPrice, function (index, item1) {
                    if (item1 == 1) {
                        if (item.reduceMoney > 5000000) {
                            check = false
                            return false
                        }
                    } else if (item1 == 2) {
                        console.log(item.reduceMoney > 20000000)
                        if (item.reduceMoney < 5000000 || item.reduceMoney > 20000000) {

                            check = false
                            return false
                        }
                    } else if (item1 == 3) {
                        if (item.reduceMoney < 20000000 || item.reduceMoney > 50000000) {
                            check = false
                            return false
                        }
                    } else if (item1 == 4) {
                        if (item.reduceMoney < 50000000) {
                            check = false
                            return false
                        }
                    } else if (item1 == 5) {
                        if ($("#price-begin").val().trim() != 0 && $("#price-end").val().trim() == 0) {

                            if (item.reduceMoney < Number($("#price-begin").val())) {
                                check = false
                                return false
                            }
                        }
                        if ($("#price-begin").val().trim() == 0 && $("#price-end").val().trim() != 0) {


                            if (item.reduceMoney > Number($("#price-end").val())) {
                                check = false
                                return false
                            }
                        }
                        if ($("#price-begin").val().trim() != 0 && $("#price-end").val().trim() != 0) {
                            console.log("3")

                            if (item.reduceMoney < Number($("#price-begin").val()) || item.reduceMoney > Number($("#price-end").val())) {
                                check = false
                                return false
                            }
                        }
                    }
                })

                if (check == false) {
                    return true
                }

                var colDiv = $("<div>").addClass("col");

// Tạo đối tượng div với class "card h-100 item-product"
                var cardDiv = $("<div>").addClass("card h-100 item-product");

// Tạo đối tượng div với class "card-body mb-4 mt-3"
                var cardBodyDiv = $("<div>").addClass("card-body mb-4 mt-3");

// Tạo đối tượng img
                var img = $("<img>").addClass("img-fluid d-flex mx-auto my-4")
                    .attr("src", "/image/product/" + item.image)
                    .attr("alt", "Hình ảnh bị lỗi");

// Tạo đối tượng a với href
                var aTag = $("<a>").attr("href", "/product/detail/" + item.id)
                    .addClass("stretched-link mb-2 text-black");

// Tạo đối tượng span với class "fs-5 card-title" và text
                var spanTitle = $("<span>").addClass("fs-5 card-title mb-3")
                    .text(item.nameProduct);

// Tạo các đối tượng div và span cho giá và đánh giá
                if (item.price != 0) {
                    var strikeThroughDiv = $("<div>").addClass("mb-3").append($("<span>").addClass("text-strike-through fs-6")
                        .html(new Intl.NumberFormat().format(item.price) + "<small class='align-text-top'>đ</small>"));
                }
                var boldTextDiv = $("<div>").addClass("mb-3").append($("<span>").addClass("text-danger fs-6 fw-bold")
                    .html(new Intl.NumberFormat().format(item.reduceMoney) + "<small class='align-text-top'>đ</small>"));

                var starDiv = $("<div>").append($("<span>").html(item.point + "<i class='bx bxs-star' style='color:#fee500'></i>" + " (" + item.quantityEvalute + ")"));

// Gắn các đối tượng con vào các đối tượng cha
                aTag.append(spanTitle);
                cardBodyDiv.append(img, aTag, strikeThroughDiv, boldTextDiv, starDiv);
                cardDiv.append(cardBodyDiv);
                colDiv.append(cardDiv);
                $("#list-product-tivi").append(colDiv)
            })
        }
    })
}

$("#filterRate").on("change", function () {
    loadProduct()
})
$(".filter-brand").on("change", function () {
    loadProduct()
})
$("#key").on("keydown", function () {
    clearTimeout(timeout);
    timeout = setTimeout(function () {
        // Thực hiện xử lý sau khi đã chờ một khoảng thời gian
        loadProduct()
    }, 500);

})
$("#loadMore").on("click", function () {
    this.value = Number(this.value) + 10
    loadProduct()
})

$(".filter-price").on("change", function () {
    loadProduct()
})
$("#btn-reset").on("click", function () {
    $("#key").val("");
    $("#filterRate").val(-1)
    $.each($(".filter-brand"), function (index, item) {
        $(item).prop('checked', false)
    })
    $("#price-begin").val("");
    $("#price-end").val("");
    $.each($(".filter-price"), function (index, item) {
        $(item).prop("checked", false)
    })
    loadProduct()
})

$("#price-begin").on("keydown", function () {
    clearTimeout(timeout);

    timeout = setTimeout(function () {
        // Thực hiện xử lý sau khi đã chờ một khoảng thời gian
        loadProduct()
    }, 500);

})
$("#price-end").on("keydown", function () {
    clearTimeout(timeout);
    timeout = setTimeout(function () {
        // Thực hiện xử lý sau khi đã chờ một khoảng thời gian
        loadProduct()
    }, 500);

})

