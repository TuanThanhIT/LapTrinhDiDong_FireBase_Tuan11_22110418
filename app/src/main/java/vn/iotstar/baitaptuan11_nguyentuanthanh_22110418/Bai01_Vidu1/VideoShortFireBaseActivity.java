package vn.iotstar.baitaptuan11_nguyentuanthanh_22110418.Bai01_Vidu1;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vn.iotstar.baitaptuan11_nguyentuanthanh_22110418.R;

public class VideoShortFireBaseActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private VideosFireBaseAdapter videosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.vpager);
        getVideos();
    }

    private void getVideos() {
        // Thiết lập tham chiếu đến bảng "videos" trên Firebase
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference("videos");

        // Thiết lập tùy chọn cho FirebaseRecyclerAdapter
        FirebaseRecyclerOptions<Video1Model> options =
                new FirebaseRecyclerOptions.Builder<Video1Model>()
                        .setQuery(mDataBase, Video1Model.class)
                        .build();

        // Khởi tạo adapter và gán cho ViewPager2
        videosAdapter = new VideosFireBaseAdapter(options);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager2.setAdapter(videosAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        videosAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        videosAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videosAdapter.notifyDataSetChanged();
    }
}
