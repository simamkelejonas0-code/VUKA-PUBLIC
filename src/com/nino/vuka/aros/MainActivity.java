package com.nino.vuka.aros;
import android.app.Activity; import android.os.Bundle; import android.widget.*; import android.view.*; import android.Manifest; import android.content.pm.PackageManager; import java.io.*; import java.util.concurrent.*;
public class MainActivity extends Activity {
    TextureView textureView; VideoLoop videoLoop; TextView chatLog, diagText, reflexText, signatureText; EditText talkInput;
    Button askBtn, torchBtn, clearBtn, diagBtn, permBtn; FrameLayout previewFrame; boolean torchOn=false; int frameChars=0;
    ScheduledExecutorService loopExecutor; String latestVision="0";
    @Override protected void onCreate(Bundle b){ super.onCreate(b);
        LinearLayout root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(0xFF0A0A0A);
        previewFrame=new FrameLayout(this); previewFrame.setLayoutParams(new LinearLayout.LayoutParams(-1, 700));
        textureView=new TextureView(this); previewFrame.addView(textureView, new FrameLayout.LayoutParams(-1,-1));
        videoLoop=new VideoLoop(textureView, this); previewFrame.addView(videoLoop); root.addView(previewFrame);
        diagText=new TextView(this); diagText.setTextColor(0xFF00FF88); diagText.setTextSize(9); diagText.setText("[FRAME LIVE 0] WAITING"); root.addView(diagText);
        reflexText=new TextView(this); reflexText.setTextColor(0xFFFFAA00); reflexText.setTextSize(7); reflexText.setText("Reflex Arc: A Eye -> B Sensory spark-1.3 -> C Inter spark-1.2 -> D Motor wonder-1.0 -> E SAM/voice/code"); root.addView(reflexText);
        chatLog=new TextView(this); chatLog.setTextColor(0xFFFFFFFF); chatLog.setTextSize(11); ScrollView sv=new ScrollView(this); sv.addView(chatLog); sv.setLayoutParams(new LinearLayout.LayoutParams(-1,0,1f)); root.addView(sv);
        talkInput=new EditText(this); talkInput.setHint("What do you see?"); talkInput.setTextColor(0xFFFFFFFF); talkInput.setHintTextColor(0xFF888888); root.addView(talkInput);
        LinearLayout row=new LinearLayout(this); askBtn=new Button(this); askBtn.setText("ASK ARISE - REAL EYE"); torchBtn=new Button(this); torchBtn.setText("TORCH OFF"); row.addView(askBtn, new LinearLayout.LayoutParams(0,-2,1f)); row.addView(torchBtn, new LinearLayout.LayoutParams(0,-2,1f)); root.addView(row);
        LinearLayout row2=new LinearLayout(this); clearBtn=new Button(this); clearBtn.setText("CLEAR"); diagBtn=new Button(this); diagBtn.setText("DIAG"); permBtn=new Button(this); permBtn.setText("GRANT CAM"); row2.addView(clearBtn, new LinearLayout.LayoutParams(0,-2,1f)); row2.addView(diagBtn, new LinearLayout.LayoutParams(0,-2,1f)); row2.addView(permBtn, new LinearLayout.LayoutParams(0,-2,1f)); root.addView(row2);
        // SIGNATURE small fonts but visible not shouting - as you requested
        signatureText=new TextView(this); signatureText.setTextColor(0xFF888888); signatureText.setTextSize(8); signatureText.setGravity(Gravity.CENTER);
        signatureText.setText("created by SIIMAMKELE NGXONONO from KIRKWOOD"); root.addView(signatureText);
        setContentView(root);
        try{ File dir=getExternalFilesDir(null); if(dir!=null&&!dir.exists()) dir.mkdirs(); File dest=new File(dir, "META_API_KEY.txt"); if(!dest.exists()){ FileWriter fw=new FileWriter(dest); fw.write(ModelCaller.KEY_VALUE); fw.close(); } chatLog.append("KEY READY "+dest.getPath()+"\n"); }catch(Exception e){}
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        loopExecutor=Executors.newScheduledThreadPool(2);
        loopExecutor.scheduleAtFixedRate(() -> { try{ String b64=videoLoop!=null?videoLoop.lastJpegBase64:""; if(b64==null||b64.length()<1000) return; String v=ModelCaller.call("muse-spark-1.3", "loop fast vision", 3000, b64); if(!v.isEmpty()) latestVision=v; }catch(Exception e){} }, 3, 5, TimeUnit.SECONDS);
        askBtn.setOnClickListener(v -> {
            String userQ=talkInput.getText().toString(); if(userQ.isEmpty()) userQ="What do you see? Describe real now, colors, curtain, bed, light, grass, yard.";
            if(frameChars<1000){ chatLog.append("EYE CLOSED - NO FRAME - Camera black - grant perm and reopen app\n"); return; }
            final String fQ=userQ; final String fB64=videoLoop.lastJpegBase64;
            chatLog.append("\nUSER: "+fQ+"\nFRAME: "+fB64.length()+" chars REAL EYE\nARISE REFLEX 6 MODELS calling with IMAGE 3000 tokens...\n");
            new Thread(() -> { String ans=ModelCaller.reflexArc(fB64, fQ); runOnUiThread(() -> { chatLog.append("ARISE REAL: "+ans+"\n"); diagText.setText("[FRAME LIVE "+frameChars+"] "+ModelCaller.lastCode); reflexText.setText(ModelCaller.reflexState); }); }).start();
        });
        permBtn.setOnClickListener(v -> { requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 101); chatLog.append("Grant perms then CLOSE and REOPEN app\n"); });
        torchBtn.setOnClickListener(v -> { boolean on=videoLoop.toggleTorch(); torchOn=on; torchBtn.setText(torchOn?"TORCH ON":"TORCH OFF"); });
        clearBtn.setOnClickListener(v -> chatLog.setText("")); diagBtn.setOnClickListener(v -> diagText.setText(ModelCaller.lastCode+" err:"+ModelCaller.lastError+" frame:"+frameChars+" vision:"+latestVision+"\n"+ModelCaller.reflexState));
    }
    public void onFrame(String b64){ frameChars=b64.length(); if(videoLoop!=null) videoLoop.lastJpegBase64=b64; runOnUiThread(() -> diagText.setText("[FRAME LIVE "+frameChars+" chars] | EYE OPEN | "+ModelCaller.lastCode)); }
    public void appendDiag(String s){ runOnUiThread(() -> { diagText.append("\n"+s); chatLog.append(s+"\n"); }); }
    public boolean hasCamPerm(){ return checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED; }
    @Override public void onRequestPermissionsResult(int c, String[] p, int[] r){ super.onRequestPermissionsResult(c,p,r); boolean ok=false; for(int i=0;i<p.length;i++) if(p[i].equals(Manifest.permission.CAMERA)&&r[i]==PackageManager.PERMISSION_GRANTED) ok=true; if(ok){ chatLog.append("CAM PERM GRANTED - REOPENING EYE...\n"); if(videoLoop!=null) videoLoop.openCam(); } else chatLog.append("CAM DENIED - Settings > Apps > VUKA-ARISE REFLEX > Permissions > Camera Allow\n"); }
}
