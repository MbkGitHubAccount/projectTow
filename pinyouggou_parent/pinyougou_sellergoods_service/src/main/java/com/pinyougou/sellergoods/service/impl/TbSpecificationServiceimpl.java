package com.pinyougou.sellergoods.service.impl;

import GroupEntity.Specification;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;
import com.pinyougou.sellergoods.service.TbSpecificationService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TbSpecificationServiceimpl implements TbSpecificationService {

    @Autowired
    private TbSpecificationMapper tbSpecificationMapper;

    @Override
    public PageResult findAll(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<TbSpecification> page = (Page<TbSpecification>) tbSpecificationMapper.selectByExample(null);

      return  new PageResult(page.getTotal(),page.getResult());

    }
    @Autowired
    TbSpecificationOptionMapper specificationOptionMapper;

    @Override
    public void add(Specification specification) {
        TbSpecification tbSpecification = specification.getSpecification();//获取规格名称
        tbSpecificationMapper.insert(tbSpecification);//保存到规格名称表中
        //获取规格的
        List<TbSpecificationOption> list = specification.getSppecificationOption();
        //遍历结合 将所有的国歌选项插入到 规格想想表中
        for (TbSpecificationOption specificationOption : list) {
            specificationOption.setSpecId(tbSpecification.getId());
            specificationOptionMapper.insert(specificationOption);
        }
    }
    /**
     * 规格的回显
     * @param id
     * @return
     */
    @Override
    public Specification findOne(Long id) {
        Specification specification=new Specification();
        //查询规格
        TbSpecification tbSpecification = tbSpecificationMapper.selectByPrimaryKey(id);
        specification.setSpecification(tbSpecification);
        //查询规格选项
        TbSpecificationOptionExample example=new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        criteria.andSpecIdEqualTo(id);
        //查询出当前规格所有的规格选项
        List<TbSpecificationOption> specificationOption = specificationOptionMapper.selectByExample(example);
        specification.setSppecificationOption(specificationOption);
        return specification;
    }
    /**
     * 规格的修改
     * @param specification
     */

    @Override
    public void update(Specification specification) {
        //规格 不需要删除直接更显就行了
        //更新规格名
        TbSpecification tbSpecification = specification.getSpecification();
        tbSpecificationMapper.updateByPrimaryKey(tbSpecification);
        //更新规格选项  先删除原来规格选项列表，在新增页面提交规格选项列表
        //删除原来规格选项、
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        Long id = tbSpecification.getId();
        criteria.andSpecIdEqualTo(id);
        specificationOptionMapper.deleteByExample(example);
        // 将接收到的数据插入数据库中
        List<TbSpecificationOption> sppecificationOption = specification.getSppecificationOption();
        for (TbSpecificationOption specificationOption : sppecificationOption) {
                    specificationOption.setSpecId(tbSpecification.getId());
            specificationOptionMapper.insert(specificationOption);
        }
    }
    /**
     * 删除选中
     * @param ids
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            //删除规格
            tbSpecificationMapper.deleteByPrimaryKey(id);
            //删除选项
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
            criteria.andSpecIdEqualTo(id);
            specificationOptionMapper.deleteByExample(example);
        }
    }
    /**
     * 分页的条件查询
     * @param specification
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public PageResult search(TbSpecification specification, Integer pageNumber, Integer pageSize) {

        PageHelper.startPage(pageNumber,pageSize);
        TbSpecificationExample exampel=new TbSpecificationExample();
        if (specification!=null){

            String specName = specification.getSpecName();

            if (!StringUtils.isBlank(specName)){
                TbSpecificationExample.Criteria criteria = exampel.createCriteria();
                criteria.andSpecNameLike("%"+specName+"%");
            }


        }

        Page<TbSpecification> page = (Page<TbSpecification>) tbSpecificationMapper.selectByExample(exampel);
        return new PageResult(page.getTotal(),page.getResult());


    }

    @Override
    public List<Map> selectSpecOptions() {

        return tbSpecificationMapper.selectSpecOptions();

    }
}
