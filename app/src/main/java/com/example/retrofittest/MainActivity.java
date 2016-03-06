package com.example.retrofittest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.retrofittest.Bean.BaseData;
import com.example.retrofittest.Interface.EndPoint;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.edit_phone_num)
    EditText editPhoneNum;
    @Bind(R.id.do_search)
    Button doSearch;
    @Bind(R.id.result)
    TextView result;
    @Bind(R.id.tips)
    TextView tips;

    String baseUrl="http://apis.baidu.com/";

    String phone="18200174137";

    private boolean isDoSearch=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        editPhoneNum.addTextChangedListener(watcher);
    }

    @OnClick(R.id.do_search)
    void search(){

        if(isDoSearch){
            Retrofit retrofit=new Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            final EndPoint endPoint=retrofit.create(EndPoint.class);

            phone=editPhoneNum.getText().toString();

            rx.Observable.just(phone).map(new Func1<String, BaseData>() {
                @Override
                public BaseData call(String s) {
                    try {
                        return endPoint.getData(s).execute().body();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<BaseData>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("Error",e.getMessage());
                        }

                        @Override
                        public void onNext(BaseData baseData) {
                            if(baseData!=null){
                                String info="供应商:\t"+baseData.getRetData().getSupplier()+"\n\n"
                                        +"省份:\t"+baseData.getRetData().getProvince()+"\n\n"
                                        +"城市:\t"+baseData.getRetData().getCity()+"\n\n"
                                        +"套餐:\t"+baseData.getRetData().getSuit();
                                result.setText(info);
                            }

                        }
               });
        }
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()!=11){
                tips.setText("非法的电话号码");
                tips.setVisibility(View.VISIBLE);
                isDoSearch=false;
            }else{
                tips.setVisibility(View.GONE);
                isDoSearch=true;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
