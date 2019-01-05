package com.lsq.easton.filemanage.utils;
import com.lsq.easton.filemanage.activity.MainFragmentActivity;
import com.lsq.easton.filemanage.base.BaseApplication;
import com.lsq.easton.filemanage.bean.FileDao;
import com.lsq.easton.filemanage.bean.FileInfo;
import com.lsq.easton.filemanage.bean.MsgEvent;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
public class SendToCordova {
  public static void sendMsg() {
    List<FileInfo> mList = FileDao.queryAll();
    JSONObject jsonObject = new JSONObject();
    JSONArray jsonArray = new JSONArray();
    if (mList.size() != 0) {
      for (int i = 0; i < mList.size(); i++) {
        try {
          jsonObject.put(mList.get(i).getFileName(), mList.get(i).getFilePath());
          jsonArray.put(jsonObject);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }
    EventBus.getDefault().post(new MsgEvent(jsonArray.toString()));
    BaseApplication.getInstance().exit();
  }
}
