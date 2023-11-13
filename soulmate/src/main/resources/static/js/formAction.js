const formAction = {
    /* form 전송하기 */
    post: (url, dto, files) => {
        let fd = new FormData(document.querySelector("#frm")); //파라미터로 전송할 객체
        let headers = {
            "Content-Type": "application/json"
        }

        //form input 값 불러오기
        for (const key of Object.keys(dto)) {
            dto[key] = fd.get(key);
        }

        //fd를 필요한 데이터만으로 재구성하기
        fd = new FormData();
        fd.append("dto", new Blob([JSON.stringify(dto)], { type: "application/json" }));

        //첨부파일이 있으면 추가하고 headers 변경하기
        if (files != null) {
            headers = {
                "Content-Type": "multipart/form-data"
            };
            fd.append("file", files);
        }

        /*
        //fd 내용 확인하기
        for (const pair of fd.entries()) {
            console.log(pair[0] + ", " + pair[1]);
        }
        */

        axios.post(url, fd, {
            headers: headers
        }).then(res => {
            console.log(res);
        }).catch(err => {
            console.log(err);
        });
    }
};