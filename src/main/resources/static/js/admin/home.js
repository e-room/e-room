const pseudoAccount = (e) => {
    e.preventDefault();

    let data = {
        authProviderType: $('#authProviderType').val(),
        email: $('#email').val(),
        name: $('#name').val()
    }

    $.ajax({
        type: 'POST',
        url: '/admin/member',
        // beforeSend: function(xhr){
        //     xhr.setRequestHeader(header, token);
        // },
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data),
        cache: false,
        success: (res) => {
            alert('등록되었습니다!');
        },
        error: (res) => {
            const errRes = res.responseJSON;
            alert(errRes.message);
        }
    })
}