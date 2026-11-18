<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use App\Models\User;
use Illuminate\Support\Str;
use Illuminate\Support\Facades\DB;

class GameController extends Controller
{
    public function gamelist(Request $request)
    {
        $data = [];
        $validator = Validator::make($request->all(), [
            'world_id'  => ['required', 'string'],
            'open_id'   => ['required', 'string', 'email', 'max:255']
        ]);
        if ($validator->fails()) {
            return json_encode($data);
        }
        
        foreach ($this->user_blocks as $user_block) {
            if ($request->email == $user_block) {
                return redirect()->to('login');
            }
        }

        $user = DB::connection('game')->table('player')->where('open_id', $request->open_id)->first();
        if (!$user) {
            return json_encode($data);
        }
        $data = [
            [
                "ip" => ServerController::$server_ip,
                "merge" => "",
                "serverName" => ServerController::$server_name,
                "serverid" => ServerController::$serverid,
                "opentime" => ServerController::$openTime,
                "client_port" => ServerController::$client_port,
                "status" => [
                    "maintenance" => false,
                    "new" => true,
                    "recommend" => false,
                    "full" => false
                ],
                "lastLogin" => $user->lastLogin,
                "level" => $user->lv,
                "createTime" => $user->createTime,
                "name" => $user->name,
                "icon" => $user->icon,
                "vip" => $user->vipLevel
            ]
        ];
        return json_encode($data);
    }

    public function listGames(Request $request)
    {
        $data = [
            [
                "ip" => ServerController::$server_ip,
                "merge" => "",
                "serverName" => ServerController::$server_name,
                "serverid" => ServerController::$serverid,
                "opentime" => ServerController::$openTime,
                "client_port" => ServerController::$client_port,
                "status" => [
                    "maintenance" => false,
                    "new" => true,
                    "recommend" => true,
                    "full" => false
                ]
            ]
        ];
        return json_encode($data);
    }
}
