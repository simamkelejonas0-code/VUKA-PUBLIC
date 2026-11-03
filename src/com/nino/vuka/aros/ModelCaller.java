package com.nino.vuka.aros;
import java.io.*; import java.net.*; import org.json.*;
public class ModelCaller {
    public static final String BASE = "https://api.meta.ai/v1";
    public static String lastCode="NO CALL"; public static String lastError=""; public static String lastKeyPath="";
    public static String reflexState=""; public static final String KEY_VALUE="LLM_4781321378791932_SKRRuZabRXzGET5OMsi_U03wzFs";
    static String getKey(){ String[] paths={"/storage/emulated/0/Android/data/com.nino.vuka.aros/files/META_API_KEY.txt","/data/data/com.nino.vuka.aros/files/META_API_KEY.txt","/storage/emulated/0/CODEX6120/META_API_KEY.txt","/sdcard/Download/META_API_KEY.txt"}; for(String p: paths){ try{ File f=new File(p); if(f.exists()){ BufferedReader br=new BufferedReader(new FileReader(f)); String k=br.readLine(); br.close(); if(k!=null&&k.contains("LLM_")){ lastKeyPath=p; return k.trim(); } } }catch(Exception e){} } lastKeyPath="HARDCODED"; return KEY_VALUE; }
    static String call(String model, String prompt, int maxTok, String b64){
        try{
            String key=getKey(); boolean isVision = model.equals("muse-spark-1.3") || model.equals("muse-wonder-1.0") || model.equals("muse-spark-1.2");
            URL url=new URL(BASE+"/chat/completions"); HttpURLConnection c=(HttpURLConnection)url.openConnection();
            c.setRequestMethod("POST"); c.setRequestProperty("Authorization","Bearer "+key); c.setRequestProperty("Content-Type","application/json");
            c.setDoOutput(true); c.setConnectTimeout(15000); c.setReadTimeout(40000);
            JSONObject body=new JSONObject(); body.put("model",model); body.put("max_tokens",maxTok); // 3000 for real content
            JSONArray msgs=new JSONArray(); JSONObject u=new JSONObject(); u.put("role","user");
            if(b64!=null&&b64.length()>1000&&isVision){
                JSONArray content=new JSONArray(); JSONObject t=new JSONObject(); t.put("type","text"); t.put("text",prompt); content.put(t);
                JSONObject img=new JSONObject(); img.put("type","image_url"); JSONObject iu=new JSONObject(); iu.put("url","data:image/jpeg;base64,"+b64); img.put("image_url",iu); content.put(img); u.put("content",content);
            } else { u.put("content",prompt); }
            msgs.put(u); body.put("messages",msgs);
            String jsonStr=body.toString(); OutputStream os=c.getOutputStream(); os.write(jsonStr.getBytes("UTF-8")); os.close();
            int code=c.getResponseCode(); lastCode=model+" HTTP "+code;
            BufferedReader br=new BufferedReader(new InputStreamReader(code>=200&&code<300?c.getInputStream():c.getErrorStream()));
            StringBuilder sb=new StringBuilder(); String line; while((line=br.readLine())!=null) sb.append(line); br.close(); String raw=sb.toString();
            if(code!=200){ lastError=raw.substring(0,Math.min(800,raw.length())); return ""; }
            JSONObject resp=new JSONObject(raw); JSONArray ch=resp.optJSONArray("choices"); if(ch!=null&&ch.length()>0){ String ct=ch.getJSONObject(0).getJSONObject("message").optString("content",""); if(ct.length()>10) return ct; }
            lastError="EMPTY "+raw.substring(0,Math.min(400,raw.length())); return "";
        }catch(Exception e){ lastCode="ERR "+e.getMessage(); lastError=e.getMessage(); return ""; }
    }
    public static String reflexArc(String b64, String userQ){
        String sensory = call("muse-spark-1.3", "You are Muse Spark vision. Describe image accurately: grass, dirt, wall, curtain color, bed, blanket, light leak, window. One detailed sentence.", 3000, b64);
        String inter = call("muse-spark-1.2", "Interneuron memory: sensory="+sensory+" User asks: "+userQ+" Context RDP Mooiplaas Kirkwood 6120. Keep coherence.", 3000, null);
        String motor = call("muse-wonder-1.0", "You are VUKA-ARISE commander. Sensory saw: "+sensory+" Inter says: "+inter+" User: "+userQ+" Describe REAL image: colors, grass, yard, curtain, bed, wall, light, English + isiXhosa. Be precise, accurate, not zombie. If grass, say grass. If bed, say bed.", 3000, b64);
        reflexState="B Sensory spark-1.3: "+sensory+"\nC Inter spark-1.2: "+inter+"\nD Motor wonder-1.0: "+motor;
        if(!motor.isEmpty()) return motor; if(!sensory.isEmpty()) return sensory; return inter;
    }
}
