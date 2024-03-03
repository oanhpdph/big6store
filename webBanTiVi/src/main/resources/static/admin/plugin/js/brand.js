$(document).ready(
    getBrand()
)

function getBrand() {
    // var data = {
    //     page: 1,
    //     size: 10
    // }
    $.ajax({
        url: "/api/brand",
        method: "get",
        // data: data,
        success: function (data) {
            var table = $("#list-brand tbody")
            table.empty()
            $.each(data, function (index, item) {
                var tr = $('<tr>')
                var td1 = $('<td>').text(index + 1)
                var td2 = $('<td>').text(item.name)
                if (item.active) {
                    var td4 = $('<td>').append($('<div>').addClass('form-check form-switch mb-2').html("<input class='form-check-input active-brand' type='checkbox' value=" + item.id + " " + (item.active == true ? 'checked' : '') + ">"))
                } else {
                    var td4 = $('<td>').append($('<div>').addClass('form-check form-switch mb-2').html("<input class='form-check-input active-brand' type='checkbox' value=" + item.id + " " + (item.active == true ? 'checked' : '') + ">"))
                }

                var td5 = $('<td>').append(
                    $('<button>').addClass('btn btn-icon btn-outline-primary view-brand').val(item.id).attr('data-bs-toggle', 'modal').attr('data-bs-target', '#modalBrand').attr('type', 'button').html("<i class='bx bxs-edit-alt'> </i>"))
                tr.append(td1, td2, td4, td5)
                table.append(tr)
            })
            findById()
            updateActive()
        }
    })
}

$("#search").keyup(function () {
    var input, filter, table, tr, td, i, txtValue;
    input = $("#search");
    filter = input.val().toUpperCase();
    table = $("#list-brand");
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

function findById() {
    $.each($(".view-brand"), function (index, item) {
        var $this = this
        item.addEventListener("click", function () {
            $.ajax({
                url: "/api/brand/find-id",
                method: "get",
                data: {id: Number(item.value)},
                success: function (data) {
                    $("#name-brand").val(data.name)
                    $("#update-brand").val(data.id)
                    $("#active").attr('checked', false)
                    $("#inactive").attr('checked', false)
                    if (data.active) {
                        $("#active").attr('checked', true)
                    } else {
                        $("#inactive").attr('checked', true)
                    }
                    $("#update-brand").on('click', function () {
                        var text = ''
                        if ($("#inactive").is(':checked') == true) {
                            text = 'Nếu tắt hoạt động sản phẩm của hãng sẽ không bày bán'
                        }
                        $("#close").click()
                        Swal.fire({
                            title: "Bạn xác nhận cập nhật hãng?",
                            text: text,
                            icon: "question",
                            showCancelButton: true,
                            confirmButtonColor: "#3085d6",
                            cancelButtonColor: "#d33",
                            confirmButtonText: "Xác nhận!",
                        }).then((result) => {
                            if (result.isConfirmed) {
                                console.log($("#active").val())
                                var data = {
                                    id: $("#update-brand").val(),
                                    name: $("#name-brand").val(),
                                    active: $("#active").is(':checked') == true ? 'true' : 'false'
                                }
                                $.ajax({
                                    url: "/api/brand/update",
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
                                        td[1].innerText = $("#name-brand").val()
                                        var active = $(td[2]).find('input')
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

function updateActive() {
    $.each($(".active-brand"), function (index, item) {
        item.addEventListener('change', function () {
            var text = ''
            if (item.checked == false) {
                text = 'Nếu tắt hoạt động sản phẩm của hãng sẽ không được bày bán'
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
                        url: "/api/brand/update",
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

$("#btn-add").on("click", function () {
    Swal.fire({
        title: "Xác nhận thêm hãng mới?",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Xác nhận!",
        cancelButtonText: "Hủy"
    }).then((result) => {
        if (result.isConfirmed) {
            var data = {
                name: $("#name").val(),
                active: 'true'
            }
            if ($("#name").val().trim().length == 0) {
                $("#error").text("Chưa nhập tên hãng.")
                return
            }
            $("#error").text("")
            $.ajax({
                url: "/api/brand/find-name",
                method: "get",
                data: data,
                success: function (data1) {
                    if (data1 != "") {
                        $("#error").text("Tên hãng đã tồn tại")
                        return
                    } else {
                        $.ajax({
                            url: "/api/brand/save",
                            data: JSON.stringify(data),
                            method: "post",
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
                                    title: "Thêm thành công"
                                });
                                $("#name").val("")
                                $("#error").text("")
                                $("#close-add").click()
                                getBrand()
                            }
                        })
                    }
                }
            })
        }
    })
})
