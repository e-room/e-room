const login = (e) => {
    e.preventDefault();

    let data = $('#login-form').serialize();

    $.ajax({
        type: 'POST',
        url: '/alr20/admin/login',
        // beforeSend: function(xhr){
        //     xhr.setRequestHeader(header, token);
        // },
        dataType: "json",
        contentType: 'application/x-www-form-urlencoded; charset=utf-8',
        data: data,
        cache: false,
        success: (res) => {
            window.location.href = "/alr20/admin/main";
        },
        error: (res) => {
            const errRes = res.responseJSON;
            alert(errRes.message);
            $('#login-failure-message').removeClass('d-none').addClass('d-block');
        }
    })
}