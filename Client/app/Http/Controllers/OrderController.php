<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use App\Models\User;
use App\Models\GameTransaction;
use GuzzleHttp\Exception\TransferException;

class OrderController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        //
    }

    public function create(Request $request)
    {
        info($request->all());
        $result = [
            'code'  =>  1001
        ];

        $validator = Validator::make($request->all(), [
            'adfrom'            => ['required', 'string'],
            'adfrom2'           => ['required', 'string'],
            'amount'            => ['required', 'numeric', 'min:1'],
            'app_id'            => ['required', 'string'],
            'carrier'           => ['required', 'string'],
            'cid'               => ['required', 'numeric'],
            'crystal'           => ['required', 'numeric'],
            'gameid'            => ['required', 'string'],
            'productid'         => ['required', 'string'],
            'quantity'          => ['required', 'numeric'],
            'server_id'         => ['required', 'string'],
            'uid'               => ['required', 'string'],
            'world_id'          => ['required', 'string'],
            'sign'              => ['required', 'string']
        ]);

        if ($validator->fails()) {
            $result['code'] = 1002;
            return json_encode($result);
        }

        $user = User::where('email', $request->uid)->first();
        if (!$user) {
            $result['code'] = 1003;
            return json_encode($result);
        }

        if ($user->point < $request->amount) {
            $result['code'] = 1004;
            return json_encode($result);
        }

        $transaction = GameTransaction::create([
            'user_id'       =>  $user->id,
            'email'         =>  $user->email,
            'status'        =>  'success',
            'amount'        =>  $request->amount,
            'game'          =>  'Long Nu',
            'message'       =>  'Mua ' . $request->crystal . 'KNB',
            'description'   =>  'Mua ' . $request->crystal . 'KNB',
        ]);

        $user->point -= $request->amount;
        $user->save();

        $secret = env('APP_KEY');
        $access_token = hash_hmac('sha256', $request->password, $secret);

        $data = [
            "ordertype"         => 1,
            "cid"               => $request->cid,
            "cporderid"         => $transaction->id,
            "sign"              => "cYUPYnZgCiy0zUBg9KRRXM7H4GUsRKpI",
            "carrier"           => $request->carrier,
            "platform"          => $request->platform,
            "adfrom"            => $request->adfrom,
            "adfrom2"           => $request->adfrom2,
            "gameid"            => $request->gameid,
            "server_id"         => $request->server_id,
            "appid"             => $request->app_id,
            "open_id"           => $request->uid,
            "adfrom_orderid"    => $transaction->id,
            "quantity"          => $request->quantity,
            "productid"         => $request->productid,
            'access_token'      => $access_token
        ];

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, env('APP_RECHARGE_URL'));
        curl_setopt($ch, CURLOPT_ENCODING, 'UTF-8');
        curl_setopt($ch, CURLOPT_VERBOSE, 1);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, FALSE);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
        curl_setopt($ch, CURLOPT_HTTPHEADER, [
            'Context_Type: application/json',
            'User-Agent: Dalvik/2.1.0 (Linux; U; Android 7.1.1; ONEPLUS A5000 Build/NMF26X)'
        ]);
        $res = curl_exec($ch);
        info($res);
        $status = curl_getinfo($ch, CURLINFO_HTTP_CODE);

        if ($status == 200) {
            $result['code'] = 1000;
            $result['bill_no'] = $transaction->id;


            return json_encode($result);
        } else {

            return json_encode($result);
        }
        curl_close($ch);
    }
}
