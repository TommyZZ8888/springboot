;!(function (window) {
    "use strict";
    let Event = {
        wsMesEvent: function (message) {
            console.log(message)
        }
    }, dftOpt = {
        protocol: (window.location.protocol == 'http:') ? 'ws://' : 'wss://'
        , host: window.location.host
        , port: '80'
        , path: ''
        , isReConect: false
        , wsMesEvent: Event.wsMesEvent
    }, Util = {
        arrayLike(arrayLike) {
            Array.from(arrayLike)
        },
        isArray(arr) {
            Array.isArray(arr)
        },
        forEach(array, iterate) {
            let index = -1
                , length = array.length;
            if (typeof iterate != 'function') {
                return array;
            }
            while (++index < length) {
                iterate.call(array, array[index], index);
            }
        },
        isPlainObject(obj) {
            let flag = false;
            if (!obj || typeof obj != 'object') {
                return flag;
            }
            if (obj.constructor.prototype.hasOwnProperty("isPrototypeOf")) {
                flag = true;
            }
            return flag;
        },
        extend(...args) {
            if (args.length <= 0) {
                return
            };
            let target = args[0];
            if (args.length == 1) {
                return args[0]
            };
            this.forEach(args, (arg, i) => {
                if (i != 0) {
                    var keys = Object.keys(arg);
                    this.forEach(keys, (key, i) => {
                        var val = arg[key];
                        if (this.isPlainObject(val) || this.isArray(val)) {
                            var newTarget = this.isArray(val) ? [] : {};
                            target[key] = this.extend(newTarget, val);
                        } else {
                            target[key] = val;
                        }
                    });
                }
            });
            return target;
        }
    }, Ws = function (opt) {
        //如果浏览器不支持websocket,直接退出
        if (!this.isSupportWs()) {
            alert("对不起，您的浏览器在不支持WebSocket，请先升级您的浏览器！！");
            return;
        }
        let config = this.config = Util.extend({}, dftOpt, opt);
        //接口地址url
        this.url = config.host === "" || config.host === "" ?
            config.protocol + config.path:
            config.protocol + config.host + ':' + config.port + config.path;
        //心跳状态  为false时不能执行操作 等待重连
        this.isHeartBeat = false;
        //重连状态  避免不间断的重连操作
        this.isReconnect = config.isReConect;
        //发送的消息
        this.curSendMes = null;
        //响应的信息
        this.message = null;
        //创建webSocket
        this.ws;
        //初始化websocket
        this.initWs = function () {
            //创建WebSocket
            let ws = this.ws = new WebSocket(this.url);
            // ws.binaryType = "arraybuffer";
            //Ws连接函数：服务器连接成功
            ws.onopen = (e) => {
                console.log(`与${this.config.host}:${this.config.port}${this.config.path}连接已建立...`)
                this.isHeartBeat = true;
                //发布事件
                this.send();
            };
            //Ws消息接收函数：服务器向前端推送消息时触发
            ws.onmessage = (e) => {
                //处理各种推送消
                this.message = e.data;
                config.wsMesEvent.apply(this, [e.data]);
            }
            //Ws异常事件：Ws报错后触发
            ws.onerror = (e) => {
                this.isHeartBeat = false;
                this.reConnect();
            }
            //Ws关闭事件：Ws连接关闭后触发
            ws.onclose = (e) => {
                console.log('连接已关闭...');
                alert("websocket连接已关闭，按F5尝试重新刷新页面");
                this.isHeartBeat = false;
                ws = null;
                this.reConnect();
            };
        };
        this.initWs();
    };

    //判断是否支持WebSocket
    Ws.prototype.isSupportWs = function () {
        return (window.WebSocket || window.MozWebSocket) ? true : false;
    }

    //重新连接
    Ws.prototype.reConnect = function () {
        //不需要重新连接，直接返回
        if (!this.isReconnect) return;
        this.isReconnect = true;
        //没连接上 会一直重连，设置延迟避免请求过多
        setTimeout(() => {
            this.initWs()
            this.isReconnect = false;
        }, 5000);
    }

    //发送消息
    Ws.prototype.send = function (content) {
        this.curSendMes = content || this.curSendMes;
        //将字符串转换为byte数组
        // let bytesArr= stringToByte(this.curSendMes);
        // let bytes =new Uint8Array(bytesArr.length) ;
        // for (let i = 0; i < bytes.length; i++) {
        //     bytes[i]=bytesArr[i];
        // }
        if(this.curSendMes == null){
            return;
        }
        if (this.isHeartBeat) {
            // this.ws.send(bytes);
            this.ws.send(this.curSendMes);
        }
    }
    window.Ws = Ws;
})(window);

//将字符串转为 Array byte数组
function stringToByte(str) {
    let bytes = [];
    let len, c;
    len = str.length;
    for(let i = 0; i < len; i++) {
        c = str.charCodeAt(i);
        if(c >= 0x010000 && c <= 0x10FFFF) {
            bytes.push(((c >> 18) & 0x07) | 0xF0);
            bytes.push(((c >> 12) & 0x3F) | 0x80);
            bytes.push(((c >> 6) & 0x3F) | 0x80);
            bytes.push((c & 0x3F) | 0x80);
        } else if(c >= 0x000800 && c <= 0x00FFFF) {
            bytes.push(((c >> 12) & 0x0F) | 0xE0);
            bytes.push(((c >> 6) & 0x3F) | 0x80);
            bytes.push((c & 0x3F) | 0x80);
        } else if(c >= 0x000080 && c <= 0x0007FF) {
            bytes.push(((c >> 6) & 0x1F) | 0xC0);
            bytes.push((c & 0x3F) | 0x80);
        } else {
            bytes.push(c & 0xFF);
        }
    }
    return bytes;
}

//byte数组转字符串
function byteToString(arr) {
    if(typeof arr === 'string') {
        return arr;
    }
    let str = '',
        _arr = arr;
    for(let i = 0; i < _arr.length; i++) {
        let one = _arr[i].toString(2),
            v = one.match(/^1+?(?=0)/);
        if(v && one.length === 8) {
            let bytesLength = v[0].length;
            let store = _arr[i].toString(2).slice(7 - bytesLength);
            for(let st = 1; st < bytesLength; st++) {
                store += _arr[st + i].toString(2).slice(2);
            }
            str += String.fromCharCode(parseInt(store, 2));
            i += bytesLength - 1;
        } else {
            str += String.fromCharCode(_arr[i]);
        }
    }
    return str;
}
/***
 * 使用方式：
 * //建立连接
 * var ws1 = new Ws({
 *        host:'123.207.167.163'
 *        ,port:9010
 *        ,path:'/ajaxchattest'
 *        ,wsMesEvent:function(message){
 *            //将接收到的二进制数据转为字符串
		       var unit8Arr = new Uint8Array(event.data) ;
 *            console.log(message)
 *        }
 *    });
 *    //发送请求
 *    ws1.send("111");
 *
 *    //建立连接
 *    var ws2 = new Ws({
 *        host:'123.207.167.163'
 *        ,port:9010
 *        ,path:'/ajaxchattest'
 *        ,wsMesEvent:function(message){
 *            console.log(message)
 *        }
 *    });
 *    //发送请求
 *    ws2.send("222");
 * */