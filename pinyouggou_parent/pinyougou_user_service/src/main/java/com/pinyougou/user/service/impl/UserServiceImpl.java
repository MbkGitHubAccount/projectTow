package com.pinyougou.user.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.pinyougou.user.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbUserMapper;
import com.pinyougou.pojo.TbUser;
import com.pinyougou.pojo.TbUserExample;
import com.pinyougou.pojo.TbUserExample.Criteria;


import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import utils.HttpClient;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbUser> findAll() {
		return userMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbUser> page=   (Page<TbUser>) userMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbUser user) {
			/* `password` varchar(32) NOT NULL COMMENT '密码，加密存储',
			 `created` datetime NOT NULL COMMENT '创建时间',
			 `updated` datetime NOT NULL,
			 `source_type` varchar(1) DEFAULT NULL COMMENT '会员来源：1:PC
			 `status` varchar(1) DEFAULT NULL COMMENT '使用状态（Y正常 N非正常）',
			 `is_mobile_check` varchar(1) DEFAULT '0' COMMENT '手机是否验证 （0否  1是）',
		*/
			user.setPassword(DigestUtils.md5Hex(user.getPassword()));//对密码进行MD5加密
			user.setCreated(new Date());
			user.setUpdated(new Date());
			user.setSourceType("1");
			user.setStatus("Y");
			user.setIsMobileCheck("1");

			userMapper.insert(user);
		// 在注册成功之后将当前用户刚刚使用的验证码从缓存中清除

		redisTemplate.delete(user.getPhone());
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbUser user){
		userMapper.updateByPrimaryKey(user);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbUser findOne(Long id){
		return userMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			userMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbUser user, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbUserExample example=new TbUserExample();
		Criteria criteria = example.createCriteria();
		
		if(user!=null){			
						if(user.getUsername()!=null && user.getUsername().length()>0){
				criteria.andUsernameLike("%"+user.getUsername()+"%");
			}
			if(user.getPassword()!=null && user.getPassword().length()>0){
				criteria.andPasswordLike("%"+user.getPassword()+"%");
			}
			if(user.getPhone()!=null && user.getPhone().length()>0){
				criteria.andPhoneLike("%"+user.getPhone()+"%");
			}
			if(user.getEmail()!=null && user.getEmail().length()>0){
				criteria.andEmailLike("%"+user.getEmail()+"%");
			}
			if(user.getSourceType()!=null && user.getSourceType().length()>0){
				criteria.andSourceTypeLike("%"+user.getSourceType()+"%");
			}
			if(user.getNickName()!=null && user.getNickName().length()>0){
				criteria.andNickNameLike("%"+user.getNickName()+"%");
			}
			if(user.getName()!=null && user.getName().length()>0){
				criteria.andNameLike("%"+user.getName()+"%");
			}
			if(user.getStatus()!=null && user.getStatus().length()>0){
				criteria.andStatusLike("%"+user.getStatus()+"%");
			}
			if(user.getHeadPic()!=null && user.getHeadPic().length()>0){
				criteria.andHeadPicLike("%"+user.getHeadPic()+"%");
			}
			if(user.getQq()!=null && user.getQq().length()>0){
				criteria.andQqLike("%"+user.getQq()+"%");
			}
			if(user.getIsMobileCheck()!=null && user.getIsMobileCheck().length()>0){
				criteria.andIsMobileCheckLike("%"+user.getIsMobileCheck()+"%");
			}
			if(user.getIsEmailCheck()!=null && user.getIsEmailCheck().length()>0){
				criteria.andIsEmailCheckLike("%"+user.getIsEmailCheck()+"%");
			}
			if(user.getSex()!=null && user.getSex().length()>0){
				criteria.andSexLike("%"+user.getSex()+"%");
			}
	
		}
		
		Page<TbUser> page= (Page<TbUser>)userMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}


	@Autowired
	private RedisTemplate redisTemplate;
	/**
	 * 验证码的发送
	 * @param phone
	 */
    @Override
    public void sendSms(String phone) {
    	try {
    		//每次发送前将之前缓存中的验证码删除
			//1随机生成一个不为1的数
			int num = (int)(Math.random() * 9 + 1);
			String code = RandomStringUtils.randomNumeric(5);
			String Code= code+num;
			//将验证码存到rides 缓存中
			//用户的手机号作为key   过期时间为20分钟
			redisTemplate.boundValueOps(phone).set(Code,20L, TimeUnit.MINUTES);
			//发送验证码
			HttpClient httpClient=new HttpClient("http://localhost:10086/sms/sendSms.do");
			httpClient.addParameter("phoneNumbers",phone); //目标用户
			httpClient.addParameter("signName","MySIgn"); //设置签名
			httpClient.addParameter("templateCode","SMS_171110931");//指定模板
			httpClient.addParameter("param","{\"code\":"+Code+"}");//验证码

			httpClient.post();//指定请求方式
			//向用户发送验证码
			String content = httpClient.getContent();

			Map<String ,String> resoultMap = JSON.parseObject(content, Map.class);

			if (!"OK".equals(resoultMap.get("Code"))) {

				throw new RuntimeException("失败了！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//判断验证码是否是相等
	@Override
	public String CheckCode(String code, String phone) {
    	//从缓存中获取当前用户的验证码
		String sysCode = (String) redisTemplate.boundValueOps(phone).get();
		//判断验证码失效
		if (sysCode==null){
			return "0";
		}
		//判断验证码是否是向等
		if (!sysCode.equals(code)){
			return "2";
		}
		return "1"  ;
    }

}
