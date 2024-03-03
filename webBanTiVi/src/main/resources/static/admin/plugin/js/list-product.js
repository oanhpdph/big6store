document.getElementById("prev").addEventListener("click", function () {
    if (document.getElementById("pageInput").value > 1) {
        document.getElementById("pageInput").value = Number(document.getElementById("pageInput").value) - 1
        $("#form-product").submit()

    }
})
document.getElementById("next").addEventListener("click", function () {
    if (document.getElementById("pageInput").value < Number(document.getElementById("total").getAttribute("value"))) {
        document.getElementById("pageInput").value = Number(document.getElementById("pageInput").value) + 1
        $("#form-product").submit()
    }
})
document.getElementById("last").addEventListener("click", function () {
    if (document.getElementById("pageInput").value < Number(document.getElementById("total").getAttribute("value"))) {
        document.getElementById("pageInput").value = Number(document.getElementById("total").getAttribute("value"))
        $("#form-product").submit()
    }
})
document.getElementById("first").addEventListener("click", function () {
    if (document.getElementById("pageInput").value > 1) {
        document.getElementById("pageInput").value = 1
        $("#form-product").submit()
    }
})
document.getElementById("select-size").addEventListener("change", function () {
    $("#form-product").submit()
})
if (document.getElementById("select-group")) {
    document.getElementById("select-group").addEventListener("change", function () {
        $("#form-product").submit()
    })
}
document.getElementById("sort").addEventListener("change", function () {
    $("#form-product").submit()
})
document.getElementById("pageInput").addEventListener("blur", function () {
    if (document.getElementById("pageInput").value != Number(document.getElementById("pageInput").getAttribute("temp"))) {
        if (document.getElementById("pageInput").value <= Number(document.getElementById("total").getAttribute("value")) && document.getElementById("pageInput").value > 0) {
            $("#form-product").submit()
        } else {
            document.getElementById("pageInput").value = Number(document.getElementById("pageInput").getAttribute("temp"))
        }
    }
})
document.getElementById("pageInput").addEventListener("keypress", function (event) {
    // If the user presses the "Enter" key on the keyboard
    if (event.key === "Enter") {
        // Cancel the default action, if needed
        if (document.getElementById("pageInput").value !== Number(document.getElementById("pageInput").getAttribute("temp"))) {
            if (document.getElementById("pageInput").value <= Number(document.getElementById("total").getAttribute("value")) && document.getElementById("pageInput").value > 0) {
                $("#form-product").submit()
            } else {
                document.getElementById("pageInput").value = Number(document.getElementById("pageInput").getAttribute("temp"))
            }
        }
        event.preventDefault();
    }
});

$("#search").on("click", function () {
    if ($(this).val().trim().length !== 0) {
        $("#form-product").submit()
    }
})


if (document.getElementById("edit-attri")) {
    document.getElementById("edit-attri").addEventListener("click", function () {
        $.each($(".span-value-attri"), function (index, item) {
            var input = $('<input>', {
                class: 'form-control attributes-update',
                value: item.textContent,
                id: '__' + item.getAttribute('value')
            });
            $(item).closest('td').html(input);
        })
    })
}

