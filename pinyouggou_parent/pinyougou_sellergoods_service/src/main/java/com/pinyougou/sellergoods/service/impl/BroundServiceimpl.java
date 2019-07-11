package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;

import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.pinyougou.mapper.TbBrandMapper;
import java.util.List;
import java.util.Map;

@Service
public class BroundServiceimpl implements BrandService {

    @Autowired
    private TbBrandMapper tbBrandMapper;

    @Override
    public List<TbBrand> findAll() {
      return   tbBrandMapper.selectByExample(null);
    }

    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(null);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void add(TbBrand tbBrand) {
       tbBrandMapper.insert(tbBrand);
    }


    @Override
    public void update(TbBrand tbBrand) {
     tbBrandMapper.updateByPrimaryKey(tbBrand);

    }

    @Override
    public TbBrand findOne(Long id) {

        return tbBrandMapper.selectByPrimaryKey(id);

    }

    @Override
    public void delete(Long[] ids) {
        for (long id : ids) {
            tbBrandMapper.deleteByPrimaryKey(id);
        }



    }

    @Override
    public PageResult search(/*TbBrandExample example,*/ int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(/*example*/null);
        return new PageResult(page.getTotal(),page.getResult());
}

    @Override
    public PageResult findPage(TbBrand tbBrand, int pageNumber, int pageSize) {

        PageHelper.startPage(pageNumber,pageSize);

        TbBrandExample example=new TbBrandExample();

        TbBrandExample.Criteria criteria = example.createCriteria();

        if (tbBrand!=null){
            String name = tbBrand.getName();
            if (!StringUtils.isBlank(name)){
                //拼接查询条件
                criteria.andNameLike("%"+name+"%");
            }
            String firstChar = tbBrand.getFirstChar();

            if (!StringUtils.isBlank(firstChar)){
                criteria.andFirstCharLike(firstChar);
            }
        }else {
            example=null;
        }

        Page page = (Page) tbBrandMapper.selectByExample(example);
        return new PageResult(page.getTotal(),page.getResult());

    }

    @Override
    public List<Map> selectBrandOptions() {

        return tbBrandMapper.selectBrandOptions();
    }
}
