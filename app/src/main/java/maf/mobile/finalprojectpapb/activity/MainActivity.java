package maf.mobile.finalprojectpapb.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import maf.mobile.finalprojectpapb.ChatFragment;
import maf.mobile.finalprojectpapb.HomeFragment;
import maf.mobile.finalprojectpapb.ProfileFragment;
import maf.mobile.finalprojectpapb.R;
import maf.mobile.finalprojectpapb.databinding.HomepageBinding;

public class MainActivity extends AppCompatActivity {

    HomepageBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item ->{
            if (item.getItemId() == R.id.homeItem){
                replaceFragment(new HomeFragment());
            }else if (item.getItemId() == R.id.chatItem){
                replaceFragment(new ChatFragment());
            }else if (item.getItemId() == R.id.profileItem){
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}