$.each(document.getElementsByClassName("update-active-product"), function (index, item) {
    item.addEventListener("click", function () {
        function change() {
            item.checked = false
        }

        var text;
        if (item.checked) {
            $("#cancelActive").unbind("click")
            document.getElementById("cancelActive").addEventListener("click", change)
            $("#closeActive").unbind("click")
            document.getElementById("closeActive").addEventListener("click", change)

            var data = {
                id: item.value
            }
            $.ajax({
                url: "/product/get-product-by-id",
                method: "get",
                data: data,
                // contentType: "application/json",
                success: function (data) {
                    console.log(data)
                    var table = document.querySelector("#table-active tbody")
                    $("#table-active tbody").empty()
                    $.each(data.productDetails, function (index, item2) {
                        console.log(item2)
                        var nameProduct = []
                        nameProduct.push(data.nameProduct)
                        nameProduct.push("[")
                        $.each(item2.fieldList, function (index, item3) {
                            nameProduct.push(item3.value)
                            nameProduct.push("-")
                        })

                        nameProduct.splice(nameProduct.length - 1, 1, "]")
                        var row = document.createElement("tr")

                        var cell1 = document.createElement("td")
                        var spanName = document.createElement("span")
                        spanName.innerText = nameProduct.join(" ")
                        spanName.className = "fw-semibold"
                        cell1.appendChild(spanName)

                        var cell2 = document.createElement("td")
                        var spanSku = document.createElement("span")
                        spanSku.innerText = item2.sku
                        cell2.appendChild(spanSku)

                        var cell3 = document.createElement("td")
                        var spanPrice = document.createElement("span")
                        spanPrice.innerText = item2.priceExport
                        cell3.appendChild(spanPrice)

                        var cell4 = document.createElement("td")
                        var spanQuantity = document.createElement("span")
                        spanQuantity.innerText = item2.quantity
                        cell4.appendChild(spanQuantity)


                        var cell5 = document.createElement("td")
                        var inputActive = document.createElement("input")
                        inputActive.className = "form-check-input larger-checkbox input-active"
                        inputActive.type = "checkbox"
                        inputActive.value = item2.id
                        inputActive.checked = true
                        cell5.appendChild(inputActive)

                        row.appendChild(cell1)
                        row.appendChild(cell2)
                        row.appendChild(cell3)
                        row.appendChild(cell4)
                        row.appendChild(cell5)
                        table.appendChild(row)
                    })
                }
            })
            $("#saveActive").on("click", function () {
                var checkbox = document.getElementsByClassName("input-active")
                var temp = false
                $.each(checkbox, function (index, item) {
                    if (item.checked) {
                        temp = true
                    }
                })
                if (temp) {
                    Swal.fire({
                        title: "Bạn xác nhận cập nhật?",
                        text: "",
                        icon: "question",
                        showCancelButton: true,
                        confirmButtonColor: "#3085d6",
                        cancelButtonColor: "#d33",
                        confirmButtonText: "Xác nhận!",
                        cancelButtonText: "Hủy"
                    }).then((result) => {
                        if (result.isConfirmed) {
                            var productDetailList = []
                            $.each(checkbox, function (index, item) {
                                if (item.checked) {
                                    var detail = {};
                                    detail.id = item.value
                                    detail.active = "true"
                                    productDetailList.push(detail)
                                }
                            })
                            $.ajax({
                                url: "/product/update-active-detail",
                                method: "post",
                                data: JSON.stringify(productDetailList),
                                contentType: "application/json",
                                success: function (data) {
                                }
                            })

                            var data = {
                                id: item.value,
                                active: item.checked
                            }

                            $.ajax({
                                url: "/product/update-product",
                                method: "post",
                                data: JSON.stringify(data),
                                contentType: "application/json",
                                success: function (data) {
                                    Swal.fire({
                                        title: "Thành công!",
                                        text: "Trạng thái sản phẩm đã được cập nhật",
                                        icon: "success",
                                    });
                                }
                            })
                            document.getElementById("closeActive").removeEventListener("click", change)
                            document.getElementById("closeActive").click()
                        } else {
                            item.checked = !item.checked
                        }
                    });
                } else {
                    Swal.fire({
                        title: "Lưu ý!",
                        text: "Cần chọn ít nhất 1 sản phẩm bày bán mới có thể bán sản phẩm này",
                        icon: "warning"
                    })
                }
            })
            $("#active").click()
            return
        } else {
            text = "Sản phẩm chi tiết thuộc sản phẩm này sẽ được cập nhật lại trạng thái!"
        }
        Swal.fire({
            title: "Bạn xác nhận cập nhật?",
            text: text,
            icon: "question",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Xác nhận!",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                var data = {
                    id: item.value,
                    active: item.checked
                }
                $.ajax({
                    url: "/product/update-product",
                    method: "post",
                    data: JSON.stringify(data),
                    contentType: "application/json",
                    success: function (data) {
                        console.log(data)
                        Swal.fire({
                            title: "Thành công!",
                            text: "Trạng thái sản phẩm đã được cập nhật",
                            icon: "success",
                        });
                    }
                })
            } else {
                item.checked = !item.checked
            }
        });
    })
})

