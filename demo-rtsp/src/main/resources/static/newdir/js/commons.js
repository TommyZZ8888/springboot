//获得项目名称
function getPath() {
    var pathName = window.document.location.pathname;
    var projectName = pathName
        .substring(0, pathName.substr(1).indexOf('/') + 1);
    return projectName;
}

function getWsPath(){
    return window.document.location.host;
}

// 动态给Date对象添加新的方法，得到时间yyyy-mm-dd hh:mm:ss
Date.prototype.formatDateTime = function () {
    var y = this.getFullYear();
    var m = this.getMonth() + 1;
    var d = this.getDate();
    var hh = this.getHours();
    var mm = this.getMinutes();
    var ss = this.getSeconds();
    return y + "-" + formatNumber(m) + "-" + formatNumber(d) + " "
        + formatNumber(hh) + ":" + formatNumber(mm) + ":"
        + formatNumber(ss);
}

// 动态给Date对象添加新的方法,得到yyyy-mm-dd
Date.prototype.formatDate = function () {
    var y = this.getFullYear();
    var m = this.getMonth() + 1;
    var d = this.getDate();
    return y + "-" + formatNumber(m) + "-" + formatNumber(d);
}

// 位数不够，就使用0补位
function formatNumber(value) {
    if (value < 10) {
        value = '0' + value;
    }
    return value;
}

/**
 * 字符串工具类
 * @constructor
 */
