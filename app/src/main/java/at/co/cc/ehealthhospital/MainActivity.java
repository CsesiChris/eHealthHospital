package at.co.cc.ehealthhospital;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Scanner;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "eH-Hospital";

    static CharsetEncoder asciiEncoder = Charset.forName("ISO-8859-1").newEncoder();

    //private TextView textOut;

    private NfcAdapter nfcAdapter;

    private String eHealtWalletMacAdress;

    // Unique UUID for this application
    private static final UUID MY_UUID_SECURE =
            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_TRANSFER_INFO = 6;

    public static final String INFO = "info";

    public static final String STREAM_STOP = "###END###";

    private final BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();

    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    private int mState;

    private static TabLayout tabLayout;

    private static ImageView clear;

    public static PagerAdapter adapter;

    public static ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Intent i = getIntent();

 //       textOut = (TextView) findViewById(R.id.textout);
 //       textOut.setMovementMethod(new ScrollingMovementMethod());

        //mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
    //    mViewPager = (ViewPager) findViewById(R.id.container);
    //    setupViewPager(mViewPager);

     //   TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
     //   tabLayout.setupWithViewPager(mViewPager);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 //       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
 //       setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Log"));
        tabLayout.addTab(tabLayout.newTab().setText("FHIR record"));
        tabLayout.addTab(tabLayout.newTab().setText("Image"));
        tabLayout.addTab(tabLayout.newTab().setText("Error"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        clear = (ImageView)findViewById(R.id.clear);

        clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
             /**

                ImageView pic = (ImageView) view.findViewById(R.id.pic);
                if (pic != null) {
                    pic.setVisibility(ImageView.INVISIBLE);
                }

         //       TextView textInfo = (TextView) view.findViewById(R.id.info);
          //      if (textInfo != null) {
          //          textInfo.setVisibility(TextView.INVISIBLE);
          //      }

                TextView textout = (TextView) view.findViewById(R.id.textout);
                if (textout != null) {
                    textout.setVisibility(TextView.INVISIBLE);
                }
*/

                Tab1Fragment frag1 = (Tab1Fragment)adapter.getItem(0);
                frag1.hideText();

                Tab2Fragment frag2 = (Tab2Fragment)adapter.getItem(1);
                frag2.hideText();

                Tab3Fragment frag3 = (Tab3Fragment)adapter.getItem(2);
                frag3.hidePictureDummy();

                /*
                Toast.makeText(MainActivity.this,
                        "Remove sachen",
                        Toast.LENGTH_LONG).show(); */
            }
        });



        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(MainActivity.this,
                    "nfcAdapter==null, no NFC adapter exists",
                    Toast.LENGTH_LONG).show();
        } else if (!nfcAdapter.isEnabled()) {
            Toast.makeText(MainActivity.this,
                    "Please enable NFC",
                    Toast.LENGTH_LONG).show();
        } else {
            // Toast.makeText(MainActivity.this,
            //        "Waiting for Wallet ...",
            //        Toast.LENGTH_LONG).show();

            Log.i(LOG_TAG, "NFC adapter started ...");

 //           ((Tab1Fragment)((SectionsPageAdapter)mViewPager.getAdapter()).getItem(0)).appendToTextInfo("NFC enabled ... \nWaiting for Wallet ...");
 //           Log.i(LOG_TAG, "TODO");

            //textInfo.append("NFC enabled ... \nWaiting for Wallet ...");

            //nfcAdapter.setNdefPushMessageCallback(this, this);
            //nfcAdapter.setOnNdefPushCompleteCallback(this, this);

         //   transferInfo("NFC enabled ...");
         //   transferInfo("Waiting for Wallet ...");
        }

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
  //      if (id == R.id.action_settings) {
  //          return true;
  //      }

        return super.onOptionsItemSelected(item);
    }


