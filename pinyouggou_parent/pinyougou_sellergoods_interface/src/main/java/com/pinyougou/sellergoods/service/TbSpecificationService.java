package com.pinyougou.sellergoods.service;

import GroupEntity.Specification;
import com.pinyougou.pojo.TbSpecification;
import entity.PageResult;

import java.util.List;
import java.util.Map;

public interface TbSpecificationService {

    /**
     * 不带条件的分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageResult findAll(int pageNum, int pageSize);

    /**
     * 规格插入
     * @param specification
     */
    void add(Specification specification);

    Specification findOne(Long id);

    void update(Specification specification);

    void delete(Long[] ids);

    PageResult search(TbSpecification specification, Integer pageNumber, Integer pageSize);

    List<Map> selectSpecOptions();


}
