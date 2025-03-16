# Hướng dẫn đẩy dự án lên GitHub

## 1. Tạo repository trên GitHub

1. Đăng nhập vào tài khoản GitHub của bạn
2. Nhấn vào nút "+" ở góc trên bên phải và chọn "New repository"
3. Đặt tên repository là "tasktracker"
4. Thêm mô tả: "A command-line task management application built with Spring Boot"
5. Chọn "Public" hoặc "Private" tùy theo nhu cầu của bạn
6. Không chọn "Initialize this repository with a README" vì chúng ta đã có README
7. Nhấn "Create repository"

## 2. Khởi tạo Git trong dự án

Mở terminal hoặc command prompt và di chuyển đến thư mục gốc của dự án:

```bash
cd /đường/dẫn/đến/tasktracker
```

Khởi tạo Git repository:

```bash
git init
```

## 3. Thêm các file vào staging area

```bash
git add .
```

## 4. Commit các thay đổi

```bash
git commit -m "Initial commit: Task Tracker application"
```

## 5. Kết nối với repository GitHub

Thay thế `yourusername` bằng tên người dùng GitHub của bạn:

```bash
git remote add origin https://github.com/yourusername/tasktracker.git
```

## 6. Đẩy code lên GitHub

```bash
git push -u origin master
```

hoặc nếu bạn đang sử dụng branch main:

```bash
git push -u origin main
```

## 7. Xác thực

Nếu đây là lần đầu tiên bạn sử dụng Git với GitHub, bạn có thể được yêu cầu xác thực. Hãy nhập tên người dùng và mật khẩu GitHub của bạn hoặc sử dụng token nếu bạn đã cấu hình.

## 8. Kiểm tra

Sau khi đẩy thành công, hãy truy cập URL repository của bạn để xác nhận rằng tất cả các file đã được đẩy lên:

```
https://github.com/yourusername/tasktracker
```
