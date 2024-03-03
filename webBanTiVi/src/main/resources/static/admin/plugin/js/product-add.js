var dataProductDetail = {}
$(document).ready(function () {
    loadDataField()
    loadDataGroup()
    loadVariant()
    loadDataBrand()
    // getProduct()
})
$("#select-type").change(function () {
    changeTableProductDetail()
})


function loadDataField() {
    $.ajax({
        url: "/field/all-active",
        method: "get",
        success: function (data) {
            // var dataTable = $('#tableAttributes tbody');
            $("#tableAttributes tbody").empty()
            $.each(data, function (index, item) {
                if (item.variant == false) {
                    var button = document.createElement('button');
                    button.innerHTML = 'Thêm'; // Đặt văn bản cho nút
                    button.className = "btn btn-outline-secondary addAttributes mx-2"
                    button.value = item.name
                    button.id = "_" + item.id

                    var label = document.createElement('label')
                    button.appendChild(label)
                    // Tạo một ô trong bảng
                    var cell1 = document.createElement('td');
                    var cell2 = document.createElement('td');
                    var cell3 = document.createElement('td');

                    var tbody = document.getElementById('tableAttributes').getElementsByTagName('tbody')[0];

                    cell1.innerText = document.getElementById('tableAttributes').rows.length
                    cell2.innerText = item.name
                    cell3.appendChild(button); // Thêm nút vào ô
                    // Lấy bảng theo ID
                    var indexRow = tbody.querySelectorAll("tr")

                    // Thêm ô (cell) vào dòng (row) trong bảng
                    var row = tbody.insertRow($("tableAttributes").rows); // Thay đổi chỉ số hàng theo ý muốn
                    row.appendChild(cell1);
                    row.appendChild(cell2);
                    row.appendChild(cell3);
                }
            });
        },
    })

}

function loadVariant() {
    $.ajax({
        url: "/field/all-active",
        method: "get",
        success: function (data) {
            // var dataTable = $('#tableAttributes tbody');
            $("#table-variant tbody").empty()
            $.each(data, function (index, item) {
                if (item.variant == true) {
                    var button = document.createElement('button');
                    button.innerHTML = 'Thêm'; // Đặt văn bản cho nút
                    button.className = "btn btn-outline-secondary add-variant mx-2"
                    button.value = item.name
                    button.id = "_" + item.id

                    var label = document.createElement('label')
                    button.appendChild(label)
                    // Tạo một ô trong bảng
                    var cell1 = document.createElement('td');
                    var cell2 = document.createElement('td');
                    var cell3 = document.createElement('td');

                    var tbody = document.getElementById('table-variant').getElementsByTagName('tbody')[0];


                    cell1.innerText = document.getElementById('table-variant').rows.length
                    cell2.innerText = item.name
                    cell3.appendChild(button); // Thêm nút vào ô
                    // Lấy bảng theo ID

                    // Thêm ô (cell) vào dòng (row) trong bảng
                    var row = tbody.insertRow($("table-variant").rows); // Thay đổi chỉ số hàng theo ý muốn
                    row.appendChild(cell1);
                    row.appendChild(cell2);
                    row.appendChild(cell3);
                }
            });
        },
    })
}

function loadDataGroup() {
    $.ajax({
        url: "/group/all",
        method: "get",
        success: function (data) {
            var select = $('#select-group');
            select.empty();
            $.each(data, function (index, item) {
                select.append($('<option>', {
                    value: item.id,
                    text: item.nameGroup
                }));
            });
        },
    })
}

function loadDataBrand() {
    $.ajax({
        url: "/api/brand/all",
        method: "get",
        success: function (data) {
            var select = $('#select-brand');
            select.empty();
            select.append($('<option>', {
                value: -1,
                text: 'Chưa chọn'
            }));
            $.each(data, function (index, item) {

                if (item.active == true) {
                    select.append($('<option>', {
                        value: item.id,
                        text: item.name
                    }));
                }
            });
        },
    })
}

function clickImage() {
    if (this) {
        // Lấy tham chiếu đến phần tử cha của input (ở đây, phần tử cha là thẻ "li")
        var parentElement = this.closest('li');
        if (parentElement) {
            // Tìm thẻ img bên trong phần tử cha
            var inputFile = parentElement.querySelector('input[type="file"]');
            if (inputFile) {
                // Bạn đã tìm thấy thẻ img, bạn có thể làm việc với nó ở đây.
                inputFile.click()
            }
        }
    }
}

