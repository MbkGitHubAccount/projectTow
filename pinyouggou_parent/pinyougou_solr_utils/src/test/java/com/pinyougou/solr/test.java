package com.pinyougou.solr;

import com.pinyougou.pojo.TbItem;
import com.pinyougou.utils.SolrUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-*.xml")
public class test {

    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private  SolrUtils solrUtils;
    /**
     *
     * 增删改的时候记得提交
     * 当id存在的时候就是修改  覆盖
     * 当id不存在的时候 就新增
     *
     */
    @Test
    public  void  InportData(){
        solrUtils.DataInport();
    }

    @Test
    public void save(){
        TbItem item=new TbItem();
        item.setId(1L);
        item.setBrand("华为");
        item.setSeller("华为专卖店");
        item.setTitle("华为 移动4G 256G");
        solrTemplate.saveBean(item);
        solrTemplate.commit();

    }


    /**
     * c查询
     */
    @Test
    public void select() {
            TbItem item = solrTemplate.getById(1L, TbItem.class);
            System.out.println("id:"+item.getId()+"品牌：" + item.getBrand()+"  "+item.getSeller()+item.getTitle());


        }


    /**
     *  根据Id删除
     */
    @Test
    public void delete() {
            solrTemplate.deleteById("1");
            solrTemplate.commit();

    }

    /**
     * 添加多条数据
     */
    @Test
    public void saveMany(){
        List<TbItem>itemList=new ArrayList<>();
        for (long i = 1; i <100; i++) {
            TbItem item=new TbItem();

            item.setId(i);
            item.setBrand("华为");
            item.setSeller("华为"+i+"号旗舰店");
            item.setTitle(i+"华为p20 移动3G 128G");
            itemList.add(item);

        }
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
    }

    /**
     * 删除所有
     */

    @Test
    public void deleteAll() {
        SolrDataQuery query= new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();

    }

    /**
     * 条件查询
     */
    @Test
    public  void  findPage(){

        Query query =new SimpleQuery("*:*");// 穿件 条件对象
        //设置条件
        query.setOffset(0);
        query.setRows(5);
        //条件查询
        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);

        System.out.println("总页数"+page.getTotalPages());
        System.out.println(("总计录数"+page.getTotalElements()));


        for (TbItem item : page) {

            System.out.println(item.getId()+"  " +item.getSeller()+ " "+ item.getBrand()+"  "+item.getTitle());
        }



    }

    /**
     * 多条件查询
     */
    @Test
    public  void  queryMultiByPage(){

        Query query =new SimpleQuery();//创建条件对象

        //   //设置查询条件  店铺包含8并且title包含5
        Criteria criteria=new Criteria();
        criteria.contains("item_seller").contains("8").or("item_title").contains("5");
        query.addCriteria(criteria);
        //设置条件
        query.setOffset(0);
        query.setRows(5);
        //条件查询
        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);

        System.out.println("总页数"+page.getTotalPages());
        System.out.println(("总计录数"+page.getTotalElements()));


        for (TbItem item : page.getContent()) {

            System.out.println(item.getId()+"  " +item.getSeller()+ " "+ item.getBrand()+"  "+item.getTitle());
        }



    }

    @Test

    public void test(){

        List<Map> mapList=new ArrayList<>();
        Map<String,String> map1=new HashMap<>();
        map1.put("key1","value1");
        map1.put("key2","value2");
        mapList.add(0,map1);
        mapList.add(1,map1);
        System.out.println(mapList.get(1).get("key2"));

    }






}