var StringUtil = function () {
    /**
     * 判断字符串是否为空
     * @param string 传入字符串
     * @returns {boolean} 返回boolean类型，true表示为空值
     */
    this.isEmptyForString = function (string) {
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
    this.isEmptyForObject = function (object) {
        if (object === undefined || null === object) {
            return true;
        }
        return false;
    };
}


// 拿到表单中的数据，返回类对象
function getFormData(id) {
    var d = {};
    var t = $('#' + id).serializeArray();
    $.each(t, function () {
        d[this.name] = this.value;
    });
    return d;
}

//拿到表单中的数据，返回表单对象
function getFormDateReturnForm(id) {
    var form = new FormData();
    var t = $('#' + id).serializeArray();
    $.each(t, function () {
        form.append(this.name, this.value);
    });
    return form;
}

//重置表单,刷新表格数据
function formReset(formName) {
    if ($('select').hasClass('select-m')) {
        $(".select-m").each(function () {
            $(this).val("").select2();
        })
    }
    document.getElementById(formName).reset();
    $.table.search(formName);
}

/**
 * 重写编辑方法，弹出编辑层
 * @param idName
 */
function editTab(idName) {
    var rows = $.table.selectColumns(idName);
    console.log(rows);
    if (rows.length == 0) {
        $.modal.alertWarning("请选择一条记录");
        return;
    } else if (rows.length > 1) {
        $.modal.alertWarning("只能选择一条记录");
        return;
    }
    var url = $.table._option.updateUrl.replace("{id}", rows[0]);
    $.modal.open("编辑" + $.table._option.modalName, url);
}

/**
 * 通过出生日期得到年龄
 * @param str 日期字符串，格式yyyy-MM-dd
 * @returns {number} 年龄数字
 */
function getAge2Birth(str) {
    var strBirthday = str.split(" ");
    var returnAge;
    var strBirthdayArr = strBirthday[0].split("-");
    var birthYear = strBirthdayArr[0];
    var birthMonth = strBirthdayArr[1];
    var birthDay = strBirthdayArr[2];

    d = new Date();
    var nowYear = d.getFullYear();
    var nowMonth = d.getMonth() + 1;
    var nowDay = d.getDate();

    if (nowYear == birthYear) {
        returnAge = 0;// 同年 则为0岁
    } else {
        var ageDiff = nowYear - birthYear; // 年之差
        if (ageDiff > 0) {
            if (nowMonth == birthMonth) {
                var dayDiff = nowDay - birthDay;// 日之差
                if (dayDiff < 0) {
                    returnAge = ageDiff - 1;
                } else {
                    returnAge = ageDiff;
                }
            } else {
                var monthDiff = nowMonth - birthMonth;// 月之差
                if (monthDiff < 0) {
                    returnAge = ageDiff - 1;
                } else {
                    returnAge = ageDiff;
                }
            }
        } else {
            returnAge = -1;// 返回-1 表示出生日期输入错误 晚于今天
        }
    }
    return returnAge;// 返回周岁年龄
}


/**
 * 将 yyyy-MM-dd hh:mm:ss 格式 或yyyy-MM-dd格式的字符串日期转换成Date类型
 * @param dateStr 日期字符串 yyyy-MM-dd hh:mm:ss 格式 或yyyy-MM-dd格式
 * @returns {Date} Date类型
 */
function getDate(dateStr) {
    var str = dateStr.split(" ");
    var strArr = str[0].split("-");
    var Year = strArr[0];
    var Month = strArr[1];
    var Day = strArr[2];
    return new Date(Year, Month, Day);
}

/**
 * 得到今天的日期
 * @returns {string} 今日日期字符串
 */
function getNowDate() {
    d = new Date();
    var nowYear = d.getFullYear();
    var nowMonth = d.getMonth() + 1;
    var nowDay = d.getDate();
    return nowYear + "-" + formatNumber(nowMonth) + "-" + formatNumber(nowDay);
}

/**
 * 得到昨天的日期
 * @returns {string} 昨天天气字符串
 */
function getYesterday() {
    var day1 = new Date();
    day1.setDate(day1.getDate() - 1);
    var nowYear = day1.getFullYear();
    var nowMonth = day1.getMonth() + 1;
    var nowDay = day1.getDate();
    return nowYear + "-" + formatNumber(nowMonth) + "-" + formatNumber(nowDay);
}

/**
 * 获取指定范围区间随机数（min<=取值<=max）
 * @param min 最小值
 * @param max 最大值
 * @returns {*} 随机数
 */
function getRandomNum(min, max) {
    var Range = max - min;
    var Rand = Math.random();
    return (min + Math.round(Rand * Range));
}

/**
 * 数组工具类
 */
var ArrayUtil = function () {
    /**
     * 判断对象是否是一个数组
     * @param value 对象
     * @returns {arg is Array<any>|boolean} boolean类型
     */
    this.isArray = function (value) {
        if (typeof Array.isArray === "function") {
            return Array.isArray(value);
        } else {
            return Object.prototype.toString.call(value) === "[object Array]";
        }
    }
    /**
     * 数组去重
     * @param arr
     * @returns {any[]}
     */
    this.removeArraySameElement = function (arr) {
        var x = new Set(arr);
        return [...x];
    };
    /**
     * 移除数组中空元素
     * @param array
     * @returns {Array}
     */
    this.removeEmptyElement = function (array) {
        let stringUtil = new StringUtil();
        var ar = [];
        if (!stringUtil.isEmptyForObject(array) && array.length > 0) {
            for (var i = 0; i < array.length; i++) {
                if (null != array[i] && array[i] != "" && array[i] != " ") {
                    ar.push(array[i]);
                }
            }
        }
        return ar;
    };
    /**
     * 移除数组元素左右的空白
     * @param array
     * @returns {Array}
     */
    this.removeElementLeftAndRightSpace = function (array) {
        let stringUtil = new StringUtil();
        var ar = [];
        if (!stringUtil.isEmptyForObject(array) && array.length > 0) {
            for (var i = 0; i < array.length; i++) {
                ar.push(array[i].trim());
            }
        }
        return ar;
    };
    /**
     * 移除数组指定元素
     * @param array 传入数组
     * @param element 需要移除的数组元素
     * @returns {Array}
     */
    this.removeArrayElement = function (array, element) {
        let stringUtil = new StringUtil();
        var arrayCopy = [];
        if (!stringUtil.isEmptyForObject(array) && array.length > 0) {
            for (var i = 0; i < array.length; i++) {
                if (array[i] != element) {
                    arrayCopy.push(array[i]);
                }
            }
        }
        return arrayCopy;
    };
    /**
     * 判断数组中元素是否相同
     * @param array1 数组1
     * @param array2 数组2
     * @returns {boolean} boolean类型
     */
    this.arraySame = function (array1, array2) {
        let stringUtil = new StringUtil();
        if (!stringUtil.isEmptyForObject(array1) && array1.length > 0 && !stringUtil.isEmptyForObject(array2) && array2.length > 0) {
            if (array1.length != array2.length) {
                return false;
            }
            var sign = 0;
            for (var i = 0; i < array1.length; i++) {
                for (var j = 0; j < array2.length; j++) {
                    if (array1[i] === array2[j]) {
                        sign++;
                        break;
                    }
                }
            }
            if (sign === array1.length) {
                return true;
            }
        }
        return false;
    };
    /**
     * 从数组中1中删除数组2包含的元素
     * @param array1 数组1
     * @param array2 数组2
     * @returns {Array} 新数组
     */
    this.removeSameArray = function (array1, array2) {
        let stringUtil = new StringUtil();
        var array = array1;
        if (!stringUtil.isEmptyForObject(array2) && array2.length > 0) {
            for (let i = 0; i < array2.length; i++) {
                array = this.removeArrayElement(array, array2[i]);
            }
        }
        return array;
    };
    /**
     * 判断数组中是否包含某个元素
     * @param array 数组
     * @param element 元素
     * @returns {boolean} boolean类型
     */
    this.arrayIncludeElement = function (array, element) {
        let stringUtil = new StringUtil();
        if (stringUtil.isEmptyForObject(array)) {
            return false;
        }
        var bool = false;
        for (var i = 0; i < array.length; i++) {
            if (array[i] === element) {
                bool = true;
                break;
            }
        }
        return bool;
    };

    /**
     * 将数组转化成为指定字符隔开的字符串
     * @param array 数组
     * @param charact 分隔符
     * @returns {string} 拼接后的字符串
     */
    this.array2CharSepStr = function (array, charact) {
        let stringUtil = new StringUtil();
        var arrayStr = "";
        if (!stringUtil.isEmptyForObject(array) && array.length > 0) {
            for (var i = 0; i < array.length; i++) {
                if (i === 0) {
                    arrayStr = arrayStr + array[i];
                } else {
                    arrayStr = arrayStr + charact + array[i];
                }
            }
        }
        return arrayStr;
    };
    /**
     * 将字符串转换成数组
     * @param str 指定分割符的字符串
     * @param charact 分隔符
     * @returns {string|*|null|string[]|never}
     */
    this.charSepStr2Array = function (str, charact) {
        let stringUtil = new StringUtil();
        if (!stringUtil.isEmptyForString(str)) {
            return str.split(charact);
        }
        return "";
    };
    /**
     * 将其他数组中的元素添加进当前数组中
     * @param arrayIn 被添加的数组（当前数组）
     * @param arrayOther 要添加的数组（其他数组）
     * @returns {any[]|Array} 数组
     */
    this.addOtherAarryInArray = function (arrayIn, arrayOther) {
        var arrayInCopy = [];
        if (!this.arrayIsEmpty(arrayIn)) {
            arrayInCopy = arrayIn;
        }
        if (!this.arrayIsEmpty(arrayOther)) {
            for (let i = 0; i < arrayOther.length; i++) {
                arrayInCopy.push(arrayOther[i]);
            }
            return this.removeArraySameElement(arrayInCopy);
        }
        return arrayInCopy;
    };
    /**
     * 判断数组是否为空
     * @param array 数组
     * @returns {boolean} boolean类型
     */
    this.arrayIsEmpty = function (array) {
        if (array === undefined || array === null || array.length === 0) {
            return true;
        }
        return false;
    }
};

var timer1;
var timeCount;
function importExcel(form, url) {

    var content = "<div style='width: 100%;padding: 20px'>";
    content = content + "<input type='file' id='file'><br>";
    // content = content + "<span>请在此(或列表页面)导出相应的数据模板</span>&emsp;<a class='btn btn-info btn-xs' onclick='$.table.exportExcel(\""+form+"\")'><i class='glyphicon glyphicon-save'></i>下载模板</a><br><br>";
    content = content + "<span style='color: red'>提示：仅允许导入“xls”或“xlsx”格式文件！</span>";
    content = content + "</div>";
    layer.open({
        type: 1,
        area: ['400px', '230px'],
        fix: false,
        //不固定
        maxmin: true,
        shade: 0.3,
        title: '导入' + $.table._option.modalName + '数据',
        content: content,
        btn: ['<i class="fa fa-check"></i> 导入', '<i class="fa fa-remove"></i> 取消'],
        // 弹层外区域关闭
        shadeClose: true,
        btn1: function (index, layero) {
            var file = layero.find('#file').val();
            if (file == '' || (!endWith(file, '.xls') && !endWith(file, '.xlsx'))) {
                $.modal.msgWarning("请选择后缀为 “xls”或“xlsx”的文件。");
                return false;
            }
            // var loadIndex = layer.load(2, {shade: false});

            layer.closeAll();
            // 计时操作
            timeCount = 0;
            var loadIndex = layer.open({
                type: 1,
                shade: 0.5,
                closeBtn: 0,
                offset: 'auto',
                moveType: 1,
                anim: 3,
                title: false, //不显示标题
                content: '<div style="padding: 20px 50px;background: #00C1B3;color: white"><span id="hintLoading">数据正在拼命处理中，请耐心等待：' + timeCount + 's</span></div>',
            });

            timer1 = window.setInterval(function () {
                $('#hintLoading').text("数据正在拼命处理中，请耐心等待：" + timeCount + "s");// 显示计时
                timeCount = timeCount + 1;
            }, 1000);

            disable();
            var formData = new FormData();
            formData.append("file", $('#file')[0].files[0]);
            $.ajax({
                url: ctx + url,
                data: formData,
                cache: false,
                contentType: false,
                processData: false,
                type: 'POST',
                success: function (result) {
                    window.clearInterval(timer1);// 停止倒计时
                    if (result.code == web_status.SUCCESS) {
                        layer.closeAll();
                        msgInfo(web_status.SUCCESS, result.msg);
                        $.table.refresh();
                    } else if (result.code == web_status.WARNING) {
                        layer.close(loadIndex);
                        layer.close(index);
                        enable();
                        $.table.refresh();
                        msgInfo(web_status.WARNING, result.msg);
                    } else {
                        layer.close(loadIndex);
                        layer.close(index);
                        enable();
                        msgInfo(web_status.FAIL, result.msg);
                    }
                }
            });
        }
    });
}

// 计时操作
var timeloading = 0;
//导出报表操作
var exportExcel = function (form, type) {
    if (timeloading != 0) {
        layer.msg("正在进行数据打包操作，请稍后再进行操作");
        return;
    }
    timer1 = window.setInterval(function () {
        $('#timed-reading').text("温馨提示：数据正在加载打包中......" + timeloading + "s").css("display", "inline-block");// 显示计时
        timeloading = timeloading + 1;
    }, 1000);

    let formData = getFormData(form);
    formData['exportType'] = type;
    $.post($.table._option.exportUrl, formData, function (result) {
        window.clearInterval(timer1);// 停止倒计时
        timeloading = 0;//重置倒计时
        $('#timed-reading').css("display", "none");
        if (result.code == web_status.SUCCESS) {
            window.location.href = ctx + "common/download?fileName=" + result.msg + "&delete=" + true;
        } else {
            $.modal.alertError(result.msg);
        }
    });
};

function pdfExport(url) {
    var rowsId = $.common.isEmpty($.table._option.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns($.table._option.uniqueId);
    if (rowsId.length == 0) {
        $.modal.alertWarning("请至少选择一条记录");
        return;
    }
    var arrayUtil = new ArrayUtil();
    var ids = arrayUtil.array2CharSepStr(rowsId,",");
    var data = {ids:ids};
    $.modal.loading("正在导出数据，请稍后...");
    $.post(ctx+url, data, function (result) {
        if (result.code == web_status.SUCCESS) {
            window.location.href = ctx + "common/download?fileName=" + encodeURI(result.msg) + "&delete=" + true;
        } else if (result.code == web_status.WARNING) {
            $.modal.alertWarning(result.msg)
        } else {
            $.modal.alertError(result.msg);
        }
        $.modal.closeLoading();
    });
}

function msgInfo(code, msg) {
    var title = "<span>系统提示</span>";
    if (code === web_status.WARNING) {
        title = title + "<i style='color: #f0ad4e' class='glyphicon glyphicon-exclamation-sign'></i>";
    } else if (code === web_status.SUCCESS) {
        title = title + "<i style='color: #1ac5a5' class='glyphicon glyphicon-ok-sign'></i>";
    } else if (code === web_status.FAIL) {
        title = title + "<i style='color: #ff1522' class='glyphicon glyphicon-remove-sign'></i>";
    }
    layer.open({
        type: 1
        ,
        title: title //不显示标题栏
        ,
        closeBtn: 2
        ,
        area: ['400px', '400px']
        ,
        shade: 0.2
        ,
        btn: ['知道了']
        ,
        btnAlign: 'c'
        ,
        maxmin: true
        ,
        resize: true
        ,
        moveType: 1 //拖拽模式，0或者1
        ,
        content: '<div style="width:100%;padding: 20px; line-height: 22px;font-weight: 300;">' + msg + '</div>'
        ,
        success: function (layero) {

        }
    });
}

// 禁用按钮
var disable = function () {
    var doc = window.top == window.parent ? window.document : window.parent.document;
    $("a[class*=layui-layer-btn]", doc).addClass("layer-disabled");
}
// 启用按钮
var enable = function () {
    var doc = window.top == window.parent ? window.document : window.parent.document;
    $("a[class*=layui-layer-btn]", doc).removeClass("layer-disabled");
};

var startWith = function (value, start) {
    var reg = new RegExp("^" + start);
    return reg.test(value)
}
// 判断字符串是否是以end结尾
var endWith = function (value, end) {
    var reg = new RegExp(end + "$");
    return reg.test(value)
}


/**
 * 针对Ext的工具类(日期工具类)
 */
var DateUtil = function () {
    /***
     * 获得当前时间
     */
    this.getCurrentDate = function () {
        return new Date();
    };
    /***
     * 获得本周起止时间
     */
    this.getCurrentWeek = function () {
        //起止日期数组
        var startStop = new Array();
        //获取当前时间
        var currentDate = this.getCurrentDate();
        //返回date是一周中的某一天
        var week = currentDate.getDay();
        //返回date是一个月中的某一天
        var month = currentDate.getDate();

        //一天的毫秒数
        var millisecond = 1000 * 60 * 60 * 24;
        //减去的天数
        var minusDay = week != 0 ? week - 1 : 6;
        //alert(minusDay);
        //本周 周一
        var monday = new Date(currentDate.getTime() - (minusDay * millisecond));
        //本周 周日
        var sunday = new Date(monday.getTime() + (6 * millisecond));
        //添加本周时间
        startStop.push(monday);//本周起始时间
        //添加本周最后一天时间
        startStop.push(sunday);//本周终止时间
        //返回
        return startStop;
    };

    /***
     * 获得本月的起止时间
     */
    this.getCurrentMonth = function () {
        //起止日期数组
        var startStop = new Array();
        //获取当前时间
        var currentDate = this.getCurrentDate();
        //获得当前月份0-11
        var currentMonth = currentDate.getMonth();
        //获得当前年份4位年
        var currentYear = currentDate.getFullYear();
        //求出本月第一天
        var firstDay = new Date(currentYear, currentMonth, 1);


        //当为12月的时候年份需要加1
        //月份需要更新为0 也就是下一年的第一个月
        if (currentMonth == 11) {
            currentYear++;
            currentMonth = 0;//就为
        } else {
            //否则只是月份增加,以便求的下一月的第一天
            currentMonth++;
        }


        //一天的毫秒数
        var millisecond = 1000 * 60 * 60 * 24;
        //下月的第一天
        var nextMonthDayOne = new Date(currentYear, currentMonth, 1);
        //求出上月的最后一天
        var lastDay = new Date(nextMonthDayOne.getTime() - millisecond);

        //添加至数组中返回
        startStop.push(firstDay);
        startStop.push(lastDay);
        //返回
        return startStop;
    };

    /**
     * 得到本季度开始的月份
     * @param month 需要计算的月份
     ***/
    this.getQuarterSeasonStartMonth = function (month) {
        var quarterMonthStart = 0;
        var spring = 0; //春
        var summer = 3; //夏
        var fall = 6;   //秋
        var winter = 9;//冬
        //月份从0-11
        if (month < 3) {
            return spring;
        }

        if (month < 6) {
            return summer;
        }

        if (month < 9) {
            return fall;
        }

        return winter;
    };

    /**
     * 获得该月的天数
     * @param year年份
     * @param month月份
     * */
    this.getMonthDays = function (year, month) {
        //本月第一天 1-31
        var relativeDate = new Date(year, month, 1);
        //获得当前月份0-11
        var relativeMonth = relativeDate.getMonth();
        //获得当前年份4位年
        var relativeYear = relativeDate.getFullYear();

        //当为12月的时候年份需要加1
        //月份需要更新为0 也就是下一年的第一个月
        if (relativeMonth == 11) {
            relativeYear++;
            relativeMonth = 0;
        } else {
            //否则只是月份增加,以便求的下一月的第一天
            relativeMonth++;
        }
        //一天的毫秒数
        var millisecond = 1000 * 60 * 60 * 24;
        //下月的第一天
        var nextMonthDayOne = new Date(relativeYear, relativeMonth, 1);
        //返回得到上月的最后一天,也就是本月总天数
        return new Date(nextMonthDayOne.getTime() - millisecond).getDate();
    };

    /**
     * 获得本季度的起止日期
     */
    this.getCurrentSeason = function () {
        //起止日期数组
        var startStop = new Array();
        //获取当前时间
        var currentDate = this.getCurrentDate();
        //获得当前月份0-11
        var currentMonth = currentDate.getMonth();
        //获得当前年份4位年
        var currentYear = currentDate.getFullYear();
        //获得本季度开始月份
        var quarterSeasonStartMonth = this.getQuarterSeasonStartMonth(currentMonth);
        //获得本季度结束月份
        var quarterSeasonEndMonth = quarterSeasonStartMonth + 2;

        //获得本季度开始的日期
        var quarterSeasonStartDate = new Date(currentYear, quarterSeasonStartMonth, 1);
        //获得本季度结束的日期
        var quarterSeasonEndDate = new Date(currentYear, quarterSeasonEndMonth, this.getMonthDays(currentYear, quarterSeasonEndMonth));
        //加入数组返回
        startStop.push(quarterSeasonStartDate);
        startStop.push(quarterSeasonEndDate);
        //返回
        return startStop;
    };

    /***
     * 得到本年的起止日期
     *
     */
    this.getCurrentYear = function () {
        //起止日期数组
        var startStop = new Array();
        //获取当前时间
        var currentDate = this.getCurrentDate();
        //获得当前年份4位年
        var currentYear = currentDate.getFullYear();

        //本年第一天
        var currentYearFirstDate = new Date(currentYear, 0, 1);
        //本年最后一天
        var currentYearLastDate = new Date(currentYear, 11, 31);
        //添加至数组
        startStop.push(currentYearFirstDate);
        startStop.push(currentYearLastDate);
        //返回
        return startStop;
    };

    /**
     * 返回上一个月的第一天Date类型
     * @param year 年
     * @param month 月
     **/
    this.getPriorMonthFirstDay = function (year, month) {
        //年份为0代表,是本年的第一月,所以不能减
        if (month == 0) {
            month = 11;//月份为上年的最后月份
            year--;//年份减1
            return new Date(year, month, 1);
        }
        //否则,只减去月份
        month--;
        return new Date(year, month, 1);
        ;
    };

    /**
     * 获得上一月的起止日期
     * ***/
    this.getPreviousMonth = function () {
        //起止日期数组
        var startStop = new Array();
        //获取当前时间
        var currentDate = this.getCurrentDate();
        //获得当前月份0-11
        var currentMonth = currentDate.getMonth();
        //获得当前年份4位年
        var currentYear = currentDate.getFullYear();
        //获得上一个月的第一天
        var priorMonthFirstDay = this.getPriorMonthFirstDay(currentYear, currentMonth);
        //获得上一月的最后一天
        var priorMonthLastDay = new Date(priorMonthFirstDay.getFullYear(), priorMonthFirstDay.getMonth(), this.getMonthDays(priorMonthFirstDay.getFullYear(), priorMonthFirstDay.getMonth()));
        //添加至数组
        startStop.push(priorMonthFirstDay);
        startStop.push(priorMonthLastDay);
        //返回
        return startStop;
    };

    //获得上月的月份
    this.getLastMoth = function () {
        var lastMonthStartAndEnd = this.getPreviousMonth();
        var lastMonthStart = lastMonthStartAndEnd[0];
        var nowYear = lastMonthStart.getFullYear();
        var lastMonth = lastMonthStart.getMonth();
        var lastDay = lastMonthStart.getDay();
        console.log(lastMonth);
        return nowYear + "-" + (lastMonth + 1) + "-" + lastDay;
    };

    //获得本月的月份
    this.getNowMoth = function () {
        var nowMonthStartAndEnd = this.getCurrentMonth();
        var nowMonthStart = nowMonthStartAndEnd[0];
        var nowYear = nowMonthStart.getFullYear();
        var nowMonth = nowMonthStart.getMonth();
        var nowDay = nowMonthStart.getDay();
        return nowYear + "-" + (nowMonth + 1) + "-" + nowDay;
    };

    /**
     * 获得上一周的起止日期
     * **/
    this.getPreviousWeek = function () {
        //起止日期数组
        var startStop = new Array();
        //获取当前时间
        var currentDate = this.getCurrentDate();
        //返回date是一周中的某一天
        var week = currentDate.getDay();
        //返回date是一个月中的某一天
        var month = currentDate.getDate();
        //一天的毫秒数
        var millisecond = 1000 * 60 * 60 * 24;
        //减去的天数
        var minusDay = week != 0 ? week - 1 : 6;
        //获得当前周的第一天
        var currentWeekDayOne = new Date(currentDate.getTime() - (millisecond * minusDay));
        //上周最后一天即本周开始的前一天
        var priorWeekLastDay = new Date(currentWeekDayOne.getTime() - millisecond);
        //上周的第一天
        var priorWeekFirstDay = new Date(priorWeekLastDay.getTime() - (millisecond * 6));

        //添加至数组
        startStop.push(priorWeekFirstDay);
        startStop.push(priorWeekLastDay);

        return startStop;
    };

    /**
     * 得到上季度的起始日期
     * year 这个年应该是运算后得到的当前本季度的年份
     * month 这个应该是运算后得到的当前季度的开始月份
     * */
    this.getPriorSeasonFirstDay = function (year, month) {
        var quarterMonthStart = 0;
        var spring = 0; //春
        var summer = 3; //夏
        var fall = 6;   //秋
        var winter = 9;//冬
        //月份从0-11
        switch (month) {//季度的其实月份
            case spring:
                //如果是第一季度则应该到去年的冬季
                year--;
                month = winter;
                break;
            case summer:
                month = spring;
                break;
            case fall:
                month = summer;
                break;
            case winter:
                month = fall;
                break;

        }
        ;

        return new Date(year, month, 1);
    };

    /**
     * 得到上季度的起止日期
     * **/
    this.getPreviousSeason = function () {
        //起止日期数组
        var startStop = new Array();
        //获取当前时间
        var currentDate = this.getCurrentDate();
        //获得当前月份0-11
        var currentMonth = currentDate.getMonth();
        //获得当前年份4位年
        var currentYear = currentDate.getFullYear();
        //上季度的第一天
        var priorSeasonFirstDay = this.getPriorSeasonFirstDay(currentYear, currentMonth);
        //上季度的最后一天
        var priorSeasonLastDay = new Date(priorSeasonFirstDay.getFullYear(), priorSeasonFirstDay.getMonth() + 2, this.getMonthDays(priorSeasonFirstDay.getFullYear(), priorSeasonFirstDay.getMonth() + 2));
        //添加至数组
        startStop.push(priorSeasonFirstDay);
        startStop.push(priorSeasonLastDay);
        return startStop;
    };

    /**
     * 得到去年的起止日期
     * **/
    this.getPreviousYear = function () {
        //起止日期数组
        var startStop = new Array();
        //获取当前时间
        var currentDate = this.getCurrentDate();
        //获得当前年份4位年
        var currentYear = currentDate.getFullYear();
        currentYear--;
        var priorYearFirstDay = new Date(currentYear, 0, 1);
        var priorYearLastDay = new Date(currentYear, 11, 1);
        //添加至数组
        startStop.push(priorYearFirstDay);
        startStop.push(priorYearLastDay);
        return startStop;
    };
};


/**
 * 隐藏银行卡中间数字方法
 * @param cardNumber
 * @returns {*|string}
 */
function cardSubstring(cardNumber) {
    let stringUtil = new StringUtil();
    var text = cardNumber;
    if (!stringUtil.isEmptyForString(cardNumber) && cardNumber.length >= 8) {
        text = cardNumber.substring(0, 4);
        text = text + "***********";
        text = text + cardNumber.substring(cardNumber.length - 4, cardNumber.length);
    }
    return text;
}


/*--------------------------------------------------------将操作栏浮动到最顶层固定-------------------------------------------------------------*/
function tableOpt(){
    $("th").each(function (index,element) {
        if($(this).attr("data-field") == 'optStyle'){
            $(this).css("position","absolute").css("right","0").css("background","#eff3f8");
            $(this).find(".th-inner").css("background","#eff3f8").css("border-left","1px solid #c9cdd2");
        }
    })
    $("td").each(function (index,element) {
        if($(this).children().is(".optStyle")){
            $(this).css("position","absolute").css("right","0").css("background","rgba(228,231,233,0.93)").css("padding","0px");
            $(this).find(".optStyle").css("background","rgba(246,250,255,0.51)").css("border-left","1px solid #c9cdd2");
        }
    })
}


/*----------------------------------------------表格隐藏多余吸附层--------------------------------------------------------*/
var hideInfoTipsMap = new Map();
var hideInfoTipsIndex = 0;
function hideInfoTips(value,color,type) {
    var stringUtil = new StringUtil();
    if(stringUtil.isEmptyForString(value)){
        return "-";
    }
    var id= "hideInfoTipsIndex_"+hideInfoTipsIndex;
    hideInfoTipsMap.set(id,value);
    hideInfoTipsIndex++;
    return '<span onmouseover="OverHintHideInfoTips(this,\''+id+"\',\'"+color+"\',"+type+')" onmouseout="OutHintHideInfoTips()">'+value+'</span>'
}
var tip_indexHide;
function OverHintHideInfoTips(obj,id,color,type) {
    var msg = hideInfoTipsMap.get(id);
    var stringUtil = new StringUtil();
    var colorBack = '#00C9BA';
    var typeBack = 1;
    if(!stringUtil.isEmptyForString(color) && color != 'undefined'){
        colorBack = color;
    }
    if(!stringUtil.isEmptyForObject(type)){
        typeBack = type;
    }
    var that = obj;
    tip_indexHide = layer.tips(msg, that, {
        tips: [typeBack, colorBack],
        time: 300000
    });
}
function OutHintHideInfoTips(obj) {
    layer.close(tip_indexHide);
}

/*--------------------------------------------超长内容弹出层---------------------------------------------------*/
var backOpenLayerIndex=0;
var backOpenLayerMap = new Map();
function backOpenLayer(value) {
    var stringUtil = new StringUtil();
    if (stringUtil.isEmptyForString(value)) {
        return "-";
    }
    var id = "backOpenLayer_"+backOpenLayerIndex;
    backOpenLayerMap.set(id,value);
    var text = "<a href='javascript:;' style='color: #1c84c6' onclick='backOpenLayerInfo(\"" + id + "\")'>" + value + "</a>";
    backOpenLayerIndex++;
    return text;
}
function backOpenLayerInfo(id) {
    var text = backOpenLayerMap.get(id);
    textInfo(text);
}

/*信息弹出层*/
function textInfo(text) {
    layer.open({
        type: 1
        ,
        title: false //不显示标题栏
        ,
        closeBtn: false
        ,
        area: '600px;'
        ,
        shade: 0.2
        ,
        id: 'LAY_layuipro' //设定一个id，防止重复弹出
        ,
        btn: ['知道了']
        ,
        btnAlign: 'c'
        ,
        moveType: 1 //拖拽模式，0或者1
        ,
        content: '<div style="padding: 50px; line-height: 22px; background-color: #ffffff; color: #333333; font-weight: 300;word-wrap:break-word;">' + text + '</div>'
        ,
        success: function (layero) {

        }
    });
}

/**=========================================table表格隐藏多余字段==========================================================================*/
var hideTipsIndex = 0;
var uniqueIdKey = 0;
var uniqueIdKeyMap = new Map();

//在bootstap中formatter方法中执行 “return hideTips(value)”
function hideTips(value) {
    var stringUtil = new StringUtil();
    if (stringUtil.isEmptyForString(value)) {
        return "-";
    }
    if (hideTipsIndex == 0) {
        var text = '<div id="showDiv" style="position: absolute; background: white; border: 1px solid black;z-index: 100;display: none">' +
            '<span class="glyphicon glyphicon-remove" onclick="hiedDiv()" style="position: absolute;color: #009f95;right: 0;top: 0;"></span>' +
            '<div id="showDivText" style="margin-top: 1em;margin-left:10px;margin-right10px;margin-bottom:10px;overflow:auto;word-wrap:break-word;"></div>' +
            '</div>';
        $("body").append(text);
        hideTipsIndex++;
    }
    var id = "uniqueIdKey_" + uniqueIdKey;
    var text = '<span id="' + id + '" onclick="hideClick()" onmouseover="overShow(\'' + id + '\')" onmouseout="outHide(\'' + id + '\')">' + value + '</span>';
    uniqueIdKeyMap.set(id, value);
    uniqueIdKey++;
    return text;
}

var movex = 0;
var movey = 0;
var showFlag = false;

function overShow(id) {
    var showDiv = document.getElementById('showDiv');
    var width = $(document.body).width();
    var height = $(document.body).height();
    var showDivText = document.getElementById('showDivText');
    showDivText.innerHTML = '';
    $("#showDivText").css("width", "auto").css("height", "auto");
    showDivText.innerHTML = uniqueIdKeyMap.get(id);
    var divWidth = $("#showDiv").width();
    var divHeight = $("#showDiv").height();
    var changeDivWidth = 0, positionWidth = 0, changeDivHeight = 0, positionHeight = 0;
    if (movex > width / 1.5) {
        changeDivWidth = movex - 40;
        positionWidth = 10;
        changeDivWidth = divWidth > changeDivWidth ? changeDivWidth : divWidth;
        positionWidth = divWidth > changeDivWidth ? positionWidth : movex - changeDivWidth - 30;
    } else {
        changeDivWidth = width - movex - 40;
        positionWidth = movex + 10;
        changeDivWidth = divWidth > changeDivWidth ? changeDivWidth : divWidth;
    }
    if (movey > height / 1.5) {
        changeDivHeight = movey - 40;
        positionHeight = 10;
        changeDivHeight = divHeight > changeDivHeight ? changeDivHeight : divHeight;
        positionHeight = divHeight > changeDivHeight ? positionHeight : movey - changeDivHeight - 30;
    } else {
        changeDivHeight = height - movey - 40;
        positionHeight = movey + 10;
        changeDivHeight = divHeight > changeDivHeight ? changeDivHeight : divHeight;
    }

    $("#showDiv").css("left", positionWidth).css("top", positionHeight);
    $("#showDivText").css("width", changeDivWidth).css("height", changeDivHeight);
    showDiv.style.display = 'block';
}

function outHide(id) {
    if (showFlag == false) {
        var showDiv = document.getElementById('showDiv');
        showDiv.style.display = 'none';
        var showDivText = document.getElementById('showDivText');
        showDivText.innerHTML = '';
        $("#showDivText").css("width", "auto").css("height", "auto");
    }
}

function hideClick() {
    showFlag = true;
}

function hiedDiv() {
    showFlag = false;
    outHide();
}

$(function () {
    $(document).mousemove(function (e) {
        movex = e.pageX;
        movey = e.pageY;
    })
});
/*----------------------------------------------------table表格隐藏多余字段2---------------------------------------------------------*/

var hideTipsIndex2 = 0;
var uniqueIdKey2 = 0;
var uniqueIdKey2Map = new Map();

//在bootstap中formatter方法中执行 “return hideTips(value)”
function hideTips2(value) {
    var stringUtil = new StringUtil();
    if (stringUtil.isEmptyForString(value)) {
        return "-";
    }
    if (hideTipsIndex2 == 0) {
        var text = '<div id="showDiv2" style="position: absolute; background: white; border: 1px solid black;z-index: 100;display: none">' +
            '<div id="showDiv2Text" style="margin-left:8px;margin-top:3px;overflow:auto;word-wrap:break-word;"></div>' +
            '</div>';
        $("body").append(text);
        hideTipsIndex2++;
    }
    var id = "uniqueIdKey2_" + uniqueIdKey2;
    var text = '<span id="' + id + '" onmouseover="overShow2(\'' + id + '\')" onmouseout="outHide2(\'' + id + '\')">' + value + '</span>';
    uniqueIdKey2Map.set(id, value);
    uniqueIdKey2++;
    return text;
}

function overShow2(id) {
    var showDiv2 = document.getElementById('showDiv2');
    var width = $(document.body).width();
    var height = $(document.body).height();
    var showDiv2Text = document.getElementById('showDiv2Text');
    showDiv2Text.innerHTML = '';
    $("#showDiv2Text").css("width", "auto").css("height", "auto");
    showDiv2Text.innerHTML = uniqueIdKey2Map.get(id);
    var divWidth = $("#showDiv2").width();
    var divHeight = $("#showDiv2").height();
    var changeDivWidth = 0, positionWidth = 0, changeDivHeight = 0, positionHeight = 0;
    if (movex > width / 1.5) {
        changeDivWidth = movex - 40;
        positionWidth = 10;
        changeDivWidth = divWidth > changeDivWidth ? changeDivWidth : divWidth;
        positionWidth = divWidth > changeDivWidth ? positionWidth : movex - changeDivWidth - 30;
    } else {
        changeDivWidth = width - movex - 40;
        positionWidth = movex + 10;
        changeDivWidth = divWidth > changeDivWidth ? changeDivWidth : divWidth;
    }
    if (movey > height / 1.5) {
        changeDivHeight = movey - 40;
        positionHeight = 10;
        changeDivHeight = divHeight > changeDivHeight ? changeDivHeight : divHeight;
        positionHeight = divHeight > changeDivHeight ? positionHeight : movey - changeDivHeight - 30;
    } else {
        changeDivHeight = height - movey - 40;
        positionHeight = movey + 10;
        changeDivHeight = divHeight > changeDivHeight ? changeDivHeight : divHeight;
    }

    $("#showDiv2").css("left", positionWidth).css("top", positionHeight);
    $("#showDiv2Text").css("width", changeDivWidth).css("height", changeDivHeight);
    showDiv2.style.display = 'block';
}

function outHide2(id) {
    var showDiv2 = document.getElementById('showDiv2');
    showDiv2.style.display = 'none';
    var showDiv2Text = document.getElementById('showDiv2Text');
    showDiv2Text.innerHTML = '';
    $("#showDiv2Text").css("width", "auto").css("height", "auto");
}

/*-------------------------------------------------------图片提示层----------------------------------------------------------------*/
var pictureShowIndex = 0;
function pictureHideInfo(pictures) {
    var arrayUtil = new ArrayUtil();
    if(!arrayUtil.arrayIsEmpty(pictures)){
        var pictureId = "pcture_"+pictureShowIndex;
        pictureShowIndex ++ ;
        var text = "<div id='"+pictureId+"' class='layer-photos-demo' style='display: inline-block'>";
        pictures.forEach(function (value,index,attr) {
            text = text + "<img class='img-thumbnail' src='"+value+"' layer-src='"+value+"' style='height: 40px;width: 40px' onclick='pictureHideShow(\""+pictureId+"\")'/>"
        });
        text = text + "</div>";
        return text;
    }
    return "-";
}

function pictureHideShow(id) {
    layer.photos({
        photos: '#'+id
        ,anim: 5 //0-6的选择，指定弹出图片动画类型，默认随机（请注意，3.0之前的版本用shift参数）
    });
}

//避免上面四个弹出层对应map装填数据量大，进而在每次加载是清空数据的方法，只在bootstrap table 中第一个调用以上四个提示层之一的回调函数 formatter()函数中 调用此方法
var dataRecording = false;
function tableLoadStart() {
    if(dataRecording == false){
        //吸附层数据
        hideInfoTipsMap = new Map();
        hideInfoTipsIndex = 0;
        //弹出层数据
        backOpenLayerIndex=0;
        backOpenLayerMap = new Map();
        //隐藏显示框带关闭功能
        hideTipsIndex = 0;
        uniqueIdKey = 0;
        uniqueIdKeyMap = new Map();
        //隐藏显示框不带关闭功能
        hideTipsIndex2 = 0;
        uniqueIdKey2 = 0;
        uniqueIdKey2Map = new Map();
        //图片提示层
        pictureShowIndex = 0;
    }
    dataRecording = true;
}
//该方法是在表格中调用了以上四个提示层任意一个的情况下，必须在botstrap table 的 onLoadSuccess() 方法回调中调用此方法
function tableLoadEnd() {
    dataRecording = false;
}

/*------------------------------------------------信息提示层--------------------------------------------------------*/
var tip_index_info_prompt;
function OverHintInfoPrompt(obj,msg,type,color) {
    var stringUtil = new StringUtil();
    var colorBack = '#00C9BA';
    var typeBack = 1;
    if(!stringUtil.isEmptyForString(color) && color != 'undefined'){
        colorBack = color;
    }
    if(!stringUtil.isEmptyForObject(type)){
        typeBack = type;
    }
    var that = obj;
    tip_index_info_prompt = layer.tips(msg, that, {
        tips: [typeBack, colorBack],
        time: 20000
    });
}
function OutHintInfoPrompt(obj) {
    layer.close(tip_index_info_prompt);
}

