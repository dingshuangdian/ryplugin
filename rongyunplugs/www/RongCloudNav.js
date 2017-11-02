var exec = require('cordova/exec');

var rongclodejs = function(){};

//初始化融云方法，参数为服务器IP，例：RongCloud.startWithHost('http://192.168.1.230');
rongclodejs.prototype.startWithHost = function(targetP, success, error) {
    exec(success, error, "RongCloudNav", "startWithHost", [targetP]);
};

/**
 与融云服务器建立连接

 @param tokenId 公司服务token
 @param RCtokenId 融云token

 例：
	 RongCloud.connectWithToken({
		tokenId: '12345678',
        RCtokenId: '12345678'
	 });
 */
rongclodejs.prototype.connectWithToken = function(targetP, success, error) {
    exec(success, error, "RongCloudNav", "connectWithToken", [targetP]);
};

/**
 断开与融云服务器连接

 例：
	 RongCloud.disconnect();
 */
rongclodejs.prototype.disconnect = function() {
    exec(null, null, "RongCloudNav", "disconnect", []);
};

/**
 跳到会话列表界面

 例：
	 RongCloud.pushChatListView();
 */
rongclodejs.prototype.pushChatListView = function(targetP, success, error) {
    exec(success, error, "RongCloudNav", "pushChatListView", [targetP]);
};

/**
 跳到会话窗口界面

 @param targetId 目标会话ID
 @param conversationTitle 聊天窗口标题

 例：
	 RongCloud.pushConversionView({
        targetId: 'taskId',
        conversationTitle: 'taskName'
    })
 */

rongclodejs.prototype.pushConversionView = function(targetP, success, error) {
    exec(success, error, "RongCloudNav", "pushConversionView", [targetP]);
};

module.exports = new rongclodejs();