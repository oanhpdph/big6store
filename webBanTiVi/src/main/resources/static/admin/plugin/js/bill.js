// function oncl(id) {
//     window.location.href = "http://localhost:8080/admin/bill/bill_detail/" + id
// }

$.each(document.getElementsByClassName("status"), function (index, item) {
    console.log(item)
    item.addEventListener("click", function () {
        document.getElementById("status").value = item.value
        setAttributes()
        $("#search").click()
    })
})

function setAttributes() {
    // let size = document.getElementById("select-view");
    let key = document.getElementById("search-input");
    let date = document.getElementById("date-seach-order");
    let billStatus = document.getElementById("status");
    let paymentStatus = document.getElementById("paymentStatus");
    if (key) {
        if (key.value === null || key.value.trim() == "") {
            key.name = ""
        }
    }
    if (date) {
        if (date.value === null || date.value.trim() == "") {
            date.name = ""
        }
    }

    if (billStatus.value === null || billStatus.value.trim() == "") {
        billStatus.name = ""
    }

    if (paymentStatus.value == -1) {
        paymentStatus.name = ""
    }
}

$("#search-input").keydown(function (event) {
    if (event.key == "Enter" || event.key == 13) {
        $("#search").click()
    }
})
$("#search").click(function () {
    setAttributes()
    $("#formBill").submit()
})

$("#select-size").change(function () {
    document.getElementById("search").click();
})

$("#pageInput").blur(function () {
    let total = $("#totalPage").val()
    if ($("#pageInput").val() > total) {
        $("#pageInput").val(total)
    }
    if ($("#pageInput").val() < 0) {
        $("#search-input").val(1)
    }
    document.getElementById("search").click();
})

$("#pageInput").keydown(function (event) {
    if (event.key === "Enter" || event.key === 13) {
        let total = $("#totalPage").val()
        if ($("#pageInput").val() > total) {
            $("#pageInput").val(total)
        }
        if ($("#pageInput").val() < 0) {
            $("#pageInput").val(1)
        }
        document.getElementById("search").click();
    }
})

$("#reset").click(function () {
    $("#search-input").val("")
    $("#date-seach-order").val("")
})

$("#filterBillStatus").change(function () {
    document.getElementById("search").click();
})

$("#first").click(function () {
    let temp = document.getElementById("first")
    $("#pageInput").val(temp.getAttribute("value"))
    document.getElementById("search").click();
})
$("#prev").click(function () {
    let temp = document.getElementById("prev")
    $("#pageInput").val(temp.getAttribute("value"))
    document.getElementById("search").click();
})
$("#next").click(function () {
    let temp = document.getElementById("next")
    $("#pageInput").val(temp.getAttribute("value"))
    document.getElementById("search").click();
})
$("#last").click(function () {
    let temp = document.getElementById("last")
    $("#pageInput").val(temp.getAttribute("value"))
    document.getElementById("search").click();
})
// $("#filterBillStatus").change(function () {
//     $("#search").click()
// })

$("#paymentStatus").change(function () {
    $("#search").click()
})