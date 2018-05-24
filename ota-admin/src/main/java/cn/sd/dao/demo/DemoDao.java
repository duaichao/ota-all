package cn.sd.dao.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;
@Repository
@Service("demoDao")
public class DemoDao extends BaseDaoImpl implements IDemoDao {
	private static Log log = LogFactory.getLog(DemoDao.class);
	@Override
	public String helloDao() {
		log.info("dao ★★★★★★★★ dao");
		return getSqlMapClientTemplate().queryForObject("demo.helloDao").toString();
	}

}