$.each(document.getElementsByClassName("active-product-detail"), function (index, item) {
    item.addEventListener("click", function () {
            // if (item.checked) {
            Swal.fire({
                title: "Bạn xác nhận cập nhật?",
                text: "",
                icon: "question",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Xác nhận!",
                cancelButtonText: "Hủy"
            }).then((result) => {
                if (result.isConfirmed) {
                    var data = []
                    var product = {
                        id: item.value,
                        active: item.checked
                    }
                    data.push(product)
                    $.ajax({
                        url: "/product/update-active-detail",
                        method: "post",
                        data: JSON.stringify(data),
                        contentType: "application/json",
                        success: function (data) {
                            const Toast = Swal.mixin({
                                toast: true,
                                position: "top-end",
                                showConfirmButton: false,
                                timer: 3000,
                                timerProgressBar: true,
                                didOpen: (toast) => {
                                    toast.onmouseenter = Swal.stopTimer;
                                    toast.onmouseleave = Swal.resumeTimer;
                                }
                            });
                            Toast.fire({
                                icon: "success",
                                title: "Cập nhật thành công"
                            });
                        }
                    })
                } else {
                    item.checked = !item.checked
                }
            })
        }
    )
})

$.each(document.getElementsByClassName("edit-product"), function (index, item) {
    item.addEventListener("click", function () {
        getOneProduct(item).then(function (data) {
            var localData = data
            document.getElementById("save").value = data.id
            // $("#product-detail").val(data.id)
            var table = document.querySelector("#table-detail-modal tbody")
            $("#table-detail-modal tbody").empty()

            $("#name-product").text(data.nameProduct + " " + data.sku)
            $("#group-product").text("Nhóm sản phẩm: " + data.groupProduct.nameGroup)
            $.each(data.productFieldValues, function (index, item) {
                    if (item.field.active == true) {
                        var row = document.createElement("tr")
                        var cell1 = document.createElement("td")
                        var nameAttri = document.createElement("span")
                        nameAttri.innerText = item.field.name
                        cell1.appendChild(nameAttri)

                        var cell2 = document.createElement("td")
                        var value = document.createElement("span")
                        value.className = "span-value-attri"
                        // value.disabled = true
                        value.innerText = item.value
                        value.setAttribute('value', item.field.id)
                        cell2.appendChild(value)

                        row.appendChild(cell1)
                        row.appendChild(cell2)
                        table.appendChild(row)
                    }
                }
            )
            $("#save").on("click", function () {
                var data = {}
                data.product = []
                data.id = $("#save").val()
                data.active = localData.active
                var text = "";
                $.each($(".attributes-update"), function (index, item) {
                    if (item.value.trim().length == 0) {
                        text = "Thuộc tính không điền thông tin sẽ không thay đổi giá trị"
                    }
                    var attri = {
                        id: item.id.substring(2, item.id.length),
                        value: item.value,
                    }
                    data.product.push(attri)
                })
                Swal.fire({
                    title: "Bạn xác nhận cập nhật?",
                    text: text,
                    icon: "question",
                    showCancelButton: true,
                    confirmButtonColor: "#3085d6",
                    cancelButtonColor: "#d33",
                    confirmButtonText: "Xác nhận!",
                    cancelButtonText: "Hủy"
                }).then((result) => {
                    if (result.isConfirmed) {
                        $.ajax({
                            url: "/product/update-product",
                            method: "post",
                            data: JSON.stringify(data),
                            contentType: "application/json",
                            success: function (data) {
                                $("#close").click()
                                const Toast = Swal.mixin({
                                    toast: true,
                                    position: "top-end",
                                    showConfirmButton: false,
                                    timer: 3000,
                                    timerProgressBar: true,
                                    didOpen: (toast) => {
                                        toast.onmouseenter = Swal.stopTimer;
                                        toast.onmouseleave = Swal.resumeTimer;
                                    }
                                });
                                Toast.fire({
                                    icon: "success",
                                    title: "Cập nhật thành công"
                                });
                            }
                        })
                    }
                })

            })
        })
    })
})

