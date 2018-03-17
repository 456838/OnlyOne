package com.duowan.liveshow.controller.fm.live;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.duowan.liveshow.R;
import com.duowan.liveshow.entity.contacts.SortModel;
import com.duowan.liveshow.utils.CharacterParser;
import com.duowan.liveshow.utils.PinyinComparator;
import com.duowan.liveshow.view.adapter.ContactsSortAdapter;
import com.salton123.base.BaseSupportFragment;
import com.salton123.mvp.util.RxUtil;
import com.salton123.onlyonebase.view.widget.SideBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;


/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/6/20 15:57
 * Time: 15:57
 * Description:
 */
public class ContactsFragement extends BaseSupportFragment {
    ListView lv_songs;
    EditText et_search;
    ImageView iv_clearText;
    private SideBar sb_letter;
    private TextView tv_hintLetter;
    private List<SortModel> mAllContactsList;
    private ContactsSortAdapter mAdapter;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    @Override
    public int GetLayout() {
        return R.layout.fm_contacts;
    }

    @Override
    public void InitVariable(Bundle savedInstanceState) {

    }

    @Override
    public void InitViewAndData() {
        sb_letter = (SideBar) f(R.id.sidrbar);
        tv_hintLetter = (TextView) f(R.id.dialog);
        sb_letter.setTextView(tv_hintLetter);
        iv_clearText = (ImageView) f(R.id.ivClearText);
        et_search = (EditText) f(R.id.et_search);
        lv_songs = (ListView) f(R.id.lv_contacts);
        characterParser = CharacterParser.getInstance();
        mAllContactsList = new ArrayList<SortModel>();
        pinyinComparator = new PinyinComparator();
        Collections.sort(mAllContactsList, pinyinComparator);// 根据a-z进行排序源数据
        mAdapter = new ContactsSortAdapter(getContext(), mAllContactsList);
        lv_songs.setAdapter(mAdapter);
        lv_songs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void InitListener() {
        /**清除输入字符**/
        iv_clearText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                et_search.setText("");
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable e) {

                String content = et_search.getText().toString();
                if ("".equals(content)) {
                    iv_clearText.setVisibility(View.INVISIBLE);
                } else {
                    iv_clearText.setVisibility(View.VISIBLE);
                }
                if (content.length() > 0) {
                    ArrayList<SortModel> fileterList = (ArrayList<SortModel>) search(content);
                    mAdapter.updateListView(fileterList);
                    //mAdapter.updateData(mContacts);
                } else {
                    mAdapter.updateListView(mAllContactsList);
                }
                lv_songs.setSelection(0);

            }

        });

        //设置右侧[A-Z]快速导航栏触摸监听
        sb_letter.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    lv_songs.setSelection(position);
                }
            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        loadLocalMusic();
    }

    private void loadLocalMusic() {
        Observable.create(new ObservableOnSubscribe<List<SortModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<SortModel>> e) throws Exception {
                mAllContactsList = new ArrayList<SortModel>();
                String sortOrder = MediaStore.Audio.Media.TITLE + " asc";
                Cursor cs =_mActivity.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, sortOrder);
                if (cs != null) {
                    while (cs.moveToNext()) {
                        long music_id = cs.getLong(cs.getColumnIndex(MediaStore.Audio.Media._ID)); // 音乐id
                        String music_name = cs.getString(cs.getColumnIndex((MediaStore.Audio.Media.TITLE)));
                        String music_singer = cs.getString(cs.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        String music_path = cs.getString(cs.getColumnIndex(MediaStore.Audio.Media.DATA));
                        long music_duration = cs.getLong(cs.getColumnIndex(MediaStore.Audio.Media.DURATION));
                        int music_is_music = cs.getInt(cs.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
                        SortModel sortModel = new SortModel(music_name, music_singer, music_name);
                        File file = new File(music_path);
                        if (file.exists() && file.isFile() && file.length() >= 1 * 1024 * 1024) {
                            sortModel.setSize(file.length());
                            mAllContactsList.add(sortModel);
                        }
                    }
                    cs.close();
                }
                e.onNext(mAllContactsList);
            }
        }).compose(RxUtil.<List<SortModel>>rxSchedulerHelper()).subscribe(new Consumer<List<SortModel>>() {
            @Override
            public void accept(List<SortModel> sortModels) throws Exception {
                Collections.sort(mAllContactsList, pinyinComparator);
                mAdapter.updateListView(mAllContactsList);
            }
        });
    }


    /**
     * 模糊查询
     */
    private List<SortModel> search(String str) {
        List<SortModel> filterList = new ArrayList<SortModel>();// 过滤后的list
        //if (str.matches("^([0-9]|[/+])*$")) {// 正则表达式 匹配号码
        if (str.matches("^([0-9]|[/+]).*")) {// 正则表达式 匹配以数字或者加号开头的字符串(包括了带空格及-分割的号码)
            String simpleStr = str.replaceAll("\\-|\\s", "");
            for (SortModel contact : mAllContactsList) {
                if (contact.musicSinger != null && contact.musicName != null) {
                    if (contact.simpleNumber.contains(simpleStr) || contact.musicName.contains(str)) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        } else {
            for (SortModel contact : mAllContactsList) {
                if (contact.musicSinger != null && contact.musicName != null) {
                    //姓名全匹配,姓名首字母简拼匹配,姓名全字母匹配
                    boolean isNameContains = contact.musicName.toLowerCase(Locale.CHINESE)
                            .contains(str.toLowerCase(Locale.CHINESE));

                    boolean isSortKeyContains = contact.sortKey.toLowerCase(Locale.CHINESE).replace(" ", "")
                            .contains(str.toLowerCase(Locale.CHINESE));

                    boolean isSimpleSpellContains = contact.sortToken.simpleSpell.toLowerCase(Locale.CHINESE)
                            .contains(str.toLowerCase(Locale.CHINESE));

                    boolean isWholeSpellContains = contact.sortToken.wholeSpell.toLowerCase(Locale.CHINESE)
                            .contains(str.toLowerCase(Locale.CHINESE));

                    if (isNameContains || isSortKeyContains || isSimpleSpellContains || isWholeSpellContains) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        }
        return filterList;
    }

}
