package vn.iotstar.baitaptuan11_nguyentuanthanh_22110418.BaiTap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

import vn.iotstar.baitaptuan11_nguyentuanthanh_22110418.R;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText edemail, edpassword, edrppassword;
    private Button btnsignup;
    private TextView txtLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edemail = findViewById(R.id.edemail);
        edpassword = findViewById(R.id.edpassword);
        edrppassword = findViewById(R.id.edrppassword);
        btnsignup = findViewById(R.id.btnsignup);
        txtLogin = findViewById(R.id.txtLogin);
        mAuth = FirebaseAuth.getInstance();



        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edemail.getText().toString();
                String password = edpassword.getText().toString();
                String rppassword = edrppassword.getText().toString();
                if(email.equals("")||password.equals("")||rppassword.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "vui lòng nhập đầy đủ!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(rppassword)){
                    Toast.makeText(RegisterActivity.this,    "mật khẩu không khớp nhau!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidEmail(email)) {
                    Toast.makeText(RegisterActivity.this, "Địa chỉ email không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6 || !Character.isUpperCase(password.charAt(0))) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu phải có ít nhất 6 kí tự và viết hoa chữ cái đầu tiên!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Main", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // 🌿 Tạo user mới (giả sử bạn có class User đã tạo sẵn)
                                    UserModel newUser = new UserModel(user.getEmail(), 0, ""); // avatar để trống lúc đầu

                                    // 🌿 Lưu thông tin user vào Realtime Database
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                    ref.child(user.getUid()).setValue(newUser);
                                    Intent in = new Intent(RegisterActivity.this,LoginActivity.class);
                                    in.putExtra("email",email);
                                    in.putExtra("password",password);
                                    startActivity(in);
                                    Toast.makeText(RegisterActivity.this, "Đăng Kí Thành Công!",
                                            Toast.LENGTH_SHORT).show();

                                } else {
                                    Log.w("Main", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(in);
            }
        });

    }
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
