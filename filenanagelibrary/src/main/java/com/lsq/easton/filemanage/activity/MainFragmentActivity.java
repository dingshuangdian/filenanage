package com.lsq.easton.filemanage.activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import com.lsq.easton.filemanage.R;
import com.lsq.easton.filemanage.adapter.TabPagerAdapter;
import com.lsq.easton.filemanage.base.BaseApplication;
import com.lsq.easton.filemanage.base.baseActivity;
import com.lsq.easton.filemanage.bean.EventCenter;
import com.lsq.easton.filemanage.bean.FileDao;
import com.lsq.easton.filemanage.fragment.LocalMainFragment;
import java.util.ArrayList;
import java.util.List;
public class MainFragmentActivity extends baseActivity {
  private List<String> mTitleList = new ArrayList<>();
  private List<Fragment> fragments = new ArrayList<>();
  ViewPager main_viewpager;
//    @Bind(R2.id.main_top_rg)
//    RadioGroup main_top_rg;
//    @Bind(R2.id.top_rg_a)
//    RadioButton top_rg_a;
//    @Bind(R2.id.top_rg_b)
//    RadioButton top_rg_b;

  @Override
  public void onEventComming(EventCenter var1) {
  }

  @Override
  public boolean isBindEventBusHere() {
    return false;
  }

  @Override
  public void initViewAndEvent() {
    BaseApplication.getInstance().addActivity(MainFragmentActivity.this);
    main_viewpager = (ViewPager) findViewById(R.id.main_viewpager);


//        Log.e("cwj", "外置SD卡路径 = " + FileUtil.getStoragePath(this));
//        Log.e("cwj", "内置SD卡路径 = " + Environment.getExternalStorageDirectory().getAbsolutePath());
//        Log.e("cwj", "手机内存根目录路径  = " + Environment.getDataDirectory().getParentFile().getAbsolutePath());
    //fragments.add(new AllMainFragment());//全部
    fragments.add(new LocalMainFragment());//本机
    TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), mTitleList, fragments);
    main_viewpager.setAdapter(pagerAdapter);
//        main_top_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == top_rg_a.getId()) main_viewpager.setCurrentItem(0);
//                else if (checkedId == top_rg_b.getId()) main_viewpager.setCurrentItem(1);
//            }
//        });

    //设置默认选中页
    main_viewpager.setCurrentItem(0);
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_main_fragment;
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    FileDao.deleteAll1();
  }
  @Override
  public void onBackPressed() {
    finish();
//        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
//        builder.setTitle("提示");
//        builder.setMessage("留下再体验一下？");
//        builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        builder.setNegativeButton("不要了", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                finish();
//            }
//        });
//
//        builder.create().show();
  }
}
