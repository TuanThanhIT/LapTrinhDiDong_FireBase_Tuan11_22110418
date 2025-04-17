package vn.iotstar.baitaptuan11_nguyentuanthanh_22110418.BaiTap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import vn.iotstar.baitaptuan11_nguyentuanthanh_22110418.R;

public class ProfileActivity extends AppCompatActivity {

    // Khai báo các biến giao diện
    TextView txtEmail;
    TextView txtQuantityVideo;
    ImageView imgAvatar;
    Button btnSelectImage, btnUploadImage, btnSelectVideo, btnUploadVideo;

    private Uri selectedImageUri;
    private Uri selectedVideoUri;
    Button btnBackToHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Khởi tạo Cloudinary
        initCloudinary();

        // Lấy dữ liệu từ Intent
        String email = getIntent().getStringExtra("userEmail");
        String avatarUrl = getIntent().getStringExtra("userAvatar");
        int videoQuantity = getIntent().getIntExtra("userQuantityVideo", 0);

        // Ánh xạ các view
        txtEmail = findViewById(R.id.txtEmail);
        txtQuantityVideo = findViewById(R.id.txtVideoQuantity);
        imgAvatar = findViewById(R.id.imgAvatar);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnUploadVideo = findViewById(R.id.btnUploadVideo);
        btnSelectVideo = findViewById(R.id.btnSelectVideo); // Thêm nút chọn video
        btnBackToHome = findViewById(R.id.btnBackToHome);

        // Hiển thị dữ liệu người dùng
        txtEmail.setText(email);
        txtQuantityVideo.setText(String.valueOf(videoQuantity));
        Glide.with(this)
                .load(avatarUrl)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_dislike)
                .into(imgAvatar);

        // Xử lý sự kiện khi nhấn nút upload video
        btnUploadVideo.setOnClickListener(v -> {
            showUploadDialog(); // Gọi dialog nhập tiêu đề và mô tả khi upload video
        });

        // Xử lý sự kiện khi nhấn nút quay lại màn hình chính
        btnBackToHome.setOnClickListener(v -> {
            startActivity(new Intent(this, VideoShortFireBaseActivity2.class));
        });

        // Xử lý sự kiện chọn ảnh
        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1001);
        });

        // Xử lý sự kiện upload ảnh
        btnUploadImage.setOnClickListener(v -> {
            if (selectedImageUri != null) {
                uploadImageToCloudinary(selectedImageUri);
            } else {
                Toast.makeText(this, "Bạn chưa chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện chọn video
        btnSelectVideo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("video/*");
            startActivityForResult(intent, 2002); // Đặt mã yêu cầu cho video là 2002
        });
    }

    // Khởi tạo Cloudinary với các thông tin của bạn
    private void initCloudinary() {
        HashMap<String, String> config = new HashMap<>();
        config.put("cloud_name", "dbxo5tx2r"); // THAY BẰNG CLOUD_NAME của bạn
        config.put("api_key", "773656766488156"); // THAY BẰNG API_KEY của bạn
        config.put("api_secret", "GjEhywMfs-tAwCj_v05zq9o9HO4"); // THAY BẰNG API_SECRET của bạn
        MediaManager.init(this, config);
    }

    // Hàm upload ảnh lên Cloudinary
    private void uploadImageToCloudinary(Uri imageUri) {
        MediaManager.get().upload(imageUri)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Toast.makeText(ProfileActivity.this, "Đang upload ảnh...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String url = resultData.get("secure_url").toString();
                        updateAvatarUrlInFirebase(url);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(ProfileActivity.this, "Lỗi upload: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {}

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {}
                }).dispatch();
    }

    // Cập nhật ảnh đại diện trong Firebase
    private void updateAvatarUrlInFirebase(String newAvatarUrl) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(user.getUid());

        userRef.child("avatar").setValue(newAvatarUrl)
                .addOnSuccessListener(unused -> {
                    Glide.with(ProfileActivity.this).load(newAvatarUrl).into(imgAvatar);
                    Toast.makeText(ProfileActivity.this, "Đã cập nhật ảnh đại diện", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProfileActivity.this, "Lỗi lưu link ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Hiển thị dialog nhập tiêu đề và mô tả
    public void showUploadDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_upload, null);

        EditText edtTitle = dialogView.findViewById(R.id.edtTitle);
        EditText edtDescription = dialogView.findViewById(R.id.edtDescription);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Nhập Tiêu Đề và Mô Tả")
                .setPositiveButton("Upload", (dialog, which) -> {
                    String title = edtTitle.getText().toString();
                    String description = edtDescription.getText().toString();

                    if (title.isEmpty() || description.isEmpty()) {
                        Toast.makeText(ProfileActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    } else {
                        if (selectedVideoUri != null) {
                            uploadVideoToCloudinaryWithDetails(title, description, selectedVideoUri);
                        }
                    }
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    // Upload video lên Cloudinary với tiêu đề và mô tả
    private void uploadVideoToCloudinaryWithDetails(String title, String description, Uri videoUri) {
        MediaManager.get().upload(videoUri)
                .option("resource_type", "video")
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Toast.makeText(ProfileActivity.this, "Đang upload video...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String videoUrl = resultData.get("secure_url").toString();
                        saveVideoToFirebase(videoUrl, title, description);
                        increaseVideoQuantity();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(ProfileActivity.this, "Lỗi upload video: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override public void onReschedule(String requestId, ErrorInfo error) {}
                    @Override public void onProgress(String requestId, long bytes, long totalBytes) {}
                }).dispatch();
    }

    // Lưu video lên Firebase
    private void saveVideoToFirebase(String url, String title, String desc) {
        DatabaseReference videosRef = FirebaseDatabase.getInstance().getReference("videos");

        String videoId = videosRef.push().getKey();
        if (videoId == null) return;

        Map<String, Object> videoData = new HashMap<>();
        videoData.put("title", title);
        videoData.put("desc", desc);
        videoData.put("url", url);

        videosRef.child(videoId).setValue(videoData)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Đã lưu video vào Firebase", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi lưu video: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Tăng số lượng video đã upload
    private void increaseVideoQuantity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(user.getUid())
                .child("videoQuantity");

        userRef.get().addOnSuccessListener(snapshot -> {
            long current = 0;
            if (snapshot.exists()) {
                current = snapshot.getValue(Long.class);
            }
            userRef.setValue(current + 1);
            txtQuantityVideo.setText(String.valueOf(current + 1));
        });
    }

    // Xử lý kết quả trả về từ việc chọn ảnh hoặc video
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imgAvatar.setImageURI(selectedImageUri);
        }
        if (requestCode == 2002 && resultCode == RESULT_OK && data != null) {
            selectedVideoUri = data.getData();
        }
    }
}
