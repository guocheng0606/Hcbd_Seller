package com.android.hcbd.seller.bt;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.android.hcbd.seller.event.MessageEvent;
import com.android.hcbd.seller.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by liuguirong on 8/1/17.
 */

public class BluetoothController {

    private static BluetoothAdapter bluetoothAdapter;

    public static void init(Context context) {
        if (null == bluetoothAdapter)
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (null == bluetoothAdapter) {
            ToastUtils.showLongToast(context, "当前设备不支持蓝牙功能");
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.EVENT_OPEN_BLE));
        } else {

        }
    }

    public static BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public static boolean turnOnBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            return bluetoothAdapter.enable();
        }
        return false;
    }

}
