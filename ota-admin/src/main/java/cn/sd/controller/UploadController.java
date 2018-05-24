package cn.sd.controller;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.FileUtil;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.UploadUtil;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.service.b2b.IADService;
import cn.sd.service.b2b.IUserService;
import cn.sd.service.site.IAreaService;
import cn.sd.web.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/upload")
@SuppressWarnings("all")
public class UploadController extends ExtSupportAction {
    private static Log log = LogFactory.getLog(UploadController.class);

    @Resource
    private IUserService userService;
    @Resource
    private IADService adService;
    @Resource
    private IAreaService areaService;

    @RequestMapping("/face")
    public Map<String, Object> face(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> rs = new HashMap<String, Object>();
        rs.put("jsonrpc", "2.0");
        rs.put("id", "id");
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("file");
            String fileName = this.toString(request.getParameter("name"));
            Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
            if (!fileName.equals("")) {
            } else if (file != null) {
                fileName = file.getName();
            } else {
                fileName = "file_" + this.getUuid();
            }
            if (FileUtil.checkFilesuffix(fileName, "jpg,gif,png")) {
                log.info("错误的文件类型");
                Map<String, Object> err = new HashMap<String, Object>();
                err.put("code", "101");
                err.put("message", "错误的文件类型");
                rs.put("error", err);
            } else {
                Map<String, Object> site = (Map<String, Object>) request.getSession().getAttribute(Constants.SESSION_SITE_KEY);
                String pinyin = "xian";
                if (site != null && !toString(site.get("PINYIN")).equals("")) {
                    pinyin = site.get("PINYIN").toString();
                }
                String[] paths = UploadUtil.pathAdmin("face", pinyin);
                String visitUrl = UploadUtil.uploadByte(file.getBytes(), paths[0], paths[1], fileName).replace("\\", "/");
                Map<String, Object> tp = new HashMap<String, Object>();
                tp.put("ID", user.get("ID"));
                tp.put("FACE", visitUrl);
                userService.saveService(tp);
                tp.clear();
                user.put("FACE", visitUrl);
                HttpSession session = request.getSession();
                session.setAttribute("S_USER_SESSION_KEY", user);
                rs.put("result", "success");
                rs.put("icon", visitUrl);
            }
        } catch (Exception e) {
            log.error("上传文件异常", e);
            Map<String, Object> err = new HashMap<String, Object>();
            err.put("code", "101");
            err.put("message", "上传文件异常");
            rs.put("error", err);
        }
        return rs;
    }

    @RequestMapping("/image")
    public Map<String, Object> image(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> rs = new HashMap<String, Object>();
        rs.put("jsonrpc", "2.0");
        rs.put("id", "id");
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("file");
            String fileName = this.toString(request.getParameter("name"));

            String entityName = this.toString(request.getParameter("entityName"));
            Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
            if (!fileName.equals("")) {
            } else if (file != null) {
                fileName = file.getName();
            } else {
                fileName = "file_" + this.getUuid();
            }
            if (FileUtil.checkFilesuffix(fileName, "jpg,gif,png")) {
                log.info("错误的文件类型");
                Map<String, Object> err = new HashMap<String, Object>();
                err.put("code", "101");
                err.put("message", "错误的文件类型");
                rs.put("error", err);
            } else {
                String[] paths = UploadUtil.pathAdmin(entityName, /*toString(user.get("CITY"))*/"");
                String visitUrl = UploadUtil.uploadByte(file.getBytes(), paths[0], paths[1], fileName).replace("\\", "/");
                rs.put("result", "success");
            }
        } catch (Exception e) {
            log.error("上传文件异常", e);
            Map<String, Object> err = new HashMap<String, Object>();
            err.put("code", "101");
            err.put("message", "上传文件异常");
            rs.put("error", err);
        }
        return rs;
    }

    @RequestMapping("/ad/b2b")
    public void adB2b(HttpServletRequest request, HttpServletResponse response) {
        ListRange json = new ListRange();
        Map<String, Object> rs = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();

        try {

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("Filedata");
            String fileName = request.getParameter("Filename");

            String TITLE = request.getParameter("title");
            if (CommonUtils.checkString(TITLE)) {
                TITLE = URLDecoder.decode(TITLE, "UTF-8");
            }

            String HREF = request.getParameter("href");
            if (CommonUtils.checkString(HREF)) {
                HREF = URLDecoder.decode(HREF, "UTF-8");
            }
            Map<String, Object> city = null;
            String CITY_NAME = request.getParameter("cityName");
            String PINYIN = request.getParameter("pinyin");
            if (CommonUtils.checkString(CITY_NAME)) {
                CITY_NAME = new String(CITY_NAME.getBytes("ISO-8859-1"), "UTF-8");
            }

            Map<String, Object> site = (Map<String, Object>) request.getSession().getAttribute(Constants.SESSION_SITE_KEY);
            if (!fileName.equals("")) {
            } else if (file != null) {
                fileName = file.getName();
            } else {
                fileName = "file_" + this.getUuid();
            }
            if (FileUtil.checkFilesuffix(fileName, "jpg,gif,png")) {
                log.error("图片类型错误");
                json.setSuccess(false);
                json.setStatusCode("-4");
                json.setMessage("图片类型错误");
            } else {
                String[] paths = UploadUtil.pathAdmin("ad", PINYIN);
                String file_suffix = fileName.substring(fileName.indexOf("."), fileName.length());
                fileName = CommonUtils.uuid() + file_suffix;
                String visitUrl = UploadUtil.uploadByte(file.getBytes(), paths[0], paths[1], fileName).replace("\\", "/");
                rs.put("result", "success");
                params.clear();
                params.put("ID", CommonUtils.uuid());
                params.put("PATH", visitUrl);
                params.put("HREF", HREF);
                params.put("TITLE", TITLE);
                params.put("TYPE", "0");
                params.put("SUB_TYPE", 1);
                params.put("CITY_ID", (String) request.getParameter("cityId"));
                params.put("CITY_NAME", CITY_NAME);
                this.adService.saveService(params);
                json.setSuccess(true);
            }
        } catch (Exception e) {
            log.error("上传广告图异常", e);
            json.setSuccess(false);
            json.setStatusCode("0");
            json.setMessage("上传广告图异常");
        }

        //将实体对象转换为JSON Object转换
        JSONObject responseJSONObject = (JSONObject) JSON.toJSON(json);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(responseJSONObject.toString());
            log.debug("返回是\n");
            log.debug(responseJSONObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
