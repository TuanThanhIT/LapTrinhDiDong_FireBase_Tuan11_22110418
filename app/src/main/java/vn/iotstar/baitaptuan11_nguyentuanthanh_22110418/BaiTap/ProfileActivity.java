package vn.iotstar.baitaptuan11_nguyentuanthanh_22110418.BaiTap;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import vn.iotstar.baitaptuan11_nguyentuanthanh_22110418.R;

public class ProfileActivity extends AppCompatActivity {

    TextView txtEmail;
    TextView txtQuantityVideo;
    ImageView imgAvatar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        String email = getIntent().getStringExtra("userEmail");
        String avatarUrl = getIntent().getStringExtra("userAvatar");
        int videoQuantity = getIntent().getIntExtra("userQuantityVideo", 0);

        txtEmail = findViewById(R.id.txtEmail);
        txtQuantityVideo = findViewById(R.id.txtVideoQuantity);
        imgAvatar = findViewById(R.id.imgAvatar);

        // 5. Hiển thị dữ liệu
        try {
            txtEmail.setText(email);
            txtQuantityVideo.setText(String.valueOf(videoQuantity));

            Glide.with(this)
                    .load(avatarUrl)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_dislike) // Thêm ảnh báo lỗi nếu load không thành công
                    .into(imgAvatar);

        } catch (Exception e) {
            Log.e("DEBUG_PROFILE", "Lỗi khi hiển thị dữ liệu: " + e.getMessage());
            showErrorAndFinish("Lỗi hiển thị dữ liệu");
        }
    }

    private void showErrorAndFinish(String message) {
        Log.e("DEBUG_PROFILE", message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish(); // Đóng Activity nếu có lỗi
    }
}