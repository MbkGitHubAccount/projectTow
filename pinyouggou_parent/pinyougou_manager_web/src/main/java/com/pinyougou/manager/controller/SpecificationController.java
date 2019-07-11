package com.pinyougou.manager.controller;


import GroupEntity.Specification;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.sellergoods.service.TbSpecificationService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/specification")
public class SpecificationController {


    @Reference
    private TbSpecificationService specificationService;


    /**
     * 不带条件的分页查询
     * @param pageNumber
     * @param pageSize
     * @return
     */
    /*@RequestMapping("/search")
   // @RequestMapping("/findPage")
    public PageResult search( int pageNumber ,int pageSize){

        return  specificationService.findAll(pageNumber,pageSize);

    }*/
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbSpecification specification,Integer pageNumber,Integer pageSize){
        return specificationService.search(specification,pageNumber,pageSize);
    }

    /**
     * 规格的保存
     * @param specification
     * @return
     */

    @RequestMapping("/add")
    public Result add(@RequestBody Specification specification){

        try {
            specificationService.add(specification);
            return new Result(true,"新增规格成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"新增规格失败");
        }

    }


    /**
     * 规格的回显
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public Specification findOne(Long id){
        return specificationService.findOne(id);
    }





    @RequestMapping("/update")
    public Result update(@RequestBody Specification specification){

        try {
            specificationService.update(specification);
            return new Result(true,"修改规格成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改规格失败");
        }

    }


    @RequestMapping("/delete")
    public Result delete(Long[] ids){

        try {
            specificationService.delete(ids);
            return new Result(true,"删除规格成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除规格失败");
        }

    }
//  关联品牌的查询
@RequestMapping("/selectSpecOptions")
public List<Map> selectSpecOptions(){
    return specificationService.selectSpecOptions();
}



}
