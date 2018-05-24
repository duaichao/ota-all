package cn.sd.core.util.captcha;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;

public class CaptchaServiceSingleton {

    
    private static ImageCaptchaService instance = new DefaultManageableImageCaptchaService(
    		new FastHashMapCaptchaStore(),
    		new ImageCaptchaEngine(),
    		180,
    		100000,
    		75000
    );
    
    public static ImageCaptchaService getInstance(){
        return instance;
    }
}