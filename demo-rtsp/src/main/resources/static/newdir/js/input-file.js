/**
 * 判断字符串是否为空
 * @param string 传入字符串
 * @returns {boolean} 返回boolean类型，true表示为空值
 */
var isEmptyForString = function (string) {
    if (string === undefined || string === null || string === "") {
        return true;
    }
    return false;
};
/**
 * 判断对象是否为空
 * @param object 传入对象
 * @returns {boolean} 如果对象为空则返回true
 */
var isEmptyForObject = function (object) {
    if (object === undefined || null === object) {
        return true;
    }
    return false;
};


//点击触发文件选择
function click_file(fileIdName) {
    $("#" + fileIdName).click();
}

/**
 * 说明
 * @param fileIdName 表示type为file的id值
 * @param inputIdName 表示type为text的输入框的id值
 * @param imgIdName 表示图片的id值
 */
function replace_file(fileIdName, inputIdName, imgIdName) {
    var blob_img = $("#" + fileIdName)[0].files[0];
    if (isEmptyForObject(blob_img)) {
        return;
    }
    if ((blob_img.type).indexOf("image/") === -1) {
        layer.msg("请选择图片");

        //清空file选择框file内容
        var file = $("#" + fileIdName);
        file.after(file.clone().val(""));
        file.remove();

        $("#" + imgIdName).css("display", "none");
        $("#" + inputIdName).val("");
        return;
    }
    var url = window.URL.createObjectURL(blob_img);
    //替换url图片url
    $("#" + imgIdName).attr("src", url).css("display", "inline-block");
    $("#"+inputIdName).val(blob_img.name);
}


function insertFileUrl(fileIdName, inputIdName, imgIdName) {
    var content = "<div style='margin-top: 20px;margin-left: 10px;margin-right: 10px'>" +
        "<input class='form-control' id='layui-url-value' placeholder='请在此嵌入图片URL地址' onblur='hideWarn()'/>" +
        "<p style='color: red;display: none' id='layui-url-warn'>请输入图片URL地址</p>" +
        "</div>";
    layer.open({
        type: 1,
        title: "嵌入图片URL地址",
        shade: [0],
        area: ['340px', '200px'],
        anim: 4,
        skin:"layui-layer-lan",
        content:content ,
        btn: ['取消', '嵌入'],
        yes: function (index, layero) {
            layer.close(index);
        },
        btn2: function (index, layero) {
            var url = $("#layui-url-value").val();
            if(isEmptyForString(url)){
                $("#layui-url-warn").css("display","inline-block");
            }else {
                //清空file选择框file内容
                var file = $("#" + fileIdName);
                file.after(file.clone().val(""));
                file.remove();

                $("#" + inputIdName).val(url);
                $("#" + imgIdName).attr("src", url).css("display", "inline-block");
                layer.close(index);
            }
            return false;
        }
    });

}

function hideWarn() {
    var url = $("#layui-url-value").val();
    if(!isEmptyForString(url)){
        $("#layui-url-warn").css("display","none");
    }
}

function errorImg(fileIdName, inputIdName, imgIdName) {
    console.log("出错img:"+imgIdName);
    $("#" + inputIdName).val("");
    $("#" + imgIdName).css("display", "none");
    //清空file选择框file内容
    var file = $("#" + fileIdName);
    file.after(file.clone().val(""));
    file.remove();
    layer.msg("图片地址错误,请重新嵌入图片地址");
}

var tip_indexFile;

function OverHintFile(obj) {
    var that = obj;
    tip_indexFile = layer.tips('手动嵌入地址', that, {
        tips: [1, '#00C9BA'],
        time: 6000
    });
}

function OutHintFile(obj) {
    layer.close(tip_indexFile);
}

function remove_file(fileIdName, inputIdName, imgIdName){
    $("#" + inputIdName).val("");
    $("#" + imgIdName).css("display", "none");
    //清空file选择框file内容
    var file = $("#" + fileIdName);
    file.after(file.clone().val(""));
    file.remove();
}