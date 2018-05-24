package cn.sd.service.demo;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import cn.sd.dao.demo.IDemoDao;


@Service("demoService")
public class DemoService implements IDemoService {
	private static Log log = LogFactory.getLog(DemoService.class);
	@Resource
	private IDemoDao demoDao;
	@Override
	public String helloService(){
		log.info("service ★★★★★★★★ service");
		return demoDao.helloDao();
	}
}