function onchangeImage() {
    var $this = this
    if (this) {
        var parentElement = this.closest('li');
        if (parentElement) {
            var imageElement = parentElement.querySelector("img.select-image")
            // const file = this.files[0]; // Lấy tệp đã chọn
            $($this).parent().parent().find(".create-image").remove()
            console.log(this.files.length)
            console.log(this)
            if (this.files.length > 5) {
                Swal.fire({
                    position: "center",
                    icon: "error",
                    title: "Sô lượng ảnh bằng 5",
                    showConfirmButton: false,
                    timer: 1500
                });
                this.value = ''; // Xóa các file đã chọn
                return
            }

            $.each(this.files, function (index, item) {

                if (item && imageElement) {
                    // Kiểm tra xem tệp đã chọn có phải là hình ảnh
                    const listItem = document.createElement("li");
                    listItem.setAttribute("data-bs-toggle", "tooltip");
                    listItem.setAttribute("data-popup", "tooltip-custom");
                    listItem.setAttribute("data-bs-placement", "bottom");
                    listItem.className = "avatar avatar-xl pull-up border-dark border create-image";
                    listItem.setAttribute("data-bs-offset", "0,4");
                    listItem.setAttribute("data-bs-html", "true");

                    const image = document.createElement("img");
                    image.alt = "Chưa có ảnh";
                    image.className = "image-preview";
                    if (item.type.startsWith('image/')) {
                        // Tạo đường dẫn URL cho hình ảnh và hiển thị nó
                        const imageURL = URL.createObjectURL(item);
                        image.src = imageURL;

                        // imageElement.src = imageURL;
                    } else {
                        alert('Vui lòng chọn một tệp hình ảnh.');
                        this.value = ''; // Đặt lại trường nhập
                    }
                    listItem.appendChild(image);
                    $($this).parent().parent().append(listItem);
                } else {
                    imageElement.src = "/image/product/anhdefault.jpg"
                }
            })

        }
    }
}

function saveProduct() {
    $("#save-product").unbind()

    $("#save-product").on("click", function clickSave() {
        validate().then(function (response) {
            if (response == false) {
                return
            }
            var sku = document.getElementsByClassName("sku")
            var priceImport = document.getElementsByClassName("priceImport")
            var priceExport = document.getElementsByClassName("priceExport")
            var quantity = document.getElementsByClassName("quantity")
            var checkActive = document.getElementsByClassName("check-active")

            var data = {}
            data.listProduct = []
            data.product = []
            // set ảnh

            $.each(checkActive, function (index, item) {
                if (item.checked == true) {
                    var value = item.getAttribute("value")
                    var image = "imageUpload" + value
                    var arrImage = []
                    var listImage = document.getElementsByClassName(image)
                    $.each(listImage[0].files, function (index, item) {
                        // Lấy tệp từ trường chọn tệp
                        var imageItem;
                        var fileName = item.name; // Lấy đường dẫn đầy đủ của tệp
// Trích xuất tên tệp từ đường dẫn
                        var lastIndex = fileName.lastIndexOf("\\"); // Sử dụng "\\" để tách tên tệp trên Windows
                        if (lastIndex >= 0) {
                            fileName = fileName.substr(lastIndex + 1);
                        }

                        if (index == 0) {
                            imageItem = {
                                location: "true",
                                multipartFile: fileName
                            }
                        } else {
                            imageItem = {
                                location: "false",
                                multipartFile: fileName
                            }
                        }
                        arrImage.push(imageItem)
                    })
                    var temp = {
                        sku: sku[index].value.trim(),
                        priceImport: priceImport[index].value,
                        priceExport: priceExport[index].value,
                        quantity: quantity[index].value,
                        image: arrImage,
                        listAttributes: dataProductDetail.listAttributes[index],
                        active: checkActive[index].checked
                    }
                    data.listProduct.push(temp)
                }
            })
            data.nameProduct = document.getElementById("name-display").value.trim()
            data.sku = document.getElementById("sku-code").value.trim()
            data.brand = $("#select-brand").val()
            data.group = document.getElementById("select-group").value
            var inputAttributes = document.querySelectorAll(".input-data.data-attributes")
            $.each(inputAttributes, function (index, item) {
                // var arr = []
                var allTag = document.querySelector("." + item.id)
                if (allTag) {
                    data.product.push({
                        id: item.id.substring(2),
                        value: allTag.textContent,
                    })
                }
            })
            uploadImage()
            $.ajax({
                url: "/product/save-product",
                method: "post",
                data: JSON.stringify(data),
                contentType: "application/json",
                success: function () {
                    $("#table-product-detail tbody").empty()
                    clear()
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
                        title: "Thêm sản phẩm thành công"
                    });
                }
            })
        })

    })
}

