<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use App\Models\User;
use Illuminate\Support\Str;
use Illuminate\Support\Facades\DB;

class ServerController extends Controller
{
    public static $server_ip = 'tieulong.namdorado.com';
    public static $server_name = 'Dương Quá';
    public static $client_port = 8501;
    public static $serverid = 1;
    public static $openTime = '2023-09-29 11:00:00';

    /**
     * Lets the environment (e.g. Docker) override the server entry shown to the H5 client.
     * Without these variables the defaults above are kept.
     */
    public static function configureFromEnv()
    {
        static::$server_ip   = env('GAME_PUBLIC_HOST', static::$server_ip);
        static::$server_name = env('GAME_SERVER_NAME', static::$server_name);
        static::$client_port = (int) env('GAME_CLIENT_PORT', static::$client_port);
        static::$serverid    = (int) env('GAME_SERVER_ID', static::$serverid);
        static::$openTime    = env('GAME_OPEN_TIME', static::$openTime);
    }

    public function loadConfig(Request $request)
    {
        $data = [];
        if ($request->ip() != ServerController::$server_ip && env('DEBUG') == 'false') {
            return json_encode($data);
        }
        $db_host = '127.0.0.1';
        if (env('DEBUG') == 'false') {
            //$db_host = env('DB_HOST');
        }
        
        $data = [
            "world" => [
                "platform" => "Web",
                "world_gm_port" => "9003",
                "world_dbhost" => $db_host . ':' . env('DB_PORT'),
                "world_dbname" => "tieulong_dbhunkftalk",
                "world_dbuser" => "root",
                "world_dbpwd" => env('DB_PASSWORD'),
                "world_log_dbhost" => $db_host . ':' . env('DB_PORT'),
                "world_log_dbname" => "tieulong_dbhunkftalk_log",
                "world_log_dbuser" => env('DB_USERNAME'),
                "world_log_dbpwd" => env('DB_PASSWORD'),
                "world_game_server_port" => "8003",
                "Redis_ADDR" => "127.0.0.1",
                "world_ip" => '127.0.0.1',
                "world_open" => "on",
                "AAY_SecretKey" => "e829d41afd80e7e8d268a5b02858a069",
                "pid" => "ee6a417e9d284a7991858ad154b930f3"
            ],
            "game" => [
                "server_id" => "1",
                "server_name" => ServerController::$server_name,
                "platform" => "Web",
                "socket_port" => "7501",
                "game_dbhost" => $db_host . ':' . env('DB_PORT'),
                "game_dbname" => env('GAME_DATABASE'),
                "game_dbuser" => env('DB_USERNAME'),
                "game_dbpwd" => env('DB_PASSWORD'),
                "game_logdbhost" => $db_host . ':' . env('DB_PORT'),
                "game_logdbname" => "tieulong_game_server1_log",
                "game_logdbuser" => env('DB_USERNAME'),
                "game_logdbpwd" => env('DB_PASSWORD'),
                "client_port" => ServerController::$client_port,
                "maxloaded" => "9999",
                "maxcache" => "",
                "maxqueues" => "",
                "openTime" => ServerController::$openTime,
                "game_PackageSignCheck_NeedCheatCheck" => "",
                "game_PackageSignCheck_KickOutFaker" => "",
                "game_PackageCheck_PlatformKey" => "",
                "ip" => ServerController::$server_ip,
                "pid" => "20001",
                "dd" => "new,full"
            ],
            "zone" => [
                "zone_id" => "20001",
                "zone_name" => "Kiß║┐m Thß║┐",
                "zone_gm_port" => "",
                "zone_dbhost" => "",
                "zone_dbname" => "",
                "zone_dbuser" => "",
                "zone_dbpwd" => "",
                "zone_log_dbhost" => "",
                "zone_log_dbname" => "",
                "zone_log_dbuser" => "",
                "zone_log_dbpwd" => "",
                "zone_game_server_port" => "",
                "zone_game_login_pwd" => "",
                "world_id" => "",
                "world_name" => "",
                "platform" => "",
                "world_game_login_pwd" => "",
                "zone_ip" => "",
                "zone_open" => "false",
                "pid" => "10001"
            ]
        ];
        info(json_encode($data));
        return json_encode($data);
    }

    public function gameStatus(Request $request)
    {
        $data = [
            "status"     => 'false'
        ];
        return json_encode($data);
    }
}
