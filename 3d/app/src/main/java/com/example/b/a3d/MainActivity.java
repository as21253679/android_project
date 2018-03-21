package com.example.b.a3d;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.UUID;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends Activity {
    private static final String TAG = "THINBTCLIENT";
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket btSocket = null;
    private InputStream inputStream = null;
    private OutputStream outStream = null;
    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private boolean t1=false;
    private static String address = "30:14:12:18:25:71";
    private GlView mGlView;
    Thread Thread0;
    private String roll="",pitch="",yaw="";
    public static float rollf=0.0f,pitchf=3.0f,yawf=0.0f;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this,
                    "Bluetooth is not available.",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(this,
                    "Please enable your BT and re-run this program.",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        mGlView = new GlView(this);
        setContentView(mGlView);
    }
    @Override
    public void onStart() {
        super.onStart();
        Thread0 = new Thread(new Runnable() {
            @Override
            public void run() {
                //String mess = (edit.getText()).toString() ;
                String mess = "a999";
                char c;
                byte[] msgBuff = mess.getBytes();
                while (t1) {
                   /* try {
                        byte[] msgBuffer = "a".getBytes();
                        outStream.write(msgBuffer);
                    } catch (IOException e) {
                        Log.e(TAG, "ON RESUME: Exception during write.", e);
                    }*/
                    try {
                        while (inputStream.available() > 0) {
                            c = (char) inputStream.read();
                            if (c == 'a') {
                                c = (char) inputStream.read();
                                if (c == 'r') {
                                    roll = "";
                                    while (true) {
                                        c = (char) inputStream.read();
                                        if (c == 'r') break;
                                        roll += String.valueOf(c);
                                    }
                                }
                                c = (char) inputStream.read();
                                if (c == 'p') {
                                    pitch = "";
                                    while (true) {
                                        c = (char) inputStream.read();
                                        if (c == 'p') break;
                                        pitch += String.valueOf(c);
                                    }
                                }
                                c = (char) inputStream.read();
                                if (c == 'y') {
                                    yaw = "";
                                    while (true) {
                                        c = (char) inputStream.read();
                                        if (c == 'y') break;
                                        yaw += String.valueOf(c);
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "ON RESUME: Exception during write.", e);
                    }
                    try {
                        rollf = ((Float.parseFloat(roll))+1.55f)*58;
                        pitchf = ((Float.parseFloat(pitch))+1.55f)*58;
                        yawf = ((Float.parseFloat(yaw))+1.55f)*58;
                    }catch (Exception e)
                    {}
                    try {
                        Thread.sleep(100);
                    }
                    catch (Exception e)
                    {}
                }
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
       String message = "Hello message from client to server.";
        byte[] msgBuffer = message.getBytes();
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e(TAG, "ON RESUME: Socket creation failed.", e);
        }
        mBluetoothAdapter.cancelDiscovery();
        try {
            btSocket.connect();
            Log.e(TAG, "ON RESUME: BT connection established, data transfer link open.");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                Log.e(TAG,
                        "ON RESUME: Unable to close socket during connection failure", e2);
            }
        }
        try {
            outStream = btSocket.getOutputStream();
            inputStream = btSocket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "ON RESUME: Output stream creation failed.", e);
        }
        try {
            outStream.write(msgBuffer);
            t1=true;
        } catch (IOException e) {
            Log.e(TAG, "ON RESUME: Exception during write.", e);
        }
        Thread0.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "ON PAUSE: Couldn't flush output stream.", e);
            }
        }
        try {
            btSocket.close();
        } catch (IOException e2) {
            Log.e(TAG, "ON PAUSE: Unable to close socket.", e2);
        }
    }
}

class GlView extends GLSurfaceView {

    private GlRender mGlRender;
    public GlView(Context context){
        super(context);

        mGlRender = new GlRender();
        setRenderer(mGlRender);
    }
}

