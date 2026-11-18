@extends('layouts.appnoauth')

@section('content')
    <div class="register-container">
        <h1>Đăng ký tài khoản</h1>

        <div class="connect">
            <p>{{ env('APP_FACEBOOK') }}</p>
        </div>
        <form action="{{ route('account.register') }}" method="post">
            @if (app('request')->input('error'))
                <label class="error">{{ app('request')->input('error') }}</label>
            @endif
            <div>
                <input type="text" name="name" class="username" placeholder="Tên của bạn" autocomplete="off" required>
            </div>
            <div>
                <input type="text" name="email" class="username" placeholder="Địa chỉ Email" autocomplete="off"
                    required>
            </div>
            <div>
                <input type="password" name="password" class="password" placeholder="Mật khẩu" required>
            </div>
            <div>
                <input type="password" name="password_confirmation" class="password" placeholder="Xác nhận mật khẩu"
                    required>
            </div>
            <button id="submit" type="submit">Đăng ký</button>
        </form>
        <a href="{{ route('account.login') }}">
            <button type="button" class="register-tis">Đăng nhập</button>
        </a>

        <a href="https://namdorado.com/points/nap-the" target="__blank">
            <button type="button" class="register-tis">Nạp thẻ</button>
        </a>

    </div>
@endsection