$.each($(".view-product"), function (index, item) {
        item.addEventListener("click", function () {
            getOneProductDetail(item).then(function (data) {
                {
                    $("#save-product-detail").val(data.id)
                    $("#sku").val(data.sku)
                    $("#quantity").val(data.quantity)
                    $("#price-export").val(data.priceExport)
                    $("#price-import").val(data.priceImport)

                    var list = []
                    list.push(data.product.nameProduct)
                    list.push('[')
                    $.each(data.fieldList, function (index, item) {
                        list.push(item.value)
                    })
                    list.push(']')
                    $("#image").empty()
                    $.each(data.listImage, function (index, item) {
                        var img = $('<img>', {
                            class: 'w-25 image-preview cursor-pointer m-2',
                            src: '/image/product/' + item.link,
                            alt: 'Lỗi hình ảnh',
                        });
                        img.attr("data-bs-toggle", "tooltip");
                        img.attr("data-popup", "tooltip-custom");
                        img.attr("data-bs-placement", "bottom");
                        img.attr("data-bs-offset", "0,4");
                        img.attr("data-bs-html", "true");
                        if (item.location) {
                            img.attr("title", "Ảnh chính");
                            img.addClass('border border-1 p-2')
                        } else {
                            img.attr("title", "Ảnh phụ");
                        }
                        var input = $('<input>', {
                            class: 'file-input',
                            type: 'file',
                            hidden: true,
                            id: 'image' + item.id
                        });

                        $("#image").append(img)
                        $("#image").append(input)

                        var fileInput = input;
                        var imagePreview = img;

                        imagePreview.on("click", function () {
                            fileInput.click()
                        })
                        // Lắng nghe sự kiện khi người dùng chọn tệp
                        fileInput.on('change', function () {
                            var file = fileInput.prop("files")[0]; // Lấy tệp đã chọn
                            if (file) {
                                // Kiểm tra xem tệp đã chọn có phải là hình ảnh
                                if (file.type.startsWith('image/')) {
                                    // Tạo đường dẫn URL cho hình ảnh và hiển thị nó
                                    var imageURL = URL.createObjectURL(file);
                                    console.log(imageURL)
                                    imagePreview.attr('src', imageURL);
                                    console.log(imagePreview)
                                } else {
                                    alert('Vui lòng chọn một tệp hình ảnh.');
                                    fileInput.value = ''; // Đặt lại trường nhập
                                }
                            } else {
                                imagePreview.src = '/image/product/' + item.link
                            }
                        });
                    })

                    $("#name-product").text(list.join(' '))
                    var date = new Date(data.createDate);

                    var formattedDate = date.toISOString().slice(0, 16);
                    $("#date-create").text('Ngày tạo: ' + formattedDate)
                    $("#active").attr('checked', false)
                    $("#inactive").attr('checked', false)
                    if (data.active) {
                        $("#active").attr('checked', true)
                    } else {
                        $("#inactive").attr('checked', true)
                    }
                }
            })
        })
    }
)
$("#save-product-detail").on('click', function () {
    var $this = this
    validateUpdateProductDetail().then(function (data) {
        if (data == false) {
            const Toast = Swal.mixin({
                toast: true,
                position: "top-end",
                showConfirmButton: false,
                timer: 3000,
                timerProgressBar: true,
                didOpen: (toast) => {
                    toast.onmouseenter = Swal.stopTimer;
                    toast.onmouseleave = Swal.resumeTimer;
                }
            });
            Toast.fire({
                icon: "error",
                title: "Xảy ra lỗi, xem lại thông tin sản phẩm"
            });
        } else {
            Swal.fire({
                title: "Bạn xác nhận cập nhật?",
                text: "",
                icon: "question",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Xác nhận!",
                cancelButtonText: "Hủy"
            }).then((result) => {
                if (result.isConfirmed) {
                    var data = {}
                    data.id = $this.value
                    data.sku = $("#sku").val()
                    data.priceImport = $("#price-import").val()
                    data.priceExport = $("#price-export").val()
                    console.log(document.getElementById("active").checked)
                    if (document.getElementById("active").checked) {
                        data.active = true
                    } else {
                        data.active = false
                    }
                    data.quantity = $("#quantity").val()
                    data.image = []

                    $.each($(".file-input"), function (index, item) {
                        var imageItem;
                        var fileName = item.value; // Lấy đường dẫn đầy đủ của tệp
// Trích xuất tên tệp từ đường dẫn
                        var lastIndex = fileName.lastIndexOf("\\"); // Sử dụng "\\" để tách tên tệp trên Windows
                        if (lastIndex >= 0) {
                            fileName = fileName.substr(lastIndex + 1);
                        }
                        imageItem = {
                            id: item.id.substring(5, item.id.length),
                            multipartFile: fileName,
                        }
                        data.image.push(imageItem)
                    })
                    uploadImage()

                    $.ajax({
                        url: '/product/update-productdetail',
                        method: 'post',
                        data: JSON.stringify(data),
                        contentType: 'application/json',
                        success: function (data) {
                            $("#close").click()
                            const Toast = Swal.mixin({
                                toast: true,
                                position: "top-end",
                                showConfirmButton: false,
                                timer: 3000,
                                timerProgressBar: true,
                                didOpen: (toast) => {
                                    toast.onmouseenter = Swal.stopTimer;
                                    toast.onmouseleave = Swal.resumeTimer;
                                }
                            });
                            Toast.fire({
                                icon: "success",
                                title: "Cập nhật thành công",
                                text: "Tải lại trang để làm mới thông tin"
                            });
                        }
                    })
                }
            })
        }
    })

})

