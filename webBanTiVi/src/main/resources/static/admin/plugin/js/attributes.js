$(document).ready(getField(),
)

function getField() {
    var data = {
        page: 1,
        size: 10
    }
    $.ajax({
        url: "/field/get",
        method: "get",
        data: data,
        success: function (data) {
            var table = $("#list-attributes tbody")
            table.empty()
            $.each(data, function (index, item) {
                var tr = $('<tr>')
                var td1 = $('<td>').text(index + 1)
                var td2 = $('<td>').text(item.name)
                var td3 = $('<td>').text(item.variant == true ? 'Thuộc tính biến thể' : 'Thuộc tính thường')
                if (item.variant) {
                    var td4 = $('<td>').append($('<div>').addClass('form-check form-switch mb-2').html("<input class='form-check-input active-attributes variant' type='checkbox' value=" + item.id + " " + (item.active == true ? 'checked' : '') + ">"))
                } else {
                    var td4 = $('<td>').append($('<div>').addClass('form-check form-switch mb-2').html("<input class='form-check-input active-attributes' type='checkbox' value=" + item.id + " " + (item.active == true ? 'checked' : '') + ">"))
                }

                var td5 = $('<td>').append(
                    $('<button>').addClass('btn btn-icon btn-outline-primary view-attributes').val(item.id).attr('data-bs-toggle', 'modal').attr('data-bs-target', '#modalDetailAttributes').attr('type', 'button').html("<i class='bx bxs-edit-alt'> </i>"))

                tr.append(td1, td2, td3, td4, td5)
                table.append(tr)
            })
            updateActive()
            findById()
        }
    })
}

function updateActive() {
    $.each($(".active-attributes"), function (index, item) {
        item.addEventListener('change', function () {
            var text = ''
            if (item.checked == false) {
                text = 'Nếu tắt hoạt động thuộc tính sẽ không được hiển thị'
            }
            if (item.closest('.variant') && item.checked == false) {
                text = 'Tắt hoạt động của thuộc tính biến thể sẽ ngừng bán các sản phẩm đang có thuộc tính này'
            }
            Swal.fire({
                title: "Bạn xác nhận cập nhật trạng thái?",
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
                        url: "/field/update",
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
                    this.checked = !this.checked
                }
            })

        })
    })
}

function findById() {
    $.each($(".view-attributes"), function (index, item) {
        var $this = this
        item.addEventListener("click", function () {
            $.ajax({
                url: "/field/get-one",
                method: "get",
                data: {id: Number(item.value)},
                success: function (data) {
                    $("#name-attribute").val(data.name)
                    $("#update-attribute").val(data.id)
                    $("#type-attribute").addClass(data.variant).text(data.variant == true ? 'Thuộc tính biến thể' : 'Thuộc tính thường')
                    $("#active").attr('checked', false)
                    $("#inactive").attr('checked', false)
                    if (data.active) {
                        $("#active").attr('checked', true)
                    } else {
                        $("#inactive").attr('checked', true)
                    }
                    $("#update-attribute").on('click', function () {
                        var text = ''
                        if ($("#inactive").is(':checked') == true) {
                            text = 'Nếu tắt hoạt động thuộc tính sẽ không được hiển thị'
                        }
                        if ($("#type-attribute").hasClass('true') && $("#inactive").is(':checked') == true) {
                            text = 'Tắt hoạt động của thuộc tính biến thể sẽ ngừng bán các sản phẩm đang có thuộc tính này'
                        }
                        $("#close").click()
                        Swal.fire({
                            title: "Bạn xác nhận cập nhật thuộc tính?",
                            text: text,
                            icon: "question",
                            showCancelButton: true,
                            confirmButtonColor: "#3085d6",
                            cancelButtonColor: "#d33",
                            confirmButtonText: "Xác nhận!",
                            focusConfirm: true
                        }).then((result) => {
                            if (result.isConfirmed) {
                                var data = {
                                    id: $("#update-attribute").val(),
                                    name: $("#name-attribute").val(),
                                    active: $("#active").is(':checked') == true ? 'true' : 'false'
                                }
                                $.ajax({
                                    url: "/field/update",
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
                                        var tr = $($this).closest('tr')
                                        var td = $(tr).find('td')
                                        td[1].innerText = $("#name-attribute").val()
                                        var active = $(td[3]).find('input')
                                        if ($("#active").is(':checked')) {
                                            $(active).attr('checked', true)
                                        } else {
                                            $(active).removeAttr('checked')
                                        }
                                    }
                                })
                            } else {
                                $this.click()
                            }
                        })
                    })
                }
            })
        })
    })
}

$("#search").keyup(function () {
    var input, filter, table, tr, td, i, txtValue;
    input = $("#search");
    filter = input.val().toUpperCase();
    table = $("#list-attributes");
    tr = table.find("tr");

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
})

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
                        var value = $("#name-attribute-1").val().trim()
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
                            active:true,
                            variant:$("#normal").prop("checked") == false ? true : false
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
                                   window.location.reload()
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

// }

