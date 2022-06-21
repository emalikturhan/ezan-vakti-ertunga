$(document).ready(function () {
    var mydiv = $("#content");
    mydiv.scrollTop(mydiv.prop("scrollHeight"));

    $("#send").click(function () {
        if ($('#fileupload').val().length > 0) {
            var file = document.querySelector('#fileupload').files[0];
            getBase64(file).then(function (cevap) {
                $.post("post.php", { set_img: cevap, data: $("#data").val(), statu: online }).done(function (y) {
                    if (y == 1) {
                        var d = new Date();
                        var date = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate() + " " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds();
                        socket.emit("message", kanal, "", date, "", sender, receiver, "2", rimage, rtype, "0", "0");
                        location.reload();

                    } else {
                        Stop();
                    }
                });
            });


        } else {
            if ($("#msg_txt").val() == "") {
                alert("Boş Mesaj Gönderemezsiniz");
            } else {
                var d = new Date();
                var date = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate() + " " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds();
                $.post("post.php", { set_msg: $("#msg_txt").val(), data: $("#data").val(), statu: online }).done(function (y) {
                    //alert(y);

                });
                socket.emit("message", kanal, $("#msg_txt").val(), date, "", sender, receiver, "1", rimage, rtype, "0", "0");
                var msg = '<div class="row m-0">';
                msg += '<div class="message-card">';
                msg += '<div class="card-body sender-background">';
                msg += '<span>' + $("#msg_txt").val() + '</span>';
                msg += '</div>';
                msg += '<span class="sender-time"><small>' + date + '</small></span>';
                msg += '</div>';
                msg += '</div>';

                $("#msgbox").append($("<li>").html(msg));
                var mydiv = $("#content");
                mydiv.scrollTop(mydiv.prop("scrollHeight"));
                $("#msg_txt").val("");
            }
        }
    });


    $("#send_special").click(function () {
        if ($('#fileupload').val().length > 0) {
            var file = document.querySelector('#fileupload').files[0];
            getBase64(file).then(function (cevap) {
                $.post("post.php", { set_img_special: cevap, datax: $("#data").val(), statu: online }).done(function (y) {
                    if (y == 1) {
                        var d = new Date();
                        var date = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate() + " " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds();
                        socket.emit("message", kanal, "", date, "", sender, receiver, "2", rimage, rtype, "0", "0");
                        location.reload();
                    } else {
                        Stop();
                    }
                });
            });


        } else {
            if ($("#msg_txt").val() == "") {
                alert("Boş Mesaj Gönderemezsiniz");
            } else {
                $.post("post.php", { set_msg_special: $("#msg_txt").val(), datax: $("#data").val(), statu: online }).done(function (y) {
                    //alert(y);
                });
                var d = new Date();
                var date = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate() + " " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds();
                socket.emit("message", kanal, $("#msg_txt").val(), date, "", sender, receiver, "1", rimage, rtype, "0", "0");
                var msg = '<div class="row m-0">';
                msg += '<div class="message-card">';
                msg += '<div class="card-body sender-background">';
                msg += '<span>' + $("#msg_txt").val() + '</span>';
                msg += '</div>';
                msg += '<span class="sender-time"><small>' + date + '</small></span>';
                msg += '</div>';
                msg += '</div>';

                $("#msgbox").append($("<li>").html(msg));
                var mydiv = $("#content");
                mydiv.scrollTop(mydiv.prop("scrollHeight"));
                $("#msg_txt").val("");
            }
        }
    });


    $("#file").click(function () {
        $('#fileupload').trigger('click');
        Start();
        return false;
    });



    $("#iptal").click(function () {
        Stop();
        return false;
    });


});


var interval = "";

function Start() {
    interval = setInterval(function () {
        if ($('#fileupload').val().length > 0) {
            $("#info").show();
            $("#msg_txt").prop("disabled", true);
            console.log("aaa");
        }
    }, 1000);
}

function Stop() {
    clearInterval(interval);
    $('#fileupload').val("");
    $("#msg_txt").prop("disabled", false);
    $("#info").hide();
}


function getBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = error => reject(error);
    });
}



function GetCity(value){
    $.post("post.php", { getcity: value}).done(function (y) {
       $("#city").html(y);
    });
}