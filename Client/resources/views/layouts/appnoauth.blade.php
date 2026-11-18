<!DOCTYPE HTML>
<html>

<head>

    <meta charset="utf-8">
    <link rel="shortcut icon" href="favicon.png">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="screen-orientation" content="landscape" />
    <title>{{ env('APP_NAME') }}</title>
    <meta name="keywords" content="" />
    <meta name="description" content="{{ env('APP_NAME') }}" />
    <link rel="stylesheet" href="css/style.css" />

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
    
    <script src="js/jquery.min.js"></script>
    <script src="js/common.js"></script>
    <!--背景图片自动更换-->
    <script src="js/supersized.3.2.7.min.js"></script>
    <script src="js/supersized-init.js"></script>
    <!--表单验证-->
    <script src="js/jquery.validate.min.js?var1.14.0"></script>
</head>

<body>
    @yield('content')
</body>

</html>
