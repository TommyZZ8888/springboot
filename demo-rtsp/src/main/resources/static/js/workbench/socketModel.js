;
class WebSock {
    constructor(url) {
        this.url = url
        this.websocket = new WebSocket(`ws://${url}`)
        this.onOpen();
        this.onError();
    }
    // 连接成功
    onOpen() {
        this.websocket.addEventListener('open', (e) => {
            console.log('连接成功')
        })
    }
    // 连接失败
    onError() {
        this.websocket.addEventListener('error', (err) => {
            console.log('连接失败')
        })
    }
    // 响应消息
    onMsg(callback) {
        this.websocket.addEventListener('message', (e) => {
            callback(e)
        })
    }
    // 发送消息
    sendMsg(msg) {
        this.websocket.send(msg)
    }
}


