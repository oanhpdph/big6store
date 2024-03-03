const deliverInput = document.getElementById("deliver");
const phoneDeliverInput = document.getElementById("deliveryPhone");
const addressDeliverInput = document.getElementById("receivingAddress");
const deliveryFeeInput = document.getElementById("deliveryFee");
const noteInput = document.getElementById("note");
const codeStatus = document.getElementById("codeStatus")


const listInput = [addressDeliverInput, deliveryFeeInput, noteInput]

// function changeBtn() {
//     //nút chỉnh sửa đang ẩn
//     console.log("oanh")
//     for (let i = 0; i < listInput.length; i++) {
//         if (listInput[i].disabled === true) {
//             listInput[i].disabled = false
//         } else if (listInput[i].disabled === false) {
//             listInput[i].disabled = true
//         }
//     }
//
// }

// $("#btnSave").click(function () {
//         var $form = $("#formUpdateDeliveryNotes");
//         var $inputs = $form.find("input")
//         var serializedData = $form.serialize();
//         var id = $("#btnSave").val()
//
//         $.post("/admin/bill/bill_detail/update_delivery_notes/" + id, serializedData, function (data, status) {
//             if (status == "success") {
//                 alert("Cập nhật thành công")
//                 changeBtn()
//             }
//         })
//     }
// )

// $("#btnEdit").click(function () {
//     changeBtn()
// })

function update() {
    var $form = $("#formUpdateStatus");
    var serializedData = $form.serialize();
    $.post('/admin/bill/bill_detail/update_status/' + $("#idBill").val(),
        serializedData, function (data, status) {
            if (status == "success") {
                alert("Cập nhật thành công")
                location.reload()
            }
        })
}

$("#billStatus").change(function () {
    if (confirm("Xác nhận cập nhật trạng thái hóa đơn ?")) {
        update()
    } else {
        var selectElement = document.getElementById("billStatus");
        for (var i = 0; i < selectElement.options.length; i++) {
            if (selectElement.options[i].value === $("#codeStatus").val()) {
                selectElement.selectedIndex = i;
                break;
            }
        }
    }
})

$("#paymentStatus").change(function () {
    if (confirm("Xác nhận cập nhật trạng thái thanh toán?")) {
        update()
    } else {
        let selectElement = document.getElementById("paymentStatus");
        for (let i = 0; i < selectElement.options.length; i++) {
            if (selectElement.options[i].value === $("#payment").val()) {
                selectElement.selectedIndex = i;
                break;
            }
        }
    }
})