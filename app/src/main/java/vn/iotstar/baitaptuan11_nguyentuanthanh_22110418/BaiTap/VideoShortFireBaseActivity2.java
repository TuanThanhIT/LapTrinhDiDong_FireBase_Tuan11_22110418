package vn.iotstar.baitaptuan11_nguyentuanthanh_22110418.BaiTap;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vn.iotstar.baitaptuan11_nguyentuanthanh_22110418.R;

public class VideoShortFireBaseActivity2 extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private WebPageFirebaseAdapter webAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.vpager);
        getUserAndVideos();
    }

    private void getUserAndVideos() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(uid);
            ref.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    UserModel user = task.getResult().getValue(UserModel.class);
                    getVideos(user);
                }
            });
        }
    }

    private void getVideos(UserModel user) {
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference("videos");

        FirebaseRecyclerOptions<VideoModel> options =
                new FirebaseRecyclerOptions.Builder<VideoModel>()
                        .setQuery(mDataBase, VideoModel.class)
                        .build();

        webAdapter = new WebPageFirebaseAdapter(options, user, this);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager2.setAdapter(webAdapter);
        webAdapter.startListening(); // ✅ Chỉ gọi khi adapter đã tạo xong
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (webAdapter != null) {
            webAdapter.stopListening(); // ✅ Dừng nếu đang lắng nghe
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webAdapter != null) {
            webAdapter.notifyDataSetChanged(); // Cập nhật lại nếu có thay đổi
        }
    }
}