function uploadImage() {
    var fileInput = document.getElementsByClassName('file-input');

    var formData = new FormData();
    $.each(fileInput, function (index, item) {
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
            // Xử lý kết quả từ máy chủ (nếu cần)
            console.log("thành công")
        },
        error: function (error) {
            console.error('Lỗi:', error);
        }
    });

}

function getOneProduct(item) {
    var data = {
        id: item.getAttribute('value')
    }
    return new Promise(function (resolve, reject) {
        $.ajax({
            url: "/product/get-product-by-id",
            method: "get",
            data: data,
            success: function (data) {
                resolve(data)
            }, error: function (error) {
                reject(error)
            }
        })
    })
}

var productDetail;

function getOneProductDetail(item) {
    var data = {
        id: item.getAttribute('value'),
    }
    return new Promise(function (resolve, reject) {
        $.ajax({
            url: "/product/get-one-product",
            method: "get",
            data: data,
            success: function (data) {
                productDetail = data;
                resolve(data)
            },
            error: function (error) {
                reject(error)
            }
        })
    })
}

$(".product-detail").on("click", function () {
    getOneProduct(this).then(function (data) {
        $("#name__product__add").text(data.nameProduct + " " + data.sku)
        $("#add-product-detail").val(data.id)
        var fieldList = data.productDetails[0].fieldList
        $(".attributes").remove()
        $.each(fieldList, function (index, item) {
            if (fieldList.length - 1 !== index) {
                // Sao chép thẻ cha và tất cả các thẻ con
                var clonedElement = $("#attribute").clone();

                // Xóa thuộc tính id để tránh trùng lặp
                clonedElement.removeAttr("id");
                $(clonedElement).addClass("attributes")
                // Thêm thẻ sao chép vào đích
                $("#group-attribute").append(clonedElement);
            }
        })

        $.each($(".name-attributes"), function (index, item) {
            $(item).text(fieldList[index].field.name)
            $(".value-attribute").eq(index).attr('temp', fieldList[index].field.id)
        })
    })
})

