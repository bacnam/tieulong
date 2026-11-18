<?php

/** @var \Laravel\Lumen\Routing\Router $router */

App\Http\Controllers\ServerController::configureFromEnv();

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It is a breeze. Simply tell Lumen the URIs it should respond to
| and give it the Closure to call when that URI is requested.
|
*/

$router->get('gm/gmNotices', function () {
    $data = [
        "state"     => 1000,
        "notices"   =>  [
            [
                "title" =>  "Tiêu đề",
                "content"   =>  "images/notices/nap-the.jpg",
                "type"  =>  2
            ],
            /*[
                "title" =>  "Tiêu đề",
                "content"   =>  "resource/assets/form/accumconsume_banner.jpg",
                "type"  =>  2
            ]*/
        ]
    ];
    return json_encode($data);
});

$router->group(['prefix' => 'abchabchca8b27acbachbap1bhacb1p'], function () use ($router) {
    $router->get('gm/gameStatus', ['as' => 'game.gamestatus', 'uses' => 'ServerController@gameStatus']);
    $router->get('gm/gmNotice!loadConfig', ['as' => 'game.loadconfig', 'uses' => 'ServerController@loadConfig']);

    $router->post('orders/create', ['as' => 'orders.create', 'uses' => 'OrderController@create']);
    $router->get('orders/create', ['as' => 'orders.create', 'uses' => 'OrderController@create']);
});



$router->get('gm/gmNotice!listGames', ['as' => 'game.listgames', 'uses' => 'GameController@listGames']);
$router->get('gm/user!gamelist', ['as' => 'game.gamelist', 'uses' => 'GameController@gamelist']);


$router->get('/', ['as' => 'home', 'uses' => 'HomeController@index']);
$router->get('/mobile', ['as' => 'home', 'uses' => 'HomeController@mobile']);
$router->get('login', ['as' => 'account.login', 'uses' => 'AccountController@login']);
$router->post('login', ['as' => 'account.login', 'uses' => 'AccountController@do_login']);
$router->get('register', ['as' => 'account.register', 'uses' => 'AccountController@register']);
$router->post('register', ['as' => 'account.register', 'uses' => 'AccountController@do_register']);