//    private void setupViewPager(ViewPager viewPager) {
//        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
//        mSectionsPageAdapter.addFragment(new Tab1Fragment(), "Communication");
//        mSectionsPageAdapter.addFragment(new Tab2Fragment(), "FHIR record");
//        mSectionsPageAdapter.addFragment(new Tab3Fragment(), "Image");
//        viewPager.setAdapter(mSectionsPageAdapter);
//    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String action = intent.getAction();
        Log.i(LOG_TAG, "In RESUME: " + action);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Intent intent = getIntent();
        String action = intent.getAction();

        Log.i(LOG_TAG, "In RESUME Fragments: " + action);

        if (action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            Parcelable[] parcelables =
                    intent.getParcelableArrayExtra(
                            NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage inNdefMessage = (NdefMessage) parcelables[0];
            NdefRecord[] inNdefRecords = inNdefMessage.getRecords();
            NdefRecord NdefRecord_0 = inNdefRecords[0];
            String inMsg = new String(NdefRecord_0.getPayload());

            eHealtWalletMacAdress = inMsg;

            transferInfo("Wallet detected (" +inMsg + ")");

            // Get the BluetoothDevice object
            BluetoothDevice device = mAdapter.getRemoteDevice(eHealtWalletMacAdress);
            // Attempt to connect to the device
            connect(device, false);
        }
    }


    private void transferInfo(String str) {
        Message msg = mHandler.obtainMessage(MESSAGE_TRANSFER_INFO);
        Bundle bundle = new Bundle();
        bundle.putString(INFO, str);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    public synchronized void connect(BluetoothDevice device, boolean secure) {
        Log.d(LOG_TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device, secure);
        mConnectThread.start();

 //       Message msg = mHandler.obtainMessage(MESSAGE_TRANSFER_INFO);
 //       Bundle bundle = new Bundle();
 //       bundle.putString(INFO, "Connect via Bluetooth ...");
//        msg.setData(bundle);
 //       mHandler.sendMessage(msg);

        transferInfo("Connect via Bluetooth ...");

        //connected

        // Update UI title
//        updateUserInterfaceTitle();
    }


    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     * @param device The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice
            device, final String socketType) {
        Log.d(LOG_TAG, "connected, Socket Type:" + socketType);

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket, socketType);

     //   Message msg = mHandler.obtainMessage(MESSAGE_TRANSFER_INFO);
     //   Bundle bundle = new Bundle();
     //   bundle.putString(INFO, "Start transfer via Bluetooth ...");
     //   msg.setData(bundle);
     //   mHandler.sendMessage(msg);

        transferInfo("Start transfer via Bluetooth ...");

        mConnectedThread.start();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {

        Activity a = getParent();

        /*
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter adapter = (PagerAdapter)viewPager.getAdapter();
        Tab1Fragment frag1 = (Tab1Fragment)adapter.getItem(0);
        Tab2Fragment frag2 = (Tab2Fragment) adapter.getItem(1);
        Tab3Fragment frag3 = (Tab3Fragment) adapter.getItem(2);
*/

        @Override
        public void handleMessage(Message msg) {

            Log.d(LOG_TAG, "handleMessage:" + msg.what);

            //final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            //PagerAdapter adapter = (PagerAdapter)viewPager.getAdapter();

  //          final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
  //          PagerAdapter adapter = (PagerAdapter)viewPager.getAdapter();
            Tab1Fragment frag1 = (Tab1Fragment) adapter.getItem(0);
            Tab2Fragment frag2 = (Tab2Fragment) adapter.getItem(1);
            Tab3Fragment frag3 = (Tab3Fragment) adapter.getItem(2);


            switch (msg.what) {

                case MESSAGE_TRANSFER_INFO:

                    //Tab1Fragment frag1 = (Tab1Fragment)adapter.getItem(0);
                    frag1.appendToTextInfo(msg.getData().getString(INFO));
                    break;

                case MESSAGE_TOAST:

                        Toast.makeText(a, msg.getData().getString("TOAST"),
                                Toast.LENGTH_SHORT).show();
                    break;

                case MESSAGE_READ:
                        byte[] readBuf = (byte[]) msg.obj;
                        // construct a string from the valid bytes in the buffer
                        String readMessage = new String(readBuf, 0, msg.arg1);
                        //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);

                        Log.d(LOG_TAG, "message:" + readMessage);

                        // textOut.append(readMessage);
                        // Log.d(LOG_TAG, "\n" +readMessage);

                        if (readMessage.startsWith(STREAM_STOP)) {
                            Log.d(LOG_TAG, STREAM_STOP + ":\n" + readMessage);
                            //Tab1Fragment frag1 = (Tab1Fragment)adapter.getItem(0);
                            //frag1.onCreateView(getLayoutInflater(), viewPager, null);
                            frag1.appendToTextInfo("Transfer finished ...");
                            //Tab3Fragment frag3 = (Tab3Fragment) adapter.getItem(2);
                            frag3.onCreateView(getLayoutInflater(), viewPager, null);
                            //frag3.showPicture(readBuf);
                            frag3.showPictureDummy();
                        } else if (isPuretext(readMessage)) {
                            //Log.d(LOG_TAG, "message:" + readMessage);
                            //Tab2Fragment frag2 = (Tab2Fragment) adapter.getItem(1);
                            frag2.appendToTextInfo(readMessage);
                        } else {
                            //Tab3Fragment frag3 = (Tab3Fragment) adapter.getItem(2);
                            Log.i(LOG_TAG, "PICTURE Data!!!");
                            //frag3.appendPictureData(readBuf);
                        }



                        //Toast.makeText(a, readMessage, Toast.LENGTH_SHORT).show();
                        //setStatus(R.string.title_not_connected);
                    break;

            }
        }
    };

    private static boolean isPuretext(String v) {
        return asciiEncoder.canEncode(v);
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private String mSocketType;

        public ConnectThread(BluetoothDevice device, boolean secure) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                if (secure) {
                    tmp = device.createRfcommSocketToServiceRecord(
                            MY_UUID_SECURE);
                } else {
                    tmp = device.createInsecureRfcommSocketToServiceRecord(
                            MY_UUID_INSECURE);
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Socket Type: '" + mSocketType + "' create() failed", e);
            }
            mmSocket = tmp;
            mState = STATE_CONNECTING;
        }

        public void run() {
            Log.i(LOG_TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
            setName("ConnectThread" + mSocketType);

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(LOG_TAG, "unable to close() " + mSocketType +
                            " socket during connection failure", e2);
                }
//                connectionFailed();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (MainActivity.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice, mSocketType);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "close() of connect " + mSocketType + " socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket, String socketType) {
            Log.d(LOG_TAG, "create ConnectedThread: " + socketType);
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(LOG_TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            mState = STATE_CONNECTED;
        }

        public void run() {
            Log.i(LOG_TAG, "BEGIN mConnectedThread");
 //           byte[] buffer = new byte[8024];
 //           int bytes = 0;

            // Keep listening to the InputStream while connected
            Log.i(LOG_TAG, "State:" + mState);
            int i = 1;
            while (mState == STATE_CONNECTED) {
               try {
//                   if (i < 1000) {
//                       Log.i(LOG_TAG, "Loop:" + i + " : " + bytes);
//                       i++;
//                   }
                    // Read from the InputStream
                    //bytes = mmInStream.read(buffer);

                   //bytes = mmInStream.read(buffer);
                   //String readMessage = new String(buffer, 0, bytes);


//                   BufferedInputStream bis = new BufferedInputStream(mmInStream);
//                   ByteArrayOutputStream buf = new ByteArrayOutputStream();
//                   int result = bis.read();
//                   while(result != -1) {
//                       buf.write((byte) result);
//                       result = bis.read();
//                   }
//                   //StandardCharsets.UTF_8.name() > JDK 7
//                   String fhirRecord =  buf.toString("UTF-8");
                   //Log.i(LOG_TAG, readMessage);

                   //textOut.setText(readMessage);

                   // Read from the InputStream
                   //bytes = mmInStream.read(buffer);

                   int nRead;
                   byte[] data = new byte[4000];

                   //mmInStream.read();


                   //while ((nRead = mmInStream.read(data, 0, data.length)) != -1) {
                   while ((nRead = mmInStream.read(data)) != -1) {
                       //buffer.write(data, 0, nRead);
                       Log.i(LOG_TAG, "**** READ STREAM");
                       // Send the obtained bytes to the UI Activity
                       mHandler.obtainMessage(MESSAGE_READ, data.length, -1, data)
                               .sendToTarget();
                   }




                   //Imageread



//                //    Scanner s = new Scanner(mmInStream).useDelimiter("\\A");
//                //    String fhirRecord = s.hasNext() ? s.next() : "";
//                    // Send the obtained bytes to the UI Activity
////                    mHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer)
////                            .sendToTarget();
//
//                    Toast.makeText(MainActivity.this,
//                            fhirRecord,
//                            Toast.LENGTH_LONG).show();


 //                  Message msg = mHandler.obtainMessage(MESSAGE_TRANSFER_INFO);
 //                  Bundle bundle = new Bundle();
 //                  bundle.putString(INFO, "FHIR health record transfer, part " + i);

 //                  msg.setData(bundle);
 //                  mHandler.sendMessage(msg);

                   transferInfo("FHIR health record transfer, part " + i);
                   i++;

                } catch (Exception e) {
                    Log.e(LOG_TAG, "disconnected", e);
//                    connectionLost();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
//                mHandler.obtainMessage(MESSAGE_WRITE, -1, -1, buffer)
//                        .sendToTarget();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                Log.e(LOG_TAG, "close() of connect socket");
                mmSocket.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "close() of connect socket failed", e);
            }
        }
    }
}