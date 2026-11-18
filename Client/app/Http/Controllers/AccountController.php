<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use App\Models\User;
use Illuminate\Support\Str;
use Illuminate\Support\Facades\Hash;

class AccountController extends Controller
{
    public function login()
    {
        return view('login', ['title' => env('APP_NAME') . ' - Đăng nhập']);
    }

    public function do_login(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'email'         => ['required', 'string', 'email'],
            'password'      => ['required', 'string']
        ]);
        foreach ($this->user_blocks as $user_block) {
            if ($request->email == $user_block) {
                return redirect()->route('account.login', ['error' => 'Tài khoản bị khóa do liên quan tới Bug, Hack!']);
            }
        }
        
        if ($validator->fails()) {
            return redirect()->route('account.login', ['error' => 'Email hoặc mật khẩu không chính xác!']);
        }
        $user = User::where('email', $request->email)->first();
        if (!$user) {
            return redirect()->route('account.login', ['error' => 'Email hoặc mật khẩu không chính xác!']);
        }
        if (Hash::check($request->password, $user->password) == false) {
            return redirect()->route('account.login', ['error' => 'Email hoặc mật khẩu không chính xác!']);
        }
        $secret = env('APP_KEY');
        $access_token = hash_hmac('sha256', $request->password, $secret);

        return redirect()->route('home', ['world_id' => '10001', 'open_id' => strtolower($request->email), 'access_token' => $access_token, 'time' => time()]);
    }

    public function register()
    {
        return view('register', ['title' => env('APP_NAME') . ' - Đăng ký tài khoản']);
    }

    public function do_register(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'name' => ['required', 'string', 'max:255'],
            'email' => ['required', 'string', 'email', 'max:255'],
            'password' => ['required', 'string', 'min:8', 'max:32', 'confirmed'],
        ], [
            'name.*'                =>  'Vui lòng nhập vào Tên',
            'email.*'               =>  'Email không chính xác',
            'password.confirmed'    =>  'Xác nhận mật khẩu sai',
            'password.*'            =>  'Mật khẩu từ 8 - 32 ký tự'
        ]);
        if ($validator->fails()) {
            return redirect()->route('account.register', ['error' => $validator->errors()->first()]);
        }
        $user = User::where('email', $request->email)->first();
        if ($user) {
            return redirect()->route('account.register', ['error' => 'Email đã tồn tại!']);
        }
        $referer_code = User::random_referer_code();
        $user = [
            'name' => $request->name,
            'email' => $request->email,
            'password' => Hash::make($request->password),
            'referer_code'  =>  $referer_code,
        ];

        User::create($user);

        $secret = env('APP_KEY');
        $sign = hash_hmac('sha256', $request->password, $secret);

        return redirect()->route('home', ['world_id' => '10001', 'open_id' => $request->email, 'sign' => $sign, 'time' => time()]);
    }
}
