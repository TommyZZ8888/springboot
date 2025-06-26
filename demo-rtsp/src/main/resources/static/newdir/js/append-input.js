//保存全局对象
var mapObj = {keyName:[],valueNumber:[]};

/**
 * 追加输入框,groupNamePref:最外层div的名字前缀，最外层div名字格式为“xxxxx-index”,index是数字
 * index:也就是最外层div的名字后面的数字，对应“xxxxx-index”中的index
 * name:input输入框的name值
 * btnClassDelete：删除按钮的class样式
 * placeholder：提示信息
 * spanIconDelete：删除按钮内部标签的样式class
 *
 * 例子：（html样例）
 *
    <div class="input-group" id="telephone-group-0">
        <input type="text" class="form-control" placeholder="请输入手机号" name="telephones">
        <span class="input-group-btn">
            <button class="btn btn-info" type="button" onclick="addInput('telephone-group',0,'emails','btn-danger','请输入手机号','glyphicon glyphicon-trash')">
                <span class="glyphicon glyphicon-plus"></span>
            </button>
        </span>
    </div>
 */
function addInput(groupNamePref,index,name,btnClassDelete,placeholder,spanIconDelete,placeholder2,name2){
    //只有第一个是加，执行了这个方法，在html页面，所以这个索引也就是index永远是不变的，是绑定这个方法设定的值
    var mIndex = parseInt(index);

    var addIndex = mapObj.keyName.indexOf(groupNamePref+"add");
    var lengthIndex = mapObj.keyName.indexOf(groupNamePref+"length");

    //添加索引记录和增加了多少的长度
    if(addIndex === -1){
        mapObj.keyName.push(groupNamePref+"add");
        mapObj.valueNumber.push((mIndex+1));
        mapObj.keyName.push(groupNamePref+"length");
        mapObj.valueNumber.push(0);
    }else {
        var addNumber = mapObj.valueNumber[addIndex];
        var lengthNumber = mapObj.valueNumber[lengthIndex];
        mapObj.valueNumber.splice(addIndex,1,(addNumber+1));
        mapObj.valueNumber.splice(lengthIndex,1,(lengthNumber+1));
    }

    var addIndex2 = mapObj.keyName.indexOf(groupNamePref+"add");
    var textIdIndex = mapObj.valueNumber[addIndex2];

    var text1 = "<div id='"+groupNamePref+textIdIndex+"' style=\"padding-top: 10px\">";
    var text2 = "<div class=\"col-xs-6\" style=\"padding-left: 0px;padding-right: 0px\">";
    var text3 = '<input type="text" class="form-control" placeholder="'+placeholder+'" name="'+name+'" id="'+name+textIdIndex+'">';
    var text4 = "</div>";
    var text5 = "<div class=\"col-xs-6\" style=\"padding-left: 0px;padding-right: 0px\">";
    var text6 = '<div class="input-group input-word-two">';
    var text7 = '<input type="text" class="form-control" placeholder="'+placeholder2+'" name="'+name2+'" id="'+name2+textIdIndex+'">';
    var text8 = '<span class="input-group-btn">';
    var text9 = '<button class="btn '+btnClassDelete+'" type="button" onclick="deleteInput(\''+groupNamePref+'\',\''+textIdIndex+'\')">';
    var text10 = '<span class="'+spanIconDelete+'"></span>';
    var text11 = '</button>';
    var text12 = '</span>';
    var text13 = '</div>';
    var text14 = "</div>"

    var text = text1+text2+text3+text4+text5+text6+text7+text8+text9+text10+text11+text12+text13+text14;

    var lengthIndex2 = mapObj.keyName.indexOf(groupNamePref+"length");
    var iLength = mapObj.valueNumber[lengthIndex2];
    //这个是索引的最大值，由初始值加上长度，因为输入框会被删除，所以这个是记录最大索引的方式
    var iIndex = mIndex+iLength;
    while (true){
        //查询这个索引为最大元素存在不，不存在继续递减查询，直到查询到存在的最大索引元素
        if($("#"+groupNamePref+iIndex).length>0){
            $("#"+groupNamePref+iIndex).after(text);
            break;
        }
        iIndex--;
    }
}
//删除输入框
function deleteInput(groupNamePref,index){
    $("#"+groupNamePref+index).remove();
}

//用在更新操作里面
function appendInputFormEdit(inputList,groupNamePref,firstIndex,name,btnClassDelete,placeholder,spanIconDelete,placeholder2,name2){

    console.log(inputList);
    if(inputList != null && inputList.length != 0){
        inputList.forEach(function (currentValue, index, arr) {
            var text1 = "<div id='"+groupNamePref+index+"' style=\"padding-top: 10px\">";
            var text2 = "<div class=\"col-xs-6\" style=\"padding-left: 0px;padding-right: 0px\">";
            var text3 = '<input type="text" class="form-control" placeholder="'+placeholder+'" name="'+name+'" id="'+name+index+'" value="'+currentValue.unitName+'">';
            var text4 = "</div>";
            var text5 = "<div class=\"col-xs-6\" style=\"padding-left: 0px;padding-right: 0px\">";
            var text6 = '<div class="input-group input-word-two">';
            var text7 = '<input type="text" class="form-control" placeholder="'+placeholder+'" name="'+name2+'" id="'+name2+index+'" value="'+currentValue.layerNumber+'">';
            var text8 = '<span class="input-group-btn">';
            var text9 = '<button class="btn '+btnClassDelete+'" type="button" onclick="deleteInput(\''+groupNamePref+'\',\''+index+'\')">';
            var text10 = '<span class="'+spanIconDelete+'"></span>';
            var text11 = '</button>';
            var text12 = '</span>';
            var text13 = '</div>';
            var text14 = '</div>';

            if(index != 0){
                var mIndex = parseInt(firstIndex);
                var addIndex = mapObj.keyName.indexOf(groupNamePref+"add");
                var lengthIndex = mapObj.keyName.indexOf(groupNamePref+"length");
                //添加索引记录和增加了多少的长度
                if(addIndex === -1){
                    mapObj.keyName.push(groupNamePref+"add");
                    mapObj.valueNumber.push((mIndex+1));
                    mapObj.keyName.push(groupNamePref+"length");
                    mapObj.valueNumber.push(0);
                }else {
                    var addNumber = mapObj.valueNumber[addIndex];
                    var lengthNumber = mapObj.valueNumber[lengthIndex];
                    mapObj.valueNumber.splice(addIndex,1,(addNumber+1));
                    mapObj.valueNumber.splice(lengthIndex,1,(lengthNumber+1));
                }
                $("#"+groupNamePref+(index-1)).after(text1+text2+text3+text4+text5+text6+text7+text8+text9+text10+text11+text12+text13+text14);
            }
        });
    }
}
