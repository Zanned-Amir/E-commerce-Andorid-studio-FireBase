package com.sph.sbh.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.sph.sbh.Adapters.SizeAdapter;
import com.sph.sbh.Adapters.SliderAdapter;
import com.sph.sbh.Fragements.DescriptionFragment;
import com.sph.sbh.Fragements.ReviewFragment;
import com.sph.sbh.Fragements.SoldFragment;
import com.sph.sbh.Helper.ManagmentCart;
import com.sph.sbh.Model.ItemsDomain;
import com.sph.sbh.Model.SliderItems;
import com.sph.sbh.R;
import com.sph.sbh.databinding.ActivityDetailBinding;

import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    private ItemsDomain object;
    private  int numberOrder=1;
    private ManagmentCart managmentCart;
    private Handler slideHandle = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managmentCart = new ManagmentCart(this);
        getBundles();
        initbanners();
        initSize();
        setupViewPager();
    }

    private void initSize() {
        ArrayList<String> list= new ArrayList<>();
            list.add("S");
            list.add("M");
            list.add("L");
            list.add("XL");
            list.add("XXL");
            SizeAdapter adap =new SizeAdapter(list);
        binding.recyclerSize.setAdapter(adap);
            binding.recyclerSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
    }

    private void initbanners() {
        List<SliderItems> sliderItems = new ArrayList<>();
        for(int i =0;i<object.getPicUrl().size();i++){
            sliderItems.add(new SliderItems(object.getPicUrl().get(i)));

        }
        binding.viewpageSlider.setAdapter(new SliderAdapter((ArrayList<SliderItems>) sliderItems,binding.viewpageSlider));
        binding.viewpageSlider.setClipToOutline(false);
        binding.viewpageSlider.setClipChildren(false);
        binding.viewpageSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    private void getBundles() {
        object =(ItemsDomain) getIntent().getSerializableExtra("object");
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText(object.getPrice()+"dt");
        binding.ratingBar3.setRating((float) object.getRating());
        binding.ratingTxt.setText(object.getRating()+" Rating");

        binding.addToCartBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.setNumberinCart(numberOrder);
                managmentCart.insertItem(object);
            }
        });
        binding.backBtn.setOnClickListener(v -> finish());
    }
    private void setupViewPager(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        DescriptionFragment  tab1= new DescriptionFragment();
        ReviewFragment tab2 = new ReviewFragment();
        SoldFragment tab3 = new SoldFragment();

        Bundle bundle1 =new Bundle();
        Bundle bundle2 =new Bundle();
        Bundle bundle3 =new Bundle();

        bundle1.putString("description",object.getDescription());

        tab1.setArguments(bundle1);
        tab2.setArguments(bundle2);
        tab3.setArguments(bundle3);
        adapter.addFrag(tab1,"Description");
        adapter.addFrag(tab2,"Reviews");
        adapter.addFrag(tab3,"Sold");

        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

    }
    private class ViewPagerAdapter extends FragmentPagerAdapter{

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmenTitleList = new ArrayList<>();
        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFrag(Fragment fragment,String title)
        {
            mFragmentList.add(fragment);
            mFragmenTitleList.add(title);
        }
        @Override
        public  CharSequence getPageTitle(int position){
            return mFragmenTitleList.get(position);
        }
    }

}