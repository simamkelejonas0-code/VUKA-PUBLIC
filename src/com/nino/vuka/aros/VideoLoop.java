package com.nino.vuka.aros;
import android.view.TextureView; import android.graphics.*; import android.hardware.Camera; import android.os.Handler; import android.os.Looper; import android.util.Base64; import java.io.ByteArrayOutputStream;
public class VideoLoop extends android.widget.FrameLayout implements TextureView.SurfaceTextureListener {
    MainActivity act; TextureView tv; public String lastJpegBase64=""; public boolean torchOn=false;
    Handler h=new Handler(Looper.getMainLooper()); Camera cam; boolean running=true;
    public VideoLoop(TextureView tv, MainActivity act){ super(act); this.tv=tv; this.act=act; tv.setSurfaceTextureListener(this); h.post(new VukaLoopRunnable(this)); }
    public void onSurfaceTextureAvailable(android.graphics.SurfaceTexture s,int w,int h){ openCam(); }
    public void onSurfaceTextureSizeChanged(android.graphics.SurfaceTexture s,int w,int h){}
    public boolean onSurfaceTextureDestroyed(android.graphics.SurfaceTexture s){ if(cam!=null){ try{ cam.stopPreview(); cam.release(); }catch(Exception e){} cam=null; } return false; }
    public void onSurfaceTextureUpdated(android.graphics.SurfaceTexture s){}
    void openCam(){
        try{
            if(!act.hasCamPerm()){ act.appendDiag("No CAM perm"); return; }
            if(cam!=null){ try{ cam.release(); }catch(Exception e){} }
            cam=Camera.open(); Camera.Parameters p=cam.getParameters();
            // FIX DIRECTION - 90 degree portrait
            cam.setDisplayOrientation(90);
            p.setPreviewSize(640,480); p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            cam.setParameters(p); cam.setPreviewTexture(tv.getSurfaceTexture()); cam.startPreview();
            act.appendDiag("Cam1 LIVE REAL 640x480 90deg - EYE OPEN - CORRECT DIRECTION");
        }catch(Exception e){ act.appendDiag("Cam OPEN FAIL "+e.getMessage()); try{ cam=Camera.open(0); cam.setDisplayOrientation(90); cam.setPreviewTexture(tv.getSurfaceTexture()); cam.startPreview(); }catch(Exception e2){ act.appendDiag("Cam FAIL2 "+e2.getMessage()); } }
    }
    public boolean toggleTorch(){
        try{ if(cam!=null){ Camera.Parameters p=cam.getParameters(); torchOn=!torchOn; p.setFlashMode(torchOn?Camera.Parameters.FLASH_MODE_TORCH:Camera.Parameters.FLASH_MODE_OFF); cam.setParameters(p); return torchOn; } }catch(Exception e){} torchOn=false; return false;
    }
}
class VukaLoopRunnable implements Runnable {
    VideoLoop loop; VukaLoopRunnable(VideoLoop l){loop=l;}
    public void run(){
        if(!loop.running) return;
        try{
            if(loop.tv.isAvailable() && loop.tv.getWidth()>50){
                // FIX DIRECTION - rotate bitmap 90deg
                Bitmap bmp=loop.tv.getBitmap(320,240);
                if(bmp!=null){
                    Matrix m=new Matrix(); m.postRotate(90);
                    Bitmap rotated=Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),m,true);
                    ByteArrayOutputStream baos=new ByteArrayOutputStream(); rotated.compress(Bitmap.CompressFormat.JPEG,35,baos); // quality 35 = ~8k-12k bytes valid JPEG
                    byte[] bytes=baos.toByteArray();
                    if(bytes.length>2000 && bytes.length<30000){
                        String b64=Base64.encodeToString(bytes,Base64.NO_WRAP); loop.act.onFrame(b64);
                    }
                    bmp.recycle(); rotated.recycle();
                }
            }
        }catch(Exception e){ loop.act.appendDiag("Loop err "+e.getMessage()); }
        loop.h.postDelayed(this, 1200);
    }
}
