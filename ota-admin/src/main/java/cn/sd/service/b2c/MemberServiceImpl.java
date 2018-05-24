package cn.sd.service.b2c;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.dao.b2c.IMemberDao;

@Repository
@Service("memberService")
public class MemberServiceImpl implements IMemberService{

	@Resource
	private IMemberDao memberDao;
	
	public int countService(Map<String, Object> params){
		return this.memberDao.countDao(params);
	}
	public List<Map<String, Object>> listService(Map<String, Object> params){
		return this.memberDao.listDao(params);
	}
}
