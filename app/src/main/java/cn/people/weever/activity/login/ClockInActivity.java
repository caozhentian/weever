package cn.people.weever.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.people.weever.R;
import cn.people.weever.activity.SubcribeCreateDestroyActivity;
import cn.people.weever.application.WeeverApplication;
import cn.people.weever.common.util.PreferencesUtil;
import cn.people.weever.map.TraceService;
import cn.people.weever.model.Car;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.CarApiService;
import cn.people.weever.net.DriverApiService;
import cn.people.weever.service.CarService;
import cn.people.weever.service.DriverService;


/**
 *   关于我们
 * Created by Administrator on 2017/1/10 0010.
 */

public class ClockInActivity extends SubcribeCreateDestroyActivity {

    private ImageView  iv_back;
    private TextView   tv_title;
    @BindView(R.id.btnClockIn)
    Button     mBtnClockIn ;
    @BindView(R.id.spnCarNum)
    Spinner mSpnCarNum;
    @BindView(R.id.spnWorkTime)
    Spinner mSpnWorkTime;

    private int    mWorkTimeType  ;

    private String  mCarNum        ;

    private String[] carNums       ;

    private DriverService mDriverService    ;
    private CarService mCarService        ;


    public static final Intent newIntent(Context packageContext){
        Intent intent   =  new Intent(packageContext , ClockInActivity.class) ;
        return intent  ;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_clock_in);
        ButterKnife.bind(this) ;
        initView();
        initData();
    }

    @Override
    public void initData() {
        mDriverService =  new DriverService() ;
        mCarService     = new CarService()     ;
        //查询车辆编号
        mCarService.query();
    }

    @Override
    public void initView() {

        iv_back = (ImageView)findViewById(R.id.img_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClockInActivity.this.finish();
            }
        });
        tv_title.setText(getString(R.string.work_clock_in));
        mSpnWorkTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mWorkTimeType = (position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBtnClockIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDriverService.clockIn(mWorkTimeType , mCarNum);
            }
        });
    }



    private void initCardNum(){
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, carNums);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        mSpnCarNum .setAdapter(adapter);
        mSpnCarNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                mCarNum = carNums[pos] ;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void processCarNumEvent(@Nullable BaseModel<List<Car>> baseModel){
        if(baseModel.getApiOperationCode() == CarApiService.TO_CAR){
            List<Car> mCarList = baseModel.getData()  ;
            Logger.d(mCarList)  ;
            carNums = new String[mCarList.size()] ;
            for(int index = 0 ; index < mCarList.size() ; index++){
                carNums[index] = mCarList.get(index).getNum() ;
            }
            initCardNum() ;
        }
    }

    public void processClockInEvent(@Nullable BaseModel<List<Car>> baseModel){
        if(baseModel.getApiOperationCode() == DriverApiService.TO_USER_CLOCK_IN){
            PreferencesUtil.setPreferences(WeeverApplication.getInstance(),"CAR_KEY",mCarNum);
            TraceService.getInstance(this).startTrace(null);
            TraceService.getInstance(WeeverApplication.getInstance()).setEntityName(mCarNum)  ;
            showToast(baseModel.getMessage());
            finish();
        }
    }

    protected<T> void dealSuccess(BaseModel baseModel){
        if(baseModel.getApiOperationCode() == DriverApiService.TO_USER_CLOCK_IN){
            processClockInEvent(baseModel) ;
        }
        else if(baseModel.getApiOperationCode() == CarApiService.TO_CAR){
            processCarNumEvent(baseModel);
        }
    }
}
