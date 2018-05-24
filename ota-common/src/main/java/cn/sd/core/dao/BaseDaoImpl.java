package cn.sd.core.dao;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapClient;

public class BaseDaoImpl extends SqlMapClientDaoSupport 
	implements IBaseDao {
	@Resource
    private SqlMapClient sqlMapClient;
	@PostConstruct
    public void initSqlMapClient(){
		super.setSqlMapClient(sqlMapClient);
	}
}
