@extends('layouts.app')

@section('content')
    <div style="margin: auto;width: 100%;height: 100%; overflow:hidden; position:relative; " class="egret-player"
        data-entry-class="Main" data-orientation="portrait" data-scale-mode="fixedWide" data-frame-rate="30"
        data-content-width="750" data-content-height="1334" data-show-paint-rect="false" data-multi-fingered="2"
        data-show-fps="false" data-show-log="false" data-show-fps-style="x:0,y:0,size:12,textColor:0xFFFFFF,bgAlpha:1">
        <div id='___preloading'
            style="margin: auto;width: 100%;height: 100%; overflow:hidden; position:relative; text-align: center; ">
            <img src="resource\assets\form\login_bg1.jpg" style="height: 100%;" />
        </div>
    </div>
    <script>
        egret.runEgret({
            renderMode: "canvas",
            audioType: 2
        });
    </script>
@endsection
