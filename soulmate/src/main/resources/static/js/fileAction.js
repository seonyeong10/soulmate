const fileAction = {
    /* 이미지 미리보기 */
    readURL: (input) => {
        const { files } = input.target;

        if (files && files[0]) {
            const reader = new FileReader();
            reader.onload = function(e) {
            document.querySelector("#preview").src = e.target.result;
        }
            reader.readAsDataURL(files[0]);
        } else {
            document.querySelector("#preview").src = "";
        }
    }
};