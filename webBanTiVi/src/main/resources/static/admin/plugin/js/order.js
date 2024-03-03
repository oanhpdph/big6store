var idBill
$(document).ready(checkAll())
$.each(document.getElementsByClassName("return"), function (index, item) {
    item.addEventListener('change', function () {
        idBill = item.value;
        var nameProduct = document.getElementsByClassName("nameProduct" + idBill)
        var quantityReturn = document.getElementsByClassName("quantityReturn" + idBill)
        var image = document.getElementsByClassName("returnImg" + item.value)
        var reason = document.getElementsByClassName("reason" + idBill)
        var completed=document.getElementById("complete");
        if (!item.checked) {
            nameProduct[0].disabled = true;
            quantityReturn[0].disabled = true;
            reason[0].disabled = true;
            $.each(image, function (index, item) {
                item.disabled = true;
            })
            completed.disabled = true;
        } else {
            nameProduct[0].disabled = false;
            quantityReturn[0].disabled = false;
            reason[0].disabled = false;
            $.each(image, function (index, item) {
                item.disabled = false;
            })
            completed.disabled = false;
        }
        checkAll()
    })
})
function total(){
 var total = 0;
 $.each(document.getElementsByClassName("return"), function (index, item) {
    idBill = item.value;
     $.each(document.getElementsByClassName("checkbox" + idBill), function (index, item) {
    if (item.checked){
    var quantity = document.getElementsByClassName("quantityReturn" + idBill);
    var price =document.getElementsByClassName("price" + idBill);
    var numberWithoutCommas = Number(price[index].value.replace(/,/g, ''));
    if (quantity[index].value > Number(quantity[index].getAttribute("max"))){
      quantity[index].value = Number(quantity[index].getAttribute("max"));
    }
    total =total + Number(quantity[index].value)*numberWithoutCommas;
    }
    })
})
 sessionStorage.setItem("totalValue", total);
 var totalWithCurrency = total +"đ"
document.getElementById("total").innerHTML=totalWithCurrency ;

}

function checkAll() {
    document.getElementById("checkboxAll").addEventListener("click", function (event) {
        $.each(document.getElementsByClassName("return"), function (index, item) {
            idBill = item.value;
            var nameProduct = document.getElementsByClassName("nameProduct" + idBill)
            var quantityReturn = document.getElementsByClassName("quantityReturn" + idBill)
            var image = document.getElementsByClassName("returnImg" + idBill)
            var reason = document.getElementsByClassName("reason" + idBill)
            var completed = document.getElementById("complete")
            if (document.getElementById("checkboxAll").checked) {
                console.log(item);
                item.checked = true;
                $.each(quantityReturn, function (index, item) {
                    item.disabled = false;
                })
                $.each(reason, function (index, item) {
                    item.disabled = false;
                })
                $.each(image, function (index, item) {
                    item.disabled = false;
                })
                completed.disabled = false;
            } else {
                item.checked = false;
                $.each(quantityReturn, function (index, item) {
                    item.disabled = true;
                })
                $.each(reason, function (index, item) {
                    item.disabled = true;
                })
                $.each(image, function (index, item) {
                    item.disabled = true;
                })
                completed.disabled = true;
            }
        })
    })
}

