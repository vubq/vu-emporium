
# Tiêu chuẩn Quản lý Admin

Đảm bảo tính nhất quán trên tất cả các Trang Quản lý Admin bằng cách tuân thủ các quy tắc sau.

## 1. Bố cục UI & Components
Mọi trang quản lý phải sử dụng các component admin dùng chung (shared components):

-   **Header**: sử dụng `AdminPageHeader`
    -   Title & Subtitle lấy từ i18n
    -   Nút Action chính (ví dụ: "Thêm mới")
-   **Filter Bar**: sử dụng `AdminFilterBar`
    -   Ô tìm kiếm (Search input) phải có debounce hoặc kích hoạt bằng phím Enter
    -   Các dropdown lọc (Trạng thái, Loại, v.v.)
    -   Nút Reset bộ lọc
-   **Data Table**: sử dụng `AdminDataTable`
    -   Prop `loading` cho trạng thái skeleton loading
    -   Prop `is-empty` cho trạng thái không có dữ liệu
    -   Props phân trang: `current-page`, `total-pages`, `page-size`, `total-elements`
    -   Events: `@page-change`, `@page-size-change`
-   **Actions**: sử dụng `ActionMenu`
    -   Sửa, Xóa, Bật/Tắt Trạng thái
    -   Sử dụng icon chung (`PencilSquareIcon`, `TrashIcon`)
-   **Modals**: sử dụng `AdminModal` (hoặc kiểm tra implementation Dialog của `Headless UI` để đồng nhất)
    -   Xác nhận Xóa: sử dụng `ConfirmModal`

## 2. Phân trang & Lọc phía Server (Server-Side)
Logic phân trang **BẮT BUỘC** phải được xử lý ở phía backend.

### Yêu cầu Backend (API):
-   Endpoints phải chấp nhận tham số `page` (bắt đầu từ 0), `size`, và các tham số lọc (ví dụ: `search`, `status`).
-   Kiểu trả về phải là `Page<T>` (Spring Data).
-   Sử dụng `Pageable` trong tham số Controller.

### Yêu cầu Frontend (API Client):
-   Phương thức API phải chấp nhận đối tượng `params`:
    ```typescript
    getAll(params?: { page?: number; size?: number; search?: string; status?: string })
    ```

### Yêu cầu Frontend (Component):
-   **KHÔNG ĐƯỢC** cắt mảng (slice) ở phía client để phân trang (ví dụ: `slice(start, end)`).
-   **KHÔNG ĐƯỢC** lọc mảng ở phía client nếu danh sách có khả năng tăng trưởng lớn.
-   Truyền `page` và `size` xuống API.
-   Cập nhật `totalElements` và `totalPages` từ phản hồi của API.

## 3. Trạng thái Loading
Trải nghiệm người dùng phải mượt mà với các phản hồi trực quan.

-   **Table Loading**: Đặt prop `loading` trên `AdminDataTable` thành `true` trong khi đang fetch dữ liệu.
-   **Button Loading**: Đặt trạng thái `loading` / `disabled` trên các nút submit trong quá trình xử lý async (Tạo, Cập nhật, Xóa).
-   **Skeleton Loaders**: `AdminDataTable` có hỗ trợ sẵn slot skeleton; bắt buộc phải sử dụng.

## 4. Đa ngôn ngữ (i18n)
-   Tất cả nhãn (labels), tiêu đề (headers), và thông báo (messages) phải sử dụng `$t('key')`.
-   Không được hardcode chuỗi ký tự (string).

## 5. Styling Components (Headless UI)
Các component như `Listbox`, `Combobox`, `Menu` (Dropdown) cần tuân thủ style sau để đảm bảo giao diện hiện đại:

-   **Container Options**:
    -   Padding: `p-1`
    -   Border Radius: `rounded-xl`
    -   Shadow: `shadow-lg`
-   **Items (Option/MenuItem)**:
    -   Margin: `mx-1 my-0.5` (tạo khoảng cách với lề)
    -   Border Radius: `rounded-lg`
    -   Background Active/Hover: `bg-indigo-50`
    -   Icon (nếu có): Kích thước cố định `w-5 h-5` container, icon thực `w-4 h-4`.

## 6. Select & Dropdown Styling (QUAN TRỌNG)
Để tránh lỗi "nháy đen" (black flash) và đảm bảo đồng nhất độ dày viền:

-   **Tắt outline mặc định của trình duyệt**: Luôn thêm `focus:outline-none`, `focus:ring-0`, `focus-visible:outline-none`.
-   **Không dùng border dày**: Sử dụng `ring-1` (thay vì `ring-2`) kết hợp với `ring-indigo-500` và `border-indigo-500` khi state là `open`.
-   **Ngăn chọn text**: Thêm `select-none` vào button dropdown.
-   **Chỉ hiện border khi mở (Open State Only)**: Border `ring-1` và màu highlight `indigo-500` CHỈ ĐƯỢC hiện khi menu đang mở (`open` state). Khi đóng hoặc đã chọn xong, border phải biến mất hoặc quay về trạng thái mặc định (gray-300), không được giữ trạng thái focus.
-   **Force Style nếu cần**: Với `MenuButton` hoặc `ListboxButton`, nếu vẫn bị browser override, có thể dùng style inline: `style="outline: none !important;"`.

Ví dụ chuẩn:
```html
<MenuButton 
  class="focus:outline-none focus:ring-0 select-none ..." 
  :class="open ? 'ring-1 ring-indigo-500 border-indigo-500' : ''"
  style="outline: none !important;"
>
```

---
*Tuân thủ tiêu chuẩn này cho tất cả các Trang Quản lý Admin trong tương lai (Sản phẩm, Đơn hàng, Người dùng, v.v.).*