class GlRender extends MainActivity implements GLSurfaceView.Renderer {
    private final static int VERTS = 8;
    //頂點座標
    float cubeVtx[] = { -1.0f,  0.5f, -1.5f,   //v0
            -1.0f,  0.5f,  1.5f,   //v1
            1.0f,  0.5f,  1.5f,   //v2
            1.0f,  0.5f, -1.5f,   //v3
            -1.0f, -0.5f, -1.5f,   //v4
            -1.0f, -0.5f,  1.5f,   //v5
            1.0f, -0.5f,  1.5f,   //v6
            1.0f, -0.5f, -1.5f }; //v7
    //頂點索引
    short cubeInx[] = { 0, 1, 2,   //Top_Face1
            0, 2, 3,   //Top_Face2
            4, 6, 5,   //Bottom_Face1
            4, 7, 6,   //Bottom_Face2
            2, 6, 7,   //Left_face1
            2, 7, 3,   //Left_Face2
            0, 4, 1,   //Right_Face1
            1, 4, 5,   //Right_Face2
            1, 5, 6,   //Front_Face1
            1, 6, 2,   //Front_Face2
            0, 3, 4,   //Back_Face1
            3, 7, 4 }; //Back_Face2
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config){
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glHint(GL10.GL_POINT_SMOOTH_HINT, GL10.GL_FASTEST);
        gl.glClearDepthf(1.0f);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glEnable(GL10.GL_DEPTH_TEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl,int w,int h){
        gl.glViewport(0, 0, w, h);
        float ratio;
        ratio = (float)w/h;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
    }

    @Override
    public void onDrawFrame(GL10 gl){

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, 0, 0, 4, 0, 0, 0, 0, 1, 0);
        gl.glFrontFace(GL10.GL_CCW);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        //設置光源
        gl.glEnable(GL10.GL_LIGHT1);
        FloatBuffer lightAmbient = FloatBuffer.wrap(new float[]{0.2f,0.0f,0.0f,1.0f});
        FloatBuffer lightDiffuse = FloatBuffer.wrap(new float[]{1.0f,0.0f,0.0f,0.0f});
        FloatBuffer lightSpecular =  FloatBuffer.wrap(new float[]{1.0f,0.0f,0.0f,1.0f});
        FloatBuffer lightPosition = FloatBuffer.wrap(new float[]{1.0f,1.0f,1.0f,1.0f});
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, lightAmbient);
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, lightDiffuse);
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_SPECULAR, lightSpecular);
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, lightPosition);

        gl.glEnable(GL10.GL_LIGHTING);
        //動態旋轉
        long time = SystemClock.uptimeMillis()% 4000L;
        float angle = 0.190f * ((int)time);

//pitch   yaw   roll
           // gl.glRotatef(pitchf,pitchf/360,0.0f , 0.0f);   //pitch
        gl.glRotatef(77,1.0f, yawf/360 , 1.0f);   //yaw
      //  gl.glRotatef(rollf,0.0f,0.0f , rollf/360);   //roll
      //  gl.glRotatef(angle, 0.0f, 1.0f, 1.0f);

        //畫出方體
        drawCube( gl, cubeVtx , cubeInx );
    }

    private void drawCube(GL10 gl,float[] vtx, short[] inx){
        gl.glVertexPointer( 3, GL10.GL_FLOAT, 0, getFloatBuffer(vtx));
        //透過索引緩衝來畫三角面
        gl.glDrawElements(GL10.GL_TRIANGLES,
                inx.length,
                GL10.GL_UNSIGNED_SHORT,
                getShortBuffer(inx));
    }

    //頂點緩衝
    private FloatBuffer getFloatBuffer(float[] array ){
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(array);
        fb.position(0);
        return fb;
    }

    //索引緩衝
    private ShortBuffer getShortBuffer(short[] array ){
        ByteBuffer ib = ByteBuffer.allocateDirect(array.length * 2);
        ib.order(ByteOrder.nativeOrder());
        ShortBuffer sb = ib.asShortBuffer();
        sb.put(array);
        sb.position(0);
        return sb;
    }
}

