package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.SearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class SearchServiceimpl implements SearchService {
    @Autowired
    private SolrTemplate solrTemplate;
    @Override
    public Map<String, Object> Search(Map searchMap) {

        HighlightQuery query=new SimpleHighlightQuery();
        //获取页面上输入的关键字
        String keywords = (String) searchMap.get("keywords");
        Criteria criteria=null;
        //判断关键字是否为空
        if (StringUtils.isNotEmpty(keywords)){
            //不为空 给查询条件赋值
             criteria = new Criteria("item_keywords").is(keywords);
        }else {
            //没有条件的时候查询所有
          criteria=  new Criteria().expression("*:*");
        }
        query.addCriteria(criteria);

        //分类条件的查询
        //获取分类条件
        String category = (String) searchMap.get("category");
        //判断条件是否为空
        if (StringUtils.isNotEmpty(category)){
            Criteria criteriaCritera = new Criteria("item_category").is(category);
            FilterQuery filterQuery=new SimpleFacetQuery(criteriaCritera);
            query.addFilterQuery(filterQuery);

        }
        //brand  获品牌条件
        String brand = (String) searchMap.get("brand");
        //判断条件是否为空
        if (StringUtils.isNotEmpty(brand)){
            Criteria brandCritera = new Criteria("item_brand").is(brand);
            FilterQuery filterQuery=new SimpleFacetQuery(brandCritera);
            query.addFilterQuery(filterQuery);

        }
      //获取规格条件
     Map<String,String> specMap = (Map<String, String>) searchMap.get("spec");
        //"item_spec_网络": "联通3G", 网络就是传过来的Key specMap.get(key) 就是传过来的value
        //"item_spec_网络": "联通3G",
        //"item_spec_网络": "联通3G",
        for (String key : specMap.keySet()) {
            Criteria specCriteria=new Criteria("item_spec_"+key).is(specMap.get(key));
            FilterQuery filterQuery=new SimpleFacetQuery(specCriteria);
            query.addFilterQuery(filterQuery);

        }

        //获取价格条件
        String price = (String) searchMap.get("price");
        if (StringUtils.isNotEmpty(price)){
             String[] pirces= price.split("-");
            // 0-1000  1000-2000  2000-*    价格临界值  0 *

            if (!"0".equals(pirces[0])){
                Criteria priceCriteria=new Criteria("item_price").greaterThanEqual(pirces[0]);
                FilterQuery filterQuery=new SimpleFacetQuery(priceCriteria);
                query.addFilterQuery(filterQuery);
            }

            if (!"*".equals(pirces[1])){
                Criteria priceCriteria=new Criteria("item_price").lessThan(pirces[1]);
                FilterQuery filterQuery=new SimpleFacetQuery(priceCriteria);
                query.addFilterQuery(filterQuery);
            }
        }

        // 获取排序条件
        String sort = (String) searchMap.get("sort");
        String sortField = (String) searchMap.get("sortField");
        if (StringUtils.isNotEmpty(sortField)){
            //判断是升序还是降序
            if ("ASC".equals(sort)){
                //升序
                query.addSort(new Sort(Sort.Direction.ASC,"item_"+sortField));
            }
            else {
                //降序
                query.addSort(new Sort(Sort.Direction.DESC,"item_"+sortField));
            }
        }
        //分页
        //获取分页条件
        Integer  pageNo = (Integer) searchMap.get("pageNo");
         int rows=60;
        query.setOffset((pageNo-1)*rows);
        query.setRows(rows);


        //设置高亮
        //设置高亮对象
        HighlightOptions highlightOptions=new HighlightOptions();
        highlightOptions.addField("item_title");//设置高亮字段
        //设置 高亮前缀 后缀
        highlightOptions.setSimplePrefix("<font color ='red'>");
        highlightOptions.setSimplePostfix("</font>");

        query.setHighlightOptions(highlightOptions);

        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
        //获取当前页列表
        List<TbItem> itemList = page.getContent();
        for (TbItem item : itemList) {

            //高亮结果集
            List<HighlightEntry.Highlight> highlights = page.getHighlights(item);

            if (highlights!=null && highlights.size()>0){
                HighlightEntry.Highlight highlight = highlights.get(0);
                List<String> snipplets = highlight.getSnipplets();
                if (snipplets!=null && snipplets.size()>0){
                    String title = snipplets.get(0);
                        item.setTitle(title);
                }


            }

        }

        Map<String, Object> resoultMap =new HashMap<>();
        resoultMap.put("rows",itemList);
        resoultMap.put("totalPages",page.getTotalPages());
        resoultMap.put("pageNo",pageNo);


          return  resoultMap;
    }
}
