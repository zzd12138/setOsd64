package javademo;

import java.io.UnsupportedEncodingException;

public class DllLoad {
    public static String DLL_PATH;
    static {
        String root = DllLoad.class.getResource("/").getPath();
        if (root.startsWith("file:/")) {  // jar包中获取到的路径
            DLL_PATH = "";
        } else {  // 实际开发中获取到的路径不带 file:/
            root = root.replaceAll("%20", " ").substring(1);
            try {
                DLL_PATH = java.net.URLDecoder.decode(root, "utf-8");
                System.out.println("获取路径成功"+DLL_PATH);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
