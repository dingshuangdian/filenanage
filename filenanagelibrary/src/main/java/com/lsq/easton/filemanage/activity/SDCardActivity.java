package com.lsq.easton.filemanage.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lsq.easton.filemanage.R;
import com.lsq.easton.filemanage.adapter.MultipleItem;
import com.lsq.easton.filemanage.adapter.MultipleItemQuickAdapter;
import com.lsq.easton.filemanage.base.baseActivity;
import com.lsq.easton.filemanage.bean.EventCenter;
import com.lsq.easton.filemanage.bean.FileDao;
import com.lsq.easton.filemanage.bean.FileInfo;
import com.lsq.easton.filemanage.utils.FileUtil;
import com.lsq.easton.filemanage.utils.SendToCordova;
import com.lsq.easton.filemanage.view.CheckBox;
import com.lsq.easton.filemanage.view.DividerItemDecoration;
import org.greenrobot.eventbus.EventBus;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static com.lsq.easton.filemanage.utils.FileUtil.fileFilter;
import static com.lsq.easton.filemanage.utils.FileUtil.getFileInfosFromFileArray;
public class SDCardActivity extends baseActivity {
  RecyclerView rlv_sd_card;
  TextView tv_path;
  TextView tv_all_size;
  TextView tv_send;
  ImageView iv_title_back;

  private List<FileInfo> fileInfos = new ArrayList<>();
  private List<MultipleItem> mMultipleItems = new ArrayList<>();
  private MultipleItemQuickAdapter mAdapter;
  private File mCurrentPathFile = null;
  private File mSDCardPath = null;
  private String path;

  TextView tv_title_middle;

  @Override
  public void onEventComming(EventCenter var1) {

  }

  @Override
  public boolean isBindEventBusHere() {
    return false;
  }

  @Override
  public void initViewAndEvent() {
    iv_title_back= (ImageView) findViewById(R.id.iv_title_back);
    tv_send = (TextView) findViewById(R.id.tv_send);
    rlv_sd_card = (RecyclerView) findViewById(R.id.rlv_sd_card);
    tv_path = (TextView) findViewById(R.id.tv_path);
    tv_all_size = (TextView) findViewById(R.id.tv_all_size);
    tv_title_middle = (TextView) findViewById(R.id.tv_title_middle);
    tv_all_size.setText(getString(R.string.size, "0B"));
    tv_send.setText(getString(R.string.send, "0"));
    path = getIntent().getStringExtra("path");
    tv_title_middle.setText(getIntent().getStringExtra("name"));
    mSDCardPath = new File(path);
    rlv_sd_card.setLayoutManager(new LinearLayoutManager(this));
    rlv_sd_card.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, R.drawable.divide_line));
    mAdapter = new MultipleItemQuickAdapter(mMultipleItems);
    rlv_sd_card.setAdapter(mAdapter);
    showFiles(mSDCardPath);
    updateSizAndCount();
    rlv_sd_card.addOnItemTouchListener(new OnItemClickListener() {
      @Override
      public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

        if (adapter.getItemViewType(position) == MultipleItem.FILE) {
          boolean isCheck = fileInfos.get(position).getIsCheck();
          fileInfos.get(position).setIsCheck(!isCheck);
          if (fileInfos.get(position).getIsCheck()) {
            FileDao.insertFile(fileInfos.get(position));
            ((CheckBox) view.findViewById(R.id.cb_file)).setChecked(true, true);
          } else {
            FileDao.deleteFile(fileInfos.get(position));
            ((CheckBox) view.findViewById(R.id.cb_file)).setChecked(false, true);
          }
          EventBus.getDefault().post(new EventCenter<>(3));
          updateSizAndCount();
        } else {
          showFiles(new File(fileInfos.get(position).getFilePath()));
        }

      }
    });
    iv_title_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        FileDao.deleteAll1();
        updateSizAndCount();
        if (mSDCardPath.getAbsolutePath().equals(mCurrentPathFile.getAbsolutePath())) {
          finish();
        } else {
          mCurrentPathFile = mCurrentPathFile.getParentFile();
          showFiles(mCurrentPathFile);
        }
      }
    });
  }

  public void updateSizAndCount() {
    final List<FileInfo> mList = FileDao.queryAll();
    long count = 0L;
    if (mList.size() == 0) {
      tv_send.setBackgroundResource(R.drawable.shape_bt_send);
      tv_send.setTextColor(getResources().getColor(R.color.md_grey_700));
      tv_all_size.setText(getString(R.string.size, "0B"));
    } else {
      tv_send.setBackgroundResource(R.drawable.shape_bt_send_blue);
      tv_send.setTextColor(getResources().getColor(R.color.md_white_1000));
      for (int i = 0; i < mList.size(); i++) {
        count = count + mList.get(i).getFileSize();
      }
      tv_all_size.setText(getString(R.string.size, FileUtil.FormetFileSize(count)));
    }
    tv_send.setText(getString(R.string.send, "" + mList.size()));
    tv_send.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        SendToCordova.sendMsg();
      }
    });
  }

  @Override
  public void onBackPressed() {
    FileDao.deleteAll1();
    updateSizAndCount();
    if (mSDCardPath.getAbsolutePath().equals(mCurrentPathFile.getAbsolutePath())) {
      finish();
    } else {
      mCurrentPathFile = mCurrentPathFile.getParentFile();
      showFiles(mCurrentPathFile);
    }
  }

  private void showFiles(File folder) {
    mMultipleItems.clear();
    tv_path.setText(folder.getAbsolutePath());
    mCurrentPathFile = folder;
    File[] files = fileFilter(folder);
    if (null == files || files.length == 0) {
      mAdapter.setEmptyView(getEmptyView());
      Log.e("files", "files::为空啦");
    } else {
      //获取文件信息
      fileInfos = getFileInfosFromFileArray(files);
      for (int i = 0; i < fileInfos.size(); i++) {
        if (fileInfos.get(i).isDirectory) {
          mMultipleItems.add(new MultipleItem(MultipleItem.FOLD, fileInfos.get(i)));
        } else {
          mMultipleItems.add(new MultipleItem(MultipleItem.FILE, fileInfos.get(i)));
        }

      }
      //查询本地数据库，如果之前有选择的就显示打钩
      List<FileInfo> mList = FileDao.queryAll();
      for (int i = 0; i < fileInfos.size(); i++) {
        for (FileInfo fileInfo : mList) {
          if (fileInfo.getFileName().equals(fileInfos.get(i).getFileName())) {
            fileInfos.get(i).setIsCheck(true);
          }
        }
      }
    }
    mAdapter.notifyDataSetChanged();
  }

  private View getEmptyView() {
    return getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup) rlv_sd_card.getParent(), false);
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_sdcard;
  }


}
