package comment_clawer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class wangyiCommentClawer {
	HashSet<String> hs = new HashSet<>();
	static int page = 0;
	static boolean flag = true;
	static String url;
	static File file;
	static PrintWriter pw= null;
	private String urlChanger(String articalUrl,int page){
		int pos1;  
        int pos2;
        pos1 = articalUrl.lastIndexOf("/");  
		pos2 = articalUrl.lastIndexOf(".");  
		String commentId= articalUrl.substring(pos1 + 1, pos2);  
		String comurl = "http://comment.news.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/"
				+ "threads/"
				+  commentId
				+ "/comments/hotList?"
				+ "offset="
				+ page
				+ "&limit=30&showLevelThreshold=72&headLimit=1&tailLimit=2&callback=getData&ibc=newspc";
		return comurl;

	}
	private String sendGet(String requestUrl) throws Exception{  
        String result = "";  
        BufferedReader in = null;  
        try {  
            URL realUrl = new URL(requestUrl);  
            // 打开和URL之间的连接  
            URLConnection connection = realUrl.openConnection();  
            // 设置通用的请求属性  
            connection.setRequestProperty("accept", "*/*");  
            connection.setRequestProperty("connection", "Keep-Alive");  
            connection.setRequestProperty("user-agent",  
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");  
            // 建立实际的连接  
            connection.connect();  
  
            // 定义 BufferedReader输入流来读取URL的响应  
            in = new BufferedReader(new InputStreamReader(  
                    connection.getInputStream()));  
            String line;  
            while ((line = in.readLine()) != null) {  
                result += line;  
            }  
        } catch (Exception e) {             
        }  
        // 使用finally块来关闭输入流  
        finally {  
            try {  
                if (in != null) {  
                    in.close();  
                }  
            } catch (Exception e2) {  
                e2.printStackTrace();  
            }  
        }  
        int index1 = result.indexOf("{");  
        int index2 = result.lastIndexOf("}");  
        result = result.substring(index1,index2+1);
        //System.out.println(result);
        return result;  
    }  
  
	private String parseJsonAlter(String dataStr) throws IOException{
    	JSONObject jsonObject = JSONObject.fromObject(dataStr);
    	JSONArray ja = jsonObject.getJSONArray("commentIds");
    	JSONObject jc = jsonObject.getJSONObject("comments");
    	//System.out.println(ja.size());
    	pw = new PrintWriter(new FileWriter(file, true));
    	for(int i=0;i<ja.size();i++){
    		String comid = ja.getString(i);
    		if (comid.length()>"210605339".length()){
    			String[] comarr = comid.split(",");
    			comid = comarr[comarr.length-1];
    			}
    		JSONObject jd = jc.getJSONObject(comid);
    		//System.out.println(jc.getString(key));
    		String temp = comid+"："+jd.getString("content");
    		pw.println(temp);
    		if(ja.size()==0){
    			flag = false;
    		}
    	}
    	pw.close();
    	return dataStr;
    }
	//public static void main(String[] args) throws Exception{
	static void CommentClawer(String url){	
		try{
			wangyiCommentClawer.url = url;
			file = fileUtils.mkFile(url);
			//String url = "http://news.163.com/17/1031/08/D22I5FF50001875P.html";
			wangyiCommentClawer te = new wangyiCommentClawer();
			String t = te.urlChanger(url,0);
			//System.out.println(t);
			te.parseJsonAlter(te.sendGet(t));
			while(flag){
				//System.out.println("循环中");
				page+=30;
				//System.out.println("page:  "+page);
				t = te.urlChanger(url, page);			
					//System.out.println(t);
				te.parseJsonAlter(te.sendGet(t));
				if(flag==false){
					break;
				}
			  }
			}catch(Exception e){
			}
		}
}
