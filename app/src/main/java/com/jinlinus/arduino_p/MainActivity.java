package com.jinlinus.arduino_p;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
// comment 작성 진행중....

public class MainActivity extends AppCompatActivity {
    private BluetoothSPP BluetoothC;
    // 블루투스를 이용하기 위한 변수를 먼저 선언한다.
    // private 접근 제한자를 사용하여 외부 클래스에서 접근하지 못하도록 한다.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothC = new BluetoothSPP(this);
        // BluetoothSPP 클래스의 인스턴스 변수인 BluetoothC를 선언한 다음
        // BluetoothC 객체를 생성하여 초기화 작업을 진행한다.
        if(!BluetoothC.isBluetoothAvailable())
        {// 블루투스가 사용 가능한 상태가 아닐 경우 아래와 같이 Toast 메시지를 출력하도록 한다.
            Toast.makeText(getApplicationContext(), "Bluetooth is not available"
            , Toast.LENGTH_SHORT).show();
            finish();
            // 액티비티를 종료시킨다.
        }

        BluetoothC.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            // 데이터를 받을 때 발생하는 리스너 이벤트를 작성한다.
            @Override
            public void onDataReceived(byte[] data, String message) {
                // 현재는 블루투스를 이용하여 send(전송) 버튼을 클릭할 경우에 텍스트를 전송하는 기능만 구현할
                // 예정이기 때문에 해당 사항은 작성하지 않았다.
            }
        });

        BluetoothC.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            // 블루투스 연결과 관련해서 발생할 수 있는 상황에 대한 리스너 이벤트를 정의한다.
            @Override
            public void onDeviceConnected(String name, String address) {
                // 기기가 연결되었을 경우 아래와 같이 연결되었다는 Toast 메시지를 출력하도록 한다.
                // 연결되었다는 문구와 함께 블루투스 기기의 mac address를 출력한다.
                Toast.makeText(getApplicationContext(), "Connected!" + name + "\n" + address
                , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeviceDisconnected() {
                // 기기 연결을 해제할 경우 연결이 해제되었다는 Toast 메시지를 출력하도록 한다.
                Toast.makeText(getApplicationContext(), "Disconnected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeviceConnectionFailed() {
                // 기기 연결에 실패할 경우 연결이 실패했다는 Toast 메시지를 출력하도록 한다.\
                // Toast,makeText()의 3번째 인수의 경우 Toast.LEGNTH_SHORT = 토스트 메시지를 출력하는 시간을
                // 나타낸 것으로 SHORT는 짧은 시간동안만 문구가 나오고 사라진다.
                // 반면에 LENGTH.LONG의 경우 긴 시간 동안 문구가 나왔다가 다시 사라진다.
                Toast.makeText(getApplicationContext(), "Device Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        Button btn01 = findViewById(R.id.btn01);
        // connect 버튼에 대한 변수 선언 및 id값 관련 설정
        // R클래스에서 Button view의 id값인 btn01을 받아온 다음 xml파일에서 설정한 속성들을 가져온다.

        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BluetoothC.getServiceState() == BluetoothState.STATE_CONNECTED)
                {// 만약 블루투스 기기가 연결되어 있는 상태라면 버튼을 클릭할 경우 연결을 끊어버리도록 한다.
                    BluetoothC.disconnect();
                }
                else
                {// 블루투스 기기가 연결되어 있지 않은 상태라면 기기 연결을 요청하는 창을 호출하도록 한다.
                    // Intent 클래스 변수 i1을 선언하여 기기 목록을 불러온다.
                    Intent i1 = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(i1, BluetoothState.REQUEST_CONNECT_DEVICE);
                    // startActivityForResult() = Intent를 사용하여 특정 Activity나 어플을 호출할 때
                    // 결과값을 받아오고자 한다면 이 함수를 사용한다.
                    // startActivity는 특정 액티비티나 앱을 호출하는 기능만 실행한다.
                }
            }
        });
    }

    public void onDestroy() {
        // 오버라이딩 메서드를 선언할 때
        // "Overriding method should call super.onDestroy" 에러 문구가 나타나는 것을 알 수 있다.
        // 이는 재정의한 메서드를 선언할 때 반드시 super.메서드명()을 작성해야 한다는 뜻이다.
        // 빨간 글씨로 나타난 메서드명을 클릭한 후 전구모양을 누를 경우 add super call이라는 항목이 나타난다.
        // 이 항목을 누르게 되면 아래와 같이 super.onDestory()라는 코드가 작성되면서
        // 에러가 사라지는 것을 알 수 있다.
        super.onDestroy();
        BluetoothC.stopService();
        // Bluetooth 연결을 중지시킨다.
    }

    public void onStart() {
        super.onStart();
        if(!BluetoothC.isBluetoothEnabled())
        {// 블루투스가 비활성화되어 있는 상황일 경우 앱 실행 시 블루투스 기능을 활성화시킬 것인지를 물어보는
            // dialog창을 호출한다.
            // startActivityForResult() 함수를 사용하여 결과값을 받아오도록 한다.
            // 사용자가 직접 선택하지 않고 강제로 블루투스를 활성화시키도록 하는 것은 권장하지 않는 방법이다.
            Intent i2 = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(i2, BluetoothState.REQUEST_ENABLE_BT);
        }
        else
        {// 블루투스가 활성화되어 있는 상황일 경우
            if(!BluetoothC.isServiceAvailable())
            {// 서비스를 이용할 수 있는 상태가 아닐 경우 블루투스 서비스를 시작하도록 한다.
                BluetoothC.setupService();
                BluetoothC.startService(BluetoothState.DEVICE_OTHER);
                setup();
            }
        }
    }

    public void setup()
    {
        Button btn02 = findViewById(R.id.btn02);
        // Text 전송 버튼 관련 설정
        // : R클래스에서 id값을 받아와서 xml파일에서 설정한 속성들을 가져온다.
        btn02.setOnClickListener(new View.OnClickListener() {
            // 전송 버튼을 클릭할 경우에 발생하는 리스너 이벤트를 정의한다.
            @Override
            public void onClick(View view) {
                BluetoothC.send("setText", true);
                // 사용자가 지정한 문자열을 Arduino Serial 모니터 화면으로 전송한다.
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 결과값을 받아와서 사용자에게 알려주는 메서드를 선언한다.
        // 메서드 상단에 Override라고 적혀있는 것을 볼 수 있는데 이것은 메서드가 재정의되었다는 것을 알려준다.
        super.onActivityResult(requestCode, resultCode, data);
        // 재정의된 메서드를 선언할 경우 super call을 추가해야 한다. --> add super call 항목 클릭 시 에러가 사라진다.
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            // 요청 코드가 연결을 요청하는 기기 목록일 경우
            if (resultCode == Activity.RESULT_OK)
                // 결괴 코드가 RESULT_OK일 경우
                BluetoothC.connect(data);
                // 블루투스를 연결시킨다.
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            // 요청 코드가 사용 가능한 블루투스 기기일 경우
            if (requestCode == Activity.RESULT_OK) {
                // 요청 코드가 RESULT_OK일 경우
                // 블루투스 서비스를 시작하도록 한다.
                BluetoothC.setupService();
                BluetoothC.startService(BluetoothState.DEVICE_OTHER);
                setup();
            } else {
                // 블루투스가 사용 가능한 상태가 아닐 경우
                // 아래와 같이 Toast 메시지를 출력하도록 하고 액티비티를 종료시킨다.
                Toast.makeText(getApplicationContext(), "Bluetooth not enabled!"
                        , Toast.LENGTH_SHORT).show();
                finish();
                // finish() --> 액티비티를 종료시키는 함수이다.
            }
        }
    }
}