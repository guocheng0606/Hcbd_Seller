package com.android.hcbd.seller.ui.activity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hcbd.seller.MyApplication;
import com.android.hcbd.seller.R;
import com.android.hcbd.seller.bt.BasePrintActivity;
import com.android.hcbd.seller.bt.BluetoothController;
import com.android.hcbd.seller.bt.BluetoothUtil;
import com.android.hcbd.seller.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BluetoothActivity extends BasePrintActivity implements View.OnClickListener {

    private static final int REQUEST_OPEN_BT_CODE = 0x11;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.switch01)
    Switch switch01;
    @BindView(R.id.lv_paired_devices)
    ListView lvPairedDevices;
    @BindView(R.id.btn_goto_setting)
    Button btnGotoSetting;
    @BindView(R.id.btn_test_conntect)
    Button btnTestConntect;

    private DeviceListAdapter mAdapter;
    private int mSelectedPosition = -1;

    @Override
    public void onBluetoothStateChanged(Intent intent) {
        intiView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        ButterKnife.bind(this);
        if (!BluetoothController.getBluetoothAdapter().isEnabled()) {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.EVENT_OPEN_BLE));
        }
        intiView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillAdapter();
    }

    private void intiView() {
        mAdapter = new DeviceListAdapter(this);
        lvPairedDevices.setAdapter(mAdapter);
        lvPairedDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedPosition = position;
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        btnGotoSetting.setOnClickListener(this);
        btnTestConntect.setOnClickListener(this);
        switch01.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (MyApplication.getInstance().getmBluetoothDevice() == null) {
                        Toast.makeText(BluetoothActivity.this, "请先连接蓝牙打印机...", Toast.LENGTH_SHORT).show();
                        switch01.setChecked(false);
                        return;
                    }
                    MyApplication.getInstance().getMyService().flag = true;
                } else {
                    MyApplication.getInstance().getMyService().flag = false;
                }
            }
        });
    }

    /**
     * 从所有已配对设备中找出打印设备并显示
     */
    private void fillAdapter() {
        //推荐使用 BluetoothUtil.getPairedPrinterDevices()
        List<BluetoothDevice> printerDevices = BluetoothUtil.getPairedDevices();
        mAdapter.clear();
        mAdapter.addAll(printerDevices);
        refreshButtonText(printerDevices);
    }

    private void refreshButtonText(List<BluetoothDevice> printerDevices) {
        if (printerDevices.size() > 0) {
            btnGotoSetting.setText("配对更多设备");
        } else {
            btnGotoSetting.setText("还未配对打印机，去设置");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_goto_setting:
                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                break;
            case R.id.btn_test_conntect:
                connectDevice();
                break;
        }
    }

    private void connectDevice(){
        if(mSelectedPosition >= 0){
            BluetoothDevice device = mAdapter.getItem(mSelectedPosition);
            if(device!= null)
                super.connectDevice(device);
        }else{
            Toast.makeText(this, "还未选择打印设备", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OPEN_BT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                MyApplication.getInstance().getMyService().flag = true;
            } else if (resultCode == Activity.RESULT_CANCELED) {
                MyApplication.getInstance().getMyService().flag = false;
            }
        }
    }


    class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {

        public DeviceListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            BluetoothDevice device = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_bluetooth_device, parent, false);
            }

            TextView tvDeviceName = (TextView) convertView.findViewById(R.id.tv_device_name);
            CheckBox cbDevice = (CheckBox) convertView.findViewById(R.id.cb_device);

            tvDeviceName.setText(device.getName());

            cbDevice.setChecked(position == mSelectedPosition);

            if (-1 == mSelectedPosition) {
                if (MyApplication.getInstance().getmBluetoothDevice() != null){
                    if (device.getName().equals(MyApplication.getInstance().getmBluetoothDevice().getName())) {
                        cbDevice.setChecked(true);
                    }else{
                        cbDevice.setChecked(false);
                    }
                }
            }

            return convertView;
        }
    }

}