$("#add-product-detail").on('click', function () {
    var $this = this
    validateAddProductDetail().then(function (data) {
        if (data == false) {
            return
        }
        Swal.fire({
            title: "Đồng ý tạo sản phẩm mới?",
            text: "",
            icon: "question",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Xác nhận!",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                var data = {}
                data.id = $this.value
                data.listProduct = []
                var listAttributes = []
                var image = []
                $.each($(".value-attribute"), function (index, item) {
                    var attri = {
                        id: $(item).attr('temp'),
                        value: $(item).val()
                    }
                    listAttributes.push(attri)
                })


                $.each($(".file-input"), function (index, item) {
                    var imageItem;
                    var fileName = item.value; // Lấy đường dẫn đầy đủ của tệp
// Trích xuất tên tệp từ đường dẫn
                    var lastIndex = fileName.lastIndexOf("\\"); // Sử dụng "\\" để tách tên tệp trên Windows
                    if (lastIndex >= 0) {
                        fileName = fileName.substr(lastIndex + 1);
                    }
                    imageItem = {
                        id: item.id.substring(5, item.id.length),
                        multipartFile: fileName,
                        location: $(this).hasClass('true') ? 'true' : 'false'
                    }
                    image.push(imageItem)
                })

                var temp = {
                    sku: $("#sku").val(),
                    priceImport: $("#price-import").val(),
                    priceExport: $("#price-export").val(),
                    quantity: $("#quantity").val(),
                    listAttributes: listAttributes,
                    image: image
                }
                data.listProduct.push(temp)
                uploadImage()
                $.ajax({
                    url: '/product/save-product',
                    method: 'post',
                    data: JSON.stringify(data),
                    contentType: 'application/json',
                    success: function (data) {
                        if (data) {
                            $("#closeAdd").click()
                            const Toast = Swal.mixin({
                                toast: true,
                                position: "top-end",
                                showConfirmButton: false,
                                timer: 3000,
                                timerProgressBar: true,
                                didOpen: (toast) => {
                                    toast.onmouseenter = Swal.stopTimer;
                                    toast.onmouseleave = Swal.resumeTimer;
                                }
                            });
                            Toast.fire({
                                icon: "success",
                                title: "Tạo thành công, tải lại trang để làm mới"
                            });
                        } else {
                            const Toast = Swal.mixin({
                                toast: true,
                                position: "top-end",
                                showConfirmButton: false,
                                timer: 3000,
                                timerProgressBar: true,
                                didOpen: (toast) => {
                                    toast.onmouseenter = Swal.stopTimer;
                                    toast.onmouseleave = Swal.resumeTimer;
                                }
                            });
                            Toast.fire({
                                icon: "error",
                                title: "Thất bại, xem lại thông tin đã nhập"
                            });
                        }
                    }
                })
            }
        })
    })
})

