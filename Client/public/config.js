if (!window['LEGEND_CONFIG']) {
    window['LEGEND_CONFIG'] = {
        GM_ADDR: 'http://tieulong.namdorado.com'
    }
}

window.LEGEND_VERSION = '202309-01';
var url = window.location.search; //获取url中"?"符后的字串
var theRequest = {};
if (url.indexOf("?") != -1) {
    var strs = url.substr(1).split("&");
    for (var i = 0; i < strs.length; i++) {
        var param = strs[i].split("=");
        theRequest[param[0]] = decodeURIComponent(param[1]);
    }
}
/**
 * window["TDGA"] && window["TDGA"].Account({
    accountId: theRequest.open_id,
    level: 1,
    gameServer: 'open_id',
});
**/
/**
 * {
 * "renderMode":, //引擎渲染模式，"canvas" 或者 "webgl"
 * "audioType": "" //使用的音频类型，0:默认，1:qq audio，2:web audio，3:audio
 * "antialias": //WebGL模式下是否开启抗锯齿，true:开启，false:关闭，默认为false
 * }
 **/