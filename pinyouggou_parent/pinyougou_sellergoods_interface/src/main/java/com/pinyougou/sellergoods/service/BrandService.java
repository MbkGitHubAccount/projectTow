package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

public interface BrandService {

     List<TbBrand> findAll();

    PageResult findPage(int pageNumber, int pageSize);

    void add(TbBrand tbBrand);

    void update(TbBrand tbBrand);

    TbBrand findOne(Long id);

    void delete(Long [] ids);

    PageResult search(/*TbBrandExample example,*/ int pageNum, int pageSize);


    PageResult findPage(TbBrand tbBrand, int pageNumber, int pageSize);

    List<Map> selectBrandOptions();

}
