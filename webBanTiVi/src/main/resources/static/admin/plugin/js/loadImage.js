const fileInputs = document.getElementsByClassName('file-input');
const imagePreviews = document.getElementsByClassName('image-preview');

for (let i = 0; i < fileInputs.length; i++) {
    const fileInput = fileInputs[i];
    const imagePreview = imagePreviews[i];
    imagePreview.addEventListener("click", function () {
        fileInput.click()
    })
    // Lắng nghe sự kiện khi người dùng chọn tệp
    fileInput.addEventListener('change', function () {
        const file = fileInput.files[0]; // Lấy tệp đã chọn
        if (file) {
            // Kiểm tra xem tệp đã chọn có phải là hình ảnh
            if (file.type.startsWith('image/')) {
                // Tạo đường dẫn URL cho hình ảnh và hiển thị nó
                const imageURL = URL.createObjectURL(file);
                imagePreview.src = imageURL;
            } else {
                alert('Vui lòng chọn một tệp hình ảnh.');
                fileInput.value = ''; // Đặt lại trường nhập
            }
        } else {
            imagePreview.src = "/image/product/anhdefault.jpg"
        }
    });
}
