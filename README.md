# Tiểu Long – Server source

Source Java của **GameServer** và **WorldServer**, dựng lại từ `Server/gameServer.jar` và `Server/worldServer.jar` bằng [Vineflower](https://github.com/Vineflower/vineflower) 1.11.1. Thay cho bản decompile JD-Core cũ (`src/gameServer`), vốn không compile được.

## Cấu trúc

```
pom.xml                 Maven parent (Java 8, UTF-8)
server-common/          Framework dùng chung: com.zhonglian.server, BaseCommon, BaseServer,
                        BaseTask, BaseThread, ConsoleTask, proto  (192 file)
game-server/            core.*, business.*, d.java  -> gameServer.jar   (659 file)
world-server/           core.*, business.*, d.java  -> worldServer.jar  (77 file)
third-party/repo/       Maven repo cục bộ: thư viện không xác định được version trên Maven Central,
                        lấy nguyên bytecode từ jar gốc (gson, bsh 2.0b5, libthrift, libfb303, jsc, jsr166y)
tools/decompile/        Script tái tạo lại toàn bộ source từ 2 file jar
docker/, docker-compose.yml   Chạy full stack bằng Docker (xem bên dưới)
```

Phần code `server-common` giống hệt nhau trong cả hai jar gốc (đã so sánh SHA-1 từng class), nên được tách thành module riêng.

## Build

Yêu cầu: JDK 8 trở lên, Maven 3.6+ và mạng tới Maven Central.

```bash
mvn -q package
# -> game-server/target/gameServer.jar
# -> world-server/target/worldServer.jar
```

Hai file này là fat jar (đã gộp dependency) và có cùng tên với jar gốc, nên chạy được bằng `Server/1.sh` và `Server/2.sh` như cũ:

```bash
java -Dfile.encoding=UTF-8 -jar worldServer.jar conf   # chạy trước
java -Dfile.encoding=UTF-8 -jar gameServer.jar  conf
```

Server cần thư mục `conf/` (properties, `data/`, `map/`), MySQL với `lower_case_table_names=1` (bản gốc chạy trên Windows) và các database trong `Server/database/*.sql`.

## Chạy bằng Docker (full stack)

Gồm 4 service: `mysql` (5.7), `world` (WorldServer), `game` (GameServer, build từ source trong image) và `client` (web portal + H5 client, PHP 8.2/Apache).

```bash
./tieulong.sh setup         # Linux: tạo .env (mật khẩu ngẫu nhiên), build, khởi động, chờ server sẵn sàng
.\tieulong.ps1 setup        # Windows / Docker Desktop (chỉ dev/test)
```

Script vận hành (`tieulong.sh` trên Linux, `tieulong.ps1` trên Windows, cùng lệnh):

| Lệnh | Việc làm |
|---|---|
| `setup` | Lần đầu: tạo `.env`, build image, khởi động, chờ GameServer báo sẵn sàng |
| `start` | Khởi động, chờ sẵn sàng, mở lại đăng nhập |
| `stop [--force]` | Dừng an toàn: chặn đăng nhập (`OnlineLimit=0`), kick người chơi, chờ hàng đợi ghi DB về 0, dừng game → world → web → MySQL |
| `restart` | `stop` + `start` |
| `deploy` | Build image mới trong lúc server vẫn chạy, sao lưu DB, dừng an toàn, khởi động bản mới |
| `status` | Trạng thái container, số người online, hàng đợi ghi DB, giới hạn online |
| `logs [service]` | Xem log trực tiếp |
| `backup` / `restore <file>` | Sao lưu tất cả DB vào `backups/` / khôi phục (tự dừng an toàn trước) |
| `console [game\|world]` | Vào console BeanShell (thoát bằng Ctrl-P Ctrl-Q) |
| `gm '<beanshell>'` | Chạy một câu lệnh BeanShell trên GameServer qua API GM |

Thêm `-y` (`-Yes` với PowerShell) để chạy không hỏi. Không dùng script thì làm tay:

```bash
cp .env.example .env        # đổi MYSQL_ROOT_PASSWORD, APP_KEY, GAME_PUBLIC_HOST
docker compose up -d --build
docker compose logs -f game # chờ dòng "[服务器启动完毕]!"
```

Sau đó mở http://localhost:8080, đăng ký tài khoản rồi vào game.

| Port trên máy host | Service | Ghi chú |
|---|---|---|
| `WEB_PORT` (8080) | client | Web portal, H5 client và các API `gm/*` |
| `GAME_CLIENT_PORT` (8501) | game | WebSocket mà trình duyệt kết nối trực tiếp tới |
| 127.0.0.1:7501 / 127.0.0.1:9003 | game / world | HTTP GM (nạp tiền, gửi thư, ban...), chỉ mở cho máy local |
| 127.0.0.1:3306 | mysql | |

- **`GAME_PUBLIC_HOST`**: tên miền hoặc IP mà trình duyệt của người chơi dùng để kết nối tới máy chạy Docker. Để `localhost` thì chỉ chơi được trên chính máy đó; muốn người khác chơi thì đặt IP LAN hoặc domain.
- **Database**: lần chạy đầu tiên (volume còn trống), `docker/mysql/init` tạo các DB từ `Server/database/*.sql`, cộng thêm DB `tieulong_web` (bảng `baubas_users`, `baubas_game_transactions`) cho web portal. Muốn làm lại từ đầu thì chạy `docker compose down -v`.
- **Config của server**: được sinh ra từ biến môi trường mỗi lần container khởi động (`docker/server/templates/*.properties`). Container không tải config từ web GM (`downConfUrl` để trống).
- **Console GM (BeanShell)**: `docker attach tieulong-game-1`; thoát ra bằng Ctrl-P rồi Ctrl-Q.
- **Luồng nạp tiền**: game gọi `http://client/.../orders/create`, client gọi lại `http://game:7501/game/recharge/notify`.
- **Log**: nằm trong các volume `game-logs` và `world-logs`, hoặc xem bằng `docker compose logs`.
- **Phiên bản MySQL**: dùng **MySQL 5.7**, vì driver Connector/J 5.1.23 của server không kết nối được với MySQL 8 hay MariaDB 10.10 trở lên. Bắt buộc `lower_case_table_names=1` (đã cấu hình trong `docker/mysql/my.cnf`).

Client (`Client/`) có thêm 3 chỗ sửa nhỏ để đọc cấu hình từ biến môi trường. Khi không đặt các biến này, client vẫn chạy như cũ:

- `GM_ADDR`: ghi đè `config.js`; giá trị `same-origin` nghĩa là dùng chính site đang mở.
- `GAME_PUBLIC_HOST`, `GAME_CLIENT_PORT`, `GAME_SERVER_*`: dùng cho danh sách server trả về H5 client.

## Thư viện

| Thư viện | Version | Nguồn |
|---|---|---|
| logback-classic/core | 1.1.3 | Central (theo `pom.properties` trong jar gốc) |
| slf4j-api | 1.7.12 | Central (như trên) |
| guava | 10.0.1 | Central (như trên) |
| bonecp | 0.7.1.RELEASE | Central (như trên) |
| mina-core | 2.0.9 | Central (như trên) |
| httpclient / httpcore / httpcore-nio | 4.4.1 | Central (như trên) |
| httpasyncclient | 4.0.2 | Central (như trên) |
| jedis | 2.1.0 | Central (như trên) |
| commons-codec / -logging / -pool | 1.9 / 1.1.1 / 1.5.4 | Central (như trên) |
| mysql-connector-java | 5.1.23 | Central (version đọc từ class của driver) |
| gson, bsh, libthrift, libfb303, jsc, jsr166y | `*-bundled` | `third-party/repo`, bytecode gốc |

Jar gốc còn đóng gói kèm một số thư viện mà code **không dùng tới**: protobuf, apns, commons-cli, c3p0/mchange, javolution, junit/hamcrest. Bản build mới đã bỏ các thư viện này. Kết quả chạy thử (mục bên dưới) cho thấy server vẫn khởi động bình thường khi không có chúng.

## Khác biệt so với bytecode gốc

Code được giữ nguyên logic. Chỉ có các điểm sau phải sửa để `javac` chấp nhận:

1. **try-with-resources**: bản gốc build bằng Eclipse (ecj), nên decompiler sinh ra khối `Throwable/addSuppressed` thay vì `try (...)`. Đã chuyển lại thành `try (...) {}` ở 7 file trong `server-common`.
2. **Constructor của inner class private**: ecj thêm một tham số `null` ẩn, decompiler giữ lại tham số này. Đã bỏ ở 83 chỗ gọi.
3. **Generic**: thêm một số cast tường minh (`RefContainer<?>`, `Enum.valueOf`, `getFeature`, `BM.AsynCall_Find`).
4. `ChatMgr.broadcast()`: đổi tên một biến cục bộ bị trùng (`playerx` → `receiver`).
5. `TDRechargeLogger`: `sun.net.www.protocol.http.HttpURLConnection` → `java.net.HttpURLConnection`. Class nội bộ của JDK thực chất là lớp con của class public này, và code chỉ gọi các method public.
6. `Scribe.Processor`: sửa cách tham chiếu `ProcessFunction` (interface protected của fb303).

Toàn bộ các bước sửa này nằm trong `tools/decompile/*.py`, nên có thể chạy lại từ đầu:

```bash
tools/decompile/decompile.sh vineflower-1.11.1.jar Server/gameServer.jar Server/worldServer.jar /tmp/out
```

Kết quả sinh ra trùng khớp 100% với source trong repo.

## Đã kiểm chứng

- **Compile**: không còn lỗi, chạy được với cả JDK 8 và JDK 21 (`--release 8`).
- **Cấu trúc class**: đủ 1.315 class có tên, khớp từng class với jar gốc. Chữ ký field/method khớp hoàn toàn, chỉ khác ở các phần do compiler tự sinh (constructor của enum, accessor ẩn) và field `HttpURLConnection` ở mục 5.
- **Chạy thử** (Java 8 + MariaDB, import `Server/database/*.sql`, cùng thư mục `conf`):
  - WorldServer và GameServer khởi động hoàn tất và login được vào nhau.
  - Mở đúng các port 7501, 8003, 8501, 9003.
  - Log khởi động trùng khớp từng dòng với bản gốc (sau khi bỏ timestamp).
  - Schema DB sau bước auto-migrate giống hệt.
  - Số robot tạo ra bằng nhau (1.110).
