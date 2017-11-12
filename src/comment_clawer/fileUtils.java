package comment_clawer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class fileUtils {
	public static String timeBuilder(int i){
		if(i == 1){
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");//设置日期格式
			String date = df.format(new Date());
			return date;
		}
		else{
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
			String date = df.format(new Date());
			return date;
		}
	}
	public static File mkFile(String url) throws IOException {
		String time1 = timeBuilder(2);
		String time2 = timeBuilder(1);
		String localurl = url
				.replace("/", "").replace(":", "");
		String commentUri = "E:\\Clawer\\wangyicomment\\"+time1+"\\"+time2;
		File dir = new File(commentUri);
		if(dir.exists()){
			System.out.println("路径已存在");
		}
		else{
			dir.mkdirs();
			if(dir.exists()){
				System.out.println("路径创建成功");
			}
			else{
				System.out.println(commentUri);
				System.out.println("创建目录失败");
				return null;
			}
		}
		System.out.println(commentUri+url+".txt");
		File commentTex = new File(commentUri+"\\"+localurl+".txt");
		if(commentTex.exists()){
			System.out.println("文件已存在");
			return commentTex;
		}
		else{
			commentTex.createNewFile();
			System.out.println("文件创建成功");
			return commentTex;
		}
	}
//	public static void main(String[] args) throws IOException {
//		mkFile();
//	}
}