function uploadImage() {
    var fileInput = document.getElementsByClassName('file-input');

    var formData = new FormData();
    $.each(fileInput, function (index, item) {
        if (item.value != "") {
            formData.append('images', item.files[0], item.files[0].name);
        }
    })

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

// Thêm thuộc tính
$("#submit-add-attribute").click(function () {
    Swal.fire({
        title: "Đồng ý thêm thuộc tính?",
        text: "",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Xác nhận!",
        cancelButtonText: "Hủy"
    }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: "/field/all",
                    method: 'get',
                    success: function (data) {
                        var check;
                        var value = $("#name-attribute").val().trim()
                        if (value.length == 0) {

                            $("#attributes-error").text("Chưa nhập tên thuộc tính")
                            check = false
                        } else {
                            $.each(data, function (index, item) {
                                if (item.name.toLowerCase() == value.toLowerCase()) {
                                    $("#attributes-error").text("Thuộc tính đã tồn tại")
                                    check = false
                                    return false
                                } else {
                                    $("#attributes-error").text("")
                                }
                            })
                        }

                        if (check == false) {
                            return
                        }
                        var dataPost = {
                            name: value,
                            active: true
                        }
                        $.ajax(
                            {
                                url: "/field/add",
                                method: "post",
                                data: JSON.stringify(dataPost), // Chuyển đổi dữ liệu thành chuỗi JSON
                                contentType: 'application/json',
                                success: function (data) {
                                    $("#attributes-error").text("")
                                    const Toast = Swal.mixin({
                                        toast: true,
                                        position: 'top-end',
                                        showConfirmButton: false,
                                        timer: 3000,
                                        timerProgressBar: true,
                                        didOpen: (toast) => {
                                            toast.addEventListener('mouseenter', Swal.stopTimer)
                                            toast.addEventListener('mouseleave', Swal.resumeTimer)
                                        }
                                    })
                                    Toast.fire({
                                        icon: 'success',
                                        title: 'Thêm thành công thuộc tính ' + data.name,
                                        customClass: {
                                            container: 'alert' // Tên lớp tùy chỉnh
                                        }
                                    })
                                    $("#name-attribute").val("")

                                    var button = document.createElement('button');
                                    button.innerHTML = 'Thêm'; // Đặt văn bản cho nút
                                    button.className = "btn btn-outline-secondary addAttributes mx-2"
                                    button.value = data.name
                                    button.id = "_" + data.id

                                    var label = document.createElement('label')
                                    button.appendChild(label)
                                    // Tạo một ô trong bảng
                                    var cell1 = document.createElement('td');
                                    var cell2 = document.createElement('td');
                                    var cell3 = document.createElement('td');

                                    var tbody = document.getElementById('tableAttributes').getElementsByTagName('tbody')[0];


                                    cell1.innerText = document.getElementById('tableAttributes').rows.length
                                    cell2.innerText = data.name
                                    cell3.appendChild(button); // Thêm nút vào ô
                                    // Lấy bảng theo ID

                                    // Thêm ô (cell) vào dòng (row) trong bảng
                                    var row = tbody.insertRow($("tableAttributes").rows); // Thay đổi chỉ số hàng theo ý muốn
                                    row.appendChild(cell1);
                                    row.appendChild(cell2);
                                    row.appendChild(cell3);

                                },
                                error: function (error) {
                                    console.log('Lỗi trong quá trình POST yêu cầu:', error);
                                }
                            }
                        )
                    }
                })
            }
        }
    )
})
$("#submit-add-variant").click(function () {
    Swal.fire({
        title: "Đồng ý thêm thuộc tính?",
        text: "",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Xác nhận!",
        cancelButtonText: "Hủy",
    }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: "/field/all",
                    method: 'get',
                    success: function (data) {
                        var check;
                        var value = $("#name-variant").val()
                        if (value.length == 0) {
                            console.log(value.length)
                            $("#variant-error").text("Chưa nhập tên thuộc tính")
                            check = false
                        } else {
                            $.each(data, function (index, item) {
                                if (item.name.toLowerCase() == value.toLowerCase()) {
                                    $("#variant-error").text("Thuộc tính đã tồn tại")
                                    check = false
                                    return false
                                } else {
                                    $("#variant-error").text("")
                                }
                            })
                        }
                        if (check == false) {
                            return
                        }

                        var dataPost = {
                            name: value,
                            variant: 1,
                            active: true
                        }
                        $.ajax(
                            {
                                url: "/field/add",
                                method: "post",
                                data: JSON.stringify(dataPost), // Chuyển đổi dữ liệu thành chuỗi JSON
                                contentType: 'application/json',
                                success: function (data) {
                                    $("#variant-error").text("")
                                    const Toast = Swal.mixin({
                                        toast: true,
                                        position: 'top-end',
                                        showConfirmButton: false,
                                        timer: 3000,
                                        timerProgressBar: true,
                                        didOpen: (toast) => {
                                            toast.addEventListener('mouseenter', Swal.stopTimer)
                                            toast.addEventListener('mouseleave', Swal.resumeTimer)
                                        }
                                    })

                                    Toast.fire({
                                        icon: 'success',
                                        title: 'Thêm thành công biến thể ' + data.name,
                                        customClass: {
                                            container: 'alert' // Tên lớp tùy chỉnh
                                        }
                                    })
                                    $("#name-variant").val("")

                                    var button = document.createElement('button');
                                    button.innerHTML = 'Thêm'; // Đặt văn bản cho nút
                                    button.className = "btn btn-outline-secondary add-variant mx-2"
                                    button.value = data.name
                                    button.id = "_" + data.id

                                    var label = document.createElement('label')
                                    button.appendChild(label)
                                    // Tạo một ô trong bảng
                                    var cell1 = document.createElement('td');
                                    var cell2 = document.createElement('td');
                                    var cell3 = document.createElement('td');

                                    var tbody = document.getElementById('table-variant').getElementsByTagName('tbody')[0];


                                    cell1.innerText = document.getElementById('table-variant').rows.length
                                    cell2.innerText = data.name
                                    cell3.appendChild(button); // Thêm nút vào ô
                                    // Lấy bảng theo ID

                                    // Thêm ô (cell) vào dòng (row) trong bảng
                                    var row = tbody.insertRow($("table-variant").rows); // Thay đổi chỉ số hàng theo ý muốn
                                    row.appendChild(cell1);
                                    row.appendChild(cell2);
                                    row.appendChild(cell3);
                                },
                                error: function (error) {
                                    console.log('Lỗi trong quá trình POST yêu cầu:', error);
                                }
                            }
                        )
                    }
                })
            }
        }
    )
})

