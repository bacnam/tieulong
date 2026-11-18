<!DOCTYPE HTML>
<html>

<head>
    <meta charset="utf-8">
    <title>{{ env('APP_NAME') }}</title>
    <link rel="shortcut icon" href="favicon.png">
    
    <link rel="apple-touch-icon" sizes="57x57" href="http://tieulong.namdorado.com/images/logo.jpg">
    <link rel="apple-touch-icon" sizes="60x60" href="http://tieulong.namdorado.com/images/logo.jpg">
    <link rel="apple-touch-icon" sizes="72x72" href="http://tieulong.namdorado.com/images/logo.jpg">
    <link rel="apple-touch-icon" sizes="76x76" href="http://tieulong.namdorado.com/images/logo.jpg">
    <link rel="apple-touch-icon" sizes="114x114" href="http://tieulong.namdorado.com/images/logo.jpg">
    <link rel="apple-touch-icon" sizes="120x120" href="http://tieulong.namdorado.com/images/logo.jpg">
    <link rel="apple-touch-icon" sizes="144x144" href="http://tieulong.namdorado.com/images/logo.jpg">
    <link rel="icon" type="image/jpg" href="http://tieulong.namdorado.com/images/logo.jpg" sizes="192x192">
    <link rel="icon" type="image/jpg" href="http://tieulong.namdorado.com/images/logo.jpg" sizes="96x96">
    <link rel="icon" type="image/jpg" href="http://tieulong.namdorado.com/images/logo.jpg" sizes="48x48">

    <meta name="viewport"
        content="width=device-width,initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="full-screen" content="true" />
    <!--<meta name="screen-orientation" content="portrait" />-->
    <meta name="screen-orientation" content="landscape" />
    <meta name="x5-fullscreen" content="true" />
    <meta name="360-fullscreen" content="true" />
    <meta name="description" content="{{ env('APP_NAME') }}" />
    <style>
        html,
        body {
            -ms-touch-action: none;
            background: #000000;
            padding: 0;
            border: 0;
            margin: 0;
            height: 100%;
        }

        body {
            margin: 0 auto;
            padding: 0;
            border: 0;
            background-image: url("./resource/assets/compatible/ditu000.jpg");
        }
    </style>
    <!--这个标签为通过egret提供的第三方库的方式生成的 javascript 文件。删除 modules_files 标签后，库文件加载列表将不会变化，请谨慎操作！-->
    <!--modules_files_start-->
    <script src="config.js?v202309-02"></script>
    <script egret="lib" src="libs/modules/egret/egret.min.js"></script>
    <script egret="lib" src="libs/modules/egret/egret.web.min.js"></script>
    <script egret="lib" src="libs/modules/res/res.min.js"></script>
    <script egret="lib" src="libs/modules/eui/eui.min.js"></script>
    <script egret="lib" src="libs/modules/game/game.min.js"></script>
    <script egret="lib" src="libs/modules/tween/tween.min.js"></script>
    <script egret="lib" src="libs/modules/socket/socket.min.js"></script>
    <!--modules_files_end-->
    <!--game_files_start-->
    <script src="g.js"></script>
    <script src="main.min.js?v202309-03"></script>
    <link rel="manifest" href="/manifest.json">
    <!--game_files_end-->
</head>

<body>
    <!--<div style="margin: auto;width: 100%;height: 100%; overflow:hidden; position:relative; " class="egret-player"
  data-entry-class="Main" data-orientation="landscape" data-frame-rate="30"
  data-content-width="1920" data-content-height="1080" data-scale-mode="fixedWidth"  data-show-paint-rect="false" data-multi-fingered="2"
  data-show-fps="false" data-show-log="false" data-show-fps-style="x:0,y:0,size:12,textColor:0xFFFFFF,bgAlpha:1">
  <div id='___preloading'
   style="margin: auto;width: 100%;height: 100%; overflow:hidden; position:relative; text-align: center; ">
   <img src="resource\assets\form\login_bg1.jpg" style="height: 100%;" />
  </div>
 </div>-->
    <!--<div style="margin: auto;width: 100%;height: 100%; overflow:hidden; position:relative; " class="egret-player"
  data-entry-class="Main" data-orientation="portrait" data-scale-mode="fixedWidth" data-frame-rate="30"
  data-content-width="640" data-content-height="1136" data-show-paint-rect="false" data-multi-fingered="2"
  data-show-fps="false" data-show-log="false" data-show-fps-style="x:0,y:0,size:12,textColor:0xFFFFFF,bgAlpha:1">
  <div id='___preloading'
   style="margin: auto;width: 100%;height: 100%; overflow:hidden; position:relative; text-align: center; ">
   <img src="resource\assets\form\login_bg1.jpg" style="height: 100%;" />
  </div>
 </div>
    <div style="margin: auto;width: 100%;height: 100%; overflow:hidden; position:relative; " class="egret-player"
        data-entry-class="Main" data-orientation="portrait" data-scale-mode="fixedWidth" data-frame-rate="30"
        data-content-width="704" data-content-height="1250" data-show-paint-rect="false" data-multi-fingered="2"
        data-show-fps="false" data-show-log="false" data-show-fps-style="x:0,y:0,size:12,textColor:0xFFFFFF,bgAlpha:1">
        <div id='___preloading'
            style="margin: auto;width: 100%;height: 100%; overflow:hidden; position:relative; text-align: center; ">
            <img src="resource\assets\form\login_bg1.jpg" style="height: 100%;" />
        </div>
    </div>-->
    <!--
        exactFit
        noBorder
        fixedWidth
        fixedHeight
        fixedNarrow
        fixedWide
    <div style="margin: auto;width: 100%;height: 100%; overflow:hidden; position:relative; " class="egret-player"
        data-entry-class="Main" data-orientation="landscape" data-scale-mode="fixedWidth" data-frame-rate="30"
        data-content-width="1707" data-content-height="1136" data-show-paint-rect="false" data-multi-fingered="2"
        data-show-fps="false" data-show-log="false" data-show-fps-style="x:0,y:0,size:12,textColor:0xFFFFFF,bgAlpha:1">
        <div id='___preloading'
            style="margin: auto;width: 100%;height: 100%; overflow:hidden; position:relative; text-align: center; ">
            <img src="resource\assets\form\login_bg1.jpg" style="height: 100%;" />
        </div>
    </div>
    -->
    @yield('content')
    <script>
        egret.runEgret({
            renderMode: "canvas",
            audioType: 2
        });
        let width = screen.width;
        let height = screen.height;
        if (height > width && window.location.href.includes('mobile') == false) {
            window.location.href = window.location.origin + '/mobile' + window.location.search;
        } else if (height < width && window.location.href.includes('mobile')) {
            window.location.href = window.location.href.replace('mobile', '');
        }
        window.LEGEND_VERSION = '20231018';
    </script>
</body>

</html>
