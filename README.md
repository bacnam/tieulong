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
