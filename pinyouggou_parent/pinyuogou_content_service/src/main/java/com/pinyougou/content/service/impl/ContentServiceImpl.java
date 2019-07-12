package com.pinyougou.content.service.impl;
import java.util.List;

import com.pinyougou.content.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbContentMapper;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.pojo.TbContentExample;
import com.pinyougou.pojo.TbContentExample.Criteria;

import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;


	@Autowired
	private RedisTemplate  redisTemplate;
	/**
	 * 查询全部
	 */
	@Override
	public List<TbContent> findAll() {
		return contentMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbContent> page=   (Page<TbContent>) contentMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbContent content) {
		//当添加广告后唏嘘得清空 缓存中的数据

		contentMapper.insert(content);
		Long categoryId = content.getCategoryId();
		redisTemplate.boundHashOps("pinyuogou_conunt").delete(categoryId);


	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbContent content){
		Long id = content.getId();//此时的content是页面传过来的
		TbContent tbContent = contentMapper.selectByPrimaryKey(id);
		Long categoryId = tbContent.getCategoryId();
		redisTemplate.boundHashOps("pinyuogou_conunt").delete(categoryId);
		contentMapper.updateByPrimaryKey(content);
		if (content.getCategoryId().longValue()!=categoryId.longValue()){
			//修改了广告的分类
			redisTemplate.boundHashOps("pinyuogou_conunt").delete(content.getCategoryId());


		}
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbContent findOne(Long id){
		return contentMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			//当删除广告的时候也必须得清空缓存中的数据
			TbContent content = contentMapper.selectByPrimaryKey(id);
			Long categoryId = content.getCategoryId();
			redisTemplate.boundHashOps("pinyuogou_conunt").delete(categoryId);

			contentMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbContent content, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbContentExample example=new TbContentExample();
		Criteria criteria = example.createCriteria();
		
		if(content!=null){			
						if(content.getTitle()!=null && content.getTitle().length()>0){
				criteria.andTitleLike("%"+content.getTitle()+"%");
			}
			if(content.getUrl()!=null && content.getUrl().length()>0){
				criteria.andUrlLike("%"+content.getUrl()+"%");
			}
			if(content.getPic()!=null && content.getPic().length()>0){
				criteria.andPicLike("%"+content.getPic()+"%");
			}
			if(content.getStatus()!=null && content.getStatus().length()>0){
				criteria.andStatusLike("%"+content.getStatus()+"%");
			}
	
		}
		
		Page<TbContent> page= (Page<TbContent>)contentMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}


    @Override
    public List<TbContent> findByCategoryId(Long categoryId) {

    	//首先要去 缓存中查看缓存是否是有数据
		List<TbContent>  contentList= (List<TbContent>) redisTemplate.boundHashOps("pinyuogou_conunt").get(categoryId);

		//判断是否是有值
		if (contentList==null){
			System.out.println(" 从数据库取的数据......");
			TbContentExample example = new TbContentExample();
			example.createCriteria().andCategoryIdEqualTo(categoryId);
			//设置排序
			example.setOrderByClause("sort_order");
			contentList = contentMapper.selectByExample(example);
			redisTemplate.boundHashOps("pinyuogou_conunt").put(categoryId,contentList);
		}else {

			System.out.println("从缓存中取数据......");
		}

		return contentList;

	}


}