function myFunction() {
    // Declare variables
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("search-input");
    filter = input.value.toUpperCase();
    table = document.getElementById("tableAttributes");
    tr = table.getElementsByTagName("tr");

    // Loop through all table rows, and hide those who don't match the search query
    for (i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[1];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}

function searchVariant() {
    // Declare variables
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("search-variant");
    filter = input.value.toUpperCase();
    table = document.getElementById("table-variant");
    tr = table.getElementsByTagName("tr");

    // Loop through all table rows, and hide those who don't match the search query
    for (i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[1];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}

document.addEventListener('click', function (event) {
    if (event.target.classList.contains('addAttributes')) {
        var itemContentDiv = document.querySelector('.item-content');
// Tạo phần tử <div class="attributes">
        var attributesGroupDiv = document.createElement('div');
        attributesGroupDiv.className = 'attributes mb-3';

// Tạo label
        var label = document.createElement("label")
        label.className = "col-form-label"
        label.innerText = event.target.value

        attributesGroupDiv.appendChild(label)
        // div input group
        var inputGroupDiv = document.createElement("div")
        inputGroupDiv.className = "input-group shadow-none"

        var inputDiv = document.createElement("div")
        inputDiv.className = "input-div col-sm-10"
        // tạo textarea
        var input = document.createElement("input")
        input.className = "form-control input-data data-attributes w-100"
        input.rows = 1
        input.id = "_" + event.target.id
        inputDiv.appendChild(input)
        inputGroupDiv.appendChild(inputDiv)

        var deleteDiv = document.createElement('div');
        deleteDiv.className = "col-sm-2 text-center"
        //tạo button delete
        var buttonDel = document.createElement('button')
        buttonDel.type = 'button';
        buttonDel.className = "btn btn-danger delete-button";
        buttonDel.value = event.target.id
        var spanInBtn = document.createElement('span');
        spanInBtn.className = "tf-icons bx bxs-trash delete-button";
        spanInBtn.value = event.target.id

        buttonDel.appendChild(spanInBtn)
        deleteDiv.appendChild(buttonDel)
        inputGroupDiv.appendChild(deleteDiv)
        attributesGroupDiv.appendChild(inputGroupDiv)
        itemContentDiv.appendChild(attributesGroupDiv);

        $.each($(".input-data"), function (index, item) {
            item.addEventListener("keydown", function (e) {
                if (e.key === "Enter" && e.target.value.trim() !== "") {
                    // Tạo một thẻ span cho tag mới
                    const inputValue = e.target.value.trim();
                    const tagValue = inputValue.slice(0);
                    var listValue = []
                    var allTag = document.querySelectorAll("." + e.target.id)

                    $.each(allTag, function (index, item) {
                        listValue.push(item.textContent)
                    })
                    // thêm tag
                    if (allTag.length > 0) {
                        setTimeout(function () {
                            Swal.fire({
                                title: "Thuộc tính chỉ được thêm 1 giá trị",
                                text: "Hãy xóa giá trị cũ nếu muốn thêm giá trị mới",
                                icon: "warning",
                                confirmButtonColor: "#3085d6",
                                confirmButtonText: "Đã hiểu",
                                customClass: {
                                    container: "alert"
                                }
                            })
                        }, 100);
                        return
                    }
                    if (listValue.includes(tagValue)) {
                        // Kiểm tra xem tag đã tồn tại chưa
                        return;
                    }
                    e.target.value = "";

                    const tag = document.createElement("div");
                    tag.textContent = tagValue;
                    tag.classList.add("tag", e.target.id);
                    e.target.closest(".input-div").appendChild(tag);
                    // Xử lý sự kiện khi người dùng nhấp vào tag để xóa

                    tag.addEventListener("click", function () {
                        removeTag(tagValue, tag, e);
                        changeTableProductDetail()
                    });
                    // Ngăn chặn hành vi mặc định của Enter (ngăn xuống dòng)
                    e.preventDefault();

                    saveProduct()
                    changeTableProductDetail()
                }
            })
        });
        event.target.hidden = true
    }
    if (event.target.classList.contains('delete-button')) {
        // Xóa thẻ cha của nút "Xóa"
        var id = event.target.value;
        if (document.getElementById(id) != null) {
            document.getElementById(id).hidden = false
        }

        var inputGroupDiv = event.target.closest('.attributes');
        if (inputGroupDiv) {
            inputGroupDiv.remove();
        }
        changeTableProductDetail()
    }
    if (event.target.classList.contains('add-variant')) {
        var itemContentDiv = document.querySelector('.item-content-variant');
// Tạo phần tử <div class="attributes">
        var attributesGroupDiv = document.createElement('div');
        attributesGroupDiv.className = 'variant mb-3';

// Tạo label
        var label = document.createElement("label")
        label.className = "col-form-label"
        label.innerText = event.target.value
        attributesGroupDiv.appendChild(label)
        // div input group
        var inputGroupDiv = document.createElement("div")
        inputGroupDiv.className = "input-group shadow-none"

        var inputDiv = document.createElement("div")
        inputDiv.className = "input-div col-sm-10"
        // tạo textarea
        var input = document.createElement("input")
        input.className = "form-control input-data data-variant w-100"
        input.rows = 1
        input.id = "_" + event.target.id
        inputDiv.appendChild(input)
        inputGroupDiv.appendChild(inputDiv)

        var deleteDiv = document.createElement('div');
        deleteDiv.className = "col-sm-2 text-center"
        //tạo button delete
        var buttonDel = document.createElement('button')
        buttonDel.type = 'button';
        buttonDel.className = "btn btn-danger delete-button-variant";
        buttonDel.value = event.target.id
        var spanInBtn = document.createElement('span');
        spanInBtn.className = "tf-icons bx bxs-trash delete-button-variant";
        spanInBtn.value = event.target.id

        buttonDel.appendChild(spanInBtn)
        deleteDiv.appendChild(buttonDel)
        inputGroupDiv.appendChild(deleteDiv)
        attributesGroupDiv.appendChild(inputGroupDiv)
        itemContentDiv.appendChild(attributesGroupDiv);
        $.each($(".input-data"), function (index, item) {
            item.addEventListener("keydown", function (e) {
                if (e.key === "Enter" && e.target.value.trim() !== "") {
                    // Tạo một thẻ span cho tag mới
                    const inputValue = e.target.value.trim();
                    const tagValue = inputValue.slice(0);
                    var listValue = []
                    var allTag = document.querySelectorAll("." + e.target.id)

                    $.each(allTag, function (index, item) {
                        listValue.push(item.textContent)
                    })
                    // thêm tag
                    if (listValue.includes(tagValue)) {
                        // Kiểm tra xem tag đã tồn tại chưa
                        return;
                    }
                    e.target.value = "";

                    const tag = document.createElement("div");
                    tag.textContent = tagValue;
                    tag.classList.add("tag", e.target.id);
                    e.target.closest(".input-div").appendChild(tag);
                    // Xử lý sự kiện khi người dùng nhấp vào tag để xóa

                    tag.addEventListener("click", function () {
                        removeTag(tagValue, tag, e);
                        changeTableProductDetail()
                    });
                    // Ngăn chặn hành vi mặc định của Enter (ngăn xuống dòng)
                    e.preventDefault();
                    saveProduct()
                    changeTableProductDetail()
                }
            })
        });
        event.target.hidden = true
    }
    if (event.target.classList.contains('delete-button-variant')) {
        // Xóa thẻ cha của nút "Xóa"
        var id = event.target.value;
        if (document.getElementById(id) != null) {
            document.getElementById(id).hidden = false
        }
        var inputGroupDiv = event.target.closest('.variant');
        if (inputGroupDiv) {
            inputGroupDiv.remove();
        }
        changeTableProductDetail()
    }
})


function genProduct(listAttri, currentCombination, currentIndex, temp) {
    if (listAttri.length == currentIndex) {
        dataProductDetail.type = $('#select-type option:selected').text();
        var listAttributes = []
        for (let i = 0; i < temp.length; i++) {
            var loop = {
                id: temp[i].id,
                value: currentCombination[i]
            }
            listAttributes.push(loop)
        }
        dataProductDetail.listAttributes.push(listAttributes)
        return
    }

    var listTemp = []
    $.each(listAttri, function (index, item) {
        var temp = {
            id: item.id,
        }
        listTemp.push(temp)
    })

    var currentList = listAttri[currentIndex].value;

    $.each(currentList, function (index, item) {
        currentCombination.push(item)
        genProduct(listAttri, currentCombination, currentIndex + 1, listTemp)
        currentCombination.pop()
    })
}

function changeTableProductDetail() {
    var group = $("#select-group")
    // var type = $("#select-type")
    var listVariant = document.querySelectorAll(".input-data.data-variant")
    var data = {
        group: group.value,
        listAttributes: []
    }
    $.each(listVariant, function (index, item) {
        var arr = []
        var allTag = document.querySelectorAll("." + item.id)
        $.each(allTag, function (index, value) {
            arr.push(value.textContent)
        })
        data.listAttributes.push({
            id: item.id.substring(2),
            value: arr,
        })
    })

    dataProductDetail.listAttributes = []
    genProduct(data.listAttributes, [], 0, [])
    if (dataProductDetail.listAttributes.length !== 0) {
        $("#table-product-detail tbody").empty()
        var list = dataProductDetail.listAttributes
        $.each(list, function (index, item) {
            // lấy tên san phẩm
            if (item.length === 0) {
                return
            }
            var temp = item
            var nameProduct = []
            document.getElementById("select-type")
            $.each(temp, function (index, itemTemp) {
                nameProduct.push(itemTemp.value)
            })
            //end
// Tạo một thẻ <tr>

            const tableRow = document.createElement("tr");

// Tạo một thẻ <td> cho Tên hiển thị
            const displayNameCell = document.createElement("td");
            var nameSpan = document.createElement("span");
            nameSpan.innerText = nameProduct.join(" ");
            nameSpan.className = "name-product"
            displayNameCell.appendChild(nameSpan)
            tableRow.appendChild(displayNameCell);

// Tạo các ô input
            const inputCells = [];
            const name = ["sku", "quantity", "priceImport", "priceExport"]
            for (let i = 0; i < 4; i++) {
                const inputCell = document.createElement("td");
                const input = document.createElement("input");
                input.className = "form-control " + (name[i] == "sku" ? "sku sku-detail" : name[i]);
                if (i === 0) {
                    input.type = "text";
                    input.value = $("#sku-code").val() + "-" + index
                } else {
                    input.type = "number";
                    input.value = 0
                }
                input.name = name[i]

                var lable = document.createElement("span")
                lable.className = "fst-italic text-danger error " + "error-" + name[i]
                inputCell.appendChild(input);
                inputCell.appendChild(lable);
                inputCells.push(inputCell);
            }
            tableRow.append(...inputCells);

// Tạo một ô chứa ảnh và input file
            const imageCell = document.createElement("td");
            const imageList = document.createElement("ul");
            imageList.className = "list-unstyled users-list avatar-group m-0 d-flex align-items-center";

            // for (let i = 0; i < 5; i++) {
            const listItem = document.createElement("li");
            listItem.setAttribute("data-bs-toggle", "tooltip");
            listItem.setAttribute("data-popup", "tooltip-custom");
            listItem.setAttribute("data-bs-placement", "bottom");
            listItem.className = "avatar avatar-xl pull-up border-dark border";
            listItem.setAttribute("data-bs-offset", "0,4");
            listItem.setAttribute("data-bs-html", "true");

            const image = document.createElement("img");
            image.src = "/image/product/anhdefault.jpg";
            image.alt = "Chưa có ảnh";
            image.className = "select-image";
            image.onclick = clickImage

            const inputFile = document.createElement("input");
            inputFile.type = "file";
            inputFile.hidden = true;
            inputFile.onchange = onchangeImage
            // inputFile.setAttribute("data-m")c
            // if (i == 0) {
            listItem.setAttribute("title", "Chọn ảnh");
            inputFile.className = "file-input true" + " imageUpload" + index;
            inputFile.multiple = true
            inputFile.accept = "image/*"
            inputFile.max = 5
            // } else {
            //     listItem.setAttribute("title", "Ảnh phụ");
            //     inputFile.className = "file-input false" + " imageUpload" + index;
            // }
            listItem.appendChild(image);
            listItem.appendChild(inputFile);
            imageList.appendChild(listItem);
            // }

            var lable = document.createElement("span")
            lable.className = "fst-italic text-danger error " + "error-image"
            imageCell.appendChild(imageList);
            imageCell.appendChild(lable)
            tableRow.appendChild(imageCell);

// Tạo ô cho nút dropdown
            const activeCell = document.createElement("td");
            const div = $('<div></div>')
            $(div).addClass("d-flex align-items-center")
            var check = document.createElement("input")
            check.className = "form-check-input larger-checkbox-2 check-active mx-3"
            check.type = "checkbox"
            check.checked = true
            check.value = index

            $(div).append(check);
            // $(div).append(
            //     $('<button>').addClass('btn btn-icon btn-primary view-attributes').val(item.id).attr('data-bs-toggle', 'modal').attr('data-bs-target', '#modalAssignAttributes').attr('type', 'button').html("<i class='bx bx-plus'></i>"))

            changeSku(check)
            $(activeCell).append(div)
            tableRow.appendChild(activeCell);

            const table = document.querySelector(".table-product tbody"); // Điều chỉnh chọn bảng cụ thể
            table.appendChild(tableRow);

            document.getElementById("detail-pro").hidden = false
        });
    } else {
        document.getElementById("detail-pro").hidden = true
    }
}

//remove tag input attribute
function removeTag(tagValue, tagElement, e) {
    e.target.closest(".input-div").removeChild(tagElement);
}


function validate() {
    return new Promise(function (resolve, reject) {
        $.ajax({
            url: '/admin/product/sku',
            method: "get",
            success: function (response) {
                $.ajax({
                    url: '/product/product-detail/all',
                    method: "get",
                    success: function (listSkuResponse) {
                        clearError()
                        var check = [];
                        var active = 0;
                        var skuCode = document.getElementById("sku-code")
                        var name = $("#name-display")
                        var brand = $("#select-brand")
                        var listSku = document.getElementsByClassName("sku")
                        var listValueSku = []
                        $.each(listSku, function (index, item) {
                            listValueSku.push(item.value)
                        })

                        var listQuantity = document.getElementsByClassName("quantity")
                        var listPriceImport = document.getElementsByClassName("priceImport")
                        var listPriceExport = document.getElementsByClassName("priceExport")
                        var checkActive = document.getElementsByClassName("check-active")
                        $.each(checkActive, function (index, item) {
                            var parentElement, span;
                            if (item.checked) {
                                active = 1
                                parentElement = listSku[index].closest('td');
                                span = parentElement.querySelector("span.error-sku")
                                if (listSku[index].value === "") {
                                    span.textContent = "Nhập mã sku cho sản phẩm"
                                    check.push(false)
                                } else {
                                    var temp = 0
                                    $.each(listValueSku, function (indexListValueSku, item) {
                                        if (listSku[index].value == item) {
                                            temp++;
                                            if (temp == 2) {
                                                check.push(false)
                                                span.textContent = "Mã sku bị trùng"
                                                return false
                                            }
                                        }
                                    })
                                    if (listSkuResponse.includes(listSku[index].value)) {
                                        check.push(false)
                                        span.textContent = "Mã sku đã tồn tại"
                                    }
                                }

                                parentElement = listQuantity[index].closest('td');
                                span = parentElement.querySelector("span.error-quantity")
                                if (listQuantity[index].value === "" || Number(item.value) < 0) {
                                    span.textContent = "Số lượng >=0"
                                    check.push(false)
                                }

                                parentElement = listPriceImport[index].closest('td');
                                span = parentElement.querySelector("span.error-priceImport")
                                if (listPriceImport[index].value === "" || Number(item.value) < 0) {
                                    span.textContent = "Giá nhập >=0 "
                                    check.push(false)
                                }

                                parentElement = listPriceExport[index].closest('td');
                                span = parentElement.querySelector("span.error-priceExport")
                                if (listPriceExport[index].value === "" || Number(item.value) < 0) {
                                    span.textContent = "Giá bán >=0"
                                    check.push(false)
                                }
                                var value = item.getAttribute("value")
                                var image = "imageUpload" + value
                                var listImage = document.getElementsByClassName(image)
                                console.log(listImage[0].files)
                                parentElement = listImage[0].closest('td');
                                span = parentElement.querySelector("span.error-image")
                                if (listImage[0].files.length < 5) {
                                    check.push(false)
                                    span.textContent = "Chọn 5 ảnh sản phẩm"
                                }

                            }

                        })
                        if (active == 0) {
                            $("#active-error").text("Cần có ít nhất 1 sản phẩm bày bán")
                            check.push(false)
                        }

                        if ($(name).val().trim().length === 0) {
                            $("#name-error").text("Chưa nhập tên sản phẩm")
                            check.push(false)
                        }

                        if ($(brand).val() == -1) {
                            $("#brand-error").text("Chưa chọn hãng cho sản phẩm")
                            check.push(false)
                        }

                        if (skuCode.value.trim().length == 0) {
                            check.push(false)
                            $("#sku-code-error").text("Chưa nhập mã sku")
                        } else {
                            $.each(response, function (index, item) {
                                if (item === skuCode.value.trim()) {
                                    check.push(false)
                                    $("#sku-code-error").text("Mã sku đã tồn tại")
                                    return false
                                }
                            })
                        }
                        if (check.indexOf(false) > -1) {
                            resolve(false);
                        } else {
                            resolve(true)
                        }
                    }
                })
            },
            error: function (error) {
                reject(error);
            }
        })
    })
}

function clearError() {
    $(".error").text("")
}

// clear form modal add product
function clear() {
    loadDataGroup()
    var container = document.querySelector(".item-content")
    while (container.firstChild) {
        container.removeChild(container.firstChild);
    }
    var containerVariant = document.querySelector(".item-content-variant")
    while (containerVariant.firstChild) {
        containerVariant.removeChild(containerVariant.firstChild);
    }
    $(".tag").remove()
    var buttonAdd = document.querySelectorAll(".addAttributes")
    $.each(buttonAdd, function (index, item) {
        item.hidden = false
    })
    var buttonAdd = document.querySelectorAll(".add-variant")
    $.each(buttonAdd, function (index, item) {
        item.hidden = false
    })

    document.getElementById("sku-code").value = ""
    document.getElementById("name-display").value = ""
}

var changeSku = function (check) {
    $(check).on("change", function () {
        var skuDetail = $(this).parent().parent().parent().find(".sku-detail")

        if (!$(this).prop("checked")) {
            // var skuDetail = $(this).parent().parent().parent().find(".sku")
            $(skuDetail).removeClass("sku")
        } else {
            // var skuDetail = $(this).parent().parent().parent().find(".sku-detail")
            $(skuDetail).addClass("sku")
        }
    })
}