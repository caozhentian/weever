package cn.people.weever.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.people.weever.R;
import cn.people.weever.activity.HomeActivity;
import cn.people.weever.activity.SubcribeResumeStopActivity;
import cn.people.weever.common.constant.APIOperationCode;
import cn.people.weever.model.Car;
import cn.people.weever.model.Driver;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.CarApiService;
import cn.people.weever.net.DriverApiService;
import cn.people.weever.service.CarService;
import cn.people.weever.service.DriverService;
import cn.people.weever.common.util.ToastUtil;


public class LoginActivity extends SubcribeResumeStopActivity {

    @BindView(R.id.edtUserName)
    EditText mEdtUserName;
    @BindView(R.id.edtPassword)
    EditText mEdtPassword;
    @BindView(R.id.spnCarNum)
    Spinner mSpnCarNum;
    @BindView(R.id.spnWorkTime)
    Spinner mSpnWorkTime;
    @BindView(R.id.btnLogin)
    Button mBtnLogin;

    private String[] carNums ;
    private Driver mLoginViewModel   ;
    private DriverService  mDriverService    ;
    private CarService     mCarService        ;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, LoginActivity.class);
        return intent;
    }

    @Override
    public void initData() {
        mLoginViewModel = new Driver() ;
        mDriverService =  new DriverService() ;
        //tetst
        Driver driver = new Driver() ;
        driver.setId("111");
        driver.setUserName("55");
        driver.setPassword("8989");
        mDriverService.save(driver);
        mCarService     = new CarService()     ;
        //查询车辆编号
        mCarService.query();
    }

    @Override
    public void initView() {

        mSpnWorkTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mLoginViewModel.setWorkTimeType(position );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                mLoginViewModel.setCardNum(carNums[pos]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        ButterKnife.bind(this);
        initView() ;
        initData() ;
    }

    @OnClick(R.id.btnLogin)
    public void onViewClicked() {
        String userName = mEdtUserName.getText().toString() ;
        mLoginViewModel.setUserName(userName);
        String password = mEdtPassword.getText().toString() ;
        mLoginViewModel.setPassword(password);
        String carNum = mSpnCarNum.getSelectedItem().toString() ;
        mLoginViewModel.setCardNum(carNum);
        try {
            mDriverService.login(mLoginViewModel);
            //startActivity(HomeActivity.newIntent(this));
        }catch(IllegalArgumentException e){
            ToastUtil.showToast(e.getMessage());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void processLoginEvent(@Nullable BaseModel<Driver> baseModel){
        if(baseModel.getApiOperationCode() == DriverApiService.TO_USER_LOGIN){
            Driver driver = baseModel.getData() ;
            mDriverService.save(driver);
            startActivity(HomeActivity.newIntent(this));
            finish() ;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void processCarNumEvent(@Nullable BaseModel<List<Car>> baseModel){
        if(baseModel.getApiOperationCode() == CarApiService.TO_CAR){
                List<Car> cars = baseModel.getData()  ;
                carNums = new String[cars.size()] ;
                for(int index = 0 ; index < cars.size() ; index++){
                    carNums[index] = cars.get(index).getNum() ;
                }
                initCardNum() ;
            }
    }


}