function clickSave() {
    var data = [];
    var check = true;
    $.each(document.getElementsByClassName("return"), function (index, item) {
        idBill = item.value;
        $.each(document.getElementsByClassName("checkbox" + idBill), function (index, item) {
            if (item.checked) {
                var idBillProduct = document.getElementsByClassName("checkbox" + idBill)
                var nameProduct = document.getElementsByClassName("nameProduct" + idBill)
                var quantityReturn = document.getElementsByClassName("quantityReturn" + idBill)
                var reason = document.getElementsByClassName("reason" + idBill)
                var stk = document.getElementById("stk").value;
                var nh = document.getElementById("nh").value;
                var csh = document.getElementById("csh").value;
                $.each(idBillProduct, function (index, item) {
                    var errorQuantity = document.querySelector(".errorQuantity" + item.value);
                    var errorReason = document.querySelector(".errorReason" + item.value);
                    if (item.checked) {
                        if (quantityReturn[index].value > Number(quantityReturn[index].getAttribute("max"))) {
                            errorQuantity.innerHTML = "Số lượng trả không lớn hơn số lượng mua!"
                            check = false;
                        } else if (quantityReturn[index].value.length == 0) {
                            errorQuantity.innerHTML = "Vui lòng điền số lượng!"
                            check = false;
                        }else if (quantityReturn[index].value <= 0){
                         errorQuantity.innerHTML = "số lượng trả phải lớn hơn 0!"
                            check = false;
                        }else {
                            errorQuantity.innerHTML = ""
                        }
                        if (reason[index].value.length == 0) {
                            errorReason.innerHTML = "Vui lòng nhập lí do!"
                            check = false;
                        } else {
                            errorReason.innerHTML = ""
                        }
                    }
                })

                 if(stk.length== 0){
                              document.querySelector(".errorSTK").innerHTML = "Trường bắt buộc!"
                              check = false

                 }
                 if(nh.length== 0){
                               document.querySelector(".errorNH").innerHTML = "Trường bắt buộc!"
                               check = false
                 }
                 if(csh.length== 0){
                               document.querySelector(".errorCSH").innerHTML = "Trường bắt buộc!"
                               check = false
                 }
                $.each(idBillProduct, function (index, item) {
                    if (item.checked) {
                        var image = "returnImg" + item.value
                        var arrImage = []
                        var listImage = document.getElementsByClassName(image)
                        var errorImage = document.querySelector(".errorImage" + item.value)
                        $.each(listImage, function (index, itm) {
                            var imageItem;
                            var fileName = itm.value; // Lấy đường dẫn đầy đủ của tệp
                            var imageName;
                            // Trích xuất tên tệp từ đường dẫn
                            var lastIndex = fileName.lastIndexOf("\\"); // Sử dụng "\\" để tách tên tệp trên Windows
                            if (lastIndex >= 0) {
                                imageName = fileName.substr(lastIndex + 1);
                            }

                            if (fileName.length != 0) {
                                imageItem = {
                                    nameImage: imageName,
                                }
                                arrImage.push(imageItem)
                            }


                        })
                        if (item.checked) {
                            if (arrImage.length == 0) {
                                errorImage.innerHTML = "Vui lòng tải lên ít nhất 1 ảnh!"
                                check = false;
                            } else {
                                errorImage.innerHTML = ""
                            }
                        }

                        var temp = {
                            idBillProduct: item.value,
                            nameProduct: nameProduct[index].value,
                            quantityReturn: quantityReturn[index].value,
                            reason: reason[index].value,
                            image: arrImage,
                            accountNumber: stk,
                            bank:nh,
                            owner:csh,
                        }
                        data.push(temp)
                    }
                })
            }
        })
    })
    if (check == false) {

        return
    }
    var billIdRequest = document.getElementById("billId");
    uploadImage();
    var xacNhan = confirm("Bạn có chắc chắn muốn trả hàng không?");
    if (xacNhan) {
        $.ajax({
            url: "/return/" + billIdRequest.value,
            method: "post",
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (data) {
                window.location.href= "http://localhost:8080"+data;
            }
        })
    } else {
        return;
    }
}

function uploadImage() {
    var fileInput = document.getElementsByClassName('file-input');
    var formData = new FormData();
    $.each(fileInput, function (index, item) {
        console.log(item.value)
        if (item.value.trim().length != 0) {
            formData.append('images', item.files[0], item.files[0].name);
        }
    })
    console.log(formData)
    $.ajax({
        url: '/product/upload',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            console.log("thành công")
        },
        error: function (error) {
            console.error('Lỗi:', error);
        }
    });
}
