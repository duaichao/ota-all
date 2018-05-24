package cn.sd.dao.b2c;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;

@Repository
@Service("memberDao")
public class MemberDaoImpl extends BaseDaoImpl implements IMemberDao {
	
	public int countDao(Map<String, Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("member.countDao", params);
	}
	
	public List<Map<String, Object>> listDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("member.listDao", params);
	}
}