function validateAddProductDetail() {
    return new Promise(function (resolve, reject) {
        $.ajax({
            url: '/product/product-detail/all',
            method: "get",
            success: function (data) {
                console.log(data)
                var check;
                if ($("#sku").val().trim().length == 0) {
                    $("#error-sku").text("Chưa nhập mã sku")
                    check = false
                } else {
                    $.each(data, function (index, item) {
                        console.log(item)
                        if ($("#sku").val().trim() == item) {
                            $("#error-sku").text("Mã sku đã tồn tại")
                            check = false
                            return false
                        } else {
                            $("#error-sku").text("")
                        }
                    })
                }
                if ($("#price-export").val().trim().length == 0 || $("#price-export").val() < 0) {
                    $("#error-priceExport").text("Giá bán phải >=0")
                    check = false
                } else {
                    $("#price-export").text("")
                }
                if ($("#price-import").val().trim().length == 0 || $("#price-import").val() < 0) {
                    $("#error-priceImport").text("Giá nhập phải >=0")
                    check = false
                } else {
                    $("#price-import").text("")
                }
                if ($("#quantity").val().trim().length == 0 || $("#quantity").val() < 0) {
                    $("#error-quantity").text("Số lượng sản phẩm phải >=0")
                    check = false
                } else {
                    $("#quantity").text("")
                }
                $.each($(".value-attribute"), function (index, item) {
                    if ($(item).val().trim().length == 0) {
                        $(".error-attribute").eq(index).text("Cần nhập giá trị cho thuộc tính")
                        check = false
                    } else {
                        $(".error-attribute").eq(index).text("")
                    }
                })
                $.each($(".file-input"), function (index, item) {
                    if ($(item).val().trim().length == 0) {
                        $("#error-image").text("Cần thêm đủ 5 ảnh cho sản phẩm")
                        check = false
                    } else {
                        $("#error-image").text("")
                    }
                })
                resolve(check)
            },
            error: function (error) {
                reject(error)
            }
        })
    })
}

function validateUpdateProductDetail() {
    return new Promise(function (resolve, reject) {
        $.ajax({
            url: '/product/product-detail/all',
            method: "get",
            success: function (data) {
                var check;
                if ($("#sku").val().trim().length == 0) {
                    $("#error-sku").text("Chưa nhập mã sku")
                    check = false
                } else {
                    if ($("#sku").val().trim() != productDetail.sku) {
                        $.each(data, function (index, item) {
                            if ($("#sku").val().trim() == item) {
                                $("#error-sku").text("Mã sku đã tồn tại")
                                check = false
                                return false
                            } else {
                                $("#error-sku").text("")
                            }
                        })
                    }
                }
                if ($("#price-export").val().trim().length === 0 || $("#price-export").val() < 0) {
                    $("#error-priceExport").text("Giá bán >=0")
                    check = false
                } else {
                    $("#error-priceExport").text("")
                }
                if ($("#price-import").val().trim().length === 0 || $("#price-import").val() < 0) {
                    $("#error-priceImport").text("Giá nhập >=0")
                    check = false
                } else {
                    $("#error-priceImport").text("")
                }
                if ($("#quantity").val().trim().length == 0 || $("#quantity").val() < 0) {
                    $("#error-quantity").text("Số lượng sản phẩm >=0")
                    check = false
                } else {
                    $("#error-quantity").text("")
                }
                resolve(check)
            }
        })
    })
}

$.each($(".view-discount"), function (index, item) {
    $(item).on("click", function () {
        getOneProductDetail(item).then(function (data) {
            var list = []
            list.push(data.product.nameProduct)
            list.push('[')
            $.each(data.fieldList, function (index, item) {
                list.push(item.value)
            })
            list.push(']')
            $("#title-discount").text("Sản phẩm: " + list.join(' '))
            if (data.coupon != null) {
                $("#code").text(data.coupon.code)
                $("#value").text(data.coupon.value)
                $("#date-start").text((data.coupon.dateStart))
                $("#date-end").text((data.coupon.dateEnd))
                $("#active-discount").text(data.coupon.active == true ? 'Đang hoạt động' : 'Không hoạt động')
                $("#have-discount").attr('hidden', false)
                $("#no-discount").attr('hidden', true)

            } else {
                $("#have-discount").attr('hidden', true)
                $("#no-discount").attr('hidden', false)
            }
        })
    })
})

