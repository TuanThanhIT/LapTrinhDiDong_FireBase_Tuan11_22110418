package vn.iotstar.baitaptuan11_nguyentuanthanh_22110418.BaiTap;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import vn.iotstar.baitaptuan11_nguyentuanthanh_22110418.R;

public class WebPageFirebaseAdapter extends FirebaseRecyclerAdapter<VideoModel, WebPageFirebaseAdapter.MyHolder> {

    private UserModel user;
    private Context context;

    public WebPageFirebaseAdapter(@NonNull FirebaseRecyclerOptions<VideoModel> options, UserModel user, Context context) {
        super(options);
        this.user = user;
        this.context = context;
    }


    class MyHolder extends RecyclerView.ViewHolder {
        WebView webView;
        TextView textVideoTitle, textVideoDescription;

        ImageView imgAvartar;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            webView = itemView.findViewById(R.id.webView);
            textVideoTitle = itemView.findViewById(R.id.textVideoTitle);
            textVideoDescription = itemView.findViewById(R.id.textVideoDescription);
            imgAvartar = itemView.findViewById(R.id.imgProfileTopRight);
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_web_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyHolder holder, int position, @NonNull VideoModel model) {
        holder.textVideoTitle.setText(model.getTitle());
        holder.textVideoDescription.setText(model.getDesc());

        WebSettings webSettings = holder.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        holder.webView.loadUrl(model.getUrl());

        // Hiển thị avatar và thông tin user
        if (user != null) { // Kiểm tra xem user đã được tải từ Firebase hay chưa
            Log.d("AVATAR_URL", "Avatar: " + user.getAvatar());
            // Hiển thị avatar
            Glide.with(holder.itemView.getContext())
                    .load(user.getAvatar())  // Load avatar từ URL
                    .placeholder(R.drawable.ic_person)  // Placeholder nếu không có avatar
                    .into(holder.imgAvartar);
        }
        holder.imgAvartar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("userEmail", user.getEmail());
                intent.putExtra("userAvatar", user.getAvatar());
                intent.putExtra("userQuantityVideo", user.getVideoQuantity());
                context.startActivity(intent);
            }
        });
    }
}
