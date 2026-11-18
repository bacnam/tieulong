@extends('layouts.appnoauth')

@section('content')
    <div class="login-container">
        <h1>Đăng nhập</h1>

        <div class="connect">
            <p>{{ env('APP_FACEBOOK') }}</p>
        </div>
        <form action="{{ route('account.login') }}" method="post">
            @if (app('request')->input('error'))
                <label class="error">{{ app('request')->input('error') }}</label>
            @endif
            <div>
                <input type="text" name="email" class="account" placeholder="Địa chỉ Email" autocomplete="off" required>
            </div>
            <div>
                <input type="password" name="password" class="pwd" placeholder="Mật khẩu" required>
            </div>
            <button id="submit" type="submit">Đăng nhập</button>
        </form>

        <a onclick="showApple()">

            <button type="button" class="register-tis"><img class="iphone" src="images/apple.png"><span
                    class="iphone">Hướng dẫn chơi trên iPhone</span></button>
        </a>

        <a onclick="showAndroid()">
            <button type="button" class="register-tis"><img class="android" src="images/android.png"><span
                    class="android">Hướng dẫn chơi trên Android</span></button>
        </a>

        <a href="{{ route('account.register') }}">
            <button type="button" class="register-tis">Đăng ký tài khoản</button>
        </a>

        <a href="https://namdorado.com/points/nap-the" target="__blank">
            <button type="button" class="register-tis">Nạp thẻ</button>
        </a>

    </div>
    <div id="apple">
        <div class="content">
            <ul>
                <li>Bước 1: Vào trang <a href="http://tieulong.namdorado.com/">http://tieulong.namdorado.com/</a> bằng trình
                    duyệt Safari. Từ đây quý nhân sĩ đã có thể đăng nhập để trải nghiệm game.</li>
                <img src="images/ios-1.jpg">
                <li>Bước 2: Mở menu ở cạnh đáy màn hình và chọn Chia Sẻ. Sau đó chọn Add to Home Sceen để tạo biểu tượng
                    truy cập nhanh cho game ngoài màn hình.</li>
                <img src="images/ios-3.jpg">
                <li>Bước 3: Nhập tên tùy ý cho biểu tượng và ấn nút Add.</li>
                <img src="images/ios-4.jpg">
                <li>Như vậy, biểu tượng (icon) truy cập nhanh vào game
                    đã được khởi tạo ngoài màn hình chính của thiết bị iPhone, giúp nhân sĩ tham gia game dễ dàng.</li>
                <img src="images/ios-5.jpg">
            </ul>
        </div>
        <div class="footer">
            <a onclick="hideApple()">
                <button type="button" class="register-tis">Đóng</button>
            </a>
        </div>
    </div>
    <div id="android">
        <div class="content">
            <ul>
                <li>Bước 1: Vào trang <a href="http://tieulong.namdorado.com/">http://tieulong.namdorado.com/</a> bằng trình
                    duyệt Chrome. Từ đây quý nhân sĩ đã có thể đăng nhập để trải nghiệm game.</li>
                <li>Bước 2: Mở menu ở phía trên (Nút 3 chấm khoanh tròn đỏ)</li>
                <img src="images/android-1.jpg">
                <li>Bước 3: Sau đó chọn Thêm vào Màn hình chờ để tạo biểu tượng
                    truy cập nhanh cho game ngoài màn hình.</li>
                <img src="images/android-2.jpg">
                <li>Bước 4: Ấn nút THÊM.</li>
                <img src="images/android-3.jpg">
                <li>Như vậy, biểu tượng (icon) truy cập nhanh vào game
                    đã được khởi tạo ngoài màn hình chính của thiết bị Android, giúp nhân sĩ tham gia game dễ dàng.</li>
                <img src="images/android-4.jpg">
            </ul>
        </div>
        <div class="footer">
            <a onclick="hideAndroid()">
                <button type="button" class="register-tis">Đóng</button>
            </a>
        </div>
    </div>
    <script>
        function showApple() {
            $('#apple').show();
        }

        function hideApple() {
            $('#apple').hide();
        }

        function hideAndroid() {
            $('#android').hide();
        }

        function showAndroid() {
            $('#android').show();
        }
    </script>
@endsection
