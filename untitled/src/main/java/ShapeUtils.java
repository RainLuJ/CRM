import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.FileResource;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @description 图层数据源的上传与发布
 * @author Jun Lu
 * @date 2023-10-19
 */
@Slf4j
public class ShapeUtils {

    private ShapeUtils() {
    }

    public static void main(String[] args) {
        System.out.println(ShapeUtils.uploadShpFiles());
    }

    // GeoServer服务器的URL
    private static final String GEO_SERVER_URL = "http://localhost:10010/geoserver";

    // 设置用户名和密码
    private static final String username = "admin";
    private static final String password = "geoserver";

    // 构建Basic认证头部信息
    private static final String auth = Base64.encode(username + ":" + password);
    private static final String authHeader = "Basic " + auth;

    // 规定存放所有zip文件(shp文件)的文件夹【绝对路径】
    private static String SHP_FILE_DIR_PATH = unifyOSPath("D:\\Program\\GIS\\WorkSpace");

    // 使用的工作区名
    private static String WORKSPACE_NAME = "practice";

    // 文件所使用的编码格式
    private static String encodeType = "UTF-8";


    // 上传文件的CONTENT_TYPE类型
    public static final String APPLICATION_ZIP = "application/zip";

    /**
     * @return java.util.List<java.lang.String> 上传成功的shapefile文件所对应的URI（工作区名:shapefile文件名）
     * @description 上传指定data目录下的zip格式的shapefile数据文件
     * @author Jun Lu
     * @date 2023-10-19
     */
    public static List<String> uploadShpFiles() {

        String shpDirPath = unifyOSPath(SHP_FILE_DIR_PATH);
        List<String> shpFileNames = new ArrayList<>();

        File unionDir = new File(shpDirPath);
        File[] subFiles = unionDir.listFiles();
        if (ArrayUtil.isEmpty(subFiles) || (subFiles.length == 1 && ObjectUtil.equals(subFiles[0].getName(), "temp"))) {
            log.warn("当前文件夹下没有任何文件！");
            return shpFileNames;
        }
        for (File subFile : subFiles) {
            if (!(subFile.isFile() && subFile.getName().endsWith(".zip"))) {
                String fileType = subFile.isFile() ? "文件" : "文件夹";
                log.warn("当前仅支持zip格式shp文件的上传！不合法的{}：[{}]", fileType, subFile.getAbsolutePath());
                continue;
            }

            try {
                // zip文件的名称会成为图层数据实际的名称
                String shapefileName = subFile.getName().replaceAll("[.][^.]+$", "");
                FileResource resource = new FileResource(subFile);

                // 构建 数据源-图层 上传的URL（数据源也会一起被创建）
                String datastoreName = shapefileName;   // 数据源的名称（暂时统一为zip文件的名称）
                String uploadUrl = String.format("%s/rest/workspaces/%s/datastores/%s/file.shp?charset=%s", GEO_SERVER_URL, WORKSPACE_NAME, datastoreName, encodeType);

                // 发送PUT请求上传图层数据
                HttpRequest request = HttpRequest.put(uploadUrl)
                        .charset(StandardCharsets.UTF_8)
                        // 将认证信息设置到请求头中
                        .header(Header.AUTHORIZATION, authHeader)
                        .header(Header.CONTENT_TYPE, APPLICATION_ZIP)
                        // 添加上传文件
                        .body(resource);
                HttpResponse response = request.execute();

                // 输出响应结果
                // 文件成功上传时没有响应信息，状态码为：201
                if (HttpStatus.HTTP_CREATED == response.getStatus()) {
                    String shpUrl = WORKSPACE_NAME + ":" + shapefileName;
                    shpFileNames.add(shpUrl);
                    log.info("文件[{}]上传成功！URL为：[{}]", subFile.getAbsolutePath(), shpUrl);

                    // 上传成功的文件会被移动至临时目录
                    String successTempDirPath = unifyOSPath(SHP_FILE_DIR_PATH + "/temp/success/" + subFile.getName());
                    FileUtil.move(new File(subFile.getAbsolutePath()), new File(successTempDirPath), true);
                    log.info("success：文件已从[{}]保存至[{}]", subFile.getAbsolutePath(), successTempDirPath);

                    continue;
                }

                if (HttpStatus.HTTP_OK == response.getStatus()) {
                    log.warn("本次请求[{}]成功，但没有文件被上传！", subFile.getAbsolutePath());

                    continue;
                }

                // 如果上传失败，则记录异常日志
                log.error("上传失败的文件：[{}]；错误原因：[{}]", subFile.getAbsolutePath(), response.body());

            } catch (Exception e) {
                log.error("处理异常的文件：[{}]，异常信息：[{}]", subFile.getAbsolutePath(), getStackTrace(e));
            }

        }

        // 返回上传成功的shapefile文件所对应的URL
        // 格式：“工作区名:shapefile文件名”
        return shpFileNames;
    }

    /**
     * @description 统一在不同操作系统下的路径符
     * @param originPath 原始路径：还没有统一为当前操作系统下允许被使用的路径
     * @author Jun Lu
     * @date 2023-10-18
     */
    private static String unifyOSPath(String originPath) {
        String separator = Pattern.quote(File.separator);
        return originPath.replaceAll("[/\\\\]", separator).replace("Q\\E", "\\");
    }

    /**
     * 获取控制台中打印的堆栈信息，将异常信息封装为普通文本，再给日志输出
     */
    private static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

}